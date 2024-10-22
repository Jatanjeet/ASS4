package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import src.PrescriptionManager;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class PrescriptionManagerAddPrescriptionTest {
    private PrescriptionManager pm;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize PrescriptionManager before each test
        pm = new PrescriptionManager();
    }

    @Test
    void testAddValidPrescription() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date examDate = dateFormat.parse("21/10/23");

        assertTrue(pm.addPrescription(1,"John", "Doeee", "20charectorslongstring", 2.5f, 1.75f, 2.0f, examDate, "Dr. Smith"),
                "Valid prescription should be added successfully.");
    }

    @Test
    void testAddPrescriptionMissingFields() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date examDate = dateFormat.parse("21/10/23");

        assertFalse(pm.addPrescription(1, "John", "", "20charectorslongstring", 2.5f, 1.75f, 1.0f, examDate, "Dr. Smith"),
                "Prescription with missing last name should be rejected.");
    }

    @Test
    void testAddPrescriptionInvalidValues() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date examDate = dateFormat.parse("21/10/23");

        assertFalse(pm.addPrescription(1, "John", "Doee", "20charectorslongstring", 50.0f, 1.75f, 2.0f, examDate, "Dr. Smith"),
                "Prescription with invalid sphere value should be rejected.");
        assertFalse(pm.addPrescription(1, "John", "Doee", "20charectorslongstring", 2.5f, 1.75f, 200.0f, examDate, "Dr. Smith"),
                "Prescription with invalid axis value should be rejected.");
    }

    @Test
    void testAddDuplicatePrescription() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date examDate = dateFormat.parse("21/12/23");

        assertTrue(pm.addPrescription(1, "John", "Doee", "20charectorslongstring", 2.5f, 1.75f, 2.0f, examDate, "Dr. Smith"),
                "Prescription should be rejected with invalid date.");
    }
}
