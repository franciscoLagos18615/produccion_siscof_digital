package com.grokonez.jwtauthentication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="BorderCrossings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BorderCrossings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private long id;
    private String name;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "governance_id", nullable = false)
    @JsonIgnore
    private Governance governance_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Governance getGovernance_id() {
        return governance_id;
    }

    public void setGovernance_id(Governance governance_id) {
        this.governance_id = governance_id;
    }
}
