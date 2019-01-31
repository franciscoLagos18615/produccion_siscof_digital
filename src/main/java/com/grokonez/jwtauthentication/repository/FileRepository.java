package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Consignment;
import com.grokonez.jwtauthentication.model.File;
import com.grokonez.jwtauthentication.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface FileRepository extends JpaRepository<File, Long> {
    @Query(value = "SELECT * FROM files WHERE ITEM_ID= ?1 ", nativeQuery = true)
    List<File> findByItemId(Long itemId);
}
