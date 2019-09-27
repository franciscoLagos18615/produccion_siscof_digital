package com.grokonez.jwtauthentication.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Consigments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Consignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_consignment")
    private long id_consignment;

    @NotNull
    private String request;


    private String status;


    private String governance;
    private String name_user;

    private String status_bin;

    /**relacion con item*/
    @OneToMany(mappedBy = "consignment_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)


    @JsonBackReference(value="consignment-item")
    private Set<Item> items;

    /*relacion con presupuesto*/

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_relation_id", nullable = false)
    @JsonIgnore
    private Budget budget;

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGovernance() {
        return governance;
    }

    public void setGovernance(String governance) {
        this.governance = governance;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }


    public String getStatus_bin() {
        return status_bin;
    }

    public void setStatus_bin(String status_bin) {
        this.status_bin = status_bin;
    }
}
