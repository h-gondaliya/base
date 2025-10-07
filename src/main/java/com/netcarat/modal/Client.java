package com.netcarat.modal;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
public class Client {

    @Id
    private Long id;

    @Column(name = "client_name", length = 255)
    private String clientName;

    @Column(name = "client_address", length = 1000)
    private String clientAddress;

    @Column(name = "email", length = 100)
    private String email;

    // Default constructor
    public Client() {}

    // Constructor with all fields
    public Client(Long id, String clientName, String clientAddress, String email) {
        this.id = id;
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}