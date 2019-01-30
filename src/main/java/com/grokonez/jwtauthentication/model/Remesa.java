package com.grokonez.jwtauthentication.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "remesas")
public class Remesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_consignment")
    private long id_consignment;

    @NotNull
    private String request;

    public long getId_consignment() {
        return id_consignment;
    }

    public void setId_consignment(long id_consignment) {
        this.id_consignment = id_consignment;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
