package com.training.erp.serviceImpl;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.User;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.CourseRequestDto;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.response.CourseDetails;
import com.training.erp.model.response.CourseResponse;
import com.training.erp.repository.CourseRepository;
import com.training.erp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseManager;

    @Autowired
    private AssignmentMapper assignmentMapper;

    @Autowired
    private UserMapper userManager;


    @Override
    public CourseResponse save(CourseRequestDto request) {
        //course
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseDescription(request.getCourseDescription());
        //save
        Course response = courseRepository.save(course);
        //response
        return courseManager.courseToCourseResponseDto(response);
    }


    @Override
    public boolean existsByCourse(String courseName) {
        return courseRepository.existsByCourseName(courseName);
    }

    @Override
    public List<CourseResponse> getCourses() {
        return courseManager.courseListToCourseResponseDtoList(courseRepository.findAll());
    }

    @Override
    public void updateCourse(CourseUpdateRequest request){
        Course course = courseRepository.findById(request.getCourse_id())
                        .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        course.setId(course.getId());
        course.setCourseName(request.getCourse_name());
        course.setCourseDescription(request.getCourse_description());
        courseRepository.save(course);
    }


    @Override
    public void deleteCourseByCourseId(long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public CourseDetails getCourseById(long id){

        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()){
            throw new CourseNotFoundException("COURSE NOT FOUND!");
        }

        return CourseDetails.builder()
                .id(course.get().getId())
                .courseName(course.get().getCourseName())
                .courseDescription(course.get().getCourseDescription())
                .trainer(userManager.userToUserDetails(course.get().getTrainer()))
                .assignments(assignmentMapper.assignmentsToAssignmentsResponse(course.get().getAssignments()))
                .build();
    }

    @Override
    public List<Course> getCoursesByBatch(Batch batch) {
        return null;
    }

}
