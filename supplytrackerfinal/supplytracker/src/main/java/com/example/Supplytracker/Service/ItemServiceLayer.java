package com.example.Supplytracker.Service;

import com.example.Supplytracker.DTO.ItemRequestDTO;
import com.example.Supplytracker.Entity.Item;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.Role;
import com.example.Supplytracker.Exceptions.ItemNotFoundException;
import com.example.Supplytracker.Exceptions.SupplierNotFoundException;
import com.example.Supplytracker.Repository.ItemRepository;
import com.example.Supplytracker.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;
@Component

public class ItemServiceLayer {
    @Autowired
    public UserRepo userrepo;
    @Autowired
    public ItemRepository itemrepo;

    // Returns a list of all items
    public List<Item> getAllItems(){
        return itemrepo.findAll();
    }

    // Adds a new item after checking if the supplier exists and has the SUPPLIER role
    public Item add(ItemRequestDTO request)throws SupplierNotFoundException {
        long supplierId = request.getSupplierId();

        User user = userrepo.findById(supplierId).orElseThrow(() -> new SupplierNotFoundException("Supplier with Id " + supplierId + " not found"));
        if (user.getRole() == Role.SUPPLIER) {
            Item item = new Item(
                    request.getName(),
                    request.getCategory(),
                    request.getSupplierId(),
                    LocalDate.now()
            );
            return itemrepo.save(item);// Save item to the database
        }

        throw new SupplierNotFoundException("Supplier with Id " + supplierId + " not found");
    }

    // Retrieves an item by its ID or throws exception if not found
    public Item getById(long id) throws ItemNotFoundException{
        return itemrepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item with id "+ id + " not found."));
    }
}
