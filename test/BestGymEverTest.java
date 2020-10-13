import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        assertEquals(LocalDate.of(2019,12,1), customerList.get(0).getLastPaidDate());

        assertEquals("Urban Urbansson", customerList.get(1).getName());
        assertEquals("7104021234", customerList.get(1).getSocialSecurityNumber());
        assertEquals(LocalDate.of(2018,12,2), customerList.get(1).getLastPaidDate());


        assertEquals("Olof Olivsson", customerList.get(2).getName());
        assertEquals("9011131234", customerList.get(2).getSocialSecurityNumber());
        assertEquals(LocalDate.of(2020,2,1), customerList.get(2).getLastPaidDate());
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
        String nameOrSocialSecurityNumber = "Bosse";
        Customer customer = bestGymEver.checkIfCustomerExistInList(customerList, nameOrSocialSecurityNumber);
        assertNull(customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_if_List_is_null_in_checkIfCustomerExistInList(){
        bestGymEver.checkIfCustomerExistInList(null,"");
    }
}
