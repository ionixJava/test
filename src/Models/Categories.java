package Models;

import ionix.Annotation.DbSchema;
import ionix.Annotation.Table;
import ionix.Data.SqlValueType;
import ionix.Data.StoreGeneratedPattern;


@Table(schema = "NORTHWND.DBO",name="Categories")
public class Categories {
    @DbSchema(columnName = "CategoryID", isKey = true,databaseGeneratedOption = StoreGeneratedPattern.Identity)
    private int categoryID;
    public int getCategoryID(){ return this.categoryID;}
    public Categories setCategoryID(int value){
        this.categoryID = value;
        return this;
    }

    @DbSchema(columnName = "CategoryName")
    private String categoryName;
    public String getCategoryName(){ return this.categoryName;}
    public Categories setCategoryName(String value){
        this.categoryName = value;
        return this;
    }

    @DbSchema(columnName = "Description")
    private String description;
    public String getDescription(){ return this.description;}
    public Categories setDescription(String value){
        this.description = value;
        return this;
    }

    @DbSchema(columnName = "Picture")
    private byte[] picture;
    public byte[]  getPicture(){ return this.picture;}
    public Categories setPicture(byte[]  value){
        this.picture = value;
        return this;
    }
}
