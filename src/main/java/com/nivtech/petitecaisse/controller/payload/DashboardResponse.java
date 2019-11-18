package com.nivtech.petitecaisse.controller.payload;

public class DashboardResponse
{
    private Double amountInRegisters;
    private Double amountInDebts;
    private Double amountInProducts;
    private Integer productsInStock;

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
}
