package it.polimi.ingsw.ps42.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class ImageLoader extends Loader{
	
	private static final String DICE_PATH = "Resource//Images//DiceImages//";
	
	private HashMap<String, String> cardsPath;
	private HashMap<String, String> leaderCardsPath;
	private HashMap<String, String> bansPath;
	private HashMap<String, String> resourcePath;
	private HashMap<String, String> bonusBarPath;
	private HashMap<String, String> familiarPath;
	
	//Conversion for the dice
	private HashMap<String, String> dicePath;

	public ImageLoader(String fileName) throws IOException {
		super(fileName);
		
		//Initialize the maps
		cardsPath = loader();
		if(cardsPath == null)
			throw new IOException();
		
		leaderCardsPath = loader();
		if(cardsPath == null)
			throw new IOException();	
		
		bansPath = loader();
		if(bansPath == null)
			throw new IOException();
		
		dicePath = loader();
		if(dicePath == null)
			throw new IOException();
	
		resourcePath = loader();
		if(resourcePath == null)
			throw new IOException();

		bonusBarPath = loader();
		if(bonusBarPath == null)
			throw new IOException();
	
		familiarPath = loader();
		if(familiarPath == null)
			throw new IOException();
	}

	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}
	
	private HashMap<String, String> loader() {
		if(parser.hasNext()) {
			JsonElement element = parser.next();
			
			if(element.isJsonObject()) {
				Type type = new TypeToken< HashMap<String, String> >(){}.getType();
				return gson.fromJson(element, type);
			}
		}
		return null;
	}
	
	private BufferedImage loadGenericImage(HashMap<String, String> map, String cardName) throws IOException {
		return ImageIO.read(new File(map.get(cardName)));
	}
	
	public BufferedImage loadCardImage(String cardName) throws IOException {
		return loadGenericImage(cardsPath, cardName);
	}
	
	public BufferedImage loadLeaderCardImage(String leaderCardName) throws IOException {
		return loadGenericImage(leaderCardsPath, leaderCardName);
	}
	
	public BufferedImage loadBanImage(Integer period, Integer index) throws IOException {
		final int OFFSET = 7;
		
		Integer exactIndex = 1 + index + period * OFFSET;
		return loadGenericImage(bansPath, exactIndex.toString());
	}
	
	public BufferedImage loadOrangeDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "Orange");
	}
	
	public BufferedImage loadBlackDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "Black");
	}
	
	public BufferedImage loadWhiteDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "White");
	}
	
	private BufferedImage loadGenericDieImage(Integer value, String folderName) throws IOException {
		String completePath = DICE_PATH + folderName + "//" + dicePath.get(value.toString());
		return ImageIO.read(new File(completePath));
	}
 	
	public BufferedImage loadResourceImage(Resource resource) throws IOException{
		
		return ImageIO.read(new File(resourcePath.get(resource.toString())));
	}
	
	public BufferedImage loadBonusBarImage(String name) throws IOException{
		return ImageIO.read(new File(bonusBarPath.get(name)));
	}
	
	public BufferedImage loadFamiliarImage(Integer playerNumber, FamiliarColor familiarColor) throws IOException{
		return ImageIO.read(new File(familiarPath.get(familiarColor.toString()) + "//" + playerNumber.toString() + ".png"));
	}
	
	public BufferedImage loadBonusFamiliarImage() throws IOException{
		return ImageIO.read(new File(familiarPath.get("BonusFamiliar")));
	}
	
	public int cardsMapSize() {
		return cardsPath.size();
	}
	
	public int leaderCardsMapSize() {
		return leaderCardsPath.size();
	}
	
	public int bansMapSize() {
		return bansPath.size();
	}
	
	public int diceMapSize() {
		return this.dicePath.size();
	}
	
	public static void main(String[] args) throws IOException {
		ImageLoader loader = new ImageLoader("Resource//Configuration//imagePaths.json");
		System.out.println(loader.cardsMapSize());
		System.out.println(loader.leaderCardsMapSize());
		System.out.println(loader.bansMapSize());
		System.out.println(loader.diceMapSize());
	}
}
