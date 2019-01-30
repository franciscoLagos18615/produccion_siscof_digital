package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
    @Query(value = "SELECT * FROM CONSIGMENTS WHERE STATUS_BIN= 'activo' ", nativeQuery = true)
    Iterable<Consignment> findAllByActive();

    @Query(value = "SELECT * FROM CONSIGMENTS WHERE STATUS_BIN= 'inactivo' ", nativeQuery = true)
    Iterable<Consignment> findAllByInactive();

    @Query(value = "SELECT GOVERNANCE FROM CONSIGMENTS WHERE ID_CONSIGNMENT= ?1 ", nativeQuery = true)
    String findNameConsignment(Long consignmentId);

    @Query(value = "SELECT EMAIL FROM CONSIGMENTS, USERS WHERE CONSIGMENTS.NAME_USER = USERS.USERNAME AND ID_CONSIGNMENT= ?1 ", nativeQuery = true)
    String findEmailOfCreateConsignment(Long consignmentId);



}
