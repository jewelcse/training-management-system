package com.training.erp.serviceImpl;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.model.request.CourseRequestDto;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.response.CourseResponseDto;
import com.training.erp.repository.CourseRepository;
import com.training.erp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Override
    public CourseResponseDto save(CourseRequestDto request) {
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setCourseDescription(request.getCourseDescription());
        courseRepository.save(course);
        return CourseResponseDto.builder()
                .courseName(request.getCourseName())
                .courseDescription(request.getCourseDescription())
                .build();
    }


    @Override
    public boolean existsByCourse(String courseName) {
        return courseRepository.existsByCourseName(courseName);
    }

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void updateCourse(CourseUpdateRequest request) throws CourseNotFoundException {
        Course course = courseRepository.findById(request.getCourse_id())
                        .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        course.setId(course.getId());
        course.setCourseName(request.getCourse_name());
        course.setCourseDescription(request.getCourse_description());
        courseRepository.save(course);
    }


    @Override
    public void deleteCourseByCourseId(long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public Course getCourseByCourseId(long courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("Course not found"));
    }

    @Override
    public List<Course> getCoursesByBatch(Batch batch) {
        return null;
    }

}
