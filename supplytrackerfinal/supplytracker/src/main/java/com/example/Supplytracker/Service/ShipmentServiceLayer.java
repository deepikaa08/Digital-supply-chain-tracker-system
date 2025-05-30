package com.example.Supplytracker.Service;

import com.example.Supplytracker.DTO.ShipmentRequestDTO;
import com.example.Supplytracker.Entity.Shipment;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Enums.Status;
import com.example.Supplytracker.Exceptions.ItemNotFoundException;
import com.example.Supplytracker.Exceptions.ShipmentNotFoundException;
import com.example.Supplytracker.Exceptions.TransporterNotFoundException;
import com.example.Supplytracker.Repository.ItemRepository;
import com.example.Supplytracker.Repository.ShipmentRepository;
import com.example.Supplytracker.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceLayer {

    @Autowired
    public UserRepo userrepo;

    @Autowired
    public ItemRepository itemrepo;

    @Autowired
    public ShipmentRepository shipmentrepo;

    // Returns a list of all shipments from the database
    public List<Shipment> getAll() {
        return shipmentrepo.findAll();
    }

    // Returns a shipment by its ID or throws an exception if not found
    public Shipment getWithId(long id) throws ShipmentNotFoundException {
        return shipmentrepo.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found"));
    }

    // Updates the status of a shipment by ID
    public Shipment updateStatus(long id, Status status) throws ShipmentNotFoundException {
        Shipment shipment = shipmentrepo.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found"));
        shipment.setCurrentStatus(status);
        return shipmentrepo.save(shipment); // Saves the updated status
    }

    // Adds a new shipment if the item ID exists
    public Shipment add(ShipmentRequestDTO request) throws ItemNotFoundException {
        if (itemrepo.existsById(request.getItemId())) {
            Shipment shipment = new Shipment(
                    request.getItemId(),
                    request.getFromLocation(),
                    request.getToLocation(),
                    request.getExpectedDelivery());
            return shipmentrepo.save(shipment); // Saves the new shipment
        } else {
            throw new ItemNotFoundException("Item with id " + request.getItemId() + " not found");
        }
    }

    // Assigns a transporter to a shipment by their IDs
    public Shipment assign(long shipmentId, long transporterId) throws ShipmentNotFoundException, TransporterNotFoundException {
        Shipment shipment = shipmentrepo.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + " not found"));

        User transporter = userrepo.findById(transporterId)
                .orElseThrow(() -> new TransporterNotFoundException("Transporter with id " + transporterId + " not found"));

        shipment.setTransporterId(transporterId);
        return shipmentrepo.save(shipment); // Saves the updated shipment with transporter assigned
    }
}
