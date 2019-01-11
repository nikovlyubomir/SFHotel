package com.company.persistance;

import com.company.models.RoomType;
import org.jsefa.Deserializer;
import org.jsefa.Serializer;
import org.jsefa.csv.CsvIOFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class RoomTypeObjectManager {

    private ArrayList<RoomType> allRoomTypes;

    //Object manager for Room objects, used to save/delete/add/get all of the Room objects :)
    RoomTypeObjectManager() {
        allRoomTypes = new ArrayList<>();
        initTypes();
    }


    public ArrayList<RoomType> getAllRoomTypes() {
        return allRoomTypes;
    }

    public boolean addRoomType(RoomType roomType) {
        for (RoomType r : this.allRoomTypes) {
            if (r.getName().equals(roomType.getName())) {
                return false;
            }
        }
        this.allRoomTypes.add(roomType);
        saveTypes();
        return true;
    }

    public boolean deleteType(String name) {
        boolean deleted = this.allRoomTypes.removeIf(type -> (type.getName().equals(name))); //Read docs for RemoveIF (pretty nice method)
        saveTypes();
        return deleted;
    }

    public RoomType getType(String name) {
        for (RoomType r : this.allRoomTypes) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    public void deleteAllTypes() {
        this.allRoomTypes.clear();
    }

    private void saveTypes() {
        Serializer serializer = CsvIOFactory.createFactory(RoomType.class).createSerializer(); //setup csv serializer
        StringWriter writer = new StringWriter(); //need the writer to write the objects
        serializer.open(writer);
        for (RoomType roomType : this.allRoomTypes) {
            serializer.write(roomType); //write each object from memory
        }
        serializer.close(true);
        FileIO.writeToFile("roomTypes.csv", writer); //Assuming data is already in memory so not appending but overriding file.
    }

    private void initTypes() {
        StringReader reader = FileIO.getFromFile("roomTypes.csv");
        Deserializer deserializer = CsvIOFactory.createFactory(RoomType.class).createDeserializer();
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            RoomType r = deserializer.next();
            this.allRoomTypes.add(r);
        }
        deserializer.close(true);
    }

}
