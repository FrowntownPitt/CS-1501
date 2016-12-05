import java.util.Scanner;

public class AirlineUI {

    public static int getInputOption(){
        return -1;
    }

    public static int menu(){
        int selected = -1;

        System.out.println("***********************************");
        System.out.println();
        System.out.println("Airline Options: ");
        System.out.println("\t1. Show all available routes");
        System.out.println("\t2. Display a Minimum Spanning Tree");
        System.out.println("\t3. Shortest Paths");
        System.out.println("\t4. Find Paths Below Given Price");
        System.out.println("\t5. Add New Route");
        System.out.println("\t6. Remove Route");
        System.out.println("\t7. Quit");
        Scanner in = new Scanner(System.in);

        selected = Integer.parseInt(in.nextLine());

        if(selected < 1 && selected > 7){
            selected = -1;
        }
        return selected;
    }


}
