package it.polimi.ingsw.ps42.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;

import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;
import it.polimi.ingsw.ps42.model.resourcepacket.Unit;

public abstract class Builder {

	protected Gson gson;
	protected FileWriter writer;
	protected BufferedWriter buffer;
	protected Scanner scanner;
	protected List<Obtain> councilConversion;
	
	
	public Builder(String fileName, String councilConversionFile) throws IOException {
		
		initGson();
		writer = new FileWriter(fileName, true);
		buffer = new BufferedWriter(writer);
		scanner = new Scanner(System.in);
		
		councilConversion = getCouncilConversion(councilConversionFile);
	}
	
	protected abstract void initGson();
	
	public void close() throws IOException{
		
		buffer.close();
		writer.close();
		scanner.close();
	}
	
	private List<Obtain> getCouncilConversion( String fileName) throws IOException{
		
		System.out.println("--CREAZIONE CONVERSIONI PRIVILEGI DEL CONSIGLIO--");
		List<Obtain> conversion = new ArrayList<>();
		
		Gson tempGson = new Gson();
		FileReader tempReader = new FileReader(fileName);
		BufferedReader tempBuffer = new BufferedReader(tempReader);
		JsonStreamParser tempParser = new JsonStreamParser(tempBuffer);
		
		while( tempParser.hasNext()){
			JsonElement element = tempParser.next();
			if( element.isJsonObject() )
				conversion.add( tempGson.fromJson(element, Obtain.class));
		}
		
		System.out.println("--FINE CREAZIONE LISTA CONVERSIONI PER PRIVILEGI DEL CONSIGLIO--");
		return conversion;
	}
	
	protected Packet askPacket(){
		String response;
		Packet packet=new Packet();
		do{
			System.out.println("Tipo Risorsa? ");
			System.out.println(Resource.FAITHPOINT.toString()+" "+Resource.MILITARYPOINT.toString()+" "
					+Resource.MONEY.toString()+" "+Resource.SLAVE.toString()+" "+Resource.STONE.toString()+" "
					+Resource.VICTORYPOINT.toString()+" "+Resource.WOOD.toString());
			String resource=scanner.nextLine();
			System.out.println("Quantità?" );
			int quantity = Integer.parseInt(scanner.nextLine());
			packet.addUnit(new Unit(Resource.parseInput(resource), quantity));
			System.out.println("Vuoi aggiungere altro?(si/no)");
			response=scanner.nextLine();
			System.out.println("stato attuale: "+packet);
		}
		while(response.toUpperCase().equals("SI"));
		return packet;
	}
	
}
