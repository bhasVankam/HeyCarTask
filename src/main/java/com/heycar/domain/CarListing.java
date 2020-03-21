package com.heycar.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "car_listings")
@Data
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class CarListing implements Serializable {

    private static final long serialVersionUID = -7898847712169990695L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "code")
    private String code;

    @Column(name = "make")
    @NotNull
    private String make;

    @Column(name = "model")
    @NotNull
    private String model;

    @Column(name = "power_in_kw")
    private Integer powerInKw;

    @Column(name = "year")
    private Integer year;

    @Column(name = "color")
    private String color;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @Column(name = "created_by") // dealer_id
    // Ideally there should be a mapping with dealer table
    // Just for the sake of this interview, an assumption has made that all dealer requests are valid
    @NotNull
    private String createdBy;

    @CreatedDate
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

}
