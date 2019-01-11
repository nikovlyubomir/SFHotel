package com.company.models;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;
import org.jsefa.csv.annotation.CsvSubRecord;

import java.util.Date;

@CsvDataType(defaultPrefix = "BO")
public class Booking {

    @CsvSubRecord(pos = 1, prefix = "RO")
    Room room;

    @CsvSubRecord(pos = 2, prefix = "US")
    User user;

    @CsvField(pos = 3, format = "dd.MM.yyyy")
    Date fromDate;

    @CsvField(pos = 4, format = "dd.MM.yyyy")
    Date toDate;

    public Booking() {
    }

    public Booking(Room room, User user, Date fromDate, Date toDate) {
        this.room = room;
        this.user = user;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    private String getDateToNormalString(Date date) {
        return date.getDate() + "/" + date.getMonth() + "/" + date.getYear();
    }

    public String toString() {
        return "Booking of room: " + this.getRoom().toString() + ", for user: " + this.getUser().getUsername() + " from: "
                + getDateToNormalString(fromDate)+ ", until: " + getDateToNormalString(toDate);
    }
}
