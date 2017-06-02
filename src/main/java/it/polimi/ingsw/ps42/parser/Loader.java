package it.polimi.ingsw.ps42.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonStreamParser;


public abstract class Loader  {

	protected Gson gson;
	private FileReader reader;
	protected BufferedReader buffer;
	protected JsonStreamParser parser;
	
	protected Loader(String fileName) throws IOException {
		
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
	}
	
	public void setFileName(String fileName) throws IOException{
		
		close();
		reader = new FileReader(fileName);
		buffer = new BufferedReader(reader);
		initGson();
	}
	
	protected abstract void initGson();
	
	public void close() throws IOException {
		
		buffer.close();
		reader.close();
	}

	
}
