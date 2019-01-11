package com.company.persistance;

import java.io.*;

public class FileIO {

    public static StringReader getFromFile(String filename){
        try {
            FileReader reader = new FileReader(filename);
            String str = "";
            int ch = reader.read();
            while(ch != -1) {
                str += (char) ch;
                ch = reader.read();
            }
            return new StringReader(str);
        } catch (IOException e) {
            e.printStackTrace();
            return new StringReader("");
        }
    }

    public static void writeToFile(String fileName, StringWriter writer){
        try {
            FileWriter fWriter = new FileWriter(fileName);
            fWriter.write(writer.toString());
            fWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
