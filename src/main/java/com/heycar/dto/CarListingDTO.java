package com.heycar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CarListingDTO {

    @NotNull
    private String code;

    @NotNull
    private String make;

    @NotNull
    private String model;

    @JsonProperty(value = "kW")
    private Integer powerInKw;

    private Integer year;

    private String color;

    @NotNull
    private BigDecimal price;
}
