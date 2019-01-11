package com.company;

import com.company.models.*;
import com.company.persistance.ModelManagerSingleton;

import java.util.*;

public class View {

    ModelManagerSingleton models;
    User activeUser;
    ArrayList<String> allowedGroups;
    Scanner sc;

    public View() {
        this.models = ModelManagerSingleton.getInstance();
        this.activeUser = null;
        sc = new Scanner(System.in);
        allowedGroups = new ArrayList<>();
        allowedGroups.add("guest");
        allowedGroups.add("administrator");
        allowedGroups.add("front desk");
    }

    public void init() {
        Timer timer = new Timer();
        timer.schedule(new ReportBookings(), 0, 30000);
        printMenu();
    }

    private void printMenu() {
        printSeparator();
        if (activeUser == null) {
            System.out.println("You are not logged in, please enter username.");
            awaitLogin();
        } else {
            switch (activeUser.getUserGroup()) {
                case "guest":
                    printChoicesGuest();
                    break;
                case "front desk":
                    printChoicesFrontDesk();
                    break;
                case "administrator":
                    printChoicesAdmin();
                    break;
                default:
                    System.out.println("NOT IMPLEMENTED");
                    break;
            }
        }
    }

    private void awaitLogin() {
        printSeparator();
        String input = sc.nextLine();
        checkIfExit(input);
        if (input.equals("register")) {
            register();
            return;
        }
        User user = models.Users().getUser(input);
        if (user != null) {
            activeUser = user;
            System.out.println("Welcome back, " + activeUser.getUsername());
            printMenu();
        } else {
            System.out.println("Wrong username, please try again, or type register to register new user. Type quit to exit.");
            awaitLogin();
        }
    }

    private void register() {
        printSeparator();
        Scanner sc = new Scanner(System.in);
        System.out.println("In order to register, first type your desired username: ");
        String inputUsername = sc.nextLine();
        System.out.println("Now type who you are ( choices are [guest, administrator, front desk] case sensitive ): ");
        String inputUserGroup = sc.nextLine();
        if (!allowedGroups.contains(inputUserGroup)) {
            System.out.println("You picked a invalid user group, returning to main menu.");
            printMenu();
            return;
        }
        User user = new User(inputUsername, inputUserGroup);
        if (!models.Users().addUser(user)) {
            System.out.println("User with specified name already exists. Try to log in.");
            printMenu();
            return;
        }
        activeUser = user;
        System.out.println("Registration successful, automatically logged you in.");
        printMenu();
    }

    private void checkIfExit(String input) {
        if (input.equals("quit")) {
            sc.close();
            System.exit(0);
        }
    }

    private void printChoicesGuest() {
        System.out.println("You are logged in as guest:");
        System.out.println("Select from options bellow:");
        System.out.println("Type 'book' to book a room:");
        System.out.println("Type 'seeavailable' to book a room:");
        System.out.println("Type 'mybookings' to see all your bookings:");
        System.out.println("Type 'cancelrsv' to cancel a reservation:");
        System.out.println("NOTE: Type quit anytime to exit the application.");
        String input = sc.nextLine();
        checkIfExit(input);
        switch (input) {
            case "book":
                printBookSelector();
                break;
            case "cancelrsv":
                printCancelSelectorGuest();
                break;
            case "seeavailable":
                printAvailableRooms(true);
                break;
            case "mybookings":
                printAllSelfBookings(false);
                break;
            default:
                printSeparator();
                System.out.println("No such option or you don't have enough permissions.");
                printMenu();
                break;

        }
    }

    private void printChoicesFrontDesk() {
        System.out.println("You are logged in as front desk manager:");
        System.out.println("Select from options bellow:");
        System.out.println("Type 'book' to book a room:");
        System.out.println("Type 'seeavailable' to see available rooms:");
        System.out.println("Type 'bookings' to see all bookings:");
        System.out.println("Type 'cancelrsv' to cancel a reservation:");
        System.out.println("Type 'amountdue' to see how much a customer should pay:");
        System.out.println("Type 'payall' to pay for all bookings of a customer:");
        System.out.println("NOTE: Type quit anytime to exit the application.");
        String input = sc.nextLine();
        checkIfExit(input);
        switch (input) {
            case "book":
                printBookSelectorFrontDesk();
                break;
            case "cancelrsv":
                printCancelSelectorFrontDesk();
                break;
            case "seeavailable":
                printAvailableRooms(true);
                break;
            case "bookings":
                printAllBookings(false);
                break;
            case "amountdue":
                printAmountDue();
                break;
            case "payall":
                printMoneyPay();
                break;
            default:
                printSeparator();
                System.out.println("No such option or you don't have enough permissions.");
                printMenu();
                break;

        }
    }

    private void printChoicesAdmin() {
        System.out.println("You are logged in as ADMIN:");
        System.out.println("Select from options bellow:");
        System.out.println("Type 'allrooms' too see all existing rooms:");
        System.out.println("Type 'allcategories' too see all existing categories:");
        System.out.println("Type 'alltypes' too see all existing types:");
        System.out.println("Type 'addtype' too add a room type:");
        System.out.println("Type 'addcategory' too add a room category:");
        System.out.println("Type 'addroom' too add a room:");
        System.out.println("Type 'report' too save a local report file:");
        System.out.println("NOTE: Type quit anytime to exit the application.");
        String input = sc.nextLine();
        checkIfExit(input);
        switch (input) {
            case "allrooms":
                printAllRooms();
                break;
            case "allcategories":
                printAllCategories(true);
                break;
            case "alltypes":
                printAllTypes(true);
                break;
            case "addtype":
                printAddType();
                break;
            case "addcategory":
                printAddCategory();
                break;
            case "addroom":
                printAddRoom();
                break;
            case "report":
                reportToFle();
                break;
            default:
                printSeparator();
                System.out.println("No such option or you don't have enough permissions.");
                printMenu();
                break;

        }
    }

    private void printAllRooms() {
        printSeparator();
        for (Room r : models.Rooms().getAllRoooms())
            System.out.println(r.toString());
        printMenu();
    }

    private void printBookSelectorFrontDesk() {
        printSeparator();
        System.out.println("Please select the number of the room you want to book.");
        printAvailableRooms(false);
        System.out.println("Type the number of the room you want to book");
        String input = sc.nextLine();
        checkIfExit(input);
        Room chosenRoom = models.Rooms().getRoom(Integer.parseInt(input));
        if (chosenRoom == null) {
            System.out.println("Room with such number does not exits, please try again.");
            printBookSelector();
            return;
        }
        System.out.println("Please input the date you want to book the room from ( format DD/MM/YY )");
        input = sc.nextLine();
        checkIfExit(input);
        Date dateFrom = parseDate(input);
        System.out.println("Please input the date you want to book the room until ( format DD/MM/YY )");
        input = sc.nextLine();
        checkIfExit(input);
        Date dateTo = parseDate(input);
        System.out.println("Please input the name of the guest.");
        input = sc.nextLine();
        checkIfExit(input);
        User userToLink = models.Users().getUser(input);
        if (userToLink != null) {
            System.out.println("Found an existing user with this name, linking it.");
        } else {
            userToLink = new User(input, "guest");
        }
        Booking newBooking = new Booking(chosenRoom, userToLink, dateFrom, dateTo);
        if (models.Bookings().addBooking(newBooking)) {
            System.out.println("Successfully booked: " + newBooking.toString());
            models.Users().addUser(userToLink);
            printMenu();
        } else {
            System.out.println("The booking you are trying to create overlaps with another booking, try again with other room or some other dates.");
            printMenu();
        }
    }

    private void printCancelSelectorGuest() {
        printSeparator();
        int res = printAllSelfBookings(true);
        if (res != 0) {
            System.out.println("Type in the ID of the booking you want to delete.");
            String input = sc.nextLine();
            checkIfExit(input);
            models.Bookings().deleteBookingByIndex(Integer.parseInt(input) - 1);
            System.out.println("Successfully deleted");
        }
        printMenu();

    }

    private void printCancelSelectorFrontDesk() {
        printSeparator();
        printAllBookings(true);
        System.out.println("Type in the ID of the booking you want to delete.");
        String input = sc.nextLine();
        checkIfExit(input);
        models.Bookings().deleteBookingByIndex(Integer.parseInt(input) - 1);
        System.out.println("Successfully deleted");
        printMenu();
    }

    private void printAvailableRooms(boolean triggerMenu) {
        printSeparator();
        System.out.println("The following rooms are available today.");
        for (Room room : models.Rooms().getAllRoooms()) {
            if (isRoomAvailable(room.getNumber())) {
                System.out.println(room.toString());
            }
        }
        if (triggerMenu)
            printMenu();
    }

    private boolean isRoomAvailable(int roomNr) {
        Date now = new Date();
        for (Booking booking : models.Bookings().getAllBookings()) {
            if (booking.getRoom().getNumber() == roomNr) {
                if (booking.getFromDate().compareTo(now) < 0 && booking.getToDate().compareTo(now) > 0)
                    return false;
            }
        }
        return true;
    }

    private void printBookSelector() {
        printSeparator();
        System.out.println("Please select the number of the room you want to book.");
        printAvailableRooms(false);
        System.out.println("Type the number of the room you want to book");
        String input = sc.nextLine();
        checkIfExit(input);
        Room chosenRoom = models.Rooms().getRoom(Integer.parseInt(input));
        if (chosenRoom == null) {
            System.out.println("Room with such number does not exits, please try again.");
            printBookSelector();
            return;
        }
        System.out.println("Please input the date you want to book the room from ( format DD/MM/YY )");
        input = sc.nextLine();
        checkIfExit(input);
        Date dateFrom = parseDate(input);
        System.out.println("Please input the date you want to book the room until ( format DD/MM/YY )");
        input = sc.nextLine();
        checkIfExit(input);
        Date dateTo = parseDate(input);
        Booking newBooking = new Booking(chosenRoom, activeUser, dateFrom, dateTo);
        if (models.Bookings().addBooking(newBooking)) {
            System.out.println("Successfully booked: " + newBooking.toString());
            printMenu();
        } else {
            System.out.println("The booking you are trying to create overlaps with another booking, try again with other room or some other dates.");
            printMenu();
        }
    }

    private int printAllSelfBookings(boolean showIndex) {
        printSeparator();
        System.out.println("Your bookings in our hotel are");
        int i = 1;
        int found = 0;
        for (Booking booking : models.Bookings().getAllBookings()) {
            if (booking.getUser().getUsername().equals(activeUser.getUsername())) {
                if (showIndex)
                    System.out.println("ID:" + i + ": " + booking.toString());
                else
                    System.out.println(booking.toString());
                found++;
            }
            i++;
        }
        if (found == 0) {
            System.out.println("It appears as you dont have any bookings in our hotel.");
        }
        if (!showIndex) //Not returning to menu because a cancel action;
            printMenu();
        return found;
    }

    private void printAllBookings(boolean showIndex) {
        printSeparator();
        System.out.println("All bookings in the hotel are.");
        int i = 1;
        for (Booking booking : models.Bookings().getAllBookings()) {
            if (showIndex)
                System.out.println("ID:" + i + ": " + booking.toString());
            else
                System.out.println(booking.toString());
            i++;
        }
        if (!showIndex) //Not returning to menu because a cancel action;
            printMenu();
    }

    //ADMIN STUFF//

    private void printAllCategories(boolean close) {
        printSeparator();
        for (RoomCategory rc : models.Categories().getAllCategories()) {
            System.out.println(rc.toString());
        }
        if (close)
            printMenu();
    }

    private void printAllTypes(boolean close) {
        printSeparator();
        for (RoomType rt : models.Types().getAllRoomTypes()) {
            System.out.println(rt.toString());
        }
        if (close)
            printMenu();
    }


    private void printAddType() {
        printSeparator();
        System.out.println("Type the name of the Type you want to add");
        String name = sc.nextLine();
        checkIfExit(name);
        ArrayList<String> options = new ArrayList<>();
        System.out.println("Type in options to add options, type DONE when done.");
        String option = sc.nextLine();
        checkIfExit(option);
        do {
            options.add(option);
            option = sc.nextLine();
            checkIfExit(option);
        } while (!option.equals("DONE"));
        RoomType type = new RoomType(name, options);
        if (models.Types().addRoomType(type))
            System.out.println("Type successfully added :" + type.toString());
        else System.out.println("Type with this name already exists.");
        printMenu();
    }

    private void printAddCategory() {
        printSeparator();
        System.out.println("Type the name of the category you want to add. (Single/Double/Apartment)");
        String name = sc.nextLine();
        checkIfExit(name);
        System.out.println("Type in the name of the type for this category.");
        String type = sc.nextLine();
        checkIfExit(type);
        RoomType rt = models.Types().getType(type);
        if (rt == null) {
            System.out.println("No such type exists, please try again");
            printMenu();
            return;
        }
        System.out.println("Type in the price of the category.");
        String price = sc.nextLine();
        RoomCategory rc = new RoomCategory(name, rt, Integer.parseInt(price));
        if (models.Categories().addRoomCategory(rc))
            System.out.println("Category successfully added :" + rc.toString());
        else System.out.println("Such category already exists.");
        printMenu();
    }

    private void printAddRoom() {
        printSeparator();
        System.out.println("Type the number of the room you want to add.");
        String number = sc.nextLine();
        checkIfExit(number);
        System.out.println("Type in the name of the category for the room.");
        String cat = sc.nextLine();
        checkIfExit(cat);
        System.out.println("Type in the name of the category type for the room.");
        String type = sc.nextLine();
        checkIfExit(type);
        RoomCategory rc = models.Categories().getCategory(cat, type);
        if (rc == null) {
            System.out.println("No such category exists, please try again.");
            printMenu();
            return;
        }
        Room room = new Room(rc, Integer.parseInt(number));
        if (models.Rooms().addRoom(room))
            System.out.println("Room successfully added :" + room.toString());
        else System.out.println("Room with this number already exists.");
        printMenu();
    }

    private void printAmountDue(){
        printSeparator();
        System.out.println("Type the name of the customer that has to pay.");
        String name = sc.nextLine();
        checkIfExit(name);
        int to_pay = models.Users().getAmmountForPay(name);
        if(to_pay ==0 ){
            System.out.println("Customer doesnt have to pay anything. SCAM.");
            printMenu();
        } else {
            System.out.println("Customer owes you " + to_pay + " BGN.");
            printMenu();
        }
    }


    private void printMoneyPay(){
        printSeparator();
        System.out.println("Type the name of the customer that has to pay.");
        String name = sc.nextLine();
        checkIfExit(name);
        int to_pay = models.Users().getAmmountForPay(name);
        if(to_pay ==0 ){
            System.out.println("Customer doesnt have to pay anything. SCAM.");
            printMenu();
        } else {
            models.Users().pay(name);
            System.out.println("Paid successfully.");
            printMenu();
        }
    }


    private Date parseDate(String stringDate) {
        String[] to_arr = stringDate.split("/");
        return new Date(Integer.parseInt(to_arr[2]), Integer.parseInt(to_arr[1]), Integer.parseInt(to_arr[0]));
    }

    private void reportToFle() {
        ReportMgr rpmgr = new ReportMgr();
        rpmgr.saveReportToFile();
        System.out.println("REPORT WAS SAVED IN FILE Report.txt");
        printMenu();
    }


    private static void printSeparator() {
        System.out.println("<----------------------------------------------------------------------->");
    }

}


class ReportBookings extends TimerTask {
    public void run() {
        System.out.println("Current bookings are.");
        for (Booking booking : ModelManagerSingleton.getInstance().Bookings().getAllBookings()) {
            System.out.println(booking.toString());
        }
        System.out.println("<----------------------------------->");
    }
}
