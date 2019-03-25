
//Imports required packages
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;



//Huffman Submit method that contains all the methods required for the decoding and encoding 
public class HuffmanSubmit implements Huffman {
  
	//Declares required global variables and data structures
	HashMap<Character, Integer> map = new HashMap<Character, Integer>();
	HashMap<Character, Integer> map_frequency = new HashMap<Character, Integer>();
	ArrayList<BTNode> nodes = new ArrayList<>();
	ArrayList<BTNode> leaves = new ArrayList<>();
	PriorityQueue<BTNode> node_queue = new PriorityQueue(30, new Minimum());
	HashMap<Character, String> map_coded = new HashMap<Character, String>();
	HashMap<String, Character> map_coded_reverse = new HashMap<String, Character>();
	
	
	//Method to read the file and create a hashmap of frequency of each character
	public void find_frequency(String inputFile, String freqFile) throws IOException {

		//Declares neccesary variables
		String current;
		Integer occurence; 
		Scanner scanner = new Scanner(System.in);
		Scanner scannerSecond = new Scanner(System.in);
		BinaryIn reading = new BinaryIn(inputFile);
		
		
		
		//try-catch to handle FileNotFoundException
		try {
			//Creates a scanner that reads the input file
			scanner = new Scanner(new File(inputFile));
		} catch (FileNotFoundException e) {
			
			System.out.println("File does not exist");
			e.printStackTrace();
		}
		
		//while loop that continues on till doc has additional lines
		
		while(!reading.isEmpty()) {
			//reads characters and adds it to frequency map appropriately
			char now = reading.readChar();
			
			if(map.containsKey(now)) {
				occurence = map.get(now) + 1;
				map.put(now, occurence);
			} else {
				map.put(now, 1);
			}
			
		}
		
		
		
	//Creates a freqFile to add the frequencies and characters in binary to.
	File freqF = new File(freqFile);
	
	freqF.delete();
	
	freqF.createNewFile();
		
		
		//try catch to handle IOException
				try {
					
					//BufferedWriter to put HashMap finding onto outputFile
					BufferedWriter editor = new BufferedWriter(new FileWriter(freqFile, true));
					
					//For loop that iterates through the map containing characters and their frequencies 
					for(Character word: map.keySet()) {
						
						//converts the character to binary and adds it along with its frequencies to the freqFile
						int ascii = (int) word;
						String value = Integer.toBinaryString(ascii);
						
						editor.append(value + ":" + map.get(word));
						editor.newLine();
						
					}
					
					//flushes and closes editor
					editor.flush();
					editor.close();
					
				} catch (IOException e) {
					System.out.println("Error: IO Exception");
					e.printStackTrace();
				}
				
		}
	
	
	//Method that creates nodes from values of characters and frequencies and adds them to a nodes list
	public void mapToNodes(HashMap<Character, Integer> map) {
		
		//clears nodes so that each iteration of the program is provided with a fresh list
		nodes.clear();
		
		for (Character character: map.keySet()) {
			Integer frequency = map.get(character);
			
			BTNode node = new BTNode((int) frequency);
			node.setCharacter(character);
			
			nodes.add(node);
			
		}
		
		//adds the nodes from nodes list to priority queue
		
		for(int i = 0; i < nodes.size(); i++) {
			
			node_queue.offer(nodes.get(i));
			
		}
		
	}
	
	//method that creates huffman trees and retrieves codes for each character using the huffman algorithm 
	public void makeHuffmanTree(HashMap<Character, Integer> map) {
		
		try {
		
		mapToNodes(map);
		
		BTNode root = new BTNode(1);
		
		//Runs the huffman algoritm to create huffman tree
		while(node_queue.size() != 1) {
			//System.out.println(node_queue.peek().getData());

			Integer left = node_queue.peek().getData();
			Character character_left = node_queue.peek().getCharacter();
			
			
			BTNode Left = node_queue.poll();
			Left.setCharacter(character_left);
			
			Integer right = node_queue.peek().getData();
			Character character_right = node_queue.peek().getCharacter();
			
			
			
			BTNode Right = node_queue.poll();
			Right.setCharacter(character_right);
			
			root = HuffmanNode(Left, Right);
			
			node_queue.add(root);
		}
		
		//sets root to the last node in the priority c
		
		root = node_queue.poll();
		
		//sets code for each leave node
		setCode(root);
		//puts all leaf nodes into a list
		leafNodesList(root);
		//puts all nodes 
		nodesToMap();
	
		} catch (NullPointerException e) {
		
		}
		
	}
	
	//sets code for characters by traversing the huffman tree
	public void setCode(BTNode root) {
		try {
			
			String code = "";
			
			
			if(!(root.left == null)) {
				code = root.getCode()+"1";
				root.left.setCode(code);
			}
			
			if(!(root.right == null)) {
				code = root.getCode() + "0";
				root.right.setCode(code);
			}
		
		
		if((root.left != null)) {
			setCode(root.left);
		}
		
		if((root.right != null)) {
			setCode(root.right);
		}
		
		} catch (NullPointerException e) {
			System.out.println("NULL");
			
		}
		
		
		
	}
	
	//method to print map 
	public void printCodedMap() {
		
		for (Character character: this.map_coded.keySet()) {
			String code = this.map_coded.get(character);
			
		
			
		}
		
	}
	
	//method that puts all nodes from a list to a map
	public void nodesToMap() {
		
		for(int i = 0; i < leaves.size(); i++) {
			BTNode current = leaves.get(i);
			
			
			map_coded.put(current.character, current.getCode());
			
		}
		
	}
	
	//Creates huffman node by adding stuff 
	public BTNode HuffmanNode(BTNode left, BTNode right) {
	
		BTNode root = new BTNode(left.getData() + right.getData());
		
		root.left = left;
		root.right = right; 
		
	
		
		return root;
		
	}
	
	//prints Tree level order
	void printLevelOrder(BTNode root) {
		Queue<BTNode> queue = new LinkedList<BTNode>();
	
			queue.offer(root);
			
			while (!queue.isEmpty()) {
	
				BTNode currNode = queue.poll();
				
				System.out.print(currNode.getData() + " ");
				
				if (currNode.left != null) {
					queue.offer(currNode.left);
				}
	
				if (currNode.right != null) {
					queue.offer(currNode.right);
				}
			}
	}
	

	
	//Puts all leaf nodes of a tree into a list by recursively traversing the tree
	public void leafNodesList(BTNode root) {
		
		try {
		if(root.right == null && root.left == null) {
			
			leaves.add(root);
		}
		
		if(!(root.left == null)) {
			leafNodesList(root.left);
		}
		
		if(!(root.right == null)) {
			leafNodesList(root.right);
			
		}}catch (NullPointerException e) {
			
		}
		
	}
	

	
	//Comparator to set PriorityQueue's priority inversely i.e. smaller is higher priority
	public class Minimum implements Comparator<BTNode>{
				
			@Override
			public int compare(BTNode a, BTNode b) {
				if (a.getData() > b.getData()) {
					return 1;
					}
					
				if (a.getData() < b.getData()) {
					return -1;
				}
					
				return 0;
			}
		}
	
	
 
	//encode method that uses the methods and provided BinaryIn and Out methods to read and write new files
	public void encode(String inputFile, String outputFile, String freqFile){
		
		
		try {
			
			//creates frequency files and makes huffman and gets codes
			find_frequency(inputFile, freqFile); 
			makeHuffmanTree(this.map);
			
			BinaryOut editor = new BinaryOut(outputFile);
			BinaryIn reader = new BinaryIn(inputFile);
			
			//reads the file and for every character grabs the code from code map and writes booleans onto encoded file
			try {
			while(!reader.isEmpty()) {
				char now = reader.readChar();
				if(map_coded.get(now)!=null) {
				String current = map_coded.get(now);
				
				char[] holder = current.toCharArray();
				
				for(int i = 0; i < holder.length; i++) {
					
					if(holder[i] == '1') {
						editor.write(true);
					} else {
						editor.write(false);
					}
					
				}
				}
			}
			} catch (NullPointerException e) {
				
			}
			
			
			//flushes and closes editor
			editor.flush();
			editor.close();
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
   }
	
	//reverse code map which allows for the decoding
	public void reverse_code_map() {
		
		for (Character character: this.map_coded.keySet()) {
			String code = this.map_coded.get(character);
			
			map_coded_reverse.put(code, character);
			
			
		}
		
		
	}


   ////decode method that uses the methods and provided BinaryIn and Out methods to read and write new files
   public void decode(String inputFile, String outputFile, String freqFile){
		// TODO: Your code here
	   
	   try {
		   //clears all datastrcutures so that the encoding algorithm does not impact the decoding algorithm
		   this.leaves.clear(); this.map.clear(); this.map_coded.clear(); this.map_frequency.clear();
		   
		   //finds coded frequency and makes the huffman tree with it to get codes to decode
		find_coded_frequency(freqFile);
		 makeHuffmanTree(this.map_frequency);
		 
		 
		 
		 BinaryOut editor = new BinaryOut(outputFile);
			
			BinaryIn reader = new BinaryIn(inputFile);
		
			//reverses code map to return character for code entered
			reverse_code_map();
			
			String code = "";
			boolean now;
			
			
			//decodes file by reading through the booleans and matching with map
			try {
				while(!reader.isEmpty()) {
					
					now = reader.readBoolean();
					
					if(now == true) {
						code = code + "1";
					} else {
						code = code + "0";
					}
					
					if(map_coded_reverse.containsKey(code)) {
					
						
						editor.write(map_coded_reverse.get(code));
						code = "";
						
					} else {
						
						continue;
					
					}
					
					
				
				}
				} catch (NullPointerException e) {
					e.printStackTrace();
					
				}
				
				
				editor.flush();
				editor.close();

		 
		 
		 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	   
	   
	   
   }
   
   //method that reads freqFile and gets frequency of characters 
   public void find_coded_frequency(String freqFile) throws IOException {

		//Declares neccesary variables
		String current;
		Integer occurence; 
		Scanner scanner = new Scanner(System.in);
		Scanner scannerSecond = new Scanner(System.in);
		
	
		
		//try-catch to handle FileNotFoundException
		try {
			//Creates a scanner that reads the input file
			scanner = new Scanner(new File(freqFile));
		} catch (FileNotFoundException e) {
			
			System.out.println("File does not exist");
			e.printStackTrace();
		}

		
		while(scanner.hasNextLine()) {
			
			//secondscanner that scans the document line by  line
			
			scannerSecond = new Scanner(scanner.nextLine());
			//loop that adds words to hashmap and updates occurences 
			while(scannerSecond.hasNext()) {
				current = scannerSecond.next();
				
				String [] holder = current.split(":");
				
				try {
	
					
					char character = (char) Integer.parseInt(holder[0], 2);
					 
					Integer times = Integer.parseInt(holder[1]);
					
					map_frequency.put(character, times);
					
					
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
			}
			
		}
			
		
	}
   




   public static void main(String[] args) throws IOException {
      HuffmanSubmit  huffman = new HuffmanSubmit();
      
     
      huffman.encode("ur.jpg", "ur.enc", "output.txt");
      
      huffman.decode("ur.enc", "ur_dec.jpg", "output.txt");
      
      huffman = new HuffmanSubmit();
		
      huffman.encode("alice30.txt", "alice.enc", "freqFile.txt");
      
      huffman.decode("alice.enc", "alice_dec.txt", "freqFile.txt");
   }
   
   //BTNode class that implements Comparable 
   public class BTNode implements Comparable <BTNode> {
		
		int data;
		BTNode left;
		BTNode right;
		String code; 
		Character character; 
		boolean printed = false; 
	// Add constructor and/or other methods if required
		
		public BTNode(int value) {
			data = value;
			left = null;
			right = null;
			code = "";
		}

		public int getData() {
		
			return data;
		}

		public BTNode getLeft() {
			return left;
		}

		public BTNode getRight() {
			return right;
		}

		public boolean isPrinted() {
			return printed;
		}

		public void setData(int data) {
			this.data = data;
		}

		public void setLeft(BTNode left) {
			this.left = left;
		}

		public void setRight(BTNode right) {
			this.right = right;
		}

		public void setPrinted(boolean printed) {
			this.printed = printed;
		}

		public String getCode() {
			return code;
		}

		public Character getCharacter() {
			return character;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setCharacter(Character character) {
			this.character = character;
		}

		@Override
		public int compareTo(BTNode o) {
			
			if(getData() > o.getData()) {
				return 1;
			} else if (getData() == o.getData()){
				return 0;
			} else {
				return -1;
			}
		
		}

		

	}


}
