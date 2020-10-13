import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;


/**
 * Created by Emil Johansson
 * Date: 2020-10-08
 * Time: 17:05
 * Project: Best Gym Ever
 */
public class BestGymEver {
    boolean test = false;
    private final ImageIcon ICON = new ImageIcon(BestGymEver.class.getResource("/files/icon/gymicon.png"));
    private final String TITLE = "Best Gym Ever";

    public static void main(String[] args) {
        BestGymEver main = new BestGymEver();
        main.mainProgram();
    }

    /**
     * The main program to run the gym.
     */
    public void mainProgram() {
        Path filepath = Paths.get("src/files/customer/customers.txt");
        List<Customer> customerList = createCustomerList(filepath);

        dialogBoxOut("Välkommen till Best Gym Ever");
        while (true) {
            String nameOrSSNumber = dialogBoxInput("Namn eller Personnummer", "");
            Customer customer = checkIfCustomerExistInList(customerList, nameOrSSNumber.trim());
            if (customer == null) {
                if (nameOrSSNumber.isBlank())
                    dialogBoxOut("Du måste skriva namn eller personnummer");
                else dialogBoxOut(nameOrSSNumber + "\nFinns inte i registret");
            }
            else {
                if (customer.getHasPaid()) {
                    customer.writeVisitDateToFile(Path.of(""));
                    dialogBoxOut(customer.getName() + "\nHar betalat sitt medlemskap.");
                }
                else
                    dialogBoxOut(customer.getName() + "\nHar inte betalat de senaste året.");
            }
        }
    }

    /**
     * A method to get an input dialog box with a message the gyms icon.
     *
     * @param message       A message that shows in the dialog box.
     * @param testParameter An input if its a test.
     * @return A string with the input.
     */
    public String dialogBoxInput(String message, String testParameter) {
        String input;
        if (test)
            input = testParameter;
        else
            input = (String) JOptionPane.showInputDialog(null, message, TITLE,
                    JOptionPane.INFORMATION_MESSAGE, ICON, null, "");
        if (input == null || input.trim().equalsIgnoreCase("exit"))
            System.exit(0);
        return input;
    }

    /**
     * A method to get a dialog box with a message and the gyms icon.
     *
     * @param message A message that shows in the dialog box.
     */
    public void dialogBoxOut(String message) {
        int x = JOptionPane.showConfirmDialog(null, message, TITLE, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, ICON);
        if (x == JOptionPane.CLOSED_OPTION)
            System.exit(0);
    }


    /**
     * Create a list with customers from a file.
     *
     * @param filepath The path to the file.
     * @return A list with customers.
     */
    public List<Customer> createCustomerList(Path filepath) {
        if (filepath == null)
            throw new IllegalArgumentException("Error: Input is null");
        List<Customer> theList = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(String.valueOf(filepath)))) {
            while (sc.hasNextLine()) {
                String socialSecurityNumber = sc.next().trim().replace(",", "");
                String name = sc.nextLine().trim();
                String lastPaidDate = sc.nextLine().trim();
                theList.add(new Customer(socialSecurityNumber, name, lastPaidDate));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return theList;
    }

    /**
     * Checking in a list if the customer is a member of the gym.
     *
     * @param list           The customer list.
     * @param nameOrSSNumber The name or social security number of the customer.
     * @return An object of customer if he/she exist in the list.
     */
    public Customer checkIfCustomerExistInList(List<Customer> list, String nameOrSSNumber) {
        if (list == null)
            throw new IllegalArgumentException("Error: List is null");
        for (Customer c : list) {
            if (c.getName().equalsIgnoreCase(nameOrSSNumber.trim())
                    || c.getSocialSecurityNumber().equalsIgnoreCase(nameOrSSNumber.trim()))
                return c;
        }
        return null;
    }
}
