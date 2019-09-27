package com.grokonez.jwtauthentication.repository;

import com.grokonez.jwtauthentication.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Transactional
public interface ItemRepository extends JpaRepository<Item, Long>  {
    @Query(value = "SELECT * FROM items WHERE CONSIGNMENT_ID= ?1 ", nativeQuery = true)
    List<Item> findByConsignmentId(Long consignmentId);

    @Query(value = "SELECT * FROM items WHERE CONSIGNMENT_ID= ?1 AND ITEM_ID=?2 " , nativeQuery = true)
    Item findByItemId(Long consignmentId,Long itemId);

    @Query(value= "SELECT SUM(MONEY) FROM items WHERE CONSIGNMENT_ID = ?1" , nativeQuery = true)
    BigInteger findTotalConsignmentById(Long consignmentId);

    @Query(value= "SELECT SUM(MONEY) FROM items WHERE CONSIGNMENT_ID = ?1" , nativeQuery = true)
    BigDecimal findTotalConsignmentByInBigDecimal(Long consignmentId);

    //retornar un item de acuerdo a su id
    @Query(value = "SELECT * FROM items WHERE ITEM_ID= ?1" , nativeQuery = true)
    Item findItemById(Long itemId);
}
