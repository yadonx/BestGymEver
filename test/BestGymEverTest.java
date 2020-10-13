import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.time.LocalDate;


/**
 * Created by Emil Johansson
 * Date: 2020-10-08
 * Time: 17:08
 * Project: Best Gym Ever
 */
public class BestGymEverTest {

    private Path filepath;
    private BestGymEver bestGymEver;

    @Before
    public void setUp() throws Exception {
        bestGymEver = new BestGymEver();
        filepath = Paths.get("test/files/customersTest.txt");
    }

    @Test
    public void test_dialogue_box_if_input_is_null() {
        bestGymEver.test = true;
        String input = bestGymEver.dialogBoxInput("", null);
        assertNotEquals("hej", input);
        assertNull(input);
    }

    @Test
    public void test_dialog_box_if_input_is_not_empty() {
        bestGymEver.test = true;
        String input = bestGymEver.dialogBoxInput("", "hej");
        assertNotNull(input);
        assertEquals("hej", input);
    }

    @Test
    public void test_dialog_box_if_input_is_empty() {
        bestGymEver.test = true;
        String input = bestGymEver.dialogBoxInput("", "");
        assertNotNull(input);
        assertTrue(input.isEmpty());
    }

    @Test
    public void test_create_customer_list_from_file() {
        bestGymEver.test = true;
        List<Customer> customerList = bestGymEver.createCustomerList(filepath);

        assertNotNull(customerList.get(0));
        assertEquals("Steffe Steffson", customerList.get(0).getName());
        assertEquals("0203021234", customerList.get(0).getSocialSecurityNumber());
        assertEquals("2019-12-01", customerList.get(0).getLastPaidDate());

        assertEquals("Urban Urbansson", customerList.get(1).getName());
        assertEquals("7104021234", customerList.get(1).getSocialSecurityNumber());
        assertEquals("2018-12-02", customerList.get(1).getLastPaidDate());


        assertEquals("Olof Olivsson", customerList.get(2).getName());
        assertEquals("9011131234", customerList.get(2).getSocialSecurityNumber());
        assertEquals("2020-02-01", customerList.get(2).getLastPaidDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_Path_is_null_in_createCustomerList(){
        bestGymEver.createCustomerList(null);
    }

    @Test
    public void test_if_customer_exist_in_list_with_name() {
        bestGymEver.test = true;
        List<Customer> customerList = bestGymEver.createCustomerList(filepath);

        String nameOrSocialSecurityNumber = "Olof Olivsson";
        Customer customer = bestGymEver.checkIfCustomerExistInList(customerList, nameOrSocialSecurityNumber);
        assertNotNull(customer);
        assertEquals("Olof Olivsson", customer.getName());
        assertEquals("9011131234", customer.getSocialSecurityNumber());
    }

    @Test
    public void test_if_customer_exist_in_list_with_social_security_number() {
        List<Customer> customerList = bestGymEver.createCustomerList(filepath);
        String nameOrSocialSecurityNumber = "7104021234";
        Customer customer = bestGymEver.checkIfCustomerExistInList(customerList, nameOrSocialSecurityNumber);
        assertNotNull(customer);
        assertEquals("7104021234", customer.getSocialSecurityNumber());
        assertEquals("Urban Urbansson", customer.getName());
    }

    @Test
    public void test_if_customer_does_not_exist_in_list() {
        List<Customer> customerList = bestGymEver.createCustomerList(filepath);
        String nameOrSocialSecurityNumber = "Bosse Ekorre";
        Customer customer = bestGymEver.checkIfCustomerExistInList(customerList, nameOrSocialSecurityNumber);
        assertNull(customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_List_is_null_in_checkIfCustomerExistInList(){
        bestGymEver.checkIfCustomerExistInList(null,"");
    }

    @Test
    public void test_if_Customer_has_paid_in_last_year() {
        Customer customer = new Customer("", "", LocalDate.now().toString());
        boolean checkDate = bestGymEver.checkPaidLastDate(customer);
        assertTrue(checkDate);
    }

    @Test
    public void test_if_Customer_has_paid_exactly_one_year_ago() {
        Customer customer = new Customer("", "", LocalDate.now().minusYears(1).toString());
        boolean checkDate = bestGymEver.checkPaidLastDate(customer);
        assertTrue(checkDate);
    }

    @Test
    public void test_if_Customer_has_paid_two_years_ago() {
        Customer customer = new Customer("", "", LocalDate.now().minusYears(2).toString());
        boolean checkDate = bestGymEver.checkPaidLastDate(customer);
        assertFalse(checkDate);
    }

    @Test
    public void test_if_Customer_has_never_paid_with_null() {
        Customer customer = new Customer("", "", null);
        boolean checkDate = bestGymEver.checkPaidLastDate(customer);
        assertFalse(checkDate);
    }

    @Test
    public void test_if_Customer_has_never_paid_with_empty_string() {
        Customer customer = new Customer("", "", "");
        boolean checkDate = bestGymEver.checkPaidLastDate(customer);
        assertFalse(checkDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_Customer_is_null_in_checkPaidLastDate() {
        bestGymEver.checkPaidLastDate(null);
    }

    @Test
    public void test_getFilepath_file_exist() {
        String testFileName = "customers";
        Path filepath = bestGymEver.getCustomerFilepath(testFileName);
        assertEquals("src\\files\\customer\\customers.txt", filepath.toString());
        assertTrue(Files.exists(filepath));
    }

    @Test
    public void test_getFilepath_file_dont_exist(){
        Path filepath2 = bestGymEver.getCustomerFilepath("Dont exist");
        assertFalse(Files.exists(filepath2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_Path_is_null_in_getFilepath(){
        bestGymEver.getCustomerFilepath(null);
    }

    @Test(expected = InputMismatchException.class)
    public void should_throw_IllegalArgumentException_if_Customer_is_empty_in_getCustomerFilepath(){
        bestGymEver.getCustomerFilepath("");
    }

    @Test
    public void test_write_to_file_if_file_dont_exist_on_disc() throws IOException {
        Customer customer = new Customer("9011131234", "Olof Olivsson", "");
        File tempFile = File.createTempFile("temp", null);
        tempFile.delete();
        tempFile.deleteOnExit();
        Path tempPath = Paths.get(tempFile.getPath());

        bestGymEver.writeCustomerVisitDate(customer, tempPath);
        Scanner sc = new Scanner(tempFile);
        assertEquals("Kund: Olof Olivsson", sc.nextLine());
        assertEquals("Personnummer: 9011131234", sc.nextLine());
        assertEquals("", sc.nextLine());
        assertEquals("Datum: " + LocalDate.now(), sc.nextLine());
        assertFalse(sc.hasNextLine());
    }

    @Test
    public void test_write_to_file_if_file_exist_on_disc() throws IOException {
        Customer customer = new Customer("9011131234", "Olof Olivsson", "");
        File tempFile = File.createTempFile("temp", null);
        tempFile.deleteOnExit();
        Path tempPath = Paths.get(tempFile.getPath());

        bestGymEver.writeCustomerVisitDate(customer, tempPath);
        Scanner sc = new Scanner(tempFile);
        assertEquals("", sc.nextLine());
        assertEquals("Datum: " + LocalDate.now(), sc.nextLine());
        assertFalse(sc.hasNextLine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_customer_is_null_in_writeCustomerVisit() {
        bestGymEver.writeCustomerVisitDate(null, filepath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_file_is_null_in_writeCustomerVisit() {
        Customer customer = new Customer("", "", "");
        bestGymEver.writeCustomerVisitDate(customer, null);
    }

}
