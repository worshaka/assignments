package au.edu.newcastle.seng1110.assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Provides the UI (via Terminal I/O) and all input data validation for the Mates Coffee replenishment system.
 *
 * @author Travis Jones c3112196
 * @version SENG1110 Assignment 3
 */
public class MatesInterface {
    private static Scanner console = new Scanner(System.in);        // Scanner object to handle all user input.
    private static Store lambtonStore = new Store("lambton");
    private static Store callaghanStore = new Store("callaghan");
    private static Store selectedStore;                             // Points to the store the user is working with during the program.

    /* Used to determine which error message to run in the showError(int errorType) method.
     * These could be changed to public in the future if it was necessary for another class
     * to call error messages, as that isn't the case with the current specs they are declared
     * as private.
     */
    private static final int INCORRECT_MENU_OPTION = 0;
    private static final int STORE_EMPTY = 1;
    private static final int INCORRECT_STORE_NAME = 2;
    private static final int NO_PRODUCT_NAME = 3;
    private static final int INFEASIBLE_EOQ = 4;

    /**
     * Handles the running of the application.
     *
     * @throws IOException if an IOException occurs
     */
    public static void main(String[] args) throws IOException {
        final String mainMenu = "[1] Choose Store\n[2] Display Stores\n[3] Open\n[4] Save\n[5] Exit Program";   // Main Menu.
        int mainMenuOption = 0; // Determines which main menu option to run.

        // Keeps the user in the main menu until they indicate they wish to exit the program.
        do {
            System.out.println("Welcome to Mates Coffee!\n");
            System.out.println(mainMenu + "\n");                // Display main menu.
            mainMenuOption = console.nextInt();

            switch (mainMenuOption) {
                case 1:
                    runMainMenuOption1();                   // User selected to enter the store sub menu.
                    break;

                case 2:
                    runMainMenuOption2();                   // User selected to display all store information.
                    break;

                case 3:
                    runMainMenuOption3();                   // User selected to upload product information from a file.
                    break;

                case 4:
                    runMainMenuOption4();                   // User slected to save product information to a file.
                    break;

                case 5:
                    break;                                  // Exit program, do nothing.

                default:
                    System.out.println();
                    showError(INCORRECT_MENU_OPTION);       // User has selected a menu option out of range, display error message.
                    pauseProgram();                         // Used throughout the program to stop too much output to the I/O terminal.
                    System.out.println();
            }
        }
        while (mainMenuOption != 5);                             // False: User has selected to exit program.
    }

    /**
     * Runs main menu option 1 which allows the user to enter a specific store and view the sub menu options
     * for that store.
     */
    public static void runMainMenuOption1() {
        final String subMenu = "[1] Add/Edit Product\n[2] Delete Product\n[3] Display Product\n"    // Sub menu.
                + "[4] Display All Products\n[5] Exit store";
        int subMenuOption = 0;                                                                      // Determins which sub menu option to run.
        String storeName = askStoreName();                                                          // User selects which store to enter.
        selectedStore = getStore(storeName);                                                        // Point the selectedStore variable to the correct store object.
        String outputStoreName = selectedStore.getStoreName().substring(0, 1).toUpperCase()          // Capitalise the first letter for output to terminal I/O.
                + selectedStore.getStoreName().substring(1);
        System.out.println("\n");

        // Loop keeps the user in the sub menu until they indicate they wish to return to the main menu.
        do {
            // Show the sub menu with a title that reminds the user which store they are in.
            System.out.println("Welcome to " + outputStoreName + " Store!\n\n" + subMenu + "\n");
            subMenuOption = console.nextInt();  // User decides which menu option to run.
            System.out.println();

            switch (subMenuOption) {
                case 1:
                    runSubMenuOption1();                // User has selected to add/edit data for a product.
                    break;

                case 2:
                    runSubMenuOption2();                // User has selected to delete a product.
                    break;

                case 3:
                    runSubMenuOption3();                // User has selected to display a product.
                    break;

                case 4:
                    runSubMenuOption4();                // User has selected to display all products.
                    break;

                case 5:
                    break;                              // User has selected to exit sub menu and return to main menu (do nothing).

                default:
                    showError(INCORRECT_MENU_OPTION);   // User has selected a menu option out of range, display error message.
                    pauseProgram();
            }
        }
        while (subMenuOption != 5);                           // False: User has selected to return to the main menu.
        System.out.println();
    }

    /**
     * Runs main menu option 2 which displays a summary of the products held at each store to the terminal I/O.
     */
    public static void runMainMenuOption2() {
        System.out.print(callaghanStore.displayStore() + lambtonStore.displayStore());
        pauseProgram();
    }

    /**
     * Runs main menu option 3 which allows the user to upload a file containing product information into the
     * program.
     * <p>
     * Precondtions: The file is assumed to be in the correct format specified in the program requirements.
     */
    public static void runMainMenuOption3() {
        System.out.println();
        System.out.print("Please enter the file name: ");
        String fileName = console.next();

        if (fileName.indexOf(".") == -1)                                        // User omitted the file extension.
        {
            fileName += ".dat";                                                 // Default file extension.
        }

        try (InputStream inputFileStream = ClassLoader.getSystemResourceAsStream(fileName)) {
            if (inputFileStream == null) {
                System.out.println();
                System.out.printf("Error: %s does not exist", fileName );
                pauseProgram();
            } else {
                Scanner inFile = new Scanner(inputFileStream);
                callaghanStore.deleteAll();                                         // The file overwrites all existing data for all stores.
                lambtonStore.deleteAll();

                while (inFile.hasNext()) {
                    String input = inFile.nextLine().toLowerCase();                 // All string comparisons conducted in lower case.
                    String productName = "";

                    if (input.contains("callaghan") || input.contains("lambton"))   // True: Start of a new store.
                    {
                        input = input.substring(0, input.length() - 1);
                        selectedStore = getStore(input);                            // Point the selectedStore variable to the correct store object.
                        inFile.nextLine();                                          // Removes the carriage return in the buffer.
                        input = inFile.nextLine().toLowerCase();                    // All string comparisons conducted in lower case.
                    }

                    // Get all the attribute values for the product, attribute values are always 2 index positions after the colon.
                    productName = input.substring(input.indexOf(":") + 2);
                    input = inFile.nextLine();
                    int demand = Integer.parseInt(input.substring(input.indexOf(":") + 2));
                    input = inFile.nextLine();
                    double setupCost = Double.parseDouble(input.substring(input.indexOf(":") + 2));
                    input = inFile.nextLine();
                    double cost = Double.parseDouble(input.substring(input.indexOf(":") + 2));
                    input = inFile.nextLine();
                    double inventoryCost = Double.parseDouble(input.substring(input.indexOf(":") + 2));
                    input = inFile.nextLine();
                    double sell = Double.parseDouble(input.substring(input.indexOf(":") + 2));
                    selectedStore.addProduct(productName, demand, setupCost, inventoryCost, cost, sell);

                    if (inFile.hasNext())   // Only need to remove a carriage return from the buffer if it isn't the end of the document.
                    {
                        inFile.nextLine();
                    }
                }
                inFile.close();
                System.out.println();
                System.out.print("The file " + fileName + " was uploaded successfully.");
                pauseProgram();
            }
        } catch (IOException e) {
            System.out.println("An error occured");
            pauseProgram();
        }
    }

    /**
     * Runs main menu option 4 which allows the user to save the product information contained in the application to an
     * external file according to the predefined format specified in the program requirements.
     */
    public static void runMainMenuOption4() throws FileNotFoundException {
        if (callaghanStore.getNoOfProducts() != 0 || lambtonStore.getNoOfProducts() != 0)   // At least one store must contain product data.
        {
            System.out.println();
            System.out.print("What do you wish to name the file? ");
            String fileName = console.next();

            if (fileName.indexOf(".") == -1)    // The user omitted the file extension.
            {
                fileName += ".dat";             // Default file extension.
            }

            PrintWriter outFile = new PrintWriter(fileName);
            String output = "";

            if (callaghanStore.getNoOfProducts() != 0)  // True: Product data exists for the callaghan store, therefore retrieve the data.
            {
                output = String.format("Callaghan:%n") + callaghanStore.toString();
            }
            if (lambtonStore.getNoOfProducts() != 0)    // True: Product data exists for the lambton store, therefore retrieve the data.
            {
                output += String.format("%nLambton:%n") + lambtonStore.toString();
            }
            outFile.print(output);  // Write the relevant data to the file.
            outFile.close();        // Empty the buffer.
            System.out.println();
            System.out.print("The file " + fileName + " has been saved to the current directory.");
            pauseProgram();
        } else    // No stores contained data, advise the user.
        {
            System.out.println();
            showError(STORE_EMPTY);
            pauseProgram();
        }
    }

    /**
     * Runs the store sub menu option 1 which allows the user to input data for a selected product. If data already exists
     * for a product the user can decide whether to overwrite the existing data or select a different product.
     */
    public static void runSubMenuOption1() {
        boolean loop;

        do {
            loop = false;  // Set to false to prevent unnecessary looping.
            String productName = askProductName();
            int index = selectedStore.getCatalogueIndex(productName);   // Search to see if the product exists.

            if (index == -1)    // Product doesn't exist, create a new product.
            {
                selectedStore.addProduct(productName);
                askInputData(selectedStore.getNoOfProducts() - 1);  // Ask the user for the value of the other product attributes.
            } else    // Product exists.
            {
                System.out.print("The product already exists, do you wish to overwrite? [Y/N] ");

                /* If the first character of the user input is y the user has indicated they wish to overwrite the existing data
                 * otherwise any other input is assumed to mean no and the program is required to ask for a product name and
                 * peform the operations in the loop again.
                 */
                loop = (console.next().toLowerCase().charAt(0)) == 'y' ? false : true;
                System.out.println();

                if (loop == false)  // User has indicated to overwrite
                {
                    askInputData(index);
                }
            }
        }
        while (loop);
    }

    /**
     * Runs the store sub menu option 2 which allows the user to delete a product from the catalogue.
     */
    public static void runSubMenuOption2() {
        if (selectedStore.getNoOfProducts() != 0)                           // True: There is at least 1 product to delete.
        {
            String productName = askProductName();
            int index = selectedStore.getCatalogueIndex(productName);       // Check the product the user is attempting to delete exists

            if (index != -1)                                                // True: The product to delete does exist.
            {
                selectedStore.deleteCatalogueItem(index);                   // Perform the deletion.
                System.out.print("The product was deleted.");
                pauseProgram();
            } else {
                showError(NO_PRODUCT_NAME);                                 // Inform the user the product doesn't exist.
                pauseProgram();
            }
        } else {
            showError(STORE_EMPTY);                                         // Inform the user there are no products to delete.
            pauseProgram();
        }
    }

    /**
     * Runs the store sub menu option 3 which allows the user to view the data of a specific product. The user can also
     * select to view a replenishment strategy for the same product.
     */
    public static void runSubMenuOption3() {
        if (selectedStore.getNoOfProducts() != 0)                                                               // True: There is at least 1 product to view.
        {
            String productName = askProductName();
            int index = selectedStore.getCatalogueIndex(productName);                                           // Check the product the user is attempting to view exists.

            if (index != -1)                                                                                    // True: The product exists.
            {
                System.out.print(selectedStore.getCatalogueString(index));                                      // Output the product data.
                System.out.println();
                System.out.print("Would you like to see the replenishment strategy? [Y/N] ");
                boolean showReplenishment = (console.next().toLowerCase().charAt(0) == 'y') ? true : false;     // Determines if the user wishes to view the replenishment strategy
                System.out.println();

                if (showReplenishment) {
                    int weeks = (int) validNumber("Enter the number of weeks: ", 1);
                    System.out.print("\n" + selectedStore.catalogueReplenishment(index, weeks));                // Display the replenishment strategy for the applicable number of weeks.
                    pauseProgram();
                } else {
                    System.out.println();                                            // Enforces standard UI spacing if the user doesn't wish to see the replenishment strategy.
                }
            } else {
                showError(NO_PRODUCT_NAME);                                          // Inform the user the product they entered doesn't exist.
                pauseProgram();
            }
        } else {
            showError(STORE_EMPTY);                                                 // Inform the user there are no products to view.
            pauseProgram();
        }
    }

    /**
     * Runs the store sub menu option 4 which allows the user to view a list of the products in the store. Once the user selects
     * this option the user selects whether they wish to view the items sorted by name or demand in ascending order.
     */
    public static void runSubMenuOption4() {
        if (selectedStore.getNoOfProducts() != 0)                                       // True: There is at least one product to view.
        {
            boolean loop;
            do {
                loop = false;                                                           // Set to false to avoid infinite looping.
                System.out.print("Do you want to view the products in ascending order by:\n[1] Name\n[2] Demand\n\n");
                switch (console.nextInt()) {
                    case 1:
                        System.out.print(selectedStore.catalogueListByName());      // User has selected to list the products ordered by name.
                        break;

                    case 2:
                        System.out.print(selectedStore.catalogueListByDemand());    // User has selected to list the products ordered by demand.
                        break;

                    default:
                        System.out.println();                                       // An incorrect menu option was entered, inform the user and loop back and start again.
                        showError(INCORRECT_MENU_OPTION);
                        System.out.println("\n");
                        loop = true;
                }
            }
            while (loop);

            pauseProgram();
        } else {
            showError(STORE_EMPTY);                                                     // Inform the user there are no products to view.
            pauseProgram();
        }
    }

    /**
     * Allows access to the static store variables in the Mates Interface class.
     *
     * @param storeName Assumed to be from the set {"callaghan", "lambton"}, if it isn't the method will always return lambtonStore.
     * @return A reference to the applicable store object.
     */
    public static Store getStore(String storeName) {
        if (storeName.equals("callaghan")) {
            return callaghanStore;
        } else {
            return lambtonStore;
        }
    }

    /**
     * Handles the ability for the user to set the attributes of a given product via a series of prompts in the terminal I/O.
     *
     * @param index An integer representing the index position of the relevant product stored in the Store object's instance variable
     *              catalogue.
     */
    public static void askInputData(int index) {
        boolean loopAgain;          // Controls the loop below

        do {
            loopAgain = false;      // Set to false to avoid infinite looping.
            selectedStore.setCatalogueDemand(index, (int) validNumber("Enter the Demand per week: ", 0));
            System.out.println();
            selectedStore.setCatalogueSetupCost(index, validNumber("Enter the setup cost: ", 0));
            System.out.println();
            selectedStore.setCatalogueInventoryCost(index, validNumber("Enter the inventory cost: ", 0));

            // If EOQ is less than demand then the values entered by the user have resulted in an infeasible plan.
            if (selectedStore.calculateCatalogueEOQ(index) < selectedStore.getCatalogueDemand(index)) {
                System.out.println("\n");
                showError(INFEASIBLE_EOQ);
                System.out.println();
                loopAgain = true;   // Loop back and get a feasible EOQ before continuing.
            }
        }
        while (loopAgain);

        System.out.println();
        selectedStore.setCatalogueCostPerUnit(index, validNumber("Enter the unit cost: ", 0));
        System.out.println();
        selectedStore.setCatalogueSellPerUnit(index, validNumber("Enter the sell price: ", 0));
        System.out.println("\n");
    }

    /**
     * Handles prompting the user for the name of a product and storing the input via the terminal I/O.
     *
     * @return A String of the product name entered by the user in lower case.
     */
    public static String askProductName() {
        System.out.print("Please enter the name of the product: ");
        String productName = console.next().toLowerCase();                  // All string comparisons are conducted in lower case.
        System.out.println();
        return productName;
    }

    /**
     * Handles prompting the user for a store name and validates that the input is an element of the set {"callaghan", "lambton"}.
     *
     * @return A String from the set {"callaghan", "lambton"}
     */
    public static String askStoreName() {
        String storeName = "";                              // Stores the user input for comparison.
        boolean loop;                                       // Controls the loop for when the user enters invalid information.

        do {
            loop = false;                                   // Set to false to avoid unnecessary looping.
            System.out.print("\nEnter the store name: ");
            storeName = console.next().toLowerCase();       // All string comparisons conducted in lower case.

            if (!storeName.equals("lambton") && !storeName.equals("callaghan")) // True: Invalid store name input.
            {
                System.out.println();
                showError(INCORRECT_STORE_NAME);
                System.out.println();
                loop = true;                                // Repeat to get a valid input.
            }
        }
        while (loop);

        return storeName;
    }

    /**
     * Handles pausing the program until the user presses enter.
     */
    public static void pauseProgram() {
        System.out.println("\n");
        System.out.print("\nPress Enter to continue...");
        console.nextLine();                                 // Remove the carraige return in the buffer.
        console.nextLine();                                 // Stop the program until the user presses enter.
        System.out.println("\n");
    }

    /**
     * Handles all the required output errors to the user for when the program encounters a problem or something disallowed
     * by the program specifications.
     *
     * @param errorType Integer representing which error message to display or can be one of the following named constants
     *                  INCORRECT_MENU_OPTION = 1; STORE_EMPTY = 1; INCORRECT_STORE_NAME = 2; INCORRECT_PRODUCT_NAME = 3; INFEASIBLE_EOQ = 4;
     */
    public static void showError(int errorType) {
        switch (errorType) {
            case 0:
                System.out.print("You have selected an invalid menu option, please try again.");
                break;

            case 1:
                System.out.print("No existing products.");
                break;

            case 2:
                System.out.print("Incorrect store name, you must choose either Lambton or Callaghan.");
                break;

            case 3:
                System.out.print("Product name does not exist.");
                break;

            case 4:
                System.out.print("The values assigned to this product result in an infeasible strategy!\nYou will need to enter new values before proceeding.\n");
                break;

            default:
                System.out.print("A general error has occured, please try again.");
        }
    }

    /**
     * Handles prompting the user for numerical input and ensures the input is valid.
     *
     * @param inputQuestion The message to prompt the user for numerical input.
     * @param minValue      The minimum allowable value the input can take.
     * @return A numeric value of type double that is greater than or equal to the minValue parameter.
     */
    public static double validNumber(String inputQuestion, int minValue) {
        boolean loopAgain;
        double input = 0;

        do {
            loopAgain = false;                      // Set to false to avoid infinite looping.
            System.out.print(inputQuestion);
            input = console.nextDouble();

            if (input < minValue)                    // True: The input value is invalid, inform the user and repeat to get a valid input.
            {
                System.out.print("\nYou cannot enter a number that is less than " + minValue + ", please try again.\n\n");
                loopAgain = true;
            }
        }
        while (loopAgain);                           // False: Valid input recieved

        return input;
    }
}
