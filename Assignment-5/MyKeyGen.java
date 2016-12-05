// MyKeyGen generates a public-private key pair for use in RSA encryption

import java.math.BigInteger;
import java.util.Random;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.PrintWriter;

public class MyKeyGen {
    
    private static BigInteger P;
    private static BigInteger Q;
    private static BigInteger N;
    private static BigInteger PHI;

    private static BigInteger generatePrime(BigInteger big, int bitWidth, int certainty){
        big = new BigInteger(bitWidth, certainty, new Random());
        return big;
    }

    public static void main(String[] args){
        P = generatePrime(P, 512, Integer.MAX_VALUE);
        Q = generatePrime(Q, 512, Integer.MAX_VALUE);

        String p = P.toString(10);
        String q = Q.toString(10);

        N = P.multiply(Q);

        PHI = P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE));

        BigInteger E = new BigInteger("65536");//PHI.add(BigInteger.ZERO);
        boolean findingE = true;
        while(findingE && E.compareTo(PHI) == -1){
            E = E.add(BigInteger.ONE);
            if(PHI.mod(E) != BigInteger.ZERO){
                findingE = false;
            }
        }

        if(findingE){
            System.out.println("There was an error generating E. No valid E for generated primes.");
            return;
        }

        BigInteger D = E.modInverse(PHI);

        try{
            PrintWriter out = new PrintWriter("pubkey.rsa");
            out.println(E.toString(10));
            out.println(N.toString(10));
            out.close();

            out = new PrintWriter("privkey.rsa");
            out.println(D.toString(10));
            out.println(N.toString(10));
            out.close();

            System.out.println("RSA keypair files generated.");
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("There was an error somewhere in saving the keypairs.");
        }

    }


}
