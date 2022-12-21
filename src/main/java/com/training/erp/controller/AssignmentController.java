package com.training.erp.controller;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.AssignmentSubmission;
import com.training.erp.entity.User;
import com.training.erp.exception.*;
import com.training.erp.model.request.AssignmentRequestDto;
import com.training.erp.model.request.AssignmentSubmissionUpdateRequest;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.model.response.TraineeAssignmentResponse;
import com.training.erp.repository.AssignmentSubmissionRepository;
import com.training.erp.service.AssignmentService;
import com.training.erp.service.UserService;
import com.training.erp.util.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class
AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private FilesStorageService filesStorageService;
    @Autowired
    private UserService userService;
    @Autowired
    private AssignmentSubmissionRepository traineesAssignmentSubmissionRepository;
    @Value("${fileLink}")
    private String link;
    // Create new assignment
    //@Secured("ROLE_TRAINER")
    @PostMapping("/assignments")
    public ResponseEntity<MessageResponse> create(@RequestBody AssignmentRequestDto assignmentRequestDto, Principal principal) throws BatchNotFoundException, UserNotFoundException, CourseNotFoundException {
        assignmentService.save(assignmentRequestDto,principal);
        return new ResponseEntity<>(new MessageResponse("Assignment Created Successfully"), HttpStatus.CREATED);
    }
    // Get all assignment
    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAssignments(){
        return ResponseEntity.ok(assignmentService.getAssignments());
    }
    // Get all assignments by trainer
    @Secured("ROLE_TRAINER")
    @GetMapping("/assignments/trainer")
    public ResponseEntity<List<Assignment>> getAssignmentsByUser(Principal principal) {
        return ResponseEntity.ok(assignmentService.getAssignments(principal));
    }
    // Gets assignment by course ID
    @GetMapping("/assignments/course/{course-id}")
    public ResponseEntity<List<Assignment>> getAssignmentsByCourse(@PathVariable("course-id") long courseId) throws CourseNotFoundException {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(courseId));
    }

    // Get assignment by assignment ID
    @GetMapping("/assignments/{assignment-id}")
    public ResponseEntity<Assignment> getAssignmentByAssignmentId(@PathVariable("assignment-id") long assignmentId) throws AssignmentNotFoundException {
        return ResponseEntity.ok(assignmentService.getAssignmentByAssignmentId(assignmentId));
    }

    // Get list of trainees assignment submissions by assignment ID
    @GetMapping("/assignments/submissions/{assignment-id}")
    public ResponseEntity<TraineeAssignmentResponse> getAssignmentSubmissionByAssignmentId(@PathVariable("assignment-id") long assignmentId) throws AssignmentNotFoundException {
        Assignment assignment = assignmentService.getAssignmentByAssignmentId(assignmentId);
        return ResponseEntity.ok(new TraineeAssignmentResponse(assignment,assignmentService.getAssignmentSubmissionByAssignmentId(assignmentId)));
    }

    // Get trainees single submission by assignment submission ID
    @GetMapping("/assignments/trainees/submissions/{submission-id}")
    public ResponseEntity<AssignmentSubmission> getUserSubmissionBySubmissionId(@PathVariable("submission-id") long submissionId) throws TraineesAssignmentSubmissionNotFoundException {
        return ResponseEntity.ok(assignmentService.getTraineesSubmissionBySubmissionId(submissionId));
    }


    // Trainer update the trainees' assignment
    // Trainer can evaluate the trainees' submission
    // Added marks for the submitted assignment
    @PostMapping("/assignments/trainees/submissions")
    public ResponseEntity<?> updateUserSubmission(@RequestBody AssignmentSubmissionUpdateRequest submission) throws TraineesAssignmentSubmissionNotFoundException {
        assignmentService.updateSubmission(submission);
        return ResponseEntity.ok("Updated");
    }

    // Delete assignment by assignment ID
    @DeleteMapping("/assignments/{assignment-id}")
    public ResponseEntity<?> deleteAssignmentByAssignmentId(@PathVariable("assignment-id") long assignmentId) throws AssignmentNotFoundException {
        assignmentService.deleteAssignmentByAssignmentId(assignmentId);
        return ResponseEntity.ok(new MessageResponse("Assignment deleted successfully"));
    }

    // Get all trainees submissions in a single list
    @GetMapping("/assignments/trainees")
    public ResponseEntity<List<AssignmentSubmission>> getSubmissionsByUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        // Check the user is valid or not
        // Find the user by username
        User user = userService.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for username "));
        // Find the trainee profile by user
//        Trainee trainee = userService.getTraineeProfile(user);
        return ResponseEntity.ok(null);

    }
    // Submit the assignment by trainee
    @PostMapping("/assignments/trainees")
    public ResponseEntity<?> uploadAssignment(@RequestParam("file") MultipartFile file,@RequestParam("assignmentId") long assignmentId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username="";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        // Find the user by username
        User user = userService.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for username "));

        Assignment assignment = assignmentService.getAssignmentByAssignmentId(assignmentId);
        boolean doesSubmitted
                = traineesAssignmentSubmissionRepository.existsById(assignmentId);
        // Check the assignment is submitted or not
        if (doesSubmitted){
            return ResponseEntity
                    .badRequest()
                    .body("Already Submitted!");
        }
        // If not then,
        // Create a new object of the submission entity
        AssignmentSubmission traineesAssignmentSubmission
                = new AssignmentSubmission();
        String message = "";
        try {
            // Save the pdf/ image/ docs file to upload folder
            String filePath = filesStorageService.saveFile(file);
            message = "Submitted the Assignment successfully";
            traineesAssignmentSubmission.setFileLocation(link+filePath);
            traineesAssignmentSubmission.setObtainedMarks(0);
            traineesAssignmentSubmission.setAssignment(assignment);
            traineesAssignmentSubmission.setUser(user);
            traineesAssignmentSubmissionRepository.save(traineesAssignmentSubmission);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "Could not upload the Assignment: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    // Get the single downloadable assignment submissions
    // As a submitted form like [pdf,img,docs] etc
    @GetMapping("/assignments/trainees/submissions/file/{filename:.+}")
    public ResponseEntity<Resource> getSingleFile(@PathVariable String filename, HttpServletRequest request) throws IOException {
        Resource resource = filesStorageService.load(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            //System.out.println(resource.getFile().getAbsolutePath() + " content");
        } catch (IOException ex) {
            System.out.println("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        //System.out.println(resource.getFilename() + " resoyrce");
        return ResponseEntity.ok() .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

    }



}
