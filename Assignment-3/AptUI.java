import java.util.Scanner;

public class AptUI {

    static AptSet prices;
    static AptSet sizes;

    public AptUI(AptSet prices, AptSet sizes){
        this.prices = prices;
        this.sizes = sizes;
    }    

    public int mainMenu(){
        int option = -1;
        System.out.println("Apartment Tracker Main Menu.\n");
        System.out.println("Here are your options: ");
        System.out.println("  1. Add a new apartment to the tracker");
        System.out.println("  2. Update an existing apartment");
        System.out.println("  3. Remove an existing apartment");
        System.out.println("  4. Retrieve the lowest price apartment");
        System.out.println("  5. Retrieve the biggest apartment");
        System.out.println("  6. Retrieve the lowest price apartment in a city");
        System.out.println("  7. Retrieve the biggest apartment in a city");
        System.out.println("  8. Quit");
        
        System.out.print("\nMake your selection: ");

        try {
            Scanner input = new Scanner(System.in);
            option = input.nextInt();

            if(option < 1 || option > 8){
                return -1;
            }
            return option;
        } catch(Exception e){
            System.err.println(e);
            return -1;
        }
    }

    public Apt askNewApt(){
        Scanner input = new Scanner(System.in);
        System.out.println("You are creating a new apartment.");
        System.out.println("Enter the following: \n");

        try{
            System.out.print("Address: ");
            String addr = input.nextLine();

            System.out.print("Apartment Number: ");
            int aptN = (int)input.nextDouble();
            input.skip(".*");

            System.out.print("City: ");
            String city = input.next();

            System.out.print("Zip code: ");
            int zip = input.nextInt();
            input.skip(".*");

            System.out.print("Price (USD): ");
            int price = (int)input.nextDouble();
            input.skip(".*");
            //if(input.hasNext()) input.next();

            System.out.print("Size (sq. ft): ");
            int size = (int)input.nextDouble();

            Apt ret = new Apt(addr, aptN, city, zip, price, size);
            return ret;
        } catch(Exception e){
            System.err.println(e);
            return null;
        }
    } 

    public boolean updateApt(){
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the following: ");
        try{
            System.out.print("Address: ");
            String addr = input.nextLine();

            System.out.print("Apartment Number: ");
            int aptN = (int)input.nextDouble();
            input.skip(".*");

            System.out.print("Zip code: ");
            int zip = (int)input.nextDouble();
            input.skip(".*");

            System.out.print("Enter the updated cost: ");
            int price = (int)input.nextDouble();
            input.skip(".*");

            boolean containedPrices = prices.updateKey(addr, aptN, zip, price);
            boolean containedSizes = sizes.updateKey(addr, aptN, zip, price);

            if(containedPrices != containedSizes){
                System.err.println("********** THERE IS AN ISSUE ************");
                System.err.println("Error: For some reason, not both queues were updated");
            } else if(!containedPrices){
                System.out.println("That apartment was not in the system.");
            } else {
                System.out.println("That apartment's price has been updated.");
            }
            return containedPrices;
        } catch (Exception e){
            System.err.println(e);
            return false;
        }
    }

    public boolean removeApt(){
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the following: ");
        try {
            System.out.print("Address: ");
            String addr = input.nextLine();

            System.out.print("Apartment Number: ");
            int aptN = input.nextInt();
            input.skip(".*");

            System.out.print("Zip code: ");
            int zip = input.nextInt();
            input.skip(".*");

            boolean containedPrices = prices.removeKey(addr, aptN, zip);
            boolean containedSizes = sizes.removeKey(addr, aptN, zip);

            if(containedPrices != containedSizes){
                System.err.println("********** THERE IS AN ISSUE ************");
            } else if(!containedPrices){
                System.out.println("That apartment was not in the system.");
            } else {
                System.out.println("That apartment has been removed.");
            }
            return containedPrices;
        } catch(Exception e){
            System.err.println(e);
            return false;
        }
    }

    public Apt getPrice(){
        Apt ret;

        ret = prices.priorityKey();

        return ret;
    }

    public Apt getSize(){
        Apt ret;

        ret = sizes.priorityKey();

        return ret;
    }

    public Apt getPriceCity(){
        Scanner input = new Scanner(System.in);

        try{
            System.out.print("Enter the city: ");
            String city = input.nextLine();

            Apt item = prices.getPriorityCity(city);
            if(item != null){
                item.display();
            } else {
                System.out.println("That city does not exist in the records. "+
                                    "Check your spelling.");
            }
            return item;
        } catch(Exception e){
            System.err.println(e);
            return null;
        }
    }

    public Apt getSizeCity(){
        Scanner input = new Scanner(System.in);

        try {
            System.out.print("Enter the city: ");
            String city = input.nextLine();

            Apt item = sizes.getPriorityCity(city);
            if(item != null){
                item.display();
            } else {
                System.out.println("That city does not exist in the records. "+
                                    "Check your spelling.");
            }
            return item;
        } catch(Exception e){
            System.err.println(e);
            return null;
        }
    }

    public void quit(){
        System.out.println("Quitting the Apartment Tracker.");
    }

}
