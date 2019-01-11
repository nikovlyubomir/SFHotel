package com.company.persistance;

import com.company.models.RoomCategory;
import org.jsefa.Deserializer;
import org.jsefa.Serializer;
import org.jsefa.csv.CsvIOFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class RoomCategoryObjectManager {

    private ArrayList<RoomCategory> allCategories;

    //Object manager for Room objects, used to save/delete/add/get all of the Room objects :)
    RoomCategoryObjectManager() {
        allCategories = new ArrayList<>();
        initCategories();
    }


    public ArrayList<RoomCategory> getAllCategories() {
        return allCategories;
    }

    public boolean addRoomCategory(RoomCategory roomCategory) {
        for (RoomCategory r : this.allCategories) {
            if (r.getName().equals(roomCategory.getName()) && r.getType().getName().equals(roomCategory.getType().getName())) {
                return false;
            }
        }
        this.allCategories.add(roomCategory);
        saveCategories();
        return true;
    }

    public boolean deleteCategory(String name, String typeName) {
        boolean deleted = this.allCategories.removeIf(cat -> (cat.getName().equals(name) && cat.getType().getName().equals(typeName))); //Read docs for RemoveIF (pretty nice method)
        saveCategories();
        return deleted;
    }

    public RoomCategory getCategory(String name, String typeName) {
        for (RoomCategory r : this.allCategories) {
            if (r.getName().equals(name) && r.getType().getName().equals(typeName))
                return r;
        }
        return null;
    }

    public void deleteAllCategories() {
        this.allCategories.clear();
    }

    private void saveCategories() {
        Serializer serializer = CsvIOFactory.createFactory(RoomCategory.class).createSerializer(); //setup csv serializer
        StringWriter writer = new StringWriter(); //need the writer to write the objects
        serializer.open(writer);
        for (RoomCategory roomCategory : this.allCategories) {
            serializer.write(roomCategory); //write each object from memory
        }
        serializer.close(true);
        FileIO.writeToFile("roomCats.csv", writer); //Assuming data is already in memory so not appending but overriding file.
    }

    private void initCategories() {
        StringReader reader = FileIO.getFromFile("roomCats.csv");
        Deserializer deserializer = CsvIOFactory.createFactory(RoomCategory.class).createDeserializer();
        deserializer.open(reader);
        while (deserializer.hasNext()) {
            RoomCategory r = deserializer.next();
            this.allCategories.add(r);
        }
        deserializer.close(true);
    }

}
