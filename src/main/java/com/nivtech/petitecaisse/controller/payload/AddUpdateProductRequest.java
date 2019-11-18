package com.nivtech.petitecaisse.controller.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class AddUpdateProductRequest
{
    @Positive
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2047)
    private String description;

    @PositiveOrZero
    private Integer newQuantity;

    @Positive
    private Double price;

    @Size(max = 255)
    private String imageUrl;

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Integer getNewQuantity()
    {
        return newQuantity;
    }

    public Double getPrice()
    {
        return price;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }
}
