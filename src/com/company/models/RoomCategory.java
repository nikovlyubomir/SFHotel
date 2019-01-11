package com.company.models;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;
import org.jsefa.csv.annotation.CsvSubRecord;

@CsvDataType(defaultPrefix = "RC")
public class RoomCategory {

    /*
     * Room category ( Single/Double/Apartment )
     */

    @CsvField(pos = 1)
    private String name;

    @CsvSubRecord(pos = 2, prefix = "RT")
    private RoomType type;

    @CsvField(pos = 3)
    private int price;

    public RoomCategory() {

    }


    public RoomCategory(String name, RoomType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public String toString(){
     return this.name + " room of type:" + this.type.getName() + ", priced at:" + this.price;
    }
}
