import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;

public class alt_generator {

	DLBTrie dictionary;
	String fileName;
	char[] altLetters;
	char[] altSymbols;

	int count = 0;

	public alt_generator(DLBTrie dict, String fileName, String letters, String symbols){
		altLetters = letters.toCharArray();
		altSymbols = symbols.toCharArray();
		dictionary = dict;
		this.fileName = fileName;
	}

	public boolean readDictionary() throws FileNotFoundException{
		// Add the words to the dictionary
		File dictFile = new File(fileName);
		FileReader dictReader = new FileReader(dictFile);
		try(BufferedReader dictBuffer = new BufferedReader(dictReader)){
			int i=0;
			String word = "";
			word = dictBuffer.readLine();
			do{
				i++;
				//System.out.println(i + ": " + word);
				//dict.addString(word, 0);
				if(word.length() <= 5)
					generateAlts(word.toLowerCase());
				word = dictBuffer.readLine();
			} while(word != null);
		}
		catch (IOException e){
			//System.out.println(e);
			System.out.println(" ****** ERROR: File not found: " + fileName);
			return false;
		}
		return true;
	}

	// EXPLANATION OF 'generateAlts' method: 
	// array of just positions of alterable
	// array of -1, 0 for alterable.  1 if altered.
	// Iterate over all bit strings until all 1's.
	// e.g. "have" -> {-1,0,-1,0} -> {-1,1,-1,0} -> {-1,0,-1,1} -> {-1,1,-1,1}
	// uses array {1,3} for doing increment stepping.
	// Find the least significant 0, change to 1, and force the lesser-significant values to 0.
	// RUNTIME: k := length of word
	//          c := number of alterable characters
	//          O(k*2^c) => O(2^c)

	public void generateAlts(String word){
		char[] alt = word.toCharArray();
		int[] mod = new int[alt.length];
		int size = 0;
		for(int i=0; i<alt.length; i++){
			mod[i] = -1;
			for(int j=0; j<altLetters.length; j++){
				if(alt[i] == altLetters[j]){
					size++;
					mod[i] = 0;
				}
			}
		}

		int[] pos = new int[size];

		int ind = 0;
		//System.out.print("Pos: ");
		for(int i=0; i<alt.length; i++){
			for(int j=0; j<altLetters.length; j++){
				if(alt[i] == altLetters[j]){
					pos[ind] = i;
					alt[i] = altSymbols[j];
					//System.out.print(pos[ind]);
					ind++;
				}
			}
		}

		int i = 0;

		dictionary.addString(word,(long)0);
		count++;

		while(true){
			for(i=0; i<pos.length; i++){
				if(mod[pos[i]] == 0){
					mod[pos[i]] = 1;
					for(int j=i; j>0; j--){
						mod[pos[j-1]] = 0;
					}
					dictionary.addString(toStr(mod, alt, word), (long)0);
					count++;
					break;
				}
			}
			if(i == pos.length){
				break;
			}
		}
	}

	private String toStr(int[] mod, char[] alt, String word){
		String ret = "";
		//System.out.println(word);
		for(int i=0; i<mod.length; i++){
			if(mod[i] == -1 || mod[i] == 0){
				ret = ret + word.substring(i,i+1);
			} else {
				ret = ret + alt[i];
			}
		}
		return ret;
	}
}