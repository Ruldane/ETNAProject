package com.nizzoli.tasksManager.services;
import com.nizzoli.tasksManager.domain.Backlog;
import com.nizzoli.tasksManager.domain.Project;
import com.nizzoli.tasksManager.domain.User;
import com.nizzoli.tasksManager.exceptions.ProjectIdException;
import com.nizzoli.tasksManager.exceptions.ProjectNotFoundException;
import com.nizzoli.tasksManager.repositories.BacklogRepository;
import com.nizzoli.tasksManager.repositories.ProjectRepository;
import com.nizzoli.tasksManager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){

        if (project.getId() != null){
            Project existproject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existproject != null && (!existproject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Le projet n'a pas été trouvé sur votre compte");
            } else  if(existproject == null){
                throw new ProjectNotFoundException("Projet avec ID: '" +project.getProjectIdentifier() + "' ne peut pas être modifié car il n'existe pas" );
            }
        }

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(username);
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId()==null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Projet ID '"+project.getProjectIdentifier().toUpperCase()+"' existe déjà");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Projet ID '"+projectId+" n'exsiste pas");
        }

        if (!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Le projet n'existe pas avec votre compte");
        }

        return project;
    }
    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectid, String username){
        projectRepository.delete(findProjectByIdentifier(projectid, username));
    }
}
