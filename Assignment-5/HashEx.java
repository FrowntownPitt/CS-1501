import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.MessageDigest;

public class HashEx {
	public static void main(String args[]) {

		// lazily catch all exceptions...
		try {
			// read in the file to hash
			Path path = Paths.get(args[0]);
			byte[] data = Files.readAllBytes(path);

			// create class instance to create SHA-256 hash
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			// process the file
			md.update(data);
			// generate a has of the file
			byte[] digest = md.digest();

			// convert the bite string to a printable hex representation
			// note that conversion to biginteger will remove any leading 0s in the bytes of the array!
			String result = new BigInteger(1, digest).toString(16);

			// print the hex representation
			System.out.println(result);
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}
	}
}
