package com.grokonez.jwtauthentication.repository;


import com.grokonez.jwtauthentication.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query(value = "SELECT * FROM budgets WHERE STATUS= 'activo' ", nativeQuery = true)
    Iterable<Budget> findAllByActive();

    @Query(value = "SELECT * FROM budgets WHERE STATUS= 'inactivo' ", nativeQuery = true)
    Iterable<Budget> findAllByInactive();
}
