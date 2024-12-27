package com.roima.examinationSystem.utils;


import com.roima.examinationSystem.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Component
@RequiredArgsConstructor
public class FileManagementUtil {

    public String saveImage(MultipartFile file) throws IOException, InvalidValueException {

        try{

            String imageType = file.getContentType().split("/")[1];

            if(!imageType.equals("png") && !imageType.equals("jpg") && !imageType.equals("jpeg")){
                throw new InvalidValueException("Invalid file type.");
            }


            String uploadDIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();
            String imageName = System.currentTimeMillis()+"_" + file.getOriginalFilename();
            String imagePath = uploadDIR + "//" + imageName;

            Path path = Paths.get(imagePath);

            System.out.println(path);

            Files.copy(file.getInputStream(),path, REPLACE_EXISTING);

            return imageName;
        }catch (IOException e){
            throw new IOException("Failed to save image!");
        }
    }

    public void deleteImage(String imageName) throws IOException {
        try{
            String uploadDIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();

            Path imagePath = Paths.get(uploadDIR,imageName);

            if(Files.exists(imagePath)) {
                Files.delete(imagePath);
            }


        } catch (IOException e) {
            throw new IOException("Failed to delete image.");
        }
    }

    public String getLiveImagePath(String image){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/").path(image).toUriString();
    }

}
