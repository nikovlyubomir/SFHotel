package com.company.persistance;

import com.company.models.Booking;
import com.company.models.Room;
import org.jsefa.Deserializer;
import org.jsefa.Serializer;
import org.jsefa.csv.CsvIOFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class BookingObjectManager {

    private ArrayList<Booking> allBookings;

    //Object manager for Room objects, used to save/delete/add/get all of the Room objects :)
    BookingObjectManager() {
        allBookings = new ArrayList<>();
        initBookings();
    }


    public ArrayList<Booking> getAllBookings() {
        return allBookings;
    }

    public boolean addBooking(Booking booking) {
        for (Booking b : this.allBookings) {
            if (b.getRoom().getNumber() == booking.getRoom().getNumber()) {
                if(b.getToDate().compareTo(booking.getFromDate()) >= 0 && b.getFromDate().compareTo(booking.getToDate()) <= 0){
                    return false;
                }
            }
        }
        this.allBookings.add(booking);
        saveBookings();
        return true;
    }

    public void deleteAllBookings() {
        this.allBookings.clear();
    }

    public boolean deleteBooking(String username, int roomNr) {
        boolean deleted = this.allBookings.removeIf(booking ->
                (booking.getUser().getUsername().equals(username) && booking.getRoom().getNumber() == roomNr));
        saveBookings();
        return deleted;
    }

    public void deleteBookingByIndex(int indx){
        this.allBookings.remove(indx);
        saveBookings();
    }

    public Booking getBooking(String username, int roomNr) {
        for (Booking b : this.allBookings) {
            if (b.getUser().getUsername().equals(username) && b.getRoom().getNumber()==roomNr)
                return b;
        }
        return null;
    }

    private void saveBookings() {
        Serializer serializer = CsvIOFactory.createFactory(Booking.class).createSerializer(); //setup csv serializer
        StringWriter writer = new StringWriter(); //need the writer to write the
        serializer.open(writer);
        for (Booking booking : this.allBookings) {
            serializer.write(booking); //write each object from memory
        }
        serializer.close(true);
        FileIO.writeToFile("bookings.csv", writer); //Assuming data is already in memory so not appending but overriding file.
    }

    private void initBookings() {
        StringReader reader = FileIO.getFromFile("bookings.csv");
        Deserializer deserializer = CsvIOFactory.createFactory(Booking.class).createDeserializer();
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            Booking b = deserializer.next();
            this.allBookings.add(b);
        }
        deserializer.close(true);
    }

}
