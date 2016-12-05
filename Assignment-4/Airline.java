import java.util.Scanner;
import java.util.HashSet;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class Airline {

    public static void main(String[] args){
        BiGraph graph = new BiGraph();
        
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the airline file name: ");
        String fileName = in.nextLine();
        
        graph.loadData(fileName);

        boolean continueMenu = true;
        while(continueMenu){
            int option = AirlineUI.menu();

            switch(option){
                  case 1: {
                    System.out.println("\nAvailable flights: ");
                    System.out.println(graph.getRoutes());
                    System.out.println("");
                    break;
                } case 2: {
                    KruskalMST mst = graph.makeMST();

                    HashSet<Integer> set = new HashSet();

                    for(int i: mst.getConnections()){
                        set.add(i);
                    }
                    if(set.size() > 1)
                        System.out.println("There are " + set.size() + 
                                " separate service routes.");
                    
                    int curr = 0;
                    Iterator eItr = mst.edges().iterator();
                    Edge[] e = new Edge[mst.getConnections().length];

                    int eit = 0;
                    while(eItr.hasNext()){
                        e[eit] = (Edge)eItr.next();
                        eit++;
                    }

                    String[] locs = graph.locations;

                    System.out.println("Service Routes: ");
                    for(int i: set){
                        curr++;
                        System.out.println("Route " + curr + ": ");
                        int amt = 0;
                        for(Edge t: mst.edges()){
                            //System.out.println(t);
                            int from = t.either();
                            int to = t.other(from);
                            if(mst.getConnections()[from] == i){
                                //System.out.println(t);
                                System.out.println("\t" + locs[from] + " - " + 
                                        locs[to] + " : " + t.weight());
                                amt++;
                            }
                        }
                        if(amt == 0){
                            System.out.println("\tThere is an isolated airport.");
                        }
                        System.out.println();
                    }

                    break;
                } case 3: {

                    System.out.print("Enter from: ");
                    String from = in.nextLine();
                    System.out.print("Enter to:   ");
                    String to = in.nextLine();

                    System.out.println("Select what type: ");
                    System.out.println("\t1. By distance");
                    System.out.println("\t2. By cost");
                    System.out.println("\t3. By number of flights");
                    int type = Integer.parseInt(in.nextLine());
                    
                    graph.getShortestPath(from, to, type);

                    break;
                } case 4: { 
                    System.out.print("Enter the max trip cost: ");
                    double maxCost = Double.parseDouble(in.nextLine());

                    graph.allTrips(maxCost);
                    //graph.testReject();

                    break;
                } case 5: {
                    
                    System.out.print("Enter from: ");
                    String from = in.nextLine();
                    System.out.print("Enter to:   ");
                    String to = in.nextLine();
                    System.out.print("Enter distance: ");
                    int dist = Integer.parseInt(in.nextLine());
                    System.out.print("Enter cost:     ");
                    double cost = Double.parseDouble(in.nextLine());
                    
                    boolean added = graph.addRoute(from, to, dist, cost);

                    if(!added){
                        System.out.println("The flight could not be added.");
                    } else {
                        System.out.println("The flight was added.");
                    }

                    System.out.println("");
                    break;
                } case 6: {
                    System.out.print("Enter from: ");
                    String from = in.nextLine();
                    System.out.print("Enter to:   ");
                    String to = in.nextLine();
                    if(graph.removeQuery(from, to)){
                        System.out.println("Found the route.  Deleting.");
                    } else {
                        System.out.println("That route does not exist.");
                    }
                    System.out.println("\n");
                    break;
                } case 7: {
                    continueMenu = false;
                    System.out.println("Saving data...");
                    System.out.println("Quitting.");
                    graph.saveData(fileName);
                    break;
                } default:{

                    break;
                }
            }
        }

    }
}
