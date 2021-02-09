package com.nizzoli.ppmtool.services;

import com.nizzoli.ppmtool.domain.Backlog;
import com.nizzoli.ppmtool.domain.Project;
import com.nizzoli.ppmtool.domain.ProjectTask;
import com.nizzoli.ppmtool.repositories.BacklogRepository;
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

   public Iterable<ProjectTask> findBacklogById (String id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
