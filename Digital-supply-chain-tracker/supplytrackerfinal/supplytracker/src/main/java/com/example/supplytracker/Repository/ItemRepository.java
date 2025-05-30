package com.example.supplytracker.Repository;


import com.example.supplytracker.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
