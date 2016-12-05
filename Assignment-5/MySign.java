import java.math.BigInteger;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.MessageDigest;

import java.nio.file.StandardOpenOption;

public class MySign {

    private static BigInteger E;
    private static BigInteger N;
    private static BigInteger D;

    public static void main(String[] args) throws Exception {
        if(args.length != 2){
            throw new Exception("Missing arguments");
        }

        if(args[0].equals("s")){
            readPrivateKey();
            byte[] contents = readFile(args[1]);
            
            byte[] digest = hashArray(contents);

            BigInteger hash = new BigInteger(1, digest);
            String result = new BigInteger(1, digest).toString(16);//hash.toString(16);

            BigInteger signature = decrypt(hash);
            
            byte[] signatureBytes = signature.toByteArray();

            // Write the hash to the new file
            Path path = Paths.get(args[1] + ".signed"); 
            Files.write(path, signature.toByteArray());
            Files.write(path, contents, StandardOpenOption.APPEND);

            System.out.println("File has been signed.  Written to: " + args[1] + ".signed");
        } else if(args[0].equals("v")){
            readPublicKey();

            byte[] contents = readFile(args[1]);

            byte[] sigBytes = copy(contents, 0, 128);
            byte[] fileContents = copy(contents, 128, contents.length);

            BigInteger signature = new BigInteger(1, sigBytes);

            BigInteger signHash = encrypt(signature);

            byte[] digest = hashArray(fileContents);
            BigInteger hash = new BigInteger(1, digest);

            if(hash.compareTo(signHash) == 0){
                System.out.println("Signature: VALID");
            } else {
                System.out.println("Signature: INVALID");
            }
        }

    }

    private static BigInteger decrypt(BigInteger hash){
        BigInteger result = hash.modPow(D, N);
        return result;
    }

    private static BigInteger encrypt(BigInteger value){
        BigInteger result = value.modPow(E, N);
        return result;
    }

    private static void readPrivateKey() throws Exception {
        BufferedReader priv = new BufferedReader(new FileReader(new File("privkey.rsa")));
        String Din = priv.readLine();
        String Nin = priv.readLine();

        D = new BigInteger(Din);
        N = new BigInteger(Nin);

        //System.out.println("D: " + D);
        //System.out.println("N: " + N);
    }

    private static void readPublicKey() throws Exception {
        BufferedReader priv = new BufferedReader(new FileReader(new File("pubkey.rsa")));
        String Ein = priv.readLine();
        String Nin = priv.readLine();

        E = new BigInteger(Ein);
        N = new BigInteger(Nin);

        //System.out.println("E: " + E);
        //System.out.println("N: " + N);
    }

    private static byte[] readFile(String fileName) throws Exception {
        byte[] data = Files.readAllBytes(Paths.get(fileName));

        return data;
    }

    private static byte[] hashArray(byte[] array) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(array);

        return md.digest();
    }

    private static BigInteger readSign(String fileName) throws Exception {
        BufferedReader file = new BufferedReader(new FileReader(new File(fileName)));

        String signature = file.readLine();

        file.close();

        return new BigInteger(signature);
    }

    private static byte[] readContents(String fileName) throws Exception {
        BufferedReader file = new BufferedReader(new FileReader(new File(fileName)));
        
        String contents = "";

        file.readLine();

        String t = file.readLine();
        while(t != null){
            contents += "\n" + t;
            t = file.readLine();
        }

        return contents.getBytes();
    }

    private static byte[] copy(byte[] array, int from, int to){
        byte[] ret = java.util.Arrays.copyOfRange(array, from, to);
        return ret;
    }

}
