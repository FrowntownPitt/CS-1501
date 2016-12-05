public class AptTracker {
    //AptSet allQueue = new AptSet();
        
    static AptSet prices = new AptSet(false);
    static AptSet sizes = new AptSet(true);

    static AptUI UI = new AptUI(prices, sizes);

    public static void main(String[] args){
        
        //testAddApartments();

        int menuSelection = 0;
        while(menuSelection != -1){
            menuSelection = UI.mainMenu();
            System.out.println();
            //System.out.println("You selected: " + menuSelection+"\n\n");
            switch(menuSelection){
                case 1:{
                    Apt n = UI.askNewApt();
                    if(n == null){
                        System.err.println("The apartment could not be added");
                        break;
                    }
                    if(prices.contains(n.address, n.aptNumber, n.zipCode)){
                        System.err.println("That apartment already exists.\n"+
                                            "Use the update option to modify it.");
                        break;
                    }
                    prices.insert(n);
                    sizes.insert(n);
                    break;
                }
                case 2:{
                    UI.updateApt();
                    break;
                }
                case 3:{
                    UI.removeApt();
                    break;
                }
                case 4:{
                    Apt prio = UI.getPrice();
                    System.out.println("Retrieving the lowest price apartment.\n");
                    if(prio != null){
                        System.out.println("Lowest price apartment: \n");
                        prio.display();
                    } else {
                        System.out.println("There are no apartments in the system.");
                    }
                    
                    break;
                }
                case 5:{
                    Apt prio = UI.getSize();
                    System.out.println("Retrieving the biggest apartment.\n");
                    if(prio != null){
                        System.out.println("Biggest apartment: \n");
                        prio.display();
                    } else {
                        System.out.println("There are no apartments in the system.");
                    }

                    break;
                }
                case 6:{
                    UI.getPriceCity();
                    break;
                }
                case 7:{
                    UI.getSizeCity();
                    break;
                }
                case 8:{
                    UI.quit();
                    menuSelection = -1;
                    break;
                }
                default:{
                    break;
                }
            }
            System.out.println("\n-----------------------\n");
        }

    }

    private static void testAddApartments(){
        /*
        Apt a = new Apt("123 Doe Ct", 123, "Pittsburgh", 15213, 110, 2000); 
        Apt b = new Apt("124 Doe Ct", 124, "Pittsburgh", 15213, 120, 3000);
        Apt c = new Apt("125 Doe Ct", 125, "Pittsburgh", 15213, 130, 4000);
        Apt d = new Apt("126 Doe Ct", 126, "Pittsburgh", 15213, 140, 5000);
        
        prices.insert(a);
        prices.insert(b);
        prices.insert(c);
        prices.insert(d);
        sizes.insert(a);
        sizes.insert(b);
        sizes.insert(c);
        sizes.insert(d);
        */
        Apt a = new Apt("1 AF", 1, "P", 15213, 100, 100);
        Apt b = new Apt("2 AF", 2, "P", 15213, 200, 100);

        prices.insert(a);
        prices.insert(b);
        sizes.insert(a);
        sizes.insert(b);
    }
}
