import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;



/**
 * Created by Emil Johansson
 * Date: 2020-10-13
 * Time: 20:21
 * Project: Best Gym Ever
 */
public class CustomerTest {
    Customer customer = new Customer("0203021234", "Steffe Steffson", LocalDate.now());

    @Test(expected = InputMismatchException.class)
    public void should_throw_IllegalArgumentException_if_lastPaidDate_is_null() {
        Customer customer = new Customer("", "", null);
    }

    @Test
    public void test_if_Customer_has_paid_in_last_year() {
        assertTrue(customer.getHasPaid());
    }

    @Test
    public void test_if_Customer_has_paid_exactly_one_year_ago() {
        Customer customer = new Customer("", "", LocalDate.now().minusYears(1));
        assertTrue(customer.getHasPaid());
    }

    @Test
    public void test_if_Customer_has_paid_two_years_ago() {
        Customer customer = new Customer("", "", LocalDate.now().minusYears(2));
        assertFalse(customer.getHasPaid());
    }

    @Test
    public void test_write_to_file_if_file_dont_exist_on_disc() throws IOException {
        customer.test = true;
        File tempFile = File.createTempFile("temp", null);
        tempFile.delete();
        tempFile.deleteOnExit();
        Path tempPath = Paths.get(tempFile.getPath());

        customer.writeVisitDateToFile(tempPath);
        Scanner sc = new Scanner(tempFile);
        assertEquals("Kund: Steffe Steffson", sc.nextLine());
        assertEquals("Personnummer: 0203021234", sc.nextLine());
        assertEquals("", sc.nextLine());
        assertEquals("Datum: " + LocalDate.now(), sc.nextLine());
        assertFalse(sc.hasNextLine());
    }

    @Test
    public void test_write_to_file_if_file_exist_on_disc() throws IOException {
        customer.test = true;
        File tempFile = File.createTempFile("temp", null);
        tempFile.deleteOnExit();
        Path tempPath = Paths.get(tempFile.getPath());

        customer.writeVisitDateToFile(tempPath);
        Scanner sc = new Scanner(tempFile);
        assertEquals("", sc.nextLine());
        assertEquals("Datum: " + LocalDate.now(), sc.nextLine());
        assertFalse(sc.hasNextLine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_Path_is_null_in_writeVisitDateToFile(){
        Customer customer = new Customer(null,"",LocalDate.parse("2018-12-02"));
        customer.writeVisitDateToFile(Path.of(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_Customer_is_empty_in_writeVisitDateToFile(){
        Customer customer = new Customer("","",LocalDate.parse("2018-12-02"));
        customer.writeVisitDateToFile(Path.of(""));
    }


}
