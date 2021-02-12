package com.nizzoli.ppmtool.services;

import com.nizzoli.ppmtool.domain.Backlog;
import com.nizzoli.ppmtool.domain.Project;
import com.nizzoli.ppmtool.domain.ProjectTask;
import com.nizzoli.ppmtool.exceptions.ProjectNotFoundException;
import com.nizzoli.ppmtool.repositories.BacklogRepository;
import com.nizzoli.ppmtool.repositories.ProjectRepository;
import com.nizzoli.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask (String projectIdentifier, ProjectTask projectTask) {
        // Project taks for a specific projet, project != nul, BL exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        // set the backlog to project taks
        projectTask.setBacklog(backlog);
        // we cant our project sequence like this : IDPRO-1 IDPRO-2
        Integer BacklogSequence = backlog.getPTSequence();
        // 0,1,2 etc... update
        BacklogSequence++;
        backlog.setPTSequence(BacklogSequence);
        //add sequence to project task
        projectTask.setProjectSequence(projectIdentifier+"_"+BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //INITIAL priority when priority null
        if (projectTask.getPriority()==null){
            projectTask.setPriority(3);
        }
        // Initial status when staatus is null
        if (projectTask.getStatus() =="" || projectTask.getStatus()  ==null){
            projectTask.setStatus( "TO_DO");
        }
        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask>findBacklogById(String id){

        Project project = projectRepository.findByProjectIdentifier(id);

        if(project==null){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String projectTask_id){

        //make sure we are searching on an existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectTask_id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+projectTask_id+"' not found");
        }
        //make sure that the backlog/project id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+projectTask_id+"' does not exist in project: '"+backlog_id);
        }
        return projectTask;
    }

    public ProjectTask updateByProjectSequence (ProjectTask updatedTask, String backlog_id, String projectTask_id) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, projectTask_id);
        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deleteProjectTaskByProjectSequence(String backlog_id, String projectTask_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, projectTask_id);
        projectTaskRepository.delete(projectTask);
    }
}
