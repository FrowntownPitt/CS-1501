import java.util.Queue;
import java.util.Iterator;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.FileNotFoundException;

public class DLBTrie {
	private Node root = new Node();
	private char NULL = '\u0000';
	//char[] validLetters;
	//char[] validNumbers;
	//char[] validSymbols;
	//char[] validChars;

	public DLBTrie(){//String letters, String numbers, String symbols){
		//validLetters = letters.toCharArray();
		//validNumbers = numbers.toCharArray();
		//validSymbols = symbols.toCharArray();
		//validChars = (letters + numbers + symbols).toCharArray();
	}

	private class Node {
		char value;
		Node sibling;
		Node child;
		boolean isEnd = false;
		long time = 0;

		private Node(){
			this(NULL, null, null);
		}

		private Node(char letter, Node sibling, Node child){
			value = letter;
			this.sibling = sibling;
			this.child = child;
		}
	}

	
	public void addString(String s, long t){
		Node currentNode = root;
		//System.out.println("ADDING STRING : " + s);
		for(int i=0; i<s.length(); i++){
			boolean wordEnd = i == s.length()-1;
			if(currentNode == null){
				System.out.println("###################################### GLITCH IN ALGORITHM #####################################");
			}
			else if(currentNode.value == NULL){
				//System.out.println("\tPlacing value " + s.charAt(i) + " in empty node.");
				currentNode.value = s.charAt(i);
				currentNode.child = new Node();
				if(wordEnd) {
					currentNode.isEnd = true;
					currentNode.time = t;
				}
				currentNode = currentNode.child;
			}
			else {
				Node tNode = currentNode;
				boolean foundVal = false;
				while(tNode != null){
					if(tNode.value == s.charAt(i)){
						if(tNode.child == null){
							tNode.child = new Node();
						}
						if(wordEnd) {
							tNode.isEnd = true;
							tNode.time = t;
						}
						currentNode = tNode.child;
						//System.out.println("\tfound existing item in current level: " + tNode.value + ". Moving to next level: " + tNode.child.value);
						break;
					} else {

						if(tNode.sibling == null){
							tNode.sibling = new Node(s.charAt(i),null,new Node());
							if(wordEnd) {
								tNode.sibling.isEnd = true;
								tNode.sibling.time = t;
							}
							currentNode = tNode.sibling.child;
							//System.out.println("\tReached end of list.  Adding item to list: " + tNode.sibling.value
							//					 + ". Moving to next level.");
							break;
						} else {
							tNode = tNode.sibling;
						}
					}
				}
			}
		}
		//System.out.println();
	}

	public boolean containsString(String s){
		Node currentNode = root;
		int level = 0;
		while(currentNode != null){
			boolean foundOnLevel = false;
			Node tNode = currentNode;
			if(currentNode.value == s.charAt(level)){
				if(level == s.length()-1){
					return currentNode.isEnd;
				}
				currentNode = currentNode.child;
				level++;
				foundOnLevel = true;
			} else {
				currentNode = currentNode.sibling;
			}
		}
		return false;
	}

	public boolean containsPrefix(String p){
		Node currentNode = root;
		int level = 0;
		while(currentNode != null){
			boolean foundOnLevel = false;
			Node tNode = currentNode;
			if(currentNode.value == p.charAt(level)){
				if(level == p.length()-1){
					return currentNode.value != NULL;
				}
				currentNode = currentNode.child;
				level++;
				foundOnLevel = true;
			} else {
				currentNode = currentNode.sibling;
			}
		}
		return false;
	}

	public long getTime(String s){
		if(containsString(s)){
			Node currentNode = root;
			int level=0;
			while(currentNode != null){
				boolean foundOnLevel = false;
				if(currentNode.value == s.charAt(level)){
					if(level == s.length()-1){
						return currentNode.time;
					}
					currentNode = currentNode.child;
					level++;
					foundOnLevel = true;
				} else {
					currentNode = currentNode.sibling;
				}
			}
		}
		return -1;
	}

	public void debug(){
		System.out.print("Root: " + root.value + "; " + String.valueOf(root.isEnd));
		Node currentNode = root.sibling;
		while(currentNode != null){
			System.out.print(", " + currentNode.value + "; " + String.valueOf(currentNode.isEnd));
			currentNode = currentNode.sibling;
		}
		System.out.println();

		currentNode = root.child;
		System.out.print("Root's Child: " + currentNode.value + "; " + String.valueOf(currentNode.isEnd));
		currentNode = currentNode.sibling;
		while(currentNode != null){
			System.out.print(", " + currentNode.value + "; " + String.valueOf(currentNode.isEnd));
			currentNode = currentNode.sibling;
		}
	}

	/*public Iterable<String> strings(){
		return stringsWithPrefix("");
	}*/

	public void stringsWithPrefix(String p, int count, String[] keys){
		LinkedString<String> q = new LinkedString<>();
		Node n = get(root, p, 0);
		//System.out.println(p + ", " + n.value); 
		collect(n, p, q, count);
		int i=0;
		Iterator<String> itr = q.iterator();
		while(itr.hasNext() && i < keys.length){
			String c = (String)itr.next();
			keys[i] = c;
			//values[i] = getTime(c);
			i++;
		}
		return;
	}

	private void collect(Node x, String p, LinkedString<String> q, int amount){
		if(q.size() >= amount) return;
		if(x == null || x.value == NULL) return;
		//if(x.isEnd) q.addItem(p+x.value);
		Node current = x;
		while(current != null && current.value != NULL){
			if(current.isEnd){// && containsString(p+current.value)){
				//System.out.println(p+current.value);
				if(containsString(p+current.value))
					q.addItem(p+current.value);
				//System.out.println(current.value);
			} 
			collect(current.child, p+current.value, q, amount);
			current = current.sibling;
		}
	}

	private Node get(Node x, String key, int d){
		if (x == null || x.value == NULL) return null;
		if (d == key.length()) return x;
		Node n = x;
		while(n != null && n.value != key.charAt(d)){
			n = n.sibling;
			//System.out.println("GET: " + d + ", " + n.value + ": " + n.child.value);
		}
		//System.out.println("Found " + key.charAt(d) + " " + n.value);
		return get(n.child, key, d+1);
	}

	public boolean collectFromFile(String fileName){
		File dictFile = new File(fileName);
		FileReader dictReader;
		try{
			dictReader = new FileReader(dictFile);
		} catch (FileNotFoundException e){
			return false;
		}
		try(BufferedReader dictBuffer = new BufferedReader(dictReader)){
			int i=0;
			String word = "";
			word = dictBuffer.readLine();
			do{
				i++;
				//System.out.println(i + ": " + word);
				//dict.addString(word, 0);
				//generateAlts(word.toLowerCase());
				if(word.length() < 7){
					System.out.println(" ****** ERROR: Malformed passwords line: " + i + ": " + word);
				}
				String p = word.substring(0,5);
				int t = Integer.parseInt(word.substring(6,word.length()));//0,5);
				addString(p,t);
				word = dictBuffer.readLine();
			} while(word != null);
		}
		catch (IOException e){
			System.err.println(e);
			System.out.println(" ****** There was an IOException thrown in the collectFromFile method.");
			return false;
		}
		return true;
	}

	public void testLinkedString(){
		LinkedString<Node> output = new LinkedString<Node>();

		System.out.println("Testing LinkedString");

		Node i = new Node('a', null, null);

		output.addItem(i);
		i = new Node('b', null, null);
		output.addItem(i);

		Iterator itr = output.iterator();

		while(itr.hasNext()){
			System.out.println(((Node)itr.next()).value + " ....");
		}

	}

}