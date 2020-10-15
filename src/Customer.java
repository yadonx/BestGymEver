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

    public Customer(String socialSecurityNumber, String name, LocalDate lastPaidDate) {
        if (lastPaidDate == null)
            throw new InputMismatchException("Error: lastPaidDate is null");
        this.name = name;
        this.socialSecurityNumber = socialSecurityNumber;
        this.lastPaidDate = lastPaidDate;
        setHasPaid();
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

    private void setHasPaid() {
        LocalDate dateOneYearAgo = LocalDate.now().minusYears(1);
        this.hasPaid = dateOneYearAgo.isBefore(lastPaidDate) || dateOneYearAgo.equals(lastPaidDate);
    }

    public boolean getHasPaid(){
        return hasPaid;
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
