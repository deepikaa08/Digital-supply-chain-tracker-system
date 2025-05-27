package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.ShipmentRequestDTO;
import com.example.supplytracker.Entity.Shipment;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Status;
import com.example.supplytracker.Exceptions.ItemNotFoundException;
import com.example.supplytracker.Exceptions.ShipmentNotFoundException;
import com.example.supplytracker.Exceptions.TransporterNotFoundException;
import com.example.supplytracker.Repository.ItemRepository;
import com.example.supplytracker.Repository.ShipmentRepository;
import com.example.supplytracker.Repository.UserRepo;
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

    public List<Shipment> getAll(){
        return shipmentrepo.findAll();
    }

    public Shipment getWithId(long id) throws ShipmentNotFoundException {
        return shipmentrepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found"));
    }

    public Shipment updateStatus(long id, Status status) throws ShipmentNotFoundException{
        Shipment shipment = shipmentrepo.findById(id).orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found"));
        shipment.setCurrentStatus(status);
        return shipmentrepo.save(shipment);

    }

    public Shipment add(ShipmentRequestDTO request) throws ItemNotFoundException {
        if(itemrepo.existsById(request.getItemId())){
            Shipment shipment = new Shipment(
                    request.getItemId(),
                    request.getFromLocation(),
                    request.getToLocation(),
                    request.getExpectedDelivery());
            return shipmentrepo.save(shipment);
        }
        else throw new ItemNotFoundException("Item with id " + request.getItemId() + " not found");
    }

    public Shipment assign(long shipmentId, long transporterId) throws ShipmentNotFoundException, TransporterNotFoundException {
        Shipment shipment = shipmentrepo.findById(shipmentId).orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + "not found"));
        User transporter = userrepo.findById(transporterId).orElseThrow(() -> new TransporterNotFoundException("Transporter with id " + transporterId + " not found"));
        shipment.setTransporterId(transporterId);
        return shipmentrepo.save(shipment);
    }
}
