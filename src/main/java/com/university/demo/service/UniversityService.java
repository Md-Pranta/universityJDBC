package com.university.demo.service;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.demo.dao.UniversityDao;
import com.university.demo.entity.University;
import com.university.demo.entity.UniversityType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UniversityService {
    @Autowired
    private UniversityDao universityDao;

    @Autowired
    ObjectMapper objectMapper;
    //image will save in this directory
    @Value("${file.upload-dir}")
    private String imageUploadDirectory;


    //parsing the university
    public University parseUniversityJson(String universityJson) throws JsonProcessingException {
        return objectMapper.readValue(universityJson, University.class);
    }

    //for handle the save method of a post
    @Transactional
    public University createUniversity(University university, MultipartFile imageFile) throws IOException, SQLException {
        if(imageFile != null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile);
            university.setImage(imagePath);
        }
        return universityDao.saveUniversity(university);
    }
    //for updating the data and checking image is present then update
    @Transactional
    public University updateUniversity(Long id, University universityDetails, MultipartFile imageFile) throws IOException, SQLException {
        University university = universityDao.getUniversityById(id).orElseThrow(()->
                new ResourceNotFoundException("University not found with id: "+id));
        university.setName(universityDetails.getName());
        university.setAddress(universityDetails.getAddress());
        university.setUniversityType(universityDetails.getUniversityType());
        university.setRating(universityDetails.getRating());
        university.setDescription(universityDetails.getDescription());
        university.setStartingDate(universityDetails.getStartingDate());
        university.setCasuallyOpensAt(universityDetails.getCasuallyOpensAt());
        university.setOtherInformation(universityDetails.getOtherInformation());

        if (imageFile!= null && !imageFile.isEmpty()){
            String imagePath = saveImage(imageFile);
            university.setImage(imagePath);
        }
        return universityDao.saveUniversity(university);

    }
    //get all the universities as a list
    public List<University>getAllUniversities(){
        return universityDao.getAllUniversities();
    }

    //Find university by id
    public University getUniversityById(Long id){
        return universityDao.getUniversityById(id).orElseThrow(()->
               new ResourceNotFoundException("University not found with id: "+id));
    }

    //delete university
    @Transactional
    public void deleteUniversity(Long id){
        University university = getUniversityById(id);
        if (university.getImage() != null){
            deleteImage(university.getImage());
        }
        universityDao.deleteUniversity(id);
    }


    //search in university by Terms
    public List<University> searchUniversities(String name, String address, UniversityType universityType, Double ratingFrom, Double ratingTo, String description, LocalDateTime casuallyOpensAtFrom, LocalDateTime casuallyOpensAtTo) {
        return universityDao.searchUniversities(name, address, universityType, ratingFrom, ratingTo, description,casuallyOpensAtFrom, casuallyOpensAtTo);
    }

    //save the image in the file System
    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID().toString()+"_"+imageFile.getOriginalFilename();
        Path filePath = Paths.get(imageUploadDirectory, fileName);
        Files.copy(imageFile.getInputStream(), filePath);
        return filePath.toString();
    }

    //delete the image if post is delete
    private void deleteImage(String imagePath){
        try {
            Path filePath = Paths.get(imageUploadDirectory+imagePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image from file that is missing or else"+e);
        }
    }



}
