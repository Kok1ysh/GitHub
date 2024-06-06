package com.example.CRUDTicket.controller;

import com.example.CRUDTicket.model.Ticket;
import com.example.CRUDTicket.repo.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TicketControllerTest {

    @Mock
    private TicketRepository ticketRepo;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket(1L, "Ticket1", "Departure1", "Destination1"));

        when(ticketRepo.findAll()).thenReturn(tickets);

        ResponseEntity<List<Ticket>> response = ticketController.getAllTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ticketRepo, times(1)).findAll();
    }

    @Test
    public void testGetAllTickets_NoContent() {
        when(ticketRepo.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Ticket>> response = ticketController.getAllTickets();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketRepo, times(1)).findAll();
    }

    @Test
    public void testGetTicketById_Found() {
        Ticket ticket = new Ticket(1L, "Ticket1", "Departure1", "Destination1");
        when(ticketRepo.findById(anyLong())).thenReturn(Optional.of(ticket));

        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());
        verify(ticketRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testGetTicketById_NotFound() {
        when(ticketRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(ticketRepo, times(1)).findById(anyLong());
    }

    @Test
    public void testAddTicket() {
        Ticket ticket = new Ticket(1L, "Ticket1", "Departure1", "Destination1");
        when(ticketRepo.save(any(Ticket.class))).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.addTicket(ticket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticket, response.getBody());
        verify(ticketRepo, times(1)).save(any(Ticket.class));
    }

    @Test
    public void testUpdateTicketById_Found() {
        Ticket oldTicket = new Ticket(1L, "Ticket1", "Departure1", "Destination1");
        Ticket newTicket = new Ticket(1L, "UpdatedTicket", "UpdatedDeparture", "UpdatedDestination");
        when(ticketRepo.findById(anyLong())).thenReturn(Optional.of(oldTicket));
        when(ticketRepo.save(any(Ticket.class))).thenReturn(newTicket);

        ResponseEntity<Ticket> response = ticketController.updateTicketById(1L, newTicket);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newTicket, response.getBody());
        verify(ticketRepo, times(1)).findById(anyLong());
        verify(ticketRepo, times(1)).save(any(Ticket.class));
    }

    @Test
    public void testUpdateTicketById_NotFound() {
        when(ticketRepo.findById(anyLong())).thenReturn(Optional.empty());

        Ticket newTicket = new Ticket(1L, "UpdatedTicket", "UpdatedDeparture", "UpdatedDestination");
        ResponseEntity<Ticket> response = ticketController.updateTicketById(1L, newTicket);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(ticketRepo, times(1)).findById(anyLong());
        verify(ticketRepo, times(0)).save(any(Ticket.class));
    }

    @Test
    public void testDeleteTicketById() {
        doNothing().when(ticketRepo).deleteById(anyLong());

        ResponseEntity<HttpStatus> response = ticketController.deleteTicketById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(ticketRepo, times(1)).deleteById(anyLong());
    }
}
