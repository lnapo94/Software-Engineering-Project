package it.polimi.ingsw.ps42.parser;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.ingsw.ps42.model.effect.Effect;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.resourcepacket.Packet;

public class BonusBarBuilder extends Builder{

	
	
	public BonusBarBuilder(String fileName) throws IOException{
		
		super(fileName);
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
		
		BonusBar bonusBar = new BonusBar(productBonus, yieldBonus);
		String parse = gson.toJson(bonusBar);
		buffer.append(parse);
		
	}

	private Obtain askObtain(){
		System.out.println("GUADAGNI");
		Packet gains = askPacket();
		return new Obtain(null, gains);
	}
	

}
