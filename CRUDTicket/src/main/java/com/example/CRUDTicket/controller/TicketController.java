package com.example.CRUDTicket.controller;

import com.example.CRUDTicket.model.Ticket;
import com.example.CRUDTicket.repo.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class TicketController {

    @Autowired
    private TicketRepository ticketRepo;

    @GetMapping("/getAllTickets")
    public ResponseEntity<List<Ticket>> getAllTickets(){

        try {
            List<Ticket> ticketList=new ArrayList<>();
            ticketRepo.findAll().forEach(ticketList::add);

            if(ticketList.isEmpty()){
                return new ResponseEntity<>( HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(ticketList, HttpStatus.OK);

        } catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTicketById/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id){

        Optional<Ticket> ticketData= ticketRepo.findById(id);

        if(ticketData.isPresent()){
            return new ResponseEntity<>(ticketData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addTicket")
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket ticket){
        Ticket ticketObj=ticketRepo.save(ticket);

        return new ResponseEntity<>(ticketObj, HttpStatus.OK);
    }

    @PostMapping("/updateTicketById/{id}")
    public ResponseEntity<Ticket> updateTicketById(@PathVariable Long id, @RequestBody Ticket newTicketData){
        Optional<Ticket> oldticketData= ticketRepo.findById(id);

        if(oldticketData.isPresent()){
            Ticket updatedTicketData= oldticketData.get();
            updatedTicketData.setName(newTicketData.getName());
            updatedTicketData.setDeparture(newTicketData.getDeparture());
            updatedTicketData.setDestination(newTicketData.getDestination());

            Ticket ticketObj=ticketRepo.save(updatedTicketData);
            return new ResponseEntity<>(ticketObj, HttpStatus.OK);
        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteTicketById/{id}")
    public ResponseEntity<HttpStatus> deleteTicketById(@PathVariable Long id){
        ticketRepo.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
