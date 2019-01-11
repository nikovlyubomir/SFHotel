package com.company;

import com.company.models.Room;
import com.company.models.RoomCategory;
import com.company.models.RoomType;
import com.company.models.User;
import com.company.persistance.ModelManagerSingleton;
import com.company.persistance.RoomObjectManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        View view = new View();
        try {
            view.init();
        } catch (StackOverflowError e){
            System.out.println("OOPS, Something bad happened. Shutting down.");
        } catch (Exception e){
            System.out.println("OOPSIE DOOPSIE< CALL THE SYS ADMIN >");
        }
    }

}
