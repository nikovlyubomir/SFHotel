package com.company;

import com.company.models.Booking;
import com.company.models.User;
import com.company.persistance.ModelManagerSingleton;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

public class ReportMgr {
    public ReportMgr() {

    }

    public void saveReportToFile() {

        PrintWriter pw = null;
        try {
            pw = new PrintWriter("report.txt");
            pw.println();
            pw.println("<--REPORT FOR SF HOTEL-->>");
            pw.println("<------------------------------------------->");
            pw.println("Active bookings.");
            for (Booking b : ModelManagerSingleton.getInstance().Bookings().getAllBookings()) {
                if (b.getToDate().compareTo(new Date()) <= 0) {
                    pw.println(b.toString());
                }
            }
            pw.println("<------------------------------------------->");
            pw.println("All existing bookings:");
            for (Booking b : ModelManagerSingleton.getInstance().Bookings().getAllBookings()) {
                pw.println(b.toString());
            }
            pw.println("<------------------------------------------->");
            pw.println("All users:");
            for (User u : ModelManagerSingleton.getInstance().Users().getAllUsers()) {
                pw.println(u.toString());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
