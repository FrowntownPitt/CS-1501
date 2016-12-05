import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

import java.io.IOException;
import java.io.FileNotFoundException;

public class pw_check {
	public static void main(String[] args) throws FileNotFoundException{
		String validLetters = "abcdefghijklmnopqrstuvwxyz";
		String validNumbers = "1234567890";
		String validSymbols = "!@$^_*";
		String altLetters = "taoeils";
		String altSymbols = "740311$";
		DLBTrie passwords = new DLBTrie();//validLetters, validNumbers, validSymbols);
		DLBTrie dictionary = new DLBTrie();//validLetters, validNumbers, validSymbols);

		long startTime = System.nanoTime();

		//dictionary.testLinkedString();

		if(args.length > 0 && args[0].equals("-find")){
			pw_generator pw_gen = new pw_generator();

			/*createDictionary(dictionary, "dictionary.txt");
			if(verifyDictionary(dictionary, "dictionary.txt")){
				System.out.println("Dictionary created successfully");
			} else {
				System.out.println("Dictionary creation had an issue of some sort.");
			}*/

			System.out.println("Reading dictionary and generating word variants....");

			alt_generator alt_gen = new alt_generator(dictionary, "dictionary.txt", altLetters, altSymbols);
			alt_gen.readDictionary();

			System.out.println("Finished creating the dictionary.\n");

			pw_gen.setOutputToFile(true, "all_passwords.txt");

			pw_gen.setDictionary(dictionary);
			pw_gen.setValidChars(validLetters,validNumbers,validSymbols, validLetters+validNumbers+validSymbols);


			System.out.println("Generating passwords....");

			long time = pw_gen.generateStrings();

			System.out.println("Finished generating passwords.\n\n"+
								"It took " + ((double)time)/1000 + " seconds to finish.\n");
		}
		else if(args.length > 0 && args[0].equals("-check")){
			System.out.println("Generating trie from file input....");
			long t1 = System.nanoTime();
			//DLBTrie passwords = new DLBTrie();
			if(!passwords.collectFromFile("all_passwords.txt")){
				System.out.println("There was an error in reading the all_passwords.txt file.  "+
									"\nMake sure you run the -find option before attempting to execute the -check option");
				throw new FileNotFoundException();
			}

			System.out.println("Finished generating trie. It took " + (System.nanoTime()-t1)/1000000 + " ms.");

			Scanner in = new Scanner(System.in);
			System.out.println("\nEnter passwords to see if they exist in the trie.  Entering a '.' will end the program.");
			System.out.print("Enter password: ");
			String s = in.next();
			while(!s.equals(".")){
				s = s.toLowerCase();
				long t = passwords.getTime(s);

				if(t < 0){
					System.out.println("\t'" + s + "' is not a valid password.  Here are 10 similar alternatives:");
					if(s.length() < 5){
						s = s+".";
					}

					int count = 0;
					if(s.length() > 5)
						s = s.substring(0,5);
					while(count < 10){
						String[] strings = new String[10-count];
						if(s.length() > 1){
							s = s.substring(0,s.length()-1);
							if(!passwords.containsPrefix(s)){
								continue;
							}
						}
						else if(s.length() == 1)
							s = "";
						else
							break;
						passwords.stringsWithPrefix(s, 10-count, strings);
						for(int i=0; i<strings.length; i++){
							if(strings[i] == null || passwords.getTime(strings[i]) < 0){
								break;
							}
							System.out.println("\t'" + strings[i] + "': " + passwords.getTime(strings[i]) + " ms");
							count++;
						}
					}
					System.out.println();
				} else {
					System.out.println("\t'" + s + "' exists.  The time to generate is: " + t + " ms.\n");
				}
				System.out.print("Enter password: ");
				s = in.next();
			}
		}
		else if(args.length > 0){
			System.out.println("ERROR: Unknown command - " + args[0]);
		}
		else {
			System.out.println("ERROR: No argument given.");
		}

		//Scanner input = new Scanner(System.in);

		// METHOD DEBUG WITH USER INPUT: 
		String s;
		/*System.out.println("\nEnter words to see if the prefix exists in the dictionary trie");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.contains(s)));
			System.out.println(String.valueOf(dictionary.containsPrefix(s)));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see if in the dictionary trie");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.contains(s)));
			System.out.println(String.valueOf(dictionary.containsString(s)));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the EXTEND result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.extend(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the NEXT result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.next(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the isFullSolution result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.isFullSolution(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the containsWord result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.containsWord(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the isValidSolution result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.isValidSolution(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the isValidPartial result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.isValidPartial(s));
		} while (!s.equals("."));*/

		/*System.out.println("\nEnter words to see the rejectPartial result");
		do{
			s = input.next();
			//System.out.println(String.valueOf(passwords.containsString(s)));
			System.out.println(pw_gen.rejectPartial(s));
		} while (!s.equals("."));*/
	}
}