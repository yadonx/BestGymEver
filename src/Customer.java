/**
 * Created by Emil Johansson
 * Date: 2020-10-08
 * Time: 20:25
 * Project: Best Gym Ever
 */
public class Customer {
    private final String name;
    private final String socialSecurityNumber;
    private final String lastPaidDate;

    public Customer(String socialSecurityNumber, String name, String lastPaidDate) {
        this.name = name;
        this.socialSecurityNumber = socialSecurityNumber;
        this.lastPaidDate = lastPaidDate;
    }

    /**
     * Get the name of the customer.
     *
     * @return The name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the social security number of the customer.
     *
     * @return The social security number of the customer.
     */
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    /**
     * Get the last paid date.
     *
     * @return The date the customer last paid.
     */
    public String getLastPaidDate() {
        return lastPaidDate;
    }

}
