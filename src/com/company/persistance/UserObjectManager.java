package com.company.persistance;

import com.company.models.Booking;
import com.company.models.User;
import org.jsefa.Deserializer;
import org.jsefa.Serializer;
import org.jsefa.csv.CsvIOFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserObjectManager {

    private ArrayList<User> allUsers;

    //Object manager for Room objects, used to save/delete/add/get all of the Room objects :)
    UserObjectManager() {
        allUsers = new ArrayList<>();
        initUsers();
    }


    public ArrayList<User> getAllUsers() {
        return allUsers;
    }

    public boolean addUser(User user) {
        for (User u : this.allUsers) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        this.allUsers.add(user);
        saveUsers();
        return true;
    }

    public void deleteAllUsers() {
        this.allUsers.clear();
    }

    public boolean deleteUser(String username) {
        boolean deleted = this.allUsers.removeIf(user -> (user.getUsername().equals(username))); //Read docs for RemoveIF (pretty nice method)
        saveUsers();
        return deleted;
    }

    public User getUser(String username) {
        for (User u : this.allUsers) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    private void saveUsers() {
        Serializer serializer = CsvIOFactory.createFactory(User.class).createSerializer(); //setup csv serializer
        StringWriter writer = new StringWriter(); //need the writer to write the
        serializer.open(writer);
        for (User user : this.allUsers) {
            serializer.write(user); //write each object from memory
        }
        serializer.close(true);
        FileIO.writeToFile("users.csv", writer); //Assuming data is already in memory so not appending but overriding file.
    }

    private void initUsers() {
        StringReader reader = FileIO.getFromFile("users.csv");
        Deserializer deserializer = CsvIOFactory.createFactory(User.class).createDeserializer();
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            User u = deserializer.next();
            this.allUsers.add(u);
        }
        deserializer.close(true);
    }

    public int getAmmountForPay(String username){
        ArrayList<Booking> bookings_to_pay = new ArrayList<>();
        for (Booking b: ModelManagerSingleton.getInstance().Bookings().getAllBookings()){
            if (b.getUser().getUsername().equals(username)){
                bookings_to_pay.add(b);
            }
        }
        int ammount = 0;
        for(Booking b: bookings_to_pay){
            ammount += b.getRoom().getPrice() * getDateDiff( b.getFromDate(), b.getToDate(), TimeUnit.DAYS);
        }
        return ammount;
    }

    public void pay(String username){
        ArrayList<Integer> bookings_paid_idx = new ArrayList<>();
        int i = 0;
        for (Booking b: ModelManagerSingleton.getInstance().Bookings().getAllBookings()){
            if (b.getUser().getUsername().equals(username)){
                bookings_paid_idx.add(i);
            }
            i++;
        }
        for(int idx: bookings_paid_idx){
            ModelManagerSingleton.getInstance().Bookings().deleteBookingByIndex(idx);
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

}
