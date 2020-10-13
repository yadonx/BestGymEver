import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
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

        dialogBoxOut("Startar Best Gym Ever");
        while (true) {
            String nameOrSSNumber = dialogBoxInput("Namn eller Personnummer", "");

            if (nameOrSSNumber == null || nameOrSSNumber.trim().equalsIgnoreCase("exit")) {
                dialogBoxOut("Programmet stängs ner.");
                System.exit(0);
            }
            Customer customer = checkIfCustomerExistInList(customerList, nameOrSSNumber.trim());
            if (customer == null) {
                if (nameOrSSNumber.isBlank())
                    dialogBoxOut("Du måste skriva namn eller personnummer");
                else dialogBoxOut(nameOrSSNumber + "\nFinns inte i registret");
            } else {
                boolean didPay = checkPaidLastDate(customer);
                if (didPay) {
                    writeCustomerVisitDate(customer, getCustomerFilepath(customer.getSocialSecurityNumber()));
                    dialogBoxOut(customer.getName() + "\nHar betalat sitt medlemskap.");
                } else dialogBoxOut(customer.getName() + "\nHar inte betalat de senaste året.");
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

    /**
     * Checking if the customer has paid in the last year.
     *
     * @param customer A customer.
     * @return A true if the customer has paid in the last year else its false.
     */
    public boolean checkPaidLastDate(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException("Error: Customer is null");
        if (customer.getLastPaidDate() == null || customer.getLastPaidDate().equals(""))
            return false;
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        LocalDate lastPaidDate = LocalDate.parse(customer.getLastPaidDate());
        return dateOneYearAgo.isBefore(lastPaidDate) || dateOneYearAgo.equals(lastPaidDate);
    }

    //todo fixa throwsen i getCustomerfilepath
    /**
     * This method creates a file for the customer.
     *
     * @param socialSecurityNumber A string to name the file.
     * @return Customers file.
     */
    public Path getCustomerFilepath(String socialSecurityNumber) {
        if (socialSecurityNumber == null)
            throw new IllegalArgumentException("Error: Input is null");
        if (socialSecurityNumber.isEmpty())
            throw new InputMismatchException("Error: Input is empty");
        return Paths.get("src/files/customer/" + socialSecurityNumber + ".txt");
    }

    /**
     * Write a string if the customer visit the gym to a personal file.
     *
     * @param customer A customer.
     * @param filepath The file to write on.
     */
    public void writeCustomerVisitDate(Customer customer, Path filepath) {
        if (customer == null || filepath == null)
            throw new IllegalArgumentException("Error: Input may be null");
        LocalDate date = LocalDate.now();
        boolean checkFile = Files.exists(filepath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.valueOf(filepath), true))) {
            if (!checkFile)
                bufferedWriter.write("Kund: " + customer.getName() +
                        "\nPersonnummer: " + customer.getSocialSecurityNumber() + "\n");
            bufferedWriter.write("\nDatum: " + date);
            System.out.println("Sparat datum på kundens fil.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
