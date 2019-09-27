package com.grokonez.jwtauthentication.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.type.descriptor.sql.LobTypeMappings;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Set;

@Entity
@Table(name="Items")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_id")
    private long item_id;


    private String type_gast;


    private Integer resolution;


    private String purchase_order;

    private Integer bill_number;


    private String rut;


    private String provider;

    private String detail;


    private BigInteger money;

    private String budget_item;

    private String name_item;

    private String status;

    //@Column(name="type", length=100, nullable=false)
    //private String type;

    //@Lob @Basic()
    //@Column(name="content")
    //@Lob(type = LobTypeMappings.BLOB)
    //private MultipartFile content;

    //@JsonDeserialize(using = StringtoByteArray.class)


    //public class StringtoByteArray extends JsonDeserializer<byte []> {
//
    //@Override
     //public byte[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
       //         throws IOException, JsonProcessingException {
            //return (Base64.getDecoder().encode(jsonParser));
        //    return (Base64.getEncoder().encode(jsonParser.getText().getBytes()));
            //return (jsonParser.getBinaryValue());
         //return (Base64.decode(base64String, Base64.NO_WRAP));
        //}
    //}


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consignment_id", nullable = false)
    @JsonIgnore
    private Consignment consignment_id;


    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    @JsonBackReference(value="item-file")
    private Set<File> files;

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }


    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getType_gast() {
        return type_gast;
    }

    public void setType_gast(String type_gast) {
        this.type_gast = type_gast;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public String getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(String purchase_order) {
        this.purchase_order = purchase_order;
    }

    public Integer getBill_number() {
        return bill_number;
    }

    public void setBill_number(Integer bill_number) {
        this.bill_number = bill_number;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigInteger getMoney() {
        return money;
    }

    public void setMoney(BigInteger money) {
        this.money = money;
    }

    public String getBudget_item() {
        return budget_item;
    }

    public void setBudget_item(String budget_item) {
        this.budget_item = budget_item;
    }

    public String getName_item() {
        return name_item;
    }

    public void setName_item(String name_item) {
        this.name_item = name_item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Consignment getConsignment() {
        return consignment_id;
    }

    public void setConsignment(Consignment consignment_id) {
        this.consignment_id = consignment_id;
    }




}
