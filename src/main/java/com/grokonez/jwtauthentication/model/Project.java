package com.grokonez.jwtauthentication.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.poi.hssf.record.crypto.Biff8DecryptingStream;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="Projects")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;


    private String name;
    private String observation;
    private String status;
    private Date date;
    private BigDecimal money_initial;
    private BigDecimal money_final;


    @OneToMany(mappedBy = "project_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    @JsonBackReference(value="project-budget")
    private Set<Budget> budgets;

    public Set<Budget> getBudgets() {
        return budgets;
    }

    public void setBudgets(Set<Budget> budgets) {
        this.budgets = budgets;
    }

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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getMoney_initial() {
        return money_initial;
    }

    public void setMoney_initial(BigDecimal money_initial) {
        this.money_initial = money_initial;
    }

    public BigDecimal getMoney_final() {
        return money_final;
    }

    public void setMoney_final(BigDecimal money_final) {
        this.money_final = money_final;
    }




}
