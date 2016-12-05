public class Apt {
    public String address;
    public int aptNumber;
    public String city;
    public int zipCode;
    public int price;
    public int size;
    private int hashCode;

    public Apt(String a, int aNum, String c, int zip, int p, int s){
        address = a;
        aptNumber = aNum;
        city = c;
        zipCode = zip;
        price = p;
        size = s;

        String hashString = a + aNum + zip;
        hashCode = hashString.hashCode();
    }

    public int getHash(){
        return hashCode;
    }

    public void display(){
        System.out.println("Address:   " + address);
        System.out.println("Apartment: " + aptNumber);
        System.out.println("City:      " + city);
        System.out.println("Zipcode:   " + zipCode);
        System.out.println("Price:     " + price);
        System.out.println("Size:      " + size);
    }

}
