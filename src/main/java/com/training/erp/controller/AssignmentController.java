package com.training.erp.controller;

import com.training.erp.entity.AssignmentSubmission;
import com.training.erp.entity.User;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.model.request.AssignmentCreateRequest;
import com.training.erp.model.request.AssignmentEvaluateRequest;
import com.training.erp.model.response.*;
import com.training.erp.repository.AssignmentSubmissionRepository;
import com.training.erp.service.AssignmentService;
import com.training.erp.service.UserService;
import com.training.erp.util.FilesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final FilesStorageService filesStorageService;

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentResponse> create(@RequestBody AssignmentCreateRequest request) {
        return new ResponseEntity<>(assignmentService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentResponse>> getAll() {
        return ResponseEntity.ok(assignmentService.getAssignments());
    }

    @GetMapping("/assignments/course/{id}")
    public ResponseEntity<List<AssignmentResponse>> getAssignmentsByCourse(@PathVariable("id") long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByCourse(id));
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<AssignmentResponse> getAssignment(@PathVariable("id") long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    @GetMapping("/assignments/{id}/submissions")
    public ResponseEntity<?> getSubmissionsByAssignment(@PathVariable("id") long assignmentId) {
        return ResponseEntity.ok(assignmentService.getSubmissionsByAssignmentId(assignmentId));
    }

    @GetMapping("/assignments/trainees/submissions/{id}")
    public ResponseEntity<AssignmentSubmissionResponse> getSubmissionById(@PathVariable("id") long submissionId) {
        return ResponseEntity.ok(assignmentService.getSubmissionById(submissionId));
    }

    @PostMapping("/assignments/evaluate")
    public ResponseEntity<UpdatedSubmissionResponse> evaluateAssignment(@RequestBody AssignmentEvaluateRequest request) {
        return ResponseEntity.ok(assignmentService.updateSubmission(request));
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") long id) {
        assignmentService.deleteAssignmentById(id);
        return ResponseEntity.ok(new MessageResponse("Assignment deleted successfully"));
    }

    @GetMapping("/assignments/my/submissions")
    public ResponseEntity<List<SubmissionResponse>> studentSubmissions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            System.out.println("in controller username: " + username);
        }
        return ResponseEntity.ok(assignmentService.getSubmissionsByStudent(username));

    }

    @PostMapping("/assignments/submit")
    public ResponseEntity<?> submit(@RequestParam("file") MultipartFile file, @RequestParam("assignmentId") long assignmentId, @RequestParam("studentId") long studentId) {
        boolean submitted = assignmentService.submitAssignment(file, assignmentId, studentId);
        if (submitted) return new ResponseEntity<>("Assignment Uploaded success", HttpStatus.CREATED);
        return new ResponseEntity<>("Assignment Uploaded failed", HttpStatus.BAD_REQUEST);
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
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        //System.out.println(resource.getFilename() + " resoyrce");
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

    }


}
