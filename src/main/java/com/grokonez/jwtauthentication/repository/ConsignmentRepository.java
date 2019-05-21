package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ConsignmentRepository extends JpaRepository<Consignment, Long> {
    @Query(value = "SELECT * FROM consigments WHERE STATUS_BIN= 'activo' ", nativeQuery = true)
    Iterable<Consignment> findAllByActive();

    @Query(value = "SELECT * FROM consigments WHERE STATUS_BIN= 'inactivo' ", nativeQuery = true)
    Iterable<Consignment> findAllByInactive();

    @Query(value = "SELECT governance FROM consigments WHERE ID_CONSIGNMENT= ?1 ", nativeQuery = true)
    String findNameConsignment(Long consignmentId);

    @Query(value = "SELECT email FROM consigments, users WHERE consigments.NAME_USER = users.USERNAME AND ID_CONSIGNMENT= ?1 ", nativeQuery = true)
    String findEmailOfCreateConsignment(Long consignmentId);

    @Query(value = "SELECT email_optional FROM users WHERE EMAIL=?1 ", nativeQuery = true)
    String findEmailOptional(String name);


}
