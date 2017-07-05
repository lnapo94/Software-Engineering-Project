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

/**
 * Loader for all the imager used by the GUI 
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
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

	/**
	 * Basic Constructor for the ImageLoader starting from a file of Path to images
	 * @param fileName the File of all the path to images to load later
	 * @throws IOException if any problem in File opening occurs
	 */
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

	/**
	 * Private method used to personalize the Gson loader
	 */
	@Override
	protected void initGson() {
		gson = new Gson();
		parser = new JsonStreamParser(buffer);
	}
	
	/**
	 * Private method used to load an HashMap from file
	 * @return the next HashMap in the File
	 */
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
	
	/**
	 * Private Loader for a generic image 
	 * @param map the HashMap of paths
	 * @param cardName the name of the Card to load
	 * @return the BufferedImage of the equivalent Card
	 * @throws IOException if any problem in File opening occurs
	 */
	private BufferedImage loadGenericImage(HashMap<String, String> map, String cardName) throws IOException {
		return ImageIO.read(new File(map.get(cardName)));
	}
	
	/**
	 * Load a Card Image from its name
	 * @param cardName the name of the Card to load
	 * @return the BufferedImage of the Card passed
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadCardImage(String cardName) throws IOException {
		return loadGenericImage(cardsPath, cardName);
	}
	
	/**
	 * Load a LeaderCard Image from its name
	 * @param leaderCardName the name of the Card to load
	 * @return the BufferedImage of the LeaderCard passed
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadLeaderCardImage(String leaderCardName) throws IOException {
		return loadGenericImage(leaderCardsPath, leaderCardName);
	}
	
	/**
	 * Load the image of a Ban from its index and Period
	 * @param period the Ban period
	 * @param index the Ban index
	 * @return the BufferedImage of the Ban
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadBanImage(Integer period, Integer index) throws IOException {
		final int OFFSET = 7;
		
		Integer exactIndex = 1 + index + period * OFFSET;
		return loadGenericImage(bansPath, exactIndex.toString());
	}
	
	/**
	 * Load the Image of the Orange Dice Face by the Integer value passed
	 * @param value the number on the Dice
	 * @return the BufferedImage of the face of the Orange dice
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadOrangeDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "Orange");
	}
	

	/**
	 * Load the Image of the Black Dice Face by the Integer value passed
	 * @param value the number on the Dice
	 * @return the BufferedImage of the face of the Black dice
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadBlackDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "Black");
	}
	

	/**
	 * Load the Image of the White Dice Face by the Integer value passed
	 * @param value the number on the Dice
	 * @return the BufferedImage of the face of the White dice
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadWhiteDieImage(Integer value) throws IOException {
		return loadGenericDieImage(value, "White");
	}
	

	/**
	 * Private method used to load the Image of a generic dice Face by the Integer value passed
	 * @param value the number on the Dice
	 * @return the BufferedImage of the face of the dice
	 * @throws IOException if any problem in File opening occurs
	 */
	private BufferedImage loadGenericDieImage(Integer value, String folderName) throws IOException {
		String completePath = DICE_PATH + folderName + "//" + dicePath.get(value.toString());
		return ImageIO.read(new File(completePath));
	}
 	

	/**
	 * Load the Image of the Resource passed
	 * @param resource the Resource type to load
	 * @return the BufferedImage of the face of the Resource
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadResourceImage(Resource resource) throws IOException{
		
		return ImageIO.read(new File(resourcePath.get(resource.toString())));
	}
	
	/**
	 * Load the BonusBar Image from its name
	 * @param name the name of the BonusBar
	 * @return the BufferedImage of the BonusBar
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadBonusBarImage(String name) throws IOException{
		return ImageIO.read(new File(bonusBarPath.get(name)));
	}
	
	/**
	 * Load the Familiar Image for a generic player by its color and number in the game list
	 * @param playerNumber the number of the Player for the game
	 * @param familiarColor the color of the Familiar to load
	 * @return the BufferedImage of the familiar wanted
	 * @throws IOException if any problem in File opening occurs
	 */
	public BufferedImage loadFamiliarImage(Integer playerNumber, FamiliarColor familiarColor) throws IOException{
		return ImageIO.read(new File(familiarPath.get(familiarColor.toString()) + "//" + playerNumber.toString() + ".png"));
	}
	
	/**
	 * Load the Bonus Familiar Image for a generic player
	 * @return the BufferedImage of the Bonus Familiar
	 * @throws IOException if any problem in File opening occurs 
	 */
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
