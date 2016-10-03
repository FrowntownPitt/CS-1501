import java.io.PrintWriter;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.SecurityException;

public class pw_generator {
	DLBTrie dictionary;
	char[] validLetters;
	char[] validNumbers;
	char[] validSymbols;
	char[] validChars;
	long startTime;

	boolean outputToFile = false;
	String fileName;
	PrintWriter file;

	public pw_generator(){

	}

	public void setDictionary(DLBTrie d){
		dictionary = d;
	}
	public void setValidChars(String letters, String numbers, String symbols, String allChars){
		validLetters = letters.toCharArray();
		validNumbers = numbers.toCharArray();
		validSymbols = symbols.toCharArray();
		validChars = allChars.toCharArray();
	}

	public void setOutputToFile(boolean out, String fileName){
		this.outputToFile = out;
		this.fileName = fileName;
	}

	private void openFile(){
		try{
			file = new PrintWriter(fileName, "UTF-8");	
		} catch (FileNotFoundException e){
			System.out.println("Unable to write to all_passwords.txt");
			outputToFile = false;
		} catch (SecurityException e){
			System.out.println("Unable to create all_passwords.txt.  Your system will not allow file creation.");
			outputToFile = false;
		} catch (UnsupportedEncodingException e){
			System.out.println("Your system does not support UTF-8 encoding.");
			outputToFile = false;
		}
	}

	private void closeFile(){
		file.close();
	}

	private void outputToFile(String pass, long t){
		file.println(pass+","+t);
	}

	boolean isFullSolution(String partial){
		if(partial.length() != 5){
			return false;
		}
		return true;
	}

	// Go through each character and increase the respective counter.  Verify.
	boolean isValidSolution(String partial){
		int countLetters = 0;
		int countNumbers = 0;
		int countSymbols = 0;
		for(char c: partial.toCharArray()){
			boolean f = false;
			for(char letter: validLetters){
				if(c == letter){
					f = true;
					countLetters++;
				}
			}
			if(!f){
				for(char letter: validNumbers){
					if(c == letter){
						f = true;
						countNumbers++;
					}
				}
			}
			if(!f){
				for(char letter: validChars){
					if(c == letter){
						countSymbols++;
					}
				}
			}
		}
		if(countLetters < 1 || countLetters > 3)
			return false;
		if(countNumbers < 1 || countNumbers > 2)
			return false;
		if(countSymbols < 1 || countSymbols > 2)
			return false;
		return true;
	}

	// Go through each character and increase the respective counter.  If a count is too high, kill it.
	boolean isValidPartial(String partial){
		int countLetters = 0;
		int countNumbers = 0;
		int countSymbols = 0;
		for(char c: partial.toCharArray()){
			boolean f = false;
			for(char letter: validLetters){
				if(c == letter){
					f = true;
					countLetters++;
				}
			}
			if(!f){
				for(char letter: validNumbers){
					if(c == letter){
						f = true;
						countNumbers++;
					}
				}
			}
			if(!f){
				for(char letter: validChars){
					if(c == letter){
						countSymbols++;
					}
				}
			}
			if(countLetters > 3)
				return false;
			if(countNumbers > 2)
				return false;
			if(countSymbols > 2)
				return false;
		}
		return true;
	}

	boolean rejectPartial(String partial){
		if(!isValidPartial(partial)){
			return true;
		} if(containsWord(partial)){
			return true;
		}
		return false;
	}

	boolean containsWord(String partial){
		if(partial.length() == 0){
			return false;
		}
		//System.out.println(partial);
		for(int i=1; i <= partial.length(); i++){
			if(!dictionary.containsPrefix(partial.substring(0,i))){
				break;
			}
			if(dictionary.containsString(partial.substring(0,i))){
				return true;
			}
		}
		return containsWord(partial.substring(1, partial.length()));
	}

	String next(String partial){
		char lastLetter = partial.charAt(partial.length()-1);
		int index = -1;
		for(int i=0; i<validChars.length; i++){
			if(lastLetter == validChars[i]){
				index = i;
			}
		}
		if(index >= validChars.length-1 || index < 0)
			return null;
		partial = partial.substring(0,partial.length()-1)+validChars[index+1];
		return partial;
	}

	String extend(String partial){
		partial = partial + validChars[0];
		if(partial.length() > 5)
			return null;
		return partial;
	}

	void backtrack(String partial){
		if(rejectPartial(partial)) return;
		if(isFullSolution(partial)){
			if(isValidSolution(partial)){
				//System.out.println(partial + ":" + getTime());
				if(outputToFile){
					outputToFile(partial, getTime());
				}
				//dictionary.addString(partial, getTime());
			}
		}
		String attempt = extend(partial);
		while(attempt != null){
			backtrack(attempt);
			attempt = next(attempt);
		}
		return;
	}

	public long generateStrings(){
		//System.out.println("Generating passwords...");
		startTime = System.nanoTime();
		openFile();
		backtrack("");
		closeFile();
		return (System.nanoTime() - startTime) / 1000000;
		//System.out.println("Finished generating passwords.");
	}

	long getTime(){
		return (System.nanoTime() - startTime)/1000000;
	}
}