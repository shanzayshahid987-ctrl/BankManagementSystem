import java.io.*;
import java.util.ArrayList;
import customer.Customer;
import admin.Admin;

public class FileManager {
    private static final String CUSTOMER_FILE = "customers.ser";
    private static final String ADMIN_FILE = "admins.ser";

    public static void saveCustomers(ArrayList<Customer> customers) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {
            out.writeObject(customers);
        } catch (IOException e) {
            System.out.println("Error saving customer data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
public static ArrayList<Customer> loadCustomers() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CUSTOMER_FILE))) {
        ArrayList<Customer> loaded = (ArrayList<Customer>) in.readObject();
        System.out.println("DEBUG: Loaded " + loaded.size() + " customers from file.");
        return loaded;
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("DEBUG: Load failed - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        return new ArrayList<>();
    }
}

    public static void saveAdmins(ArrayList<Admin> admins) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ADMIN_FILE))) {
            out.writeObject(admins);
        } catch (IOException e) {
            System.out.println("Error saving admin data: " + e.getMessage());
        }
    }

   public static ArrayList<Admin> loadAdmins() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ADMIN_FILE))) {
        ArrayList<Admin> loaded = (ArrayList<Admin>) in.readObject();
        System.out.println("DEBUG: Loaded " + loaded.size() + " admins from file.");
        return loaded;
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("DEBUG: Load failed - " + e.getClass().getSimpleName() + ": " + e.getMessage());
        return new ArrayList<>();
    }
}
}