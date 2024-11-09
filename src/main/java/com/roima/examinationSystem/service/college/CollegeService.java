package com.roima.examinationSystem.service.college;

import com.roima.examinationSystem.dto.CollegeDto;
import com.roima.examinationSystem.exception.CollegeExistsException;
import com.roima.examinationSystem.exception.CollegeNotFoundException;
import com.roima.examinationSystem.exception.InvalidParametersException;
import com.roima.examinationSystem.model.College;
import com.roima.examinationSystem.repository.CollegeRepository;
import com.roima.examinationSystem.request.AddCollegeRequest;
import com.roima.examinationSystem.request.UpdateCollegeRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CollegeService implements ICollegeService{

    private final CollegeRepository collegeRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    @Override
    public College getCollegeById(int id) throws CollegeNotFoundException {
        return collegeRepository.findById(id).orElseThrow(()->new CollegeNotFoundException("College not found!"));
    }

    @Override
    public College getCollegeByName(String name) throws CollegeNotFoundException {

        College college = collegeRepository.findByName(name);

        if(college== null){
            throw new CollegeNotFoundException("College not found!");
        }else{
            return college;
        }
    }

    @Override
    public void addCollege(AddCollegeRequest request) throws CollegeExistsException, InvalidParametersException {

            boolean isPresentedName = collegeRepository.existsByName(request.getName());
            boolean isPresentedEmail = collegeRepository.existsByEmail(request.getEmail());
            if(isPresentedName || isPresentedEmail){
                throw new CollegeExistsException("College already exists!");
            }
            College newCollege;
            if(!(request.getName() == null) && !(request.getAddress()==null) && !(request.getEmail()==null)){
                newCollege = new College(request.getName(),request.getEmail(),request.getAddress());
            }else if(!(request.getName()==null) && !(request.getEmail()==null)){
                newCollege = new College(request.getName(),request.getEmail());
            }
            else{
                throw new InvalidParametersException("Invalid parameters!!!");
            }
            collegeRepository.save(newCollege);

    }

    @Override
    public void updateCollege(UpdateCollegeRequest request, int id) throws CollegeExistsException, CollegeNotFoundException, InvalidParametersException {
        College oldCollege = getCollegeById(id);

        if(!Objects.equals(oldCollege.getName(), request.getName()) && collegeRepository.existsByName(request.getName())){
            throw new CollegeExistsException("New College name already exists!");
        }

        if(!Objects.equals(oldCollege.getEmail(), request.getEmail()) && collegeRepository.existsByEmail(request.getEmail())){
            throw new CollegeExistsException("New College email already exists!");
        }

        if(!(request.getName()==null) && !(request.getAddress()==null) && !(request.getEmail()==null)){
            oldCollege.setName(request.getName());
            oldCollege.setAddress(request.getAddress());
            oldCollege.setEmail(request.getEmail());
        }else if(!(request.getName()==null) && !(request.getEmail()==null)){
            oldCollege.setName(request.getName());
            oldCollege.setEmail(request.getEmail());
        }
        else{
            throw new InvalidParametersException("Invalid parameters!!!");
        }
        collegeRepository.save(oldCollege);

    }

    @Override
    public void deleteCollegeById(int id) throws CollegeNotFoundException {
        College college = getCollegeById(id);
        collegeRepository.delete(college);
    }

    public List<CollegeDto> getConvertedDtoList(List<College> collegeList){
        return collegeList.stream().map(this::convertToDto).toList();
    }

    public CollegeDto convertToDto(College college){
        return modelMapper.map(college, CollegeDto.class);
    }
}
