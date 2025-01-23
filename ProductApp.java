package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;


public class ProductApp {
    public static String FILE_NAME = "products.txt";
    private static final boolean HASH_ENABLED = true;

    public static void setFileName(String fileName) {
        FILE_NAME = fileName;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select Role: 1- Manager, 2- Staff, 0- Exit");
            int role = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (role == 0) {
                System.out.println("Exiting program...");
                break;
            }

            if (role != 1 && role != 2) {
                System.out.println("Invalid role. Please try again.");
                continue;
            }

            String roleName = (role == 1) ? "Manager" : "Staff";
            System.out.println("You are logged in as: " + roleName);

            if (role == 1) { // Manager
                managerMenu(scanner);
            } else { // Staff
                staffMenu(scanner);
            }
        }

        scanner.close();
    }

    private static void managerMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nManager Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Delete Products");
            System.out.println("0. Log Out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner, "Manager");
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    deleteProducts();
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void staffMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nStaff Menu:");
            System.out.println("1. Add Product");
            System.out.println("0. Log Out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addProduct(scanner, "Staff");
                    break;
                case 0:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    protected static void addProduct(Scanner scanner, String roleName) {
        System.out.println("Enter Product Name: ");
        String productName = scanner.nextLine();

        System.out.println("Enter Product Price: ");
        double productPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String record = "Role: " + roleName + ", Product: " + productName + ", Price: " + productPrice;

        if (HASH_ENABLED) {
            record = hashRecord(record);
        }

        appendToFile(record);
        System.out.println("Product added successfully!\n");
    }

    private static void viewProducts() {
        System.out.println("\n--- List of Products ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No products found. File does not exist.");
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    private static void deleteProducts() {
        System.out.println("\n--- Delete Products ---");

        // Read all products into a list
        List<String> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                products.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No products found. File does not exist.");
            return;
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return;
        }

        // Display products
        if (products.isEmpty()) {
            System.out.println("No products available to delete.");
            return;
        }

        System.out.println("Select a product to delete:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i));
        }

        // Get user input for product to delete
        System.out.print("Enter the number of the product to delete: ");
        Scanner scanner = new Scanner(System.in);
        int productNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Validate input
        if (productNumber < 1 || productNumber > products.size()) {
            System.out.println("Invalid product number. Deletion cancelled.");
            return;
        }

        // Remove the selected product
        products.remove(productNumber - 1);
        System.out.println("Product deleted successfully!");

        // Write the remaining products back to the file
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (String product : products) {
                writer.write(product + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    private static void appendToFile(String record) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(record + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    static String hashRecord(String record) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(record.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing record: " + e.getMessage());
        }
    }

    static String unHashedRecord(String record) {
        return record; //return record
    }
}
