package com.grokonez.jwtauthentication.controller;

import com.grokonez.jwtauthentication.model.Budget;
import com.grokonez.jwtauthentication.repository.BudgetRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

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


    //method that return a new budget
    @RequestMapping(path = "budget/new", method = RequestMethod.POST)
    public Integer CreateUser(@RequestBody Budget budget) {

        budgetRepository.save(budget);
        return 1;


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
