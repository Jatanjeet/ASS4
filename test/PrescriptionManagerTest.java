package test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.PrescriptionManager;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class PrescriptionManagerTest {
    private SimpleDateFormat dateFormat;
    private Date examDate;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize a DateFormat and Exam Date before each test
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        examDate = dateFormat.parse("21/10/23");

        // Add a valid prescription before testing the remark functionality
        PrescriptionManager.addPrescription(1, "John", "Doee", "20charectorslongstring", 
                                            -2.50f, 100.0f, -1.75f, examDate, "Dr. Smith");
    }

    // Test for adding remarks to the created prescription
    @Test
    void testAddFirstRemark() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Adding the first remark should be successful
        assertTrue(prescription.addRemark("Great service and friendly old optometrist.", "Client"), 
                   "First remark should be added successfully.");
    }

    @Test
    void testAddSecondRemark() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Adding two remarks should be successful
        assertTrue(prescription.addRemark("One two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen sixteen seventeen eighteen nineteen twenty.", "Client"), 
                   "First remark should be added successfully.");
        assertTrue(prescription.addRemark("One two three four five six seven eight nine ten.", "Optometrist"), 
                   "Second remark should be added successfully.");
    }

    @Test
    void testAddThirdRemarkFails() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Adding two remarks should be successful
        assertTrue(prescription.addRemark("Great service and friendly old optometrist.", "Client"), 
                   "First remark should be added successfully.");
        assertTrue(prescription.addRemark("Prescription was correct and old accurate.", "Optometrist"), 
                   "Second remark should be added successfully.");

        // Adding a third remark should fail
        assertFalse(prescription.addRemark("Follow up appointment scheduled today evening.", "Client"), 
                    "Third remark should not be allowed.");
    }

    @Test
    void testInvalidRemarkTooManyWords() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Test remark with more than 20 words
        String longRemark = "This is a very long remark that should exceed the twenty word limit imposed on to the remarks by the remarks.";
        assertFalse(prescription.addRemark(longRemark, "Client"), 
                    "Remark with more than 20 words should be rejected.");
    }

    @Test
    void testInvalidRemarkTooFewWords() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Test remark with fewer than 6 words
        String shortRemark = "Too short.";
        assertFalse(prescription.addRemark(shortRemark, "Client"), 
                    "Remark with fewer than 6 words should be rejected.");
    }

    @Test
    void testInvalidRemarkCategory() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Test remark with an invalid category
        assertFalse(prescription.addRemark("This is a valid remark in terms of word count.", "Doctor"), 
                    "Remark with an invalid category should be rejected.");
    }

    @Test
    void testRemarkFirstWordNotUppercase() {
        // Retrieve the prescription by ID
        PrescriptionManager prescription = PrescriptionManager.findPrescriptionByID(1);
        assertNotNull(prescription, "Prescription should exist in the system.");

        // Test remark where the first word does not start with an uppercase letter
        assertFalse(prescription.addRemark("invalid start of sentence.", "Client"), 
                    "Remark where the first word doesn't start with an uppercase letter should be rejected.");
    }
}
