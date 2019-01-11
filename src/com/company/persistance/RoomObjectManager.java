package com.company.persistance;

import com.company.models.Room;
import org.jsefa.Deserializer;
import org.jsefa.Serializer;
import org.jsefa.csv.CsvIOFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class RoomObjectManager {

    private ArrayList<Room> allRoooms;

    //Object manager for Room objects, used to save/delete/add/get all of the Room objects :)
    RoomObjectManager() {
        allRoooms = new ArrayList<>();
        initRooms();
    }


    public ArrayList<Room> getAllRoooms() {
        return allRoooms;
    }

    public boolean addRoom(Room room) {
        for (Room r : this.allRoooms) {
            if (r.getNumber() == room.getNumber()) {
                return false;
            }
        }
        this.allRoooms.add(room);
        saveRooms();
        return true;
    }

    public boolean deleteRoom(int number) {
        boolean deleted = this.allRoooms.removeIf(room -> (room.getNumber() == number)); //Read docs for RemoveIF (pretty nice method)
        saveRooms();
        return deleted;
    }

    public Room getRoom(int number) {
        for (Room r : this.allRoooms) {
            if (r.getNumber() == number)
                return r;
        }
        return null;
    }

    public void deleteAllRooms() {
        this.allRoooms.clear();
    }

    private void saveRooms() {
        Serializer serializer = CsvIOFactory.createFactory(Room.class).createSerializer(); //setup csv serializer
        StringWriter writer = new StringWriter(); //need the writer to write the objects
        serializer.open(writer);
        for (Room room : this.allRoooms) {
            serializer.write(room); //write each object from memory
        }
        serializer.close(true);
        FileIO.writeToFile("rooms.csv", writer); //Assuming data is already in memory so not appending but overriding file.
    }

    private void initRooms() {
        StringReader reader = FileIO.getFromFile("rooms.csv");
        Deserializer deserializer = CsvIOFactory.createFactory(Room.class).createDeserializer();
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            Room r = deserializer.next();
            this.allRoooms.add(r);
        }
        deserializer.close(true);
    }

}
