package com.example.CRUDTicket.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Tickets")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String departure;
    private String destination;
        }
