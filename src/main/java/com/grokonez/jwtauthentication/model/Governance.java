package com.grokonez.jwtauthentication.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.swing.border.Border;
import java.util.Set;


@Entity
@Table(name="Governances")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Governance {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    private String name;

    @OneToMany(mappedBy = "governance_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value="governance-border")
    private Set<BorderCrossings> borderCrossings;

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

    public Set<BorderCrossings> getBorderCrossings() {
        return borderCrossings;
    }

    public void setBorderCrossings(Set<BorderCrossings> borderCrossings) {
        this.borderCrossings = borderCrossings;
    }
}
