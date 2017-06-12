package it.polimi.ingsw.ps42.parser;

import java.io.IOException;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.position.CouncilPosition;
import it.polimi.ingsw.ps42.model.position.MarketPosition;
import it.polimi.ingsw.ps42.model.position.Position;
import it.polimi.ingsw.ps42.model.position.TowerPosition;
import it.polimi.ingsw.ps42.model.position.YieldAndProductPosition;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class PositionBuilder extends Builder {

	
	public PositionBuilder( String fileName, String councilConversionFile) throws IOException {
		
		super(fileName, councilConversionFile);
	}
	
	public PositionBuilder( String fileName) throws IOException {
		
		super(fileName);
	}
	
	protected void initGson(){
		
		GsonBuilder builder=new GsonBuilder().serializeNulls().setPrettyPrinting();
		
		this.gson = builder.registerTypeAdapter(Position.class, new Serializer()).create();
		
	}
	
	public void close() throws IOException{
		
		buffer.close();
		writer.close();
		scanner.close();
	}
	
	public void addPosition() throws IOException {
		
		ActionType type = askActionType();
		int level = askLevel();
		Obtain bonus = askBonus();
		int malus = askMalus();
		Position position;
		
		if(type == ActionType.COUNCIL){
			CouncilObtain councilBouns = new CouncilObtain( 1 , councilConversion);
			position = new CouncilPosition(level, bonus, malus, councilBouns);
		}
		
		else if(type == ActionType.MARKET) {
			String response;
			CouncilObtain councilBonus = null;
			System.out.println("Vuoi aggiungere un bonus privilegio del consiglio?(si/no)");
			response = scanner.nextLine();
			if( response.toUpperCase().equals("SI"))
				councilBonus = askCouncilObtain();
			position = new MarketPosition(level, bonus, malus, councilBonus );
		}
		else if(type == ActionType.PRODUCE || type == ActionType.YIELD)
			position = new YieldAndProductPosition(type, level, bonus, malus);

		else if( type == ActionType.TAKE_BLUE || type == ActionType.TAKE_GREEN ||
					type == ActionType.TAKE_YELLOW || type == ActionType.TAKE_VIOLET )
			
			position = new TowerPosition(type, level, bonus, malus);
		
		else throw new IOException();		//Wrong name passed
		
		addPositionToFile( position);
		
	}
	
	private ActionType askActionType(){
		System.out.println("inserisci il tipo di azione legato alla posizione");
		System.out.println( ActionType.TAKE_BLUE + " "+ ActionType.TAKE_GREEN + " "+ ActionType.TAKE_VIOLET+ " "+
							ActionType.TAKE_YELLOW+ " "+ ActionType.COUNCIL +" "+ ActionType.MARKET +" "+
							ActionType.PRODUCE +" "+ ActionType.YIELD);
		return ActionType.parseInput( scanner.nextLine());
	}
	
	private int askLevel(){
		System.out.println("inserisci il livello della posizione");
		return Integer.parseInt(scanner.nextLine());
	}
		
	private int askMalus(){
		System.out.println("inserisci il malus della posizione");
		return Integer.parseInt(scanner.nextLine());
	}	
	
	private Obtain askBonus(){
		
		Packet costs = new Packet();
		Packet gains = new Packet();
		String response;

		System.out.println("Aggiungere guadagni? (si/no)");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI"))
			gains=askPacket();
		return new Obtain(costs, gains, null);
	}
	
	private void addPositionToFile( Position position ) throws IOException {
		
		String parse = gson.toJson(position);
		buffer.write(parse);
	}
	
	private CouncilObtain askCouncilObtain(){

		System.out.println("inserisci la quantità di privilegi del consiglio");
		int quantity = Integer.parseInt(scanner.nextLine());

		return new CouncilObtain(quantity, councilConversion);
	}
	
	public static void main(String[] args) throws IOException {
		PositionBuilder builder = new PositionBuilder("marketPosition.json", "Resource//Position//CouncilPosition//CouncilConvertion.json");
		for (int i = 0; i < 4; i++) {
			builder.addPosition();
		}
		builder.close();
	}
	

}
