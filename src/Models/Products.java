package Models;

import ionix.Annotation.DbSchema;
import ionix.Annotation.Table;
import ionix.Data.StoreGeneratedPattern;

import java.math.BigDecimal;


@Table(name="PRODUCTS")
public class Products {
    @DbSchema(columnName = "PRODUCTID", isKey = true,databaseGeneratedOption = StoreGeneratedPattern.AutoGenerateSequence)
    private int productId;
    public int getProductId(){ return this.productId;}
    public Products setgetProductId(int value){
        this.productId = value;
        return this;
    }

    @DbSchema(columnName = "PRODUCTNAME")
    private String productName;
    public String getProductName(){ return this.productName;}
    public Products setProductName(String value){
        this.productName = value;
        return this;
    }

    @DbSchema(columnName = "SUPPLIERID")
    private int supplierId;
    public int getSupplierId(){ return this.supplierId;}
    public Products setSupplierId(int value){
        this.supplierId = value;
        return this;
    }

    @DbSchema(columnName = "CATEGORYID")
    private int categoryId;
    public int  getCategoryId(){
        return this.categoryId;
    }
    public Products setCategoryId(int  value){
        this.categoryId = value;
        return this;
    }

    @DbSchema(columnName = "QUANTITYPERUNIT")
    private String quantityPerUnit;
    public String getQuantityPerUnit(){ return this.quantityPerUnit;}
    public Products setQuantityPerUnit(String value){
        this.quantityPerUnit = value;
        return this;
    }

    @DbSchema(columnName = "UNITPRICE")
    private BigDecimal unitPrice;
    public BigDecimal getUnitPrice(){
        return this.unitPrice;
    }
    public Products setUnitPrice(BigDecimal  value){
        this.unitPrice = value;
        return this;
    }

    @DbSchema(columnName = "UNITSINSTOCK")
    private int unitsInStock;
    public int getUnitsInStock(){ return this.unitsInStock;}
    public Products setUnitsInStock(int value){
        this.unitsInStock = value;
        return this;
    }

    @DbSchema(columnName = "UNITSONORDER")
    private int unitsInOrder;
    public int getUnitsInOrder(){ return this.unitsInOrder;}
    public Products setUnitsInOrder(int value){
        this.unitsInOrder = value;
        return this;
    }

    @DbSchema(columnName = "REORDERLEVEL")
    private int reorderLevel;
    public int getReorderLevel(){ return this.reorderLevel;}
    public Products setReorderLevel(int value){
        this.reorderLevel = value;
        return this;
    }

    @DbSchema(columnName = "DISCONTINUED")
    private int discontinued;
    public int getDiscontinued(){ return this.discontinued;}
    public Products setDiscontinued(int value){
        this.discontinued = value;
        return this;
    }
}
