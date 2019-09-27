package com.grokonez.jwtauthentication.repository;


import com.grokonez.jwtauthentication.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query(value = "SELECT * FROM budgets WHERE STATUS= 'activo' ", nativeQuery = true)
    Iterable<Budget> findAllByActive();

    @Query(value = "SELECT * FROM budgets WHERE STATUS= 'inactivo' ", nativeQuery = true)
    Iterable<Budget> findAllByInactive();

    @Query(value = "SELECT * FROM budgets WHERE PROJECT_ID= ?1", nativeQuery = true)
    Iterable<Budget> findByIdProject(Long projectId);

    @Query(value = "SELECT * FROM budgets WHERE BUDGET_ID= ?1", nativeQuery = true)
    Budget findById2(Long budgetId);

    @Query(value = "SELECT * FROM budgets WHERE STATUS_APPROBATION='APROBADO' AND BUDGET > 0 ", nativeQuery = true)
    Iterable<Budget> findByStatusAndMoney();

    @Query(value = "SELECT * FROM budgets WHERE GOVERNANCE= ?1 AND BUDGET > 0 AND STATUS_APPROBATION='APROBADO' ", nativeQuery = true)
    Iterable<Budget> findByGovernanceAndMoney(String governance);


}
