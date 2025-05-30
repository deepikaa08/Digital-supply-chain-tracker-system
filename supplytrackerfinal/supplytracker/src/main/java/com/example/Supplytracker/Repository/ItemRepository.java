package com.example.Supplytracker.Repository;


import com.example.Supplytracker.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
