package it.polimi.ingsw.ps42.parser;

import java.io.IOException;

import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.CouncilObtain;
import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class BonusBarBuilder extends Builder{

	
	
	public BonusBarBuilder(String fileName) throws IOException{
		
		super(fileName);
	}
	
	public BonusBarBuilder(String fileName, String conversionFileName) throws IOException{
		
		super(fileName, conversionFileName);
	
	}
	
	@Override
	protected void initGson() {
		
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Effect.class, new Serializer());
		gson = builder.serializeNulls().setPrettyPrinting().create();

	}
	
	public void createBonusBar() throws IOException{
		
		System.out.println("NUOVO BONUS PER AZIONE PRODUZIONE");
		Obtain productBonus = askObtain();
		System.out.println("NUOVO BONUS PER AZIONE RACCOLTO");
		Obtain yieldBonus = askObtain();
		System.out.println("Nome bonusBar? ");
		String name = scanner.nextLine();
		BonusBar bonusBar = new BonusBar(productBonus, yieldBonus, name );
		String parse = gson.toJson(bonusBar);
		buffer.append(parse);
		
	}

	private Obtain askObtain(){
		
		String response;
		CouncilObtain councilObtain = null;
		System.out.println("GUADAGNI:");
		Packet gains = askPacket();
		System.out.println("Aggiungere privilegio del consiglio?");
		response = scanner.nextLine();
		if(response.toUpperCase().equals("SI")){
			System.out.println("Quantità di Privilegi del Consiglio?");
			int quantity = scanner.nextInt();
			councilObtain = new CouncilObtain(quantity, councilConversion);
			
		}
		return new Obtain(null, gains, councilObtain);
	}

}
