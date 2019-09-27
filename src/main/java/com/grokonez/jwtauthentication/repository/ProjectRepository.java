package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Budget;
import com.grokonez.jwtauthentication.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT * FROM projects WHERE status='APROBADO' and money_final > 0 ", nativeQuery = true)
    Iterable<Project> findByStatusAndMoneyFinal();
}


