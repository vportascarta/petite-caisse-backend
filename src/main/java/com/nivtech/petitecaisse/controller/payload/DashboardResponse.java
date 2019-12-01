package com.nivtech.petitecaisse.controller.payload;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashboardResponse
{
    private Double amountInRegisters;
    private Double amountInDebts;
    private Double amountInProducts;
    private Integer productsInStock;
    private List<Pair<String, Long>> pastSales;

    public Double getAmountInRegisters()
    {
        return amountInRegisters;
    }

    public void setAmountInRegisters(Double amountInRegisters)
    {
        this.amountInRegisters = amountInRegisters;
    }

    public Double getAmountInDebts()
    {
        return amountInDebts;
    }

    public void setAmountInDebts(Double amountInDebts)
    {
        this.amountInDebts = amountInDebts;
    }

    public Double getAmountInProducts()
    {
        return amountInProducts;
    }

    public void setAmountInProducts(Double amountInProducts)
    {
        this.amountInProducts = amountInProducts;
    }

    public Integer getProductsInStock()
    {
        return productsInStock;
    }

    public void setProductsInStock(Integer productsInStock)
    {
        this.productsInStock = productsInStock;
    }

    public List<Pair<String, Long>> getPastSales()
    {
        return pastSales;
    }

    public void setPastSales(List<Pair<String, Long>> pastSales)
    {
        this.pastSales = pastSales;
    }
}
