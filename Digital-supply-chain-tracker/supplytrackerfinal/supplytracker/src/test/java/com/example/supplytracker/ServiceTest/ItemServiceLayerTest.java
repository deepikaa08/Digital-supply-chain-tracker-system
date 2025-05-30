package com.example.supplytracker.ServiceTest;

import com.example.supplytracker.DTO.ItemRequestDTO;
import com.example.supplytracker.Entity.Item;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.SupplierNotFoundException;
import com.example.supplytracker.Repository.ItemRepository;
import com.example.supplytracker.Repository.UserRepo;
import com.example.supplytracker.Service.ItemServiceLayer;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ItemServiceLayerTest {

    private ItemRepository itemrepo;
    private UserRepo userrepo;
    private ItemServiceLayer service;

    @BeforeEach
    public void setup(){
        // Initialize mocks
        itemrepo = Mockito.mock(ItemRepository.class);
        userrepo = Mockito.mock(UserRepo.class);

        // Inject mocks into service
        service = new ItemServiceLayer();
        service.userrepo = userrepo;
        service.itemrepo = itemrepo;
    }

    @Test
    public void getAllItemsTest(){
        // Prepare mock data
        List<Item> mockItems = Arrays.asList(new Item(), new Item());
        when(itemrepo.findAll()).thenReturn(mockItems);

        List<Item> result = service.getAllItems();

        assertEquals(mockItems,result);
    }

    @Test
    public void addTest() throws SupplierNotFoundException {
        // Create a sample item request DTO
        ItemRequestDTO dto = new ItemRequestDTO();
        dto.setName("Book");
        dto.setCategory("Stationary");
        dto.setSupplierId(1L);

        // Create expected item and supplier
        Item mockItem = new Item("Book","Stationary",1L, LocalDate.now());

        User mockSupplier = new User();
        mockSupplier.setId(1L);
        mockSupplier.setRole(Role.SUPPLIER);

        // Define behavior for mocks
        when(userrepo.findById(1L)).thenReturn(Optional.of(mockSupplier));

        when(itemrepo.save(any(Item.class))).thenReturn(mockItem);

        Item result = service.add(dto);

        assertEquals(mockItem,result);
    }

    @Test
    public void getByIdTest() throws ItemNotFoundException {
        // Prepare mock item
        Item mockItem = new Item();
        mockItem.setId(11L);

        // Define repository behavior
        when(itemrepo.findById(11L)).thenReturn(Optional.of(mockItem));
        Item result = service.getById(11L);
        assertEquals(mockItem,result);
    }

}
