package src;


import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class PrescriptionManager {
    private static final float SPHERE_MIN = -20.00f;
    private static final float SPHERE_MAX = 20.00f;
    private static final float AXIS_MIN = 0f;
    private static final float AXIS_MAX = 180f;
    private static final float CYLINDER_MIN = -4.00f;
    private static final float CYLINDER_MAX = 4.00f;

    private int prescID;
    private String firstName;
    private String lastName;
    private String address;
    private float sphere;
    private float axis;
    private float cylinder;
    private Date examinationDate;
    private String optometrist;
    private ArrayList<String> postRemarks = new ArrayList<>();
    private static ArrayList<PrescriptionManager> prescriptions = new ArrayList<>(); // List to store all prescriptions

    // No-argument constructor
    public PrescriptionManager() {
    }

    // Constructor with parameters
    public PrescriptionManager(int prescID, String firstName, String lastName, String address, 
                               float sphere, float axis, float cylinder, Date examinationDate, String optometrist) {
        this.prescID = prescID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.sphere = sphere;
        this.axis = axis;
        this.cylinder = cylinder;
        this.examinationDate = examinationDate;
        this.optometrist = optometrist;
    }

    // Getters
    public int getPrescID() {
        return prescID;
    }

    public ArrayList<String> getPostRemarks() {
        return postRemarks;
    }

    private static boolean isValidDate(int day, int month) {
        // Check for valid day based on month and leap year
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: // 31 days
                return day >= 1 && day <= 31;
            case 4: case 6: case 9: case 11: // 30 days
                return day >= 1 && day <= 30;
            case 2: // February
                    return day >= 1 && day <= 28; // Non-leap year
            default:
                return false; // Invalid month
        }
    }

    // Add prescription method (same as before)
    public static boolean addPrescription(int prescID, String firstName, String lastName, String address,
                                          float sphere, float axis, float cylinder, Date examinationDate, String optometrist) {
        // Validate prescription fields
        if (firstName.length() < 4 || firstName.length() > 15) {
            System.out.println("Error: First Name must be between 4 and 15 characters.");
            return false;
        }
        if (lastName.length() < 4 || lastName.length() > 15) {
            System.out.println("Error: Last Name must be between 4 and 15 characters.");
            return false;
        }
        if (address.length() < 20) {
            System.out.println("Error: Address must be at least 20 characters long.");
            return false;
        }
        if (sphere < SPHERE_MIN || sphere > SPHERE_MAX) {
            System.out.println("Error: Sphere value must be between " + SPHERE_MIN + " and " + SPHERE_MAX + ".");
            return false;
        }
        if (cylinder < CYLINDER_MIN || cylinder > CYLINDER_MAX) {
            System.out.println("Error: Cylinder value must be between " + CYLINDER_MIN + " and " + CYLINDER_MAX + ".");
            return false;
        }
        if (axis < AXIS_MIN || axis > AXIS_MAX) {
            System.out.println("Error: Axis value must be between " + AXIS_MIN + " and " + AXIS_MAX + ".");
            return false;
        }
        if (optometrist.length() < 8 || optometrist.length() > 25) {
            System.out.println("Error: Optometrist name must be between 8 and 25 characters.");
            return false;
        }
        int day = examinationDate.getDate();
        int month = examinationDate.getMonth() + 1; // Adding 1 for 1-based month index

        if (!isValidDate(day, month)) {
            return false;
        }


        // Create a new prescription object and add to the list
        PrescriptionManager newPrescription = new PrescriptionManager(prescID, firstName, lastName, address, sphere, axis, cylinder, examinationDate, optometrist);
        prescriptions.add(newPrescription);

        // Write the prescription to the file
        try (FileWriter writer = new FileWriter("presc.txt", true)) {
            writer.write("Prescription ID: " + prescID + "\n");
            writer.write("Patient: " + firstName + " " + lastName + "\n");
            writer.write("Address: " + address + "\n");
            writer.write("Sphere: " + sphere + ", Axis: " + axis + ", Cylinder: " + cylinder + "\n");
            writer.write("Examination Date: " + new SimpleDateFormat("dd/MM/yy").format(examinationDate) + "\n");
            writer.write("Optometrist: " + optometrist + "\n");
            writer.write("--------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to add prescription.");
            return false;
        }

        System.out.println("Prescription added successfully!");
        return true;
    }

    // Add remark method (same as before)
    public boolean addRemark(String remark, String category) {
        if (postRemarks.size() >= 2) {
            System.out.println("Error: A prescription cannot have more than 2 remarks.");
            return false;
        }

        // Check for remark length and category
        String[] words = remark.trim().split("\\s+");
        if (words.length < 6 || words.length > 20) {
            System.out.println("Error: Remark must be between 6 and 20 words.");
            return false;
        }

        if (!Character.isUpperCase(words[0].charAt(0))) {
            System.out.println("Error: The first word must start with an uppercase letter.");
            return false;
        }

        if (!category.equalsIgnoreCase("client") && !category.equalsIgnoreCase("optometrist")) {
            System.out.println("Error: Category must be either 'client' or 'optometrist'.");
            return false;
        }

        // Add remark and write to file
        postRemarks.add(remark);
        try (FileWriter writer = new FileWriter("review.txt", true)) {
            writer.write("Remark: " + remark + "\n");
            writer.write("Category: " + category + "\n");
            writer.write("--------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to add remark.");
            return false;
        }

        System.out.println("Remark added successfully!");
        return true;
    }

    // Method to find prescription by ID
    public static PrescriptionManager findPrescriptionByID(int id) {
        for (PrescriptionManager prescription : prescriptions) {
            if (prescription.getPrescID() == id) {
                return prescription;
            }
        }
        return null; // Not found
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false); // Make the date parsing strict
        Date examinationDate = null;

        while (true) {
            System.out.println("Choose an option: ");
            System.out.println("1. Add Prescription");
            System.out.println("2. Add Remark");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) {
                // Add Prescription logic
                System.out.println("Enter Prescription ID: ");
                int prescID = Integer.parseInt(sc.nextLine());

                System.out.println("Enter First Name: ");
                String firstName = sc.nextLine();

                System.out.println("Enter Last Name: ");
                String lastName = sc.nextLine();

                System.out.println("Enter Address: ");
                String address = sc.nextLine();

                System.out.println("Enter Sphere: ");
                float sphere = Float.parseFloat(sc.nextLine());

                System.out.println("Enter Axis: ");
                float axis = Float.parseFloat(sc.nextLine());

                System.out.println("Enter Cylinder: ");
                float cylinder = Float.parseFloat(sc.nextLine());

                while (examinationDate == null) {
                    System.out.println("Enter Examination Date (DD/MM/YY): ");
                    String dateString = sc.nextLine();
                    try {
                        examinationDate = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        System.out.println("Invalid date format. Please use DD/MM/YY.");
                    }
                }

                System.out.println("Enter Optometrist: ");
                String optometrist = sc.nextLine();

                // Create and add a prescription
                PrescriptionManager.addPrescription(prescID, firstName, lastName, address, sphere, axis, cylinder, examinationDate, optometrist);

            } else if (choice == 2) {
                // Add Remark logic
                System.out.println("Enter Prescription ID to add a remark: ");
                int prescID = Integer.parseInt(sc.nextLine());

                PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(prescID);
                if (prescription != null) {
                    System.out.println("Enter Remark: ");
                    String remark = sc.nextLine();

                    System.out.println("Enter Remark Category (Client/Optometrist): ");
                    String category = sc.nextLine();

                    prescription.addRemark(remark, category);
                } else {
                    System.out.println("Error: Prescription not found.");
                }

            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }

        sc.close();
    }
}