package it.polimi.ingsw.ps42.parser;

import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;


public class FaithPathLoader extends Loader {

	public FaithPathLoader( String fileName ) throws IOException{
	
		super( fileName);
		initGson();
		
	}
	
	protected void initGson(){
			
			GsonBuilder builder = new GsonBuilder();
			gson = builder.create();
			parser = new JsonStreamParser(buffer);
			
	}
	

}
