//package com.university.demo;
//
//
//import com.university.demo.entity.University;
//import com.university.demo.service.UniversityService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//public class UniversityServiceTests {
//
//    @Mock
//    private UniversityService universityService;
//
//    UniversityServiceTests() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllUniversitiesTest() {
//        when(universityService.getAllUniversities()).thenReturn(Collections.emptyList());
//        assertTrue(universityService.getAllUniversities().isEmpty());
//    }
//
//    @Test
//    void getUniversityByIdTest() {
//        University university = new University();
//        university.setId(1L);
//        university.setName("Test University");
//
//        when(universityService.getUniversityById(1L)).thenReturn(university);
//        assertEquals("Test University", universityService.getUniversityById(1L).getName());
//    }
//}