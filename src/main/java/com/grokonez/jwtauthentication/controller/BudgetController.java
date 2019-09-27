package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Budget;
import com.grokonez.jwtauthentication.repository.BudgetRepository;
import com.grokonez.jwtauthentication.repository.ProjectRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ProjectRepository projectRepository;




    //metodo que retorna un presupuesto de acuerdo a su id

    @GetMapping(path = "budget/{id}")
    public @ResponseBody
    Optional<Budget> getBudget(@PathVariable("id") Integer id){
        long lid = id.longValue();
        return budgetRepository.findById(lid);
    }


    //method get that extracts budgets from database

    @GetMapping(path = "budget/all")
    public @ResponseBody Iterable<Budget> getAllBudgets(){
        return budgetRepository.findAll();
    }

    //method that return budgets status "activo"

    @GetMapping(path = "budget/allActive")
    public @ResponseBody Iterable<Budget> getAllBudgetActive(){
        return budgetRepository.findAllByActive();
    }

    //method that return budgets status "inactivo"

    @GetMapping(path= "budget/allInactive")
    public @ResponseBody Iterable<Budget> getAllBudgetsInactive(){return budgetRepository.findAllByInactive();}


    @GetMapping(path = "budget/budgetFindByIdProject/{projectId}")
    public @ResponseBody
    Iterable<Budget> getBudgetByIdProject(@PathVariable Long projectId){

        return budgetRepository.findByIdProject(projectId);

    }

    @GetMapping(path = "budget/budgetByStatusAndMoney")
    public @ResponseBody
    Iterable<Budget> getBudgetByStatusAndMoney(){

        return budgetRepository.findByStatusAndMoney();

    }

    @GetMapping(path = "budget/budgetByGovernanceAndMoney/{governance}")
    public @ResponseBody
    Iterable<Budget> getBudgetByGovernanceAndMoney(@PathVariable String governance){

        return budgetRepository.findByGovernanceAndMoney(governance);

    }



    @PostMapping(value = "/budget/{projectId}/new")

    public Budget createBudget(@PathVariable Long projectId,

                        @Valid @RequestBody Budget budget) throws NotFoundException {
        return projectRepository.findById(projectId)
                .map(project -> {
                    if(!(budget.getStatus_approbation().equals("PENDIENTE")) ){
                        BigDecimal finalMoney= project.getMoney_final().subtract(budget.getBudget());
                        project.setMoney_final(finalMoney);
                    }

                    projectRepository.save(project);
                    budget.setProject(project);

                    return budgetRepository.save(budget);
                }).orElseThrow(()-> new NotFoundException("Proyecto no existe!"));

    }

    @PutMapping(value = "/budget/{projectId}/aprobar")
    public Budget updateBudgetAprobar(@PathVariable Long projectId,
                               @Valid @RequestBody Budget budgetUpdated) throws NotFoundException {

        return projectRepository.findById(projectId)
                .map(project -> {
                    if(budgetUpdated.getStatus_approbation().equals("APROBADO") ){
                        BigDecimal finalMoney= project.getMoney_final().subtract(budgetUpdated.getBudget());
                        project.setMoney_final(finalMoney);
                    }

                    projectRepository.save(project);
                    Budget bu = budgetRepository.findById2(budgetUpdated.getBudget_id());
                    bu.setProject(project);
                    bu.setStatus_approbation(budgetUpdated.getStatus_approbation());
                    bu.setDateChange(budgetUpdated.getDateChange());

                    return budgetRepository.save(bu);
                }).orElseThrow(()-> new NotFoundException("Proyecto no existe!"));

    }


    //method for update the budget
    @PutMapping("/budget/{id}")
    public Budget updateBudget(@PathVariable Long id,
                               @Valid @RequestBody Budget budgetUpdated) throws NotFoundException {
        return budgetRepository.findById(id)
                .map(budget -> {
                    budget.setGovernance(budgetUpdated.getGovernance());
                    budget.setBudget(budgetUpdated.getBudget());
                    budget.setDate(budgetUpdated.getDate());
                    budget.setObservation(budgetUpdated.getObservation());
                    budget.setDateChange(budgetUpdated.getDateChange());

                    return budgetRepository.save(budget);
                }).orElseThrow(() -> new NotFoundException("budget not found with id " + id));
    }


    //method for update status of budget

    @PutMapping("/budget/{id}/{status}")
    public Budget updateStatusBudget(@PathVariable Long id,
                                     @PathVariable String status,
                                     @Valid @RequestBody Budget budgetUpdated) throws NotFoundException {

        return budgetRepository.findById(id)
                .map(budget -> {

                    if(budget.getStatus() == "activo" ){
                        budget.setStatus(status);

                    }
                    else if(budget.getStatus()=="inactivo"){
                        budget.setStatus(status);
                    }
                    else{
                        budget.setStatus(status);
                    }


                    return budgetRepository.save(budget);
                }).orElseThrow(() -> new NotFoundException("Budget not found!"));

    }

    //method that delete budget for id

    @DeleteMapping("/budget/{id}")
    public Integer deleteBudget(@PathVariable long id) {

        budgetRepository.deleteById(id);
        return 1;
    }
}
