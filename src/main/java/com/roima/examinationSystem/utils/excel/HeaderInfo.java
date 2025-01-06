package com.roima.examinationSystem.utils.excel;

import com.roima.examinationSystem.exception.ExcelReaderException;
import com.roima.examinationSystem.exception.ResourceNotFoundException;
import com.roima.examinationSystem.model.McqQuestions;
import com.roima.examinationSystem.model.ProgrammingQuestions;
import com.roima.examinationSystem.model.ProgrammingTestCase;
import com.roima.examinationSystem.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class HeaderInfo {
    public Boolean isNullable;
    public CellType cellType;

}


@FunctionalInterface
interface ThrowableBiConsumer<T,U>
{
    void accept(T t, U u) throws ResourceNotFoundException, ExcelReaderException;
}

class StudentHeaderInfo extends HeaderInfo{
    private final ThrowableBiConsumer<Student, Object> studentSetter;

    public StudentHeaderInfo(Boolean isNullable, CellType cellType, ThrowableBiConsumer<Student, Object> studentSetter){
        super(isNullable, cellType);
        this.studentSetter = studentSetter;
    }

    public void setValue(Student student, Object value) throws ResourceNotFoundException, ExcelReaderException {
        studentSetter.accept(student,value);
    }
}

class McqHeaderInfo extends HeaderInfo{
    private final ThrowableBiConsumer<McqQuestions,Object> mcqQuestionSetter;

    public McqHeaderInfo(Boolean isNullable, CellType cellType, ThrowableBiConsumer<McqQuestions, Object> mcqQuestionSetter){
        super(isNullable,cellType);
        this.mcqQuestionSetter = mcqQuestionSetter;
    }

    public void setValue(McqQuestions mcqQuestions, Object value) throws ResourceNotFoundException, ExcelReaderException{
        this.mcqQuestionSetter.accept(mcqQuestions,value);
    }
}

class ProgrammingHeaderInfo extends HeaderInfo {
    private final ThrowableBiConsumer<ProgrammingQuestions, Object> programmingQuestionSetter;

    public ProgrammingHeaderInfo(Boolean isNullable, CellType cellType, ThrowableBiConsumer<ProgrammingQuestions, Object> programmingQuestionSetter){
        super(isNullable,cellType);
        this.programmingQuestionSetter = programmingQuestionSetter;
    }

    public void setValue(ProgrammingQuestions programmingQuestions, Object value) throws ResourceNotFoundException, ExcelReaderException{
        this.programmingQuestionSetter.accept(programmingQuestions,value);
    }
}

class TestCaseHeaderInfo extends HeaderInfo{
    private final ThrowableBiConsumer<ProgrammingTestCase, Object> testCaseSetter;
    public TestCaseHeaderInfo(Boolean isNullable, CellType cellType, ThrowableBiConsumer<ProgrammingTestCase, Object> testCaseSetter){
        super(isNullable,cellType);
        this.testCaseSetter = testCaseSetter;
    }

    public void setValue(ProgrammingTestCase programmingTestCase, Object value) throws ResourceNotFoundException, ExcelReaderException{
        this.testCaseSetter.accept(programmingTestCase,value);
    }
}