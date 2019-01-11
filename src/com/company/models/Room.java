package com.company.models;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;
import org.jsefa.csv.annotation.CsvSubRecord;

@CsvDataType(defaultPrefix = "RO")
public class Room {

    @CsvSubRecord(pos = 1, prefix = "RC")
    private RoomCategory category;

    @CsvField(pos = 2)
    private int number;

    public Room() {

    }

    public Room(RoomCategory category, int number) {
        this.category = category;
        this.number = number;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public void setCategory(RoomCategory category) {
        this.category = category;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrice() {
        return this.category.getPrice();
    }

    public String toString() {
        return "Room number: " + this.number + ", " + this.category.getName() + ", " + this.category.getType().getName() + ". Price:" + getPrice();
    }
}
