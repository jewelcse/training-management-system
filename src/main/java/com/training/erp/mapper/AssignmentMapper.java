package com.training.erp.mapper;

import com.training.erp.entity.Assignment;
import com.training.erp.model.response.AssignmentResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssignmentMapper {

    public AssignmentResponse assignmentToAssignmentResponse(Assignment assignment){
        if (assignment == null) return null;
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .marks(assignment.getTotalMarks())
                .courseName(assignment.getCourse().getCourseName())
                .filePath(assignment.getFileLocation())
                .build();
    }
    public List<AssignmentResponse> assignmentsToAssignmentsResponse(List<Assignment> assignments) {
        List<AssignmentResponse> assignmentResponses = new ArrayList<>();
        assignments.forEach( assignment -> assignmentResponses.add(assignmentToAssignmentResponse(assignment)));
        return assignmentResponses;
    }
}
