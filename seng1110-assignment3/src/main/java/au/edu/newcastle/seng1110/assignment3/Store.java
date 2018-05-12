package au.edu.newcastle.seng1110.assignment3;

/**
 * Defines a store object containing a String name and an array of product objects.
 *
 * @author Travis Jones c3112196
 * @version SENG1110 Assignment 3
 */
public class Store {
    private static final int DEFAULT = 3;   // The default array positions is 3 per the program requirements.

    // Instance variables
    private String storeName;               // Represents the name of the store.
    private Product[] catalogue;            // Represents what products exist in the store.
    private int noOfProducts;               // Enumerates the number of products in the store.

    /**
     * Constructor.
     *
     * @param name A String to represent the name of the store object.
     */
    public Store(String name) {
        storeName = name;
        catalogue = new Product[DEFAULT];
        noOfProducts = 0;                       // While the number of positions in the array is 3 there are 0 products.
    }

    /**
     * Accessor method for the storeName attribute.
     *
     * @return A String of the store name.
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Accessor method for a specific product object's demand attribute.
     *
     * @param index An integer representing the index position of the product object in the array.
     * @return An integer of the demand attribute of the product object.
     */
    public int getCatalogueDemand(int index) {
        return catalogue[index].getDemand();
    }

    /**
     * Returns the index position of the first occurance of a product object that contains a name
     * attribute equal to the String name passed in the method.
     *
     * @param name A String of the product name to search for.
     * @return If a product object exists with a name attribute equal to the passed paramater, then
     * an integer representing the index position is returned; otherwise -1 is returned.
     */
    public int getCatalogueIndex(String name) {
        for (int i = 0; i < noOfProducts; i++)                  // Search the entire array
        {
            if (catalogue[i].getProductName().equals(name))     // True: A product in the array contains a name equal to the string passed in the method.
            {
                return i;                                       // Return the index value
            }
        }
        return -1;                                              // No product exists with a name equal to the string passed, return -1 to signfy it doesn't exist.
    }

    /**
     * Get the string equivalent of a specific product object in the catalogue array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @return A string equivalent of the specified product object.
     */

    public String getCatalogueString(int index) {
        return catalogue[index].toString();
    }

    /**
     * Accessor method for the noOfProducts instance variable.
     *
     * @return An integer representing the number of product objects in the catalogue array.
     */
    public int getNoOfProducts() {
        return noOfProducts;
    }

    /**
     * Setter method for the demand instance variable of a specific product object in the catalogue array
     * instance variable.
     *
     * @param index The index position of the product object in the array.
     * @param d     The demand value for the specified product object.
     */
    public void setCatalogueDemand(int index, int d) {
        catalogue[index].setDemand(d);
    }

    /**
     * Setter method for the setupCost instance variable of the specific product object in the catalgoue
     * array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @param s     The setup cost value for the specified product object.
     */
    public void setCatalogueSetupCost(int index, double s) {
        catalogue[index].setSetupCost(s);
    }

    /**
     * Setter method for the inventoryCost instance variable of the specific product object in the
     * catalogue array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @param h     The inventory cost value for the specified product object.
     */
    public void setCatalogueInventoryCost(int index, double h) {
        catalogue[index].setInventoryCost(h);
    }

    /**
     * Setter method for the costPerUnit instance variable of the specific product object in the
     * catalogue array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @param cost  The cost per unit value for the specified product object.
     */
    public void setCatalogueCostPerUnit(int index, double cost) {
        catalogue[index].setCostPerUnit(cost);
    }

    /**
     * Setter method for the sellPerUnit instance variable of the specific product object in the
     * catalogue array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @param sell  The sell per unit value for the specified product object.
     */
    public void setCatalogueSellPerUnit(int index, double sell) {
        catalogue[index].setSellPerUnit(sell);
    }

    /**
     * Instantiates a new product in the catalogue array instance variable that has default values
     * for all of it's attributes except the productName instance variable which is set to the
     * passed string.
     *
     * @param name The name value of the new product object.
     */
    public void addProduct(String name) {
        if (noOfProducts == catalogue.length)           // True: There are no empty array positions, therefore resize the array to fit the new product object.
        {
            catalogue = resizeArray();
        }
        catalogue[noOfProducts] = new Product(name);    // Instantiate the new product and place it in the last index position which
        // always equals the current noOfProducts value.
        noOfProducts++;                                 // Increment by 1 to signify a new product exists in the array.
    }

    /**
     * Instantiates a new product in the catalogue array instance variable with all product instance
     * variables initialised per the paramaters passed in the method.
     *
     * @param name          The name value of the product object.
     * @param demand        The demand value of the product object.
     * @param setupCost     The setup cost of the product object.
     * @param inventoryCost The inventory cost of the product object.
     * @param cost          The cost per unit of the product object.
     * @param sell          The sell price per unit of the product object.
     */
    public void addProduct(String name, int demand, double setupCost, double inventoryCost, double cost, double sell) {
        if (noOfProducts == catalogue.length)           // True: There are no empty array positions, therefore resize the array to fit the new product object.
        {
            catalogue = resizeArray();
        }
        catalogue[noOfProducts] = new Product(name, demand, setupCost, inventoryCost, cost, sell);  // Instantiate and initialise the new product object
        // and place it in the last index position which always
        // equals the current noOfProducts value.
        noOfProducts++;                                 // Increment by 1 to signfy a new product exists in the array.
    }

    /**
     * Finds the EOQ value for a specific product object in the catalogue array instance variable.
     *
     * @param index The index position of the product object in the array.
     * @return An integer of the EOQ value for the specified product object.
     */
    public int calculateCatalogueEOQ(int index) {
        return catalogue[index].calculateEOQ();
    }

    /**
     * Deletes all the existing products.
     */
    public void deleteAll() {
        catalogue = new Product[DEFAULT];       // Instantiate a new Prodcut array with the default number of positions and point the catalogue instance
        // variable to the new array thereby deleting the old array via garbage collection.
        noOfProducts = 0;                       // Reset the number of products to 0.
    }

    /**
     * Deletes a specific product object in the catalogue array instance variable. If the product to be deleted is the last product object
     * in the array the product object is still stored by ignored by reducing the noOfProducts instance variable by 1. However if the
     * product to be deleted is in any other position, all product objects after the product to be deleted are moved up 1 position in the
     * array, thereby unreferencing the product to be deleted and deleting that object from memory via garbage collection.
     *
     * @param index The index position of the product object to be deleted.
     */
    public void deleteCatalogueItem(int index) {
        if (index != noOfProducts - 1)                          // True: The product object to be deleted isn't the last object in the array.
        {
            for (int i = index; i < noOfProducts - 1; i++)      // For all the objects beyond the product to be deleted.
            {
                catalogue[i] = catalogue[i + 1];                // Move the object up 1 position in the array.
            }
        }
        noOfProducts--;                                         // Decrease the number of products by 1 to reflect the deletion.
    }

    /**
     * Handles the need to add 1 position to the catalogue array instance variable.
     *
     * @return A reference to an array that is equal to the current catalogue array instance variable + 1 more (blank) position.
     */
    private Product[] resizeArray() {
        Product[] tempArray = new Product[noOfProducts + 1];    // Instantiate a new array that has 1 more position than the current catalogue array does.

        for (int i = 0; i < noOfProducts; i++)                  // Copy all of the product objects in the catalgoue array instance variable to the new array.
        {
            tempArray[i] = catalogue[i];
        }
        return tempArray;                                       // Return the address of the new array.
    }

    /**
     * Convert all the product objects within the catalogue array instance variable to their string equivalents.
     *
     * @return A string of the product attributes for all products in the store.
     */
    public String toString() {
        String storeToString = "";

        for (int i = 0; i < noOfProducts; i++) {
            storeToString += catalogue[i].toString();
        }
        return storeToString;
    }

    /**
     * Retrieves the replenishment strategy for a specific product based on the number of weeks passed to
     * the method.
     *
     * @return A string of the replenishment plan for a specific product object.
     */
    public String catalogueReplenishment(int index, int weeks) {
        return catalogue[index].replenishmentPlan(weeks);
    }

    /**
     * Creates a string of the store attributes in a specific format per the program requirements.
     *
     * @return A string of the store object in the required format.
     */
    public String displayStore() {
        String display = String.format("\nStore: %s\n%29s%d", storeName, "Number of products: ", noOfProducts); // Add the store name and number of products to the display string.

        for (int i = 0; i < noOfProducts; i++)                                                                  // Loop through all product objects in the catalogue array.
        {
            display += String.format("\n%17s%d: %s", "Product ", i + 1, catalogue[i].getProductName());         // Add the product name to the display string.
        }
        return display;
    }

    /**
     * Sorts the product objects in the catalogue array instance variable by demand in ascending
     * order without changing the order of how the products are currently stored.
     *
     * @return A string of the products in the catalogue sorted by demand in ascending order.
     */
    public String catalogueListByDemand() {
        Product[] copy = catalogue.clone();                             // Create a copy of the catalogue array so sorting doesn't change the data structure.

        // Selection Sort algorithim.
        for (int i = 0; i < noOfProducts; i++) {
            int index = i;                                              // Assume position i is the smallest.

            for (int j = i + 1; j < noOfProducts; j++)                  // Test the remaining positions against the current smallest encountered.
            {
                if (copy[j].getDemand() < copy[index].getDemand())      // True: Encountered a smaller value.
                {
                    index = j;                                          // Assume the encountered value is the smallest.
                }
            }
            // Swap the smallest encountered value with the first assumed smallest value.
            Product temp = copy[i];
            copy[i] = copy[index];
            copy[index] = temp;
        }
        String output = "";

        // After the temp array has been sorted, convert it's product objects to a string for output.
        for (int i = 0; i < noOfProducts; i++) {
            output += copy[i].toString();
        }
        return output;
    }

    /**
     * Sorts the product objects in the catalogue array instance variable by name in ascending
     * alphabetical order without changing the order of how the products are currently stored.
     *
     * @return A string of the products in the catalogue sorted in ascending alphabetical order.
     */
    public String catalogueListByName() {
        Product[] copy = catalogue.clone();                             // Create a copy of the catalogue array so sorting doesn't change the data structure.

        // Selection sort algorithim.
        for (int i = 0; i < noOfProducts; i++) {
            int index = i;                                              // Assume position i is the smallest.

            for (int j = i + 1; j < noOfProducts; j++)                  // Test the remaining positions against the current smallest encountered.
            {
                if (copy[j].getProductName().compareTo(copy[index].getProductName()) < 0)   // True: Encountered a smaller value.
                {
                    index = j;                                          // Assume the encountered value is the smallest.
                }
            }
            // Swap the smallest encountered value with the first assumed smallest value.
            Product temp = copy[i];
            copy[i] = copy[index];
            copy[index] = temp;
        }
        String output = "";

        // After the temp array has been sorted, convert it's product objects to a string for output.
        for (int i = 0; i < noOfProducts; i++) {
            output += copy[i].toString();
        }
        return output;
    }
}
