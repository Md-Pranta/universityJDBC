//package com.university.demo.service;
//
//import com.university.demo.dao.UniversityDao;
//import com.university.demo.entity.University;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UniversityServiceTest {
//
//    private UniversityDao universityDao;
//    private UniversityService universityService;
//
//    @BeforeEach
//    void setUp() {
//        universityDao = mock(UniversityDao.class);
//        universityService = new UniversityService(universityDao);
//    }
//
//    @Test
//    void testAddUniversity() {
//        University university = new University();
//        university.setName("Test University");
//
//        when(universityDao.saveUniversity(university)).thenReturn(university);
//
//        University savedUniversity = universityService.addUniversity(university);
//
//        assertEquals(university, savedUniversity);
//        verify(universityDao, times(1)).saveUniversity(university);
//    }
//
//    @Test
//    void testGetUniversity() {
//        University university = new University();
//        university.setId(1L);
//
//        when(universityDao.getUniversityById(1L)).thenReturn(university);
//
//        University foundUniversity = universityService.getUniversity(1L);
//
//        assertEquals(1L, foundUniversity.getId());
//        verify(universityDao, times(1)).getUniversityById(1L);
//    }
//
//    @Test
//    void testGetAllUniversities() {
//        University university = new University();
//        university.setName("Test University");
//
//        when(universityDao.getAllUniversities()).thenReturn(List.of(university));
//
//        List<University> universities = universityService.getAllUniversities();
//
//        assertEquals(1, universities.size());
//        verify(universityDao, times(1)).getAllUniversities();
//    }
//}
