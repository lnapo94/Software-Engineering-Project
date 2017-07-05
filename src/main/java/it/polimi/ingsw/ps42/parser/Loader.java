package it.polimi.ingsw.ps42.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;

/**
 * Abstract class for a generic loader from a json File
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public abstract class Loader  {

	protected Gson gson;
	private FileReader reader;
	protected BufferedReader buffer;
	protected JsonStreamParser parser;
	
	/**
	 * Constructor for the Loader, take care of reader and buffer initialization
	 * @param fileName the path to the File to read
	 * @throws IOException if any problem in File opening occurs
	 */
	protected Loader(String fileName) throws IOException {
		
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
	}
	
	/**
	 * Setter for a new File to open, also close the previous if exist
	 * @param fileName the path to the new json File to open
	 * @throws IOException if any problem in File opening occurs
	 */
	public void setFileName(String fileName) throws IOException{
		
		close();
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
	}
	
	/**
	 * Abstract method to personalize the Gson loader 
	 */
	protected abstract void initGson();
	
	/**
	 * Method that close the json File opened before
	 * @throws IOException
	 */
	public void close() throws IOException {
		
		buffer.close();
		reader.close();
	}

	
}
