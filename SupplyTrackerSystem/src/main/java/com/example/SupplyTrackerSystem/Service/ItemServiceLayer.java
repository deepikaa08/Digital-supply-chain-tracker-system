package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.ItemRequestDTO;
import com.example.supplytracker.Entity.Item;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.SupplierNotFoundException;
import com.example.supplytracker.Repository.ItemRepository;
import com.example.supplytracker.Repository.UserRepo;
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
    public List<Item> getAllItems(){
        return itemrepo.findAll();
    }

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
            return itemrepo.save(item);
        }

        throw new SupplierNotFoundException("Supplier with Id " + supplierId + " not found");
    }

    public Item getById(long id) throws ItemNotFoundException{
        return itemrepo.findById(id).orElseThrow(() -> new ItemNotFoundException("Item with id "+ id + " not found."));
    }
}
