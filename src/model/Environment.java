package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * Abstraction of the grid and puzzle environments.
 *
 */
public abstract class Environment{
	
	public abstract int getHeight();
	public abstract int getWidth();
	public abstract Colored getElement(int x, int y);
	
	protected Algorithm algo;
	
	public Algorithm getAlgo(){
		return algo;
	}
	
	/**
	 * Execute the next step of the algorithm
	 * @throws NoPathFoundException
	 * @throws PathFoundException
	 */
	public void nextStep() throws NoPathFoundException, PathFoundException{
		algo.nextStep();
	}
	
	/**
	 * Execute the algo until the end: a path is found or the open list is empty.
	 * @throws NoPathFoundException
	 * @throws PathFoundException
	 */
	public abstract void finalStep() throws NoPathFoundException, PathFoundException;
	
	/**
	 * Loads a file and splits all the characters in a double array.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public ArrayList<ArrayList<Character>> loadFile(String path) throws IOException{
		
		ArrayList<ArrayList<Character>> parsed_file = new ArrayList<ArrayList<Character>>();
		
		BufferedReader reader = new BufferedReader(new FileReader("./boards/" + path + ".txt"));
		String line = reader.readLine();
		
		while(line != null){
			
			parsed_file.add(new ArrayList<Character>());
			
			//We separate every characters of each strings
			for (char current : line.toCharArray()) {
				parsed_file.get(parsed_file.size() - 1).add(current);
			}
			
			line = reader.readLine();
		}
		
		return parsed_file;
	}
	
}
