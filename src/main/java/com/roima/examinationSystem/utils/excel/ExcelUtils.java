package com.roima.examinationSystem.utils.excel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roima.examinationSystem.exception.ExcelReaderException;
import com.roima.examinationSystem.exception.FetchException;
import com.roima.examinationSystem.exception.InvalidValueException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.*;
import com.roima.examinationSystem.repository.CategoryRepository;
import com.roima.examinationSystem.repository.CollegeRepository;
import com.roima.examinationSystem.repository.LanguageRepository;
import com.roima.examinationSystem.utils.judge0.BatchData;
import com.roima.examinationSystem.utils.judge0.BatchSubmissionResult;
import com.roima.examinationSystem.utils.judge0.BatchTestCase;
import com.roima.examinationSystem.utils.judge0.Judge0Util;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ExcelUtils {
    public final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final PasswordEncoder passwordEncoder;
    private final CollegeRepository collegeRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final Judge0Util judge0Util;


    public boolean isExcelFormat(MultipartFile file){
        return TYPE.equals(file.getContentType());
    }

    private boolean isAllHeadersPresent(Map<String,HeaderInfo> headerInfoMap, Sheet sheet){

        Row firstRow = sheet.getRow(0);
        int lastCol = firstRow.getLastCellNum();
        Iterator<Cell> cells = firstRow.cellIterator();
        Map<String,Boolean> requestExcelHeaders = new HashMap<>();
        int cellIdx = 0;
        while(cellIdx<lastCol){
            Cell cell = cells.next();
            String headerName = cell.getStringCellValue();
            if(!headerInfoMap.containsKey(headerName)){
                return false;
            }
            requestExcelHeaders.put(headerName,true);
            cellIdx++;
        }


        for(Map.Entry<String,HeaderInfo> entry : headerInfoMap.entrySet()){
            if(!requestExcelHeaders.containsKey(entry.getKey()) && !entry.getValue().isNullable){
                return false;
            }
        }

        return true;
    }


    public College getCollege(Integer id) throws ResourceNotFoundException {
        return collegeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("College not found!"));
    }

    private boolean isEmptyCell(Cell cell){
        return (cell == null || cell.getCellType() == CellType.BLANK);
    }

    private boolean isEmptyRow(Row row){
        for(int i=row.getFirstCellNum(); i < row.getLastCellNum();i++){
            if(!isEmptyCell(row.getCell(i))){
                return false;
            }
        }
        return true;
    }

    public List<Student> getStudentData(MultipartFile file) throws ExcelReaderException, ResourceNotFoundException {
        {
            try {

                if(!isExcelFormat(file)){
                    throw new ExcelReaderException("Only Excel files(.xlsx) are allowed.");
                }

                Map<String, HeaderInfo> header = new HashMap<>();
                //Header name with isNullable value, respective type and a setter function to set value with proper type conversion
                header.put("fullname", new StudentHeaderInfo(false,CellType.STRING,(student, value)-> student.getUser().setFullName((String) value)));
                header.put("email", new StudentHeaderInfo(false, CellType.STRING, (student,value) -> student.getUser().setEmail((String) value) ));
                header.put("password", new StudentHeaderInfo(false, CellType.STRING,(student,value)-> student.getUser().setPassword( passwordEncoder.encode((String) value))));
                header.put("contact", new StudentHeaderInfo(false, CellType.NUMERIC, (student, value) -> student.setContact((String) value )));
                header.put("enrollment_number", new StudentHeaderInfo(false, CellType.NUMERIC, (student,value)->student.setEnrollment_number((String) value)));
                header.put("year", new StudentHeaderInfo(false, CellType.NUMERIC, (student, value )-> student.setYear(Integer.parseInt(value.toString()))));
                header.put("semester", new StudentHeaderInfo(false, CellType.NUMERIC, (student,value)->student.setSemester(Integer.parseInt(value.toString()))));
                header.put("cgpa", new StudentHeaderInfo(false, CellType.NUMERIC, (student, value) -> student.setCgpa(Float.parseFloat(value.toString()))));
                header.put("backlog", new StudentHeaderInfo(false, CellType.NUMERIC,(student,value)->student.setBacklog(Integer.parseInt(value.toString()))));
                header.put("department", new StudentHeaderInfo(false, CellType.STRING, (student,value)->student.setDepartment((String) value)));
                header.put("college_id", new StudentHeaderInfo(false, CellType.NUMERIC, (student,value) ->student.setCollege(getCollege(Integer.parseInt(value.toString())))));



                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                if (!isAllHeadersPresent(header, sheet)) {
                    throw new ExcelReaderException("Some headers are missing.");
                }

                System.out.println("Total rows = " + sheet.getLastRowNum());

                Row firstRow = sheet.getRow(0);
                Iterator<Row> rows = sheet.iterator();
                List<Student> studentList = new ArrayList<>();

                rows.next();
                int cellIdx = 0;
                int lastCell=0;
                Object value = null;
                StudentHeaderInfo studentHeaderInfo;
                String headerTitle = null;
                while (rows.hasNext()) {
                    Row row = rows.next();
                    if(isEmptyRow(row)){
                        continue;
                    }
                    lastCell = row.getLastCellNum();
                    Student student = new Student();
                    cellIdx = 0;
                    while (cellIdx<lastCell) {
                        Cell cell = row.getCell(cellIdx);
                        headerTitle = firstRow.getCell(cellIdx).getStringCellValue();


                        studentHeaderInfo = (StudentHeaderInfo) header.get(headerTitle);
//                        System.out.print(headerInfo);
//                        System.out.print(firstRow.getCell(cellIdx).getStringCellValue());
//                        System.out.println(cell);

                        if(isEmptyCell(cell)){
                            if(!studentHeaderInfo.isNullable) {
                                throw new ExcelReaderException("Blank cell found in '"+ headerTitle +"' column.");
                            }else{
                                cellIdx++;
                                continue;
                            }
                        }

                        if(cell.getCellType() != studentHeaderInfo.getCellType()){
                            throw new ExcelReaderException("Cell type mismatch found in '" + headerTitle +"' column.");
                        }


                        switch (studentHeaderInfo.getCellType()) {
                            case CellType.STRING -> value = (String) cell.getStringCellValue();
                            case CellType.NUMERIC -> value = NumberToTextConverter.toText(cell.getNumericCellValue());
                            case CellType.BOOLEAN -> value = (Boolean) cell.getBooleanCellValue();

                            default -> throw new ExcelReaderException("Unsupported cell type found in '" + headerTitle +"' column.");
                        }

                        studentHeaderInfo.setValue(student, value);
                        cellIdx++;

                    }
                    studentList.add(student);
                }
                return studentList;
            } catch (IOException  e) {
                throw new ExcelReaderException("Failed to read excel file.");
            } catch (EmptyFileException e){
                throw new ExcelReaderException("Excel file is empty.");
            }
        }
    }

    private Category getCategory(String name, QuestionType type) throws ExcelReaderException{
        Category category = categoryRepository.findByName(name);
        if(category == null){
            category = new Category(name,type);
            categoryRepository.save(category);
        }
        else if(category.getQuestionType() != type){
            throw new ExcelReaderException("Not valid category. Given category is type of "+ category.getQuestionType().toString() +" instead of type "+type.toString() +" !!!");
        }
        return category;
    }

    private void validateMcqQuestion(McqQuestions mcqQuestions) throws ExcelReaderException{
        List<McqOptions> mcqOptions = mcqQuestions.getMcqOptions();

        if(mcqOptions==null || mcqOptions.size()<2){
            throw new ExcelReaderException("Total mcq options should be greater than 1.");
        }

        for(McqOptions options: mcqOptions){
            if(options.getOption_number() == mcqQuestions.getCorrect_option()){
                return;
            }
        }

        throw new ExcelReaderException("Correct option not found in given list of mcq options");
    }


    public List<McqQuestions> getMcqQuestions(MultipartFile file) throws ExcelReaderException, ResourceNotFoundException {

        try {

            if(!isExcelFormat(file)){
                throw new ExcelReaderException("Only Excel files(.xlsx) are allowed.");
            }

            Map<String, HeaderInfo> header = new HashMap<>();

            header.put("question", new McqHeaderInfo(false, CellType.STRING, (mcqQuestions, value) -> mcqQuestions.setQuestion((String) value)));
            header.put("difficulty", new McqHeaderInfo(false, CellType.STRING, (mcqQuestions, value) -> {
                try {
                    Difficulty difficulty = Difficulty.valueOf(value.toString());
                    mcqQuestions.setDifficulty(difficulty);
                } catch (IllegalArgumentException e) {
                    throw new ExcelReaderException("Difficulty must be one of [EASY, MEDIUM, HARD].");
                }

            }));

            header.put("correct_option", new McqHeaderInfo(false, CellType.NUMERIC, (mcqQuestions, value) -> mcqQuestions.setCorrect_option(Integer.parseInt(value.toString()))));
            header.put("category_name", new McqHeaderInfo(false, CellType.STRING, (mcqQuestions, value) -> {
                mcqQuestions.setCategory(getCategory(value.toString(), QuestionType.MCQ));
            }));

            header.put("option", new McqHeaderInfo(true, CellType.STRING, (mcqQuestions, value) -> {
                List<McqOptions> mcqOptions = mcqQuestions.getMcqOptions();
                if (mcqOptions == null) {
                    mcqOptions = new ArrayList<>();
                }
                mcqOptions.add((McqOptions) value);
                mcqQuestions.setMcqOptions(mcqOptions);
            }));

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            if (!isAllHeadersPresent(header, sheet)) {
                throw new ExcelReaderException("Some headers are missing.");
            }



            Row firstRow = sheet.getRow(0);
            Iterator<Row> rows = sheet.iterator();
            List<McqQuestions> mcqQuestionsList = new ArrayList<>();

            rows.next();
            int cellIdx = 0;
            int lastCell =0;
            int optionNumber = 0;
            Object value = null;
            McqHeaderInfo mcqHeaderInfo;

            while (rows.hasNext()){
                Row row = rows.next();
                if(isEmptyRow(row)){
                    continue;
                }
                lastCell = row.getLastCellNum();
                cellIdx = 0;
                optionNumber = 0;
                McqQuestions mcqQuestions = new McqQuestions();

                while(cellIdx<lastCell){
                    Cell cell = row.getCell(cellIdx);

                    String headerTitle = firstRow.getCell(cellIdx).getStringCellValue();
                    mcqHeaderInfo = (McqHeaderInfo) header.get(headerTitle);


                    if(isEmptyCell(cell)){
                        if(!mcqHeaderInfo.isNullable) {
                            throw new ExcelReaderException("Blank cell found in '" + headerTitle+ "' column.");
                        }else{

                            if(headerTitle.equals("option")){
                                break;
                            }
                            cellIdx++;
                            continue;
                        }
                    }

                    if(cell.getCellType() != mcqHeaderInfo.getCellType()){
                        throw new ExcelReaderException("Cell type mismatch in '" + headerTitle + "' column.");
                    }

                    switch (mcqHeaderInfo.getCellType()){
                        case CellType.STRING -> value = (String) cell.getStringCellValue();
                        case CellType.NUMERIC -> value = NumberToTextConverter.toText(cell.getNumericCellValue());
                        case CellType.BOOLEAN -> value = (Boolean) cell.getBooleanCellValue();

                        default -> throw new ExcelReaderException("Unsupported cell type found in '" + headerTitle +"' column.");
                    }

                    if(headerTitle.equals("option")){
                        optionNumber++;
                        McqOptions mcqOptions = new McqOptions(optionNumber,value.toString());
                        System.out.println(value.toString());
                        value = mcqOptions;
                    }

                    mcqHeaderInfo.setValue(mcqQuestions,value);
                    cellIdx++;
                }

                validateMcqQuestion(mcqQuestions);
                System.out.println(mcqQuestions);
                mcqQuestionsList.add(mcqQuestions);
            }

            return mcqQuestionsList;

        }catch (IOException  e) {
            throw new ExcelReaderException("Failed to read excel file.");
        } catch (EmptyFileException e){
            throw new ExcelReaderException("Excel file is empty.");
        }
    }



    private void validProgrammingQuestion(List<ProgrammingQuestions> programmingQuestionsList) throws ExcelReaderException, FetchException, InvalidValueException {

        int i=0;
        for(ProgrammingQuestions programmingQuestions : programmingQuestionsList) {
            List<ProgrammingTestCase> testCaseList = programmingQuestions.getProgrammingTestCase();

            if (testCaseList == null) {
                throw new ExcelReaderException("Programming Questions must have at least one PUBLIC/HIDDEN testcase!");
            }
            boolean isValidTestCase = false;
            for (ProgrammingTestCase testCase : testCaseList) {
                if (testCase.getType() == TestCaseType.PUBLIC || testCase.getType() == TestCaseType.HIDDEN) {
                    isValidTestCase = true;
                    break;
                }
            }
            if (!isValidTestCase) {
                throw new ExcelReaderException("Programming Questions must have at least one PUBLIC/HIDDEN testcase!");
            }

            BatchData batchData = new BatchData();
            List<BatchTestCase> batchTestCaseList = new ArrayList<>();

            for (ProgrammingTestCase testCase : testCaseList) {
                BatchTestCase batchTestCase = new BatchTestCase(testCase.getInput(), testCase.getOutput());
                batchTestCaseList.add(batchTestCase);
            }
            batchData.setCode(programmingQuestions.getImplementation());
            batchData.setJudge0LanguageId(programmingQuestions.getImplementationLanguage().getJudge0Id());
            batchData.setTestCaseList(batchTestCaseList);

            List<BatchSubmissionResult> batchSubmissionResults = judge0Util.batchSubmissions(batchData);

            System.out.println(batchSubmissionResults);

            if (!judge0Util.isAllTestCasesPassed(batchSubmissionResults)) {
                throw new ExcelReaderException("Programming question at row = "+ (i+1) +" does not passed all test cases!!!");
            }
            i++;
        }

    }

    public List<ProgrammingQuestions> getProgrammingQuestions(MultipartFile file) throws ExcelReaderException, ResourceNotFoundException, InvalidValueException, FetchException {
        try{
            if(!isExcelFormat(file)){
                throw new ExcelReaderException("Only Excel files(.xlsx) are allowed.");
            }

            Map<String,HeaderInfo> header = new HashMap<>();

            header.put("statement", new ProgrammingHeaderInfo(false, CellType.STRING, (programmingQuestion, value) -> programmingQuestion.setStatement((String) value)));
            header.put("difficulty", new ProgrammingHeaderInfo(false,CellType.STRING,(programmingQuestion, value) ->{
                try{
                    System.out.println(value.toString());
                    Difficulty difficulty = Difficulty.valueOf(value.toString());
                    programmingQuestion.setDifficulty(difficulty);

                }catch (IllegalArgumentException e){
                    throw new ExcelReaderException("Difficulty must be one of [EASY, MEDIUM, HARD].");
                }
            }));
            header.put("implementation", new ProgrammingHeaderInfo(false,CellType.STRING,(programmingQuestion,value) ->{
                try {
                    value = value.toString().replace("\\n","\n").replace("\\t","    ").replace("\t","    ");
                    System.out.println(value);
                    programmingQuestion.setImplementation((String) value);
                }catch (  Exception e){
                    throw new ExcelReaderException("Implementation must be in valid json string format.");
                }
                    }));
            header.put("language_id", new ProgrammingHeaderInfo(false,CellType.NUMERIC,(programmingQuestion,value)-> {
                Language language = languageRepository.findById(Integer.parseInt(value.toString())).orElseThrow(
                        ()-> new ExcelReaderException("Language with id "+ value +" not found.")
                );
                programmingQuestion.setImplementationLanguage(language);
            }));
            header.put("category_name",new ProgrammingHeaderInfo(false,CellType.STRING, (programmingQuestion,value)->{
                programmingQuestion.setCategory(getCategory((String) value, QuestionType.PROGRAMMING));
            }));
            header.put("testcase_input", new TestCaseHeaderInfo(true,CellType.STRING,(testCase, value)->testCase.setInput((String) value)));
            header.put("testcase_output", new TestCaseHeaderInfo(true,CellType.STRING,(testCase,value)->testCase.setOutput((String) value)));
            header.put("testcase_type",new TestCaseHeaderInfo(true,CellType.STRING,(testCase,value)->{
                try {
                    TestCaseType testCaseType = TestCaseType.valueOf((String) value);
                    testCase.setType(testCaseType);
                }catch (IllegalArgumentException e){
                    throw new ExcelReaderException("Test Case type must be one of [SAMPLE, PUBLIC, HIDDEN].");
                }
            }));

            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            if (!isAllHeadersPresent(header, sheet)) {
                throw new ExcelReaderException("Some headers are missing.");
            }

            Row firstRow = sheet.getRow(0);
            Iterator<Row> rows = sheet.iterator();

            List<ProgrammingQuestions> programmingQuestionsList = new ArrayList<>();
            List<ProgrammingTestCase> programmingTestCaseList;
            int cellIdx = 0;
            int lastCell =0;
            Object value = null;
            ProgrammingHeaderInfo programmingHeaderInfo;
            TestCaseHeaderInfo testCaseHeaderInfo;
            String headerTitle;
            rows.next();
            boolean isAllTestCaseInfo;

            while(rows.hasNext()){
                Row row = rows.next();

                if(isEmptyRow(row)){
                    continue;
                }
                lastCell = row.getLastCellNum();
                cellIdx = 0;

                ProgrammingQuestions programmingQuestions = new ProgrammingQuestions();

                while(cellIdx<lastCell) {
                    Cell cell = row.getCell(cellIdx);
                    System.out.println(cellIdx);

                    headerTitle = firstRow.getCell(cellIdx).getStringCellValue();
                    if(headerTitle.equals("testcase_input")){

                        ProgrammingTestCase testCase = new ProgrammingTestCase();

                        isAllTestCaseInfo = true;
                        for(int i=0;i<3;i++){
                            cell = row.getCell(cellIdx+i);

                            if(isEmptyCell(cell)){
                                cellIdx += 3;
                                isAllTestCaseInfo = false;
                                break;
                            }

                            headerTitle = firstRow.getCell(cellIdx+i).getStringCellValue();
                            testCaseHeaderInfo = (TestCaseHeaderInfo) header.get(headerTitle);

                            value = dataFormatter.formatCellValue(cell);
                            System.out.println(value);

                            testCaseHeaderInfo.setValue(testCase,value);
                        }

                        if(!isAllTestCaseInfo){
                            continue;
                        }

                        programmingTestCaseList = programmingQuestions.getProgrammingTestCase();

                        if(programmingTestCaseList==null){
                            programmingTestCaseList = new ArrayList<>();
                        }
                        programmingTestCaseList.add(testCase);
                        System.out.print("INPUT = " + testCase.getInput() +", ");
                        System.out.print("OUTPUT = " + testCase.getOutput() +", ");
                        System.out.println("TYPE = "+ testCase.getType());
                        programmingQuestions.setProgrammingTestCase(programmingTestCaseList);
                        cellIdx+=3;
                        continue;
                    }

                    programmingHeaderInfo = (ProgrammingHeaderInfo)header.get(headerTitle);

                    if(isEmptyCell(cell)){
                        if(!programmingHeaderInfo.isNullable) {
                            throw new ExcelReaderException("Blank cell found in '" + headerTitle+ "' column.");
                        }else{
                            cellIdx++;
                            continue;
                        }
                    }

                    if(cell.getCellType() != programmingHeaderInfo.getCellType()){
                        throw new ExcelReaderException("Cell type mismatch in '" + headerTitle + "' column.");
                    }

                    switch (programmingHeaderInfo.getCellType()){
                        case CellType.STRING -> value = (String) cell.getStringCellValue();
                        case CellType.NUMERIC -> value = NumberToTextConverter.toText(cell.getNumericCellValue());
                        case CellType.BOOLEAN -> value = (Boolean) cell.getBooleanCellValue();

                        default -> throw new ExcelReaderException("Unsupported cell type found in '" + headerTitle +"' column.");
                    }

                    programmingHeaderInfo.setValue(programmingQuestions,value);
                    cellIdx++;
                }


                programmingQuestionsList.add(programmingQuestions);


            }
            validProgrammingQuestion(programmingQuestionsList);
            return programmingQuestionsList;

        }catch (IOException  e) {
            throw new ExcelReaderException("Failed to read excel file.");
        } catch (EmptyFileException e){
            throw new ExcelReaderException("Excel file is empty.");
        }
    }

}
