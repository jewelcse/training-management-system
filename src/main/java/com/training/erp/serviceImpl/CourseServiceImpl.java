package com.training.erp.serviceImpl;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.User;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.AddTrainerRequest;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.request.RemoveTrainerRequest;
import com.training.erp.model.response.CourseDetails;
import com.training.erp.model.response.CourseResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private final AssignmentMapper assignmentMapper;
    private final UserMapper userManager;

    @Override
    public CourseResponse save(CourseCreateRequest request) {
        if (existsByCourse(request.getCourseName())) {
            throw new RuntimeException("ALREADY EXISTS!");
        }
        //course
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseDescription(request.getCourseDescription());
        //save
        Course response = courseRepository.save(course);
        //response
        return courseMapper.courseToCourseResponseDto(response);
    }

    @Override
    public CourseResponse update(CourseUpdateRequest request) {
        Course course = getCourse(request.getId());
        course.setId(course.getId());
        course.setCourseName(request.getCourseName());
        course.setCourseDescription(request.getCourseDescription());
        return courseMapper.courseToCourseResponseDto(courseRepository.save(course));
    }

    @Override
    public List<CourseResponse> getCourses() {
        return courseMapper.courseListToCourseResponseDtoList(courseRepository.findAll());
    }

    @Override
    public CourseDetails getCourseById(long id) {
        Course course = getCourse(id);
        return CourseDetails.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .trainer(userManager.userToUserDetails(course.getTrainer()))
                .assignments(assignmentMapper.assignmentsToAssignmentsResponse(course.getAssignments()))
                .build();
    }

    @Override
    public MessageResponse addTrainerToCourse(AddTrainerRequest request) {
        Course course = getCourse(request.getCourseId());
        User trainer = getUser(request.getTrainerId());
        // add trainer
        course.setTrainer(trainer);
        // save
        courseRepository.save(course);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }

    @Override
    public MessageResponse removeTrainerFromCourse(RemoveTrainerRequest request) {
        Course course = getCourse(request.getCourseId());
        User trainer = getUser(request.getTrainerId());
        // add trainer
        course.setTrainer(null);
        // save
        courseRepository.save(course);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }

    @Override
    public void deleteCourseById(long id) {
        courseRepository.deleteById(id);
    }
    @Override
    public boolean existsByCourse(String courseName) {
        return courseRepository.existsByCourseName(courseName);
    }

    private Course getCourse(long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new CourseNotFoundException("COURSE NOT FOUND!");
        }
        return course.get();
    }

    private User getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("COURSE NOT FOUND!");
        }
        return user.get();
    }

}
