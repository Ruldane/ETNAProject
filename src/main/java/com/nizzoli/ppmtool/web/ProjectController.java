package com.nizzoli.ppmtool.web;

import com.nizzoli.ppmtool.domain.Project;
import com.nizzoli.ppmtool.services.MapValidationErrorService;
import com.nizzoli.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    // create a project
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){

        ResponseEntity<?>errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap!=null) return errorMap;

        Project project1 = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
    }

    // delete a project
    @GetMapping("/{projectId}") // {pathVariable}
    public ResponseEntity<?>getProjectById(@PathVariable String projectId){

        Project project = projectService.findProjectByIdentifier(projectId);
        return  new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProject(){
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId){
        projectService.deleteProjectByIdentifier(projectId);

        return new ResponseEntity<String>("Project with ID:" + projectId + "was deleted", HttpStatus.OK);
    }
}