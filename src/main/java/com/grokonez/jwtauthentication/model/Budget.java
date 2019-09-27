package com.grokonez.jwtauthentication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name="Budgets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "budget_id")
    private long budget_id;

    private BigDecimal budget;


    private String governance;


    private Date date;

    private String observation;

    private String status;

    private Date dateChange;

    private String status_approbation;

    private String name;

    /*relacion con proyecto*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project_id;

    /*relacion con remesa*/
    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    @JsonBackReference(value="budget-consignment")
    private Set<Consignment> consignments;

    public Set<Consignment> getConsignments() {
        return consignments;
    }

    public void setConsignments(Set<Consignment> consignments) {
        this.consignments = consignments;
    }

    public Project getProject() {
        return project_id;
    }

    public void setProject(Project project) {
        this.project_id = project;
    }


    public long getBudget_id() {
        return budget_id;
    }

    public void setBudget_id(long budget_id) {
        this.budget_id = budget_id;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getGovernance() {
        return governance;
    }

    public void setGovernance(String governance) {
        this.governance = governance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Date getDateChange() {
        return dateChange;
    }

    public void setDateChange(Date dateChange) {
        this.dateChange = dateChange;
    }

    public String getStatus_approbation() {
        return status_approbation;
    }

    public void setStatus_approbation(String status_approbation) {
        this.status_approbation = status_approbation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
