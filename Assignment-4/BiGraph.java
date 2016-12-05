import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import java.util.Iterator;

import java.lang.NumberFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BiGraph {

    int numberNodes = 0;

    Bag<Route>[] routes;

    String[] locations;

    public BiGraph(){

    }

    @SuppressWarnings("unchecked")
    public void loadData(String filename){
        try{
            File inFile = new File(filename);
            BufferedReader inputData = new BufferedReader(new FileReader(inFile));
            numberNodes = Integer.parseInt(inputData.readLine());

            routes = (Bag<Route>[]) new Bag[numberNodes];
            locations = new String[numberNodes];

            for(int i=0; i<numberNodes; i++){
                locations[i] = inputData.readLine();
                routes[i] = new Bag<Route>(locations[i]);
            }

            int numberRoutes = 0;
            String readString = inputData.readLine();
            int lines = numberNodes + 1;
            while(readString != null){
                //System.out.println(readString);
                String[] args = readString.split("\\s+");
                lines++;
                if(args.length != 4){
                    System.out.println("The file is malformed.");
                } else {
                    Route route = new Route(args);
                    this.routes[Integer.parseInt(args[0])-1].add(route);
                    String n = args[0];
                    args[0] = args[1];
                    args[1] = n;
                    route = new Route(args);
                    this.routes[Integer.parseInt(args[0])-1].add(route);
                    //System.out.println(this.routes[from-1].size());
                }
                //System.out.println("\n");
                readString = inputData.readLine();
            }
        } catch (FileNotFoundException e){
            System.err.println("That file was not found.");
            e.printStackTrace();
        } catch (NumberFormatException e){
            System.err.println("The file was misformed.");
            e.printStackTrace();
        } catch (IOException e){
            System.err.println("There has been an IO Exception");
            e.printStackTrace();
        }
    }

    public void saveData(String filename){
        try{
            PrintWriter writer = new PrintWriter(filename);
            writer.println(numberNodes);
            for(int i=0; i<numberNodes; i++){
                writer.println(locations[i]);
            }
            for(int i=0; i<routes.length; i++){
                Iterator itr = routes[i].iterator();
                while(itr.hasNext()){
                    Route r = (Route)itr.next();
                    if(r.to > r.from){
                        writer.println(r.from + " " + r.to + " " + 
                                r.distance + " " + r.cost);
                        //System.out.println(r.from + ", " + r.to);
                    }
                }
            }
            //writer.println();
            writer.close();
        } catch (Exception e){
            System.err.println("There was an error writing to the file.");
            e.printStackTrace();
        }
    }

    public String getRoutes(){
        String out = "";
        for(int i=0; i<routes.length; i++)
            out = out + routes[i] + "\n";
        return out;
    }

    public boolean removeQuery(String from, String to){
        int f = -1;
        int t = -1;

        for(int i=0; i<locations.length; i++){
            if(locations[i].equalsIgnoreCase(from)){
                f = i;
            }
            if(locations[i].equalsIgnoreCase(to)){
                t = i;
            }
        }

        if(f != -1 && t != -1){
            Iterator itr = routes[f].iterator();
            boolean firstHas = false;
            Route r = null;
            while(itr.hasNext()){
                r = (Route)itr.next();
                if(r.to == (t+1)){
                    itr.remove();
                    firstHas = true;
                }
            }
            boolean secondHas = false;
            itr = routes[t].iterator();
            r = null;
            while(itr.hasNext()){
                r = (Route)itr.next();
                if(r.to == (f+1)){
                    itr.remove();
                    secondHas = true;
                }
            } 
            return firstHas && secondHas;
        } else {
            return false;
        }
    }

    public boolean addRoute(String from, String to, int dist, double cost){
        int f = -1;
        int t = -1;

        for(int i=0; i<locations.length; i++){
            if(locations[i].equalsIgnoreCase(from)){
                f = i;
            }
            if(locations[i].equalsIgnoreCase(to)){
                t = i;
            }
        }

        if(f == -1 || t == -1){
            return false;
        }

        if(contains(f, f+1, t+1)){
            System.out.println("That route already exists.");
            return false;
        } else {
            Route r = new Route(f+1, t+1, dist, cost);
            routes[f].add(r);
            r = new Route(t+1, f+1, dist, cost);
            routes[t].add(r);
        }
        
        return true;
    }

    public boolean contains(int route, int from, int to){
        Iterator itr = routes[route].iterator();
        while(itr.hasNext()){
            Route r = (Route)itr.next();
            if(r.from == from && r.to == to)
                return true;
        }
        return false;
    }

    public KruskalMST makeMST(){
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(numberNodes);
        for(int i=0; i<routes.length; i++){
            Iterator itr = routes[i].iterator();
            while(itr.hasNext()){
                Route r = (Route)itr.next();
                if(r.from < r.to){
                    ewg.addEdge(new Edge(r.from-1, r.to-1, r.distance));
                }
            }
        }

        KruskalMST mst = new KruskalMST(ewg);

        return mst;
    }

    // @param param which argument to use for the edge weights.
    //              1 is distance
    //              2 is cost
    //              3 is edge weight of 1
    public boolean getShortestPath(String from, String to, int param){
        if(param < 1 || param > 3){
            System.out.println("You did not select a valid type.");
            return false;
        }
        int f = -1;
        int t = -1;

        for(int i=0; i<locations.length; i++){
            if(locations[i].equalsIgnoreCase(from)){
                f = i;
            }
            if(locations[i].equalsIgnoreCase(to)){
                t = i;
            }
        }

        if(f == -1 || t == -1 || t == f)
            return false;


        EdgeWeightedDigraph ewg = new EdgeWeightedDigraph(routes.length);

        for(int i=0; i<routes.length; i++){
            Iterator itr = routes[i].iterator();
            while(itr.hasNext()){
                Route r = (Route)itr.next();
                if(param == 1)
                    ewg.addEdge(new DirectedEdge(r.from-1, r.to-1, r.distance));
                else if(param == 2)
                    ewg.addEdge(new DirectedEdge(r.from-1, r.to-1, r.cost));
                else if(param == 3)
                    ewg.addEdge(new DirectedEdge(r.from-1, r.to-1, 1));
            }
        }

        DijkstraSP dij = new DijkstraSP(ewg, f);

        if(dij.hasPathTo(t)){
            if(param == 1)
                System.out.println("Total distance: " + dij.distTo(t));
            if(param == 2)
                System.out.println("Total cost: " + dij.distTo(t));
            if(param == 3)
                System.out.println("Total flights: " + dij.distTo(t));

            System.out.println("Follow the path: ");
            for(DirectedEdge e: dij.pathTo(t)){
                System.out.println("\t"+locations[e.from()] + " -> " + locations[e.to()] + 
                        ": " + e.weight());
            }
        } else {
            System.out.println("Path does not exist.");
        }

        return true;

    }

    //////////////////////////////
    // Recursive backtracking code
    
    double maxCost;

    public void allTrips(double cost){
        maxCost = cost;
        int[] start = new int[numberNodes];
        for(int i=0; i<start.length; i++){
            start[i] = -1;
        }
        for(int i=0; i<numberNodes; i++){
            start[0] = i;
            backtrack(start);
        }
    }

    private void backtrack(int[] partial){
        if(reject(partial) || !meetsCost(partial)){  return;}
        if(isValid(partial)) display(partial);
        int[] attempt = extend(partial);
        while(attempt != null){
            backtrack(attempt);
            attempt = next(attempt);
        }
    }

    private double getCost(int[] partial){
        double cost = 0;
        for(int i=0; i<partial.length-1; i++){
            int f = partial[i];
            int t = partial[i+1];
            if(f == -1) break;
            for(Route r: routes[f]){
                if(r.to-1 == t){
                    cost += r.cost;
                    break;
                }
            }
        }
        return cost;
    }

    private boolean meetsCost(int[] partial){
        return getCost(partial) <= maxCost;
    }

    // Bass-aackwards, reject => doNotReject
    private boolean reject(int[] partial){
        if(containsDuplicates(partial)) return true;
        int i=0;
        while(i < partial.length-1 && partial[i+1] != -1){
            boolean contains = false;
            for(Route r:routes[partial[i]]){
                if((r.from-1 == partial[i] || r.to-1 == partial[i]) &&
                        (r.from-1 == partial[i+1] || r.to-1 == partial[i+1])){
                    contains = true;
                    break;
                }
            }
            if(!contains) return true;
            i++;
        }
        return false;
    }

    private boolean containsDuplicates(int[] partial){
        boolean[] visited = new boolean[partial.length];
        for(int i: partial){
            if(i == -1) return false;
            if(visited[i]) return true;
            visited[i] = true;
        }
        return false;
    }

    private boolean isValid(int[] partial){
        return true;
    }

    private void display(int[] partial){
        int i=0;
        if(partial[0] == -1) return;
        if(getCost(partial) == 0.){
            return;
        }
        System.out.println("Cost: " + getCost(partial));
        while(i < partial.length-1 && partial[i+1] != -1){
            if(partial[i] == -1) return;
            int f = partial[i];
            int t = partial[i+1];
            int[] edge = new int[2];
            edge[0] = f;  edge[1] = t;
            double costEdge = getCost(edge);
            System.out.println("\t" + locations[f] + "->" + locations[t] + ": " + costEdge);
            i++;
        }

        System.out.println("\n");
    }

    private int[] extend(int[] partial){
        int len = partial.length;
        int[] temp = new int[len];
        boolean extended = false;
        for(int i=0; i<len; i++){
            if(partial[i] != -1 || extended){
                temp[i] = partial[i];
            } else {
                temp[i] = 0;
                extended = true;
            }
        }
        if(extended)
            return temp;
        return null;
    }

    private int[] next(int[] partial){
        int len = partial.length;
        int[] temp = new int[len];
        boolean didNext = false;
        for(int i=0; i<len; i++){
            if((i == len-1 || partial[i+1] == -1) && !didNext){
                if(partial[i] >= numberNodes-1){
                    return null;
                } else {
                    temp[i] = partial[i]+1;
                    didNext = true;
                }
            } else {
                temp[i] = partial[i];
            }
        }

        return temp;

    }


    // Route "routes" class
    public class Route {
        private int from;
        private int to;
        private int distance;
        private double cost;

        public String fromS;
        public String toS;

        public Route(int f, int t, int d, double c){
            from = f;
            to = t;
            distance = d;
            cost = c;

            fromS = locations[f-1];
            toS = locations[t-1];
        }

        public Route(String[] args){
            from = Integer.parseInt(args[0]);
            to = Integer.parseInt(args[1]);
            distance = Integer.parseInt(args[2]);
            cost = Double.parseDouble(args[3]);

            fromS = locations[from-1];
            toS = locations[to-1];
        }

        public String toString(){
            String out = toS + ": " + distance + ", " + cost;
            //System.out.println(fromS);
            return out;
        }
    }

}
