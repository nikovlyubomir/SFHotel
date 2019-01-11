package com.company.models;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

import java.util.ArrayList;

@CsvDataType(defaultPrefix = "RT")
public class RoomType {
    /*
     * Room type (Deluxe, VIP, Ordinary )
     * */

    @CsvField(pos = 1)
    public String name;

    @CsvField(pos = 2)
    public ArrayList<String> options;

    public RoomType() {

    }

    public RoomType(String name, ArrayList<String> options) {
        this.name = name;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String toString() {
        String options = "|";
        for (String opt : this.options){
            options += opt + "|";
        }
        return this.name + "; Options:" + options;
    }
}
