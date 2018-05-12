package au.edu.newcastle.seng1110.assignment3;

/**
 * Defines the attributes and operations of a product object.
 *
 * @author Travis Jones c3112196
 * @version SENG1110 Assignment 3
 */
public class Product {
    //instance variables
    private String productName;             // Represents the name of the product.
    private int demand;                     // The demand per week for the product.
    private double setupCost;               // The cost associated with replenishing the product.
    private double inventoryCost;           // The cost to hold the product in inventory.
    private double costPerUnit;             // The cost to purchase a one unit of the product.
    private double sellPerUnit;             // The sale price of one unit of the product.

    /**
     * Constructor - Assigns default values to all attributes except the product name which is
     * set to the string passed.
     *
     * @param name A string to represent the name of the product object.
     */
    public Product(String name) {
        productName = name;
        demand = 0;
        setupCost = 0;
        inventoryCost = 0;
        costPerUnit = 0;
        sellPerUnit = 0;
    }

    /**
     * Constructor - intalises all attributes to the values passed.
     *
     * @param name A string to represent the name of the product object.
     * @param d    The demand for the product per week.
     * @param s    The cost associated with replenishing the product.
     * @param h    The cost to hold the product in inventory per week.
     * @param cost The cost per unit to purchase the product.
     * @param sell The sell price of the product per unit.
     */
    public Product(String name, int d, double s, double h, double cost, double sell) {
        productName = name;
        demand = d;
        setupCost = s;
        inventoryCost = h;
        costPerUnit = cost;
        sellPerUnit = sell;
    }

    /**
     * Accessor method for the productName instance variable
     *
     * @return The product name string.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Accessor method for the demand instance variable.
     *
     * @return The demand per week.
     */
    public int getDemand() {
        return demand;
    }

    /**
     * Accessor method for the setupCost instance variable.
     *
     * @return The cost associated with replenishing the product.
     */
    public double getSetupCost() {
        return setupCost;
    }

    /**
     * Accessor method for the inventoryCost instance variable.
     *
     * @return The cost to store one unit of the product in inventory per week.
     */
    public double getInventoryCost() {
        return inventoryCost;
    }

    /**
     * Accessor method for the costPerUnit instance variable.
     *
     * @return The cost to purchase one unit of the product.
     */
    public double getCostPerUnit() {
        return costPerUnit;
    }

    /**
     * Accessor method for the sellPerUnit instance variable.
     *
     * @return The sell price per unit.
     */
    public double getSellPerUnit() {
        return sellPerUnit;
    }

    /**
     * Setter method for the demand instance variable.
     *
     * @param d The demand value per week.
     */
    public void setDemand(int d) {
        demand = d;
    }

    /**
     * Setter method for the setupCost instance variable.
     *
     * @param s The setup cost value.
     */
    public void setSetupCost(double s) {
        setupCost = s;
    }

    /**
     * Setter method for the inventoryCost instance variable.
     *
     * @param h The inventory cost value.
     */
    public void setInventoryCost(double h) {
        inventoryCost = h;
    }

    /**
     * Setter method for the costPerUnit instance variable.
     *
     * @param cost The cost per unit value.
     */
    public void setCostPerUnit(double cost) {
        costPerUnit = cost;
    }

    /**
     * Setter method for the sellPerUnit instance variable.
     *
     * @param sell The sell price per unit value.
     */
    public void setSellPerUnit(double sell) {
        sellPerUnit = sell;
    }

    /**
     * Calculates the EOQ (Economic Order Quantity) which translates to the optimal replenishment
     * quantity based on the attribute values of the product object.
     */
    public int calculateEOQ() {
        // The EOQ is defined by the equation EOQ = sqrt(2sd/h) where s = setup cost, d = demand per week & h = inventory cost per week.
        return (int) Math.round(Math.sqrt(2 * setupCost * demand / inventoryCost));
    }

    /**
     * Converts the product string to its string equivalent
     *
     * @return A string of the product object.
     */
    public String toString() {
        return String.format("%nName: %s%nDemand Rate: %d%nSetup Cost: %.2f%nUnit Cost: %.2f%n"
                        + "Inventory Cost: %.2f%nSelling Price: %.2f%n", productName, demand, setupCost, costPerUnit,
                inventoryCost, sellPerUnit);
    }

    /**
     * Calculates the replenishment plan for the product for the nominal amount of weeks passed to the
     * method. The replenishment plan is defined as a table displaying stock information and a cost and
     * profit statement. The table consists of a tuple for each week in the replenishment plan and
     * indicates the values of, the amount of stock to order, the demand and the stock remaining in
     * inventory.
     *
     * @param weeks The number of weeks to calculate the replenishment plan for.
     */
    public String replenishmentPlan(int weeks) {
        String table = String.format("%4s %13s %10s %13s", "Week", "Order Qty", "Demand", "Inventory\n");   // Stores the table out put, starting with the table header.
        int stock = 0;                           // Stores the running tally of stock each week for table output.
        int totalUnits = 0;                     // Stores the total amount of units ordered for cost calculations.
        int replenishmentCount = 0;             // Stores a count of the number of replenishments that occur for cost calculations.
        int sumStock = 0;                       // Stores the total amount of items kept in stock per week for cost calculations.

        // Creates the table output by one row at a time (ie. one loop execution per row).
        for (int i = 1; i <= weeks; i++) {
            /* Stores the order quantity that is output at row i in the table. The order quantity
             * is assumed to be 0, proceeding if statements will amend the orderQty value for
             * when the orderQty needs to be a different value.
             */
            int orderQty = 0;

            //If demand is larger than remaining stock it is necessary to get a replenishment.
            if (getDemand() > stock) {
                /* If the stock order to be placed is the very last stock order to occur in the
                 * period then the order quantity needs to be amended to ensure the stock balance
                 * at week n is 0.

                 * The last stock order will occur when the standard order quantity (EOQ value)
                 * + remaining stock will satisfy demand for the remaining weeks in the period.
                 * This is represented mathematically as the boolean condition in the if statement.
                 */
                if (weeks + 1 - i <= (calculateEOQ() + stock) / getDemand()) {
                    /* The last order quantity + remaining stock must equal remaining weeks * demand
                     * for the stock to finish at 0. The remaining weeks will always be n + 1 - i
                     * because the order needs to fulfil the demand for the current week.
                     */
                    orderQty = (weeks + 1 - i) * getDemand() - stock;
                } else {
                    orderQty = calculateEOQ();      // If no conditions above are true it's ok to use the normal EOQ value.
                }
                totalUnits += orderQty;             // Update total units tally.
                replenishmentCount++;               // Replenishment has occured, increment the count by 1.
            }
            stock += orderQty - getDemand();        // Update stock level.
            sumStock += stock;                      // Update tally of stock in inventory.
            table += String.format("%4d %13d %10d %12d \n", i, orderQty, getDemand(), stock);     // Update table.
        }
        double cost = replenishmentCount * getSetupCost() + totalUnits * getCostPerUnit() + sumStock * getInventoryCost();    // Calculate cost.
        double profit = weeks * getDemand() * getSellPerUnit() - cost;  // Calculate profit.

        //Add the cost and profit to the table to complete the replenishment strategy.
        table += String.format("\n\nThe total cost is $%.2f\nThe total profit is $%.2f", cost, profit);

        return table;
    }
}
