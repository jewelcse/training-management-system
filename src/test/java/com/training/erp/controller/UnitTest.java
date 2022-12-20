package com.training.erp.controller;

import com.training.erp.entity.*;
import com.training.erp.repository.BatchRepository;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UnitTest {
    @Autowired
    UserService userService;

    @Autowired
    UserController userController;

    @Autowired
    BatchService batchService;

    @Autowired
    CourseService courseService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TrainerRepository trainerRepository;


    @MockBean
    BatchRepository batchRepository;

    @MockBean
    CourseRepository courseRepository;


    @Before
    public void setup()
    {
        System.out.println("[Started] - Testing...");
    }


    @Test
    void addUserTest(){
        // create a user object
        User request = new User();
        request.setUsername("Sabujcse");
        request.setEmail("sabuj@gmail.com");
        request.setPassword("sabuj");
        request.setRoles(new HashSet<>());
        request.setEnabled(true);
        request.setNonLocked(true);
        userRepository.save(request);
        // check
        verify(userRepository, times(1)).save(request);
    }

    @Test
    public void checkUserNameTest() {

        User request = new User();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("admin");
        request.setRoles(new HashSet<>());
        request.setEnabled(true);
        request.setNonLocked(true);

        Optional<User> optUser = Optional.of(request);
        when(userRepository.findById(1L)).thenReturn(optUser);
        assertTrue(userRepository.findById(1l).get().getUsername().contains("admin"));

    }


    @Test
    public void getBatchListTest() {
        Timestamp times = null;
        Set<Trainer> trainers = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();
        Set<Course> courses = new HashSet<>();
        List<Batch> batches = Arrays.asList(
                new Batch(1L,"Java Batch","This is a java batch", null, null,trainees,trainers,courses),
                new Batch(2L,"MERN Batch","This is a mern batch", null, null,trainees,trainers,courses),
                new Batch(3L,"Android Batch","This is a android batch", null, null,trainees,trainers,courses)
        );
        when(batchRepository.findAll()).thenReturn(batches);
        assertEquals(3, batchService.getAllBatch().size());
    }


    @Test
    public void getCourseListTest() {
        Trainer trainer = new Trainer();
        List<Assignment> assignments = new ArrayList<>();
        Set<Batch> batches = new HashSet<>();

        List<Course> courses = Arrays.asList(
                new Course(1L,"Spring Boot Course","This is spring boot course",trainer,batches,assignments),
                new Course(2L,"Spring Security","This is spring security course",trainer,batches,assignments),
                new Course(3L,"CSS3","This is css3  course",trainer,batches,assignments),
                new Course(4L,"React JS","This is react course",trainer,batches,assignments)

        );
        when(courseRepository.findAll()).thenReturn(courses);

        assertEquals(4, courseService.getCourses().size());
    }




//    @Test
//    public void checkIfUSerIsNull() {
//        assertNull(Optional.of(userRepository.findById(1l).get()));
//    }
//
//    @Test
//    public void checkIfUsersNull2() {
//        assertNull(Optional.of(userRepository.findById(2l).get()));
//    }

    @After
    public void end()
    {
        System.out.println("[Ended] - Testing");
    }




}