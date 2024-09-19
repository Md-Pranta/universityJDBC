package com.university.demo.entity;



import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class University {
    private Long id;
    private String name;
    private String address;
    private UniversityType universityType;
    private double rating;
    private String description;
    private String image;
    private LocalDate startingDate;
    private LocalDateTime casuallyOpensAt;
    private Map<String, Object> otherInformation;

    public University(){}

    public University(Long id, String name, String address, UniversityType universityType, double rating, String description,
                      String imagePath, LocalDate startingDate, LocalDateTime casuallyOpensAt, Map<String, Object> otherInformation) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.universityType = universityType;
        this.rating = rating;
        this.description = description;
        this.image = imagePath;
        this.startingDate = startingDate;
        this.casuallyOpensAt = casuallyOpensAt;
        this.otherInformation = otherInformation;
    }
}
