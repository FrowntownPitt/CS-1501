import java.util.HashMap;

public class AptSet {

    HashMap<Integer, Integer> pqindex = new HashMap<>(100);
    HashMap<Integer, Apt> cities = new HashMap<>(100);

    private int n = 0;
    private int maxN = 2;

    private Apt[] pq;

    private boolean priorityMax;

    /**
     * @param priorityType false if min, true if max
     */
    public AptSet(boolean priorityType){
        this.priorityMax = priorityType;
        pq = new Apt[maxN];
    }

    public int getSize(){
        return n;
    }

    public boolean getPriorityType(){
        return priorityMax;
    }

    public Apt priorityKey(){
        if(n == 0){
            return null;
        }
        return pq[0];
    }

    public Apt removePriorityKey(){
        if(isEmpty())
            return null;
        Apt ret = pq[0];

        exchange(0, --n);
        sink(0);

        // Remove the key from the hash table
        pqindex.remove(ret.getHash());

        return ret;
    }

    private int getIndex(int hashCode){
        if(pqindex.get(hashCode) != null){
            int index = pqindex.get(hashCode);
            return index;
        } else {
            return -1;
        }
    }

    public boolean removeKey(String address, int aptN, int zip){
        String hashString = address + aptN + zip;
        int hashCode = hashString.hashCode();
        
        int index = getIndex(hashCode); 

        if(index < 0) return false;
        
        Apt rem = pq[index];
        int cityHash = rem.city.hashCode(); 

        exchange(index, n-1);

        n--;

        sink(index);

        pqindex.remove(hashCode);

        boolean removeCityHash = false;
        if(cities.get(cityHash) == rem){
            cities.remove(cityHash);
            System.out.println("Removed the priority item.");
            removeCityHash = !findNewCity(rem.city);
        }
        //if(removeCityHash) cities.remove(cityHash);

        return true;
    }

    private boolean findNewCity(String city){
        int cityHash = city.hashCode();
        boolean aCityRemoved = false;
        
        for(int i=0; i<n; i++){
            //System.out.println(i);
            if(pq[i].city.hashCode() == cityHash){
                //pq[i].display();
                //System.out.println("PRICE: " + pq[i].price);
                if(!cities.containsKey(cityHash)){
                    cities.put(cityHash, pq[i]);
                }
                if(swapCities(cityHash, pq[i])){
                    //System.out.println("SWAPPING: " + pq[i].price);
                    aCityRemoved = true;
                }
            }
        }
        return aCityRemoved;
    }

    public void insert(Apt item){
        int index = n;
        if(n >= maxN){
            growQueue();
        }
        pq[n] = item;
        String hashString = item.address + item.aptNumber + item.zipCode;
        int hashCode = hashString.hashCode();
        
        pqindex.put(hashCode, n);

        int cityHash = item.city.hashCode();
        if(!cities.containsKey(cityHash)){
            cities.put(cityHash, item);
        } else {
            swapCities(cityHash, item);
        }
        
        swim(n);

        n += 1;
    }

    private boolean swapCities(int cityHash, Apt item){ 
        if(priorityMax){
            if(item.size > cities.get(cityHash).size){
                cities.put(cityHash, item);
                return true;
            }
        } else {
            //System.out.println(item.price);
            if(item.price < cities.get(cityHash).price){
                cities.put(cityHash, item);
                return true;
            }
        }
        return false;
    }

    public Apt getPriorityCity(String city){
        int cityHash = city.hashCode();

        if(cities.containsKey(cityHash)){
            return cities.get(cityHash);
        } else {
            return null;
        }
    }

    public boolean updateKey(String address, int aptN, int zip, int update){
        String hashString = address + aptN + zip;
        int hashCode = hashString.hashCode();

        int index = getIndex(hashCode);
        if(index < 0) return false;
        
        Apt up = pq[index];
        int cityHash = up.city.hashCode();
        
        int p = pq[index].price;

        pq[index].price = update;
        
        sink(index);
        swim(index);

        if(!priorityMax){ // Only update the cities if it is the prices pq
            if(cities.get(cityHash).getHash() == up.getHash()){
                if(update > p){
                    cities.remove(cityHash);
                    findNewCity(up.city);
                    //cities.get(cityHash).display();
                } else {
                    swapCities(cityHash, up);
                }
            } else {
                if(update < cities.get(cityHash).price){
                    swapCities(cityHash, up);
                }
            }
        }

        return true;
    }

    private void growQueue(){
        //System.out.println("Regrowing the array");
        int tempN = maxN * 2;
        Apt[] tmp = new Apt[tempN];
        for(int i=0; i<n; i++){
            tmp[i] = pq[i];
            pq[i] = null;
        }
        pq = tmp;
        maxN = tempN;
        return;
    }

    public boolean isEmpty(){
        return n==0;
    }

    public boolean contains(String address, int aptNum, int zipCode){
        String a = address + aptNum + zipCode;
        if(pqindex.containsKey(a.hashCode())){
            return true;
        }
        return false;
    }

    private void insert(int hashCode, int index){
        pqindex.put(hashCode, index);
        return;
    }

    /////////////////////////////////////////////////////
    // Helper functions
    /////////////////////////////////////////////////////

    // This will return TRUE if i's price is less than j's price
    private boolean less(int i, int j){
        return pq[i].price < pq[j].price;
    }

    // This will return TRUE if i's size is greater than j's size
    private boolean greater(int i, int j){
        return pq[i].size > pq[j].size;
    }

    private void exchange(int i, int j){
        int hash1 = pq[i].getHash();
        int hash2 = pq[j].getHash();
        Apt t = pq[i];
        pq[i] = pq[j];
        pq[j] = t;

        pqindex.put(hash1, j);
        pqindex.put(hash2, i);
    }

    private void swim(int i){
        if(priorityMax){ // If the priority is of size
            while(i > 0 && greater(i, (i-1)/2)){
                exchange(i, (i-1)/2);
                i = (i-1)/2;
            }
        } else {
            while(i > 0 && less(i, (i-1)/2)){
                exchange(i, (i-1)/2);
                i = (i-1)/2;
            }
        }
    }

    private void sink(int i){
        if(priorityMax){ // Max priority (size)
            while(2*i+1 < n){
                int j = i*2+1;
                if(j < n-1 && !greater(j, j+1)) j++;
                if(greater(i, j)) break;
                exchange(i, j);
                i = j;
            }
        } else {
            while(2*i+1 < n){
                int j = i*2+1;
                if(j < n-1 && !less(j, j+1)) j++;
                if(less(i, j)) break;
                exchange(i, j);
                i = j;
            }
        }
    }
}
