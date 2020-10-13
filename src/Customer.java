import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.InputMismatchException;


/**
 * Created by Emil Johansson
 * Date: 2020-10-08
 * Time: 20:25
 * Project: Best Gym Ever
 */
public class Customer{
    boolean test = false;
    private final String name;
    private final String socialSecurityNumber;
    private final LocalDate lastPaidDate;
    private boolean hasPaid;

    public Customer(String socialSecurityNumber, String name, String lastPaidDate) {
        if (lastPaidDate == null || lastPaidDate.isEmpty())
            throw new InputMismatchException("Error: lastPaidDate is either null or empty");
        this.name = name;
        this.socialSecurityNumber = socialSecurityNumber;
        this.lastPaidDate = LocalDate.parse(lastPaidDate);
        this.hasPaid = getHasPaid();
    }

    public String getName() {
        return name;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public LocalDate getLastPaidDate() {
        return lastPaidDate;
    }

    /**
     * Checking if the customer has paid in the last year.
     *
     * @return A true if the customer has paid in the last year else its false.
     */
    public boolean getHasPaid() {
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        return dateOneYearAgo.isBefore(lastPaidDate) || dateOneYearAgo.equals(lastPaidDate);
    }

    /**
     * This method creates a file and write the date of the customers visit.
     * Using the socialSecurityNumber as file name.
     *
     */
    public void writeVisitDateToFile(Path testPath) {
        if (this.socialSecurityNumber == null || this.socialSecurityNumber.isEmpty())
            throw new IllegalArgumentException("Error: socialSecurityNumber may be null or empty");
        Path filepath = Paths.get("src/files/customer/" + this.socialSecurityNumber + ".txt");
        if (test)
            filepath = testPath;
        boolean checkFile = Files.exists(filepath);

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(String.valueOf(filepath), true))) {
            if (!checkFile)
                bufferedWriter.write("Kund: " + this.name +
                        "\nPersonnummer: " + this.socialSecurityNumber + "\n");
            bufferedWriter.write("\nDatum: " + LocalDate.now());
            System.out.println("Sparat datum på kundens fil.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
