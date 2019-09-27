package com.grokonez.jwtauthentication.controller;


import com.grokonez.jwtauthentication.model.Consignment;
import com.grokonez.jwtauthentication.model.Project;
import com.grokonez.jwtauthentication.repository.ProjectRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping(path = "project/{id}")
    public @ResponseBody
    Optional<Project> getProjectById(@PathVariable("id") Integer id){
        long lid = id.longValue();
        return projectRepository.findById(lid);
    }


    @GetMapping(path = "project/all")
    public @ResponseBody Iterable<Project> getAllProject(){
        return projectRepository.findAll();
    }

    @GetMapping(path = "project/findByStatusAndMoney")
    public @ResponseBody Iterable<Project> getProjectByStatusAndMoney(){
        return projectRepository.findByStatusAndMoneyFinal();
    }

    @RequestMapping(path = "project/new", method = RequestMethod.POST)
    public Integer createProject(@RequestBody Project project) {

        projectRepository.save(project);
        return 1;


    }


    @PutMapping("/project/{id}")
    public Project updateProject(@PathVariable Long id,
                                     @Valid @RequestBody Project projectUpdate) throws NotFoundException {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setStatus(projectUpdate.getStatus());
                    project.setMoney_initial(projectUpdate.getMoney_initial());
                    project.setMoney_final(projectUpdate.getMoney_final());

                    //project.setStatus(projectUpdate.getStatus());
                    //project.setGovernance(projectUpdate.getGovernance());

                    return projectRepository.save(project);
                }).orElseThrow(() -> new NotFoundException("project not found with id " + id));
    }

    @DeleteMapping("/project/{id}")
    public Integer deleteProject(@PathVariable long id) {

        projectRepository.deleteById(id);
        return 1;
    }

}
