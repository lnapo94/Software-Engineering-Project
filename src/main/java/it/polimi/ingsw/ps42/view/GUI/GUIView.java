package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

import it.polimi.ingsw.ps42.view.GUI.dialog.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.polimi.ingsw.ps42.message.BanMessage;
import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.CouncilRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.PlayerToken;
import it.polimi.ingsw.ps42.message.ReconnectMessage;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.Card;
import it.polimi.ingsw.ps42.model.StaticList;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.exception.ElementNotFoundException;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.model.player.Familiar;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.parser.ImageLoader;
import it.polimi.ingsw.ps42.parser.TimerLoader;
import it.polimi.ingsw.ps42.view.TableInterface;
import it.polimi.ingsw.ps42.view.View;

/**
 * Class that represents all the Graphical User Interface component of our implementation
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class GUIView extends View implements TableInterface{

	//The GUI main frame, where all the others Components are placed
	private JFrame mainFrame;
	private JLayeredPane mainLayeredPane;
	private Dimension tableImageDimension;
	private Dimension lowTableDimension;
	
	//The Label where the zoom of the Card is displayed
	private CardZoom cardZoom;
	
	//The Container for all the cards taken
	private CardContainer cardContainer;
	
	//The JButton used to skip a move
	private JButton skipMove;
	
	//JButton used to show the LeaderCardShowDialog
	private JButton leaderCardButton;
	
	//The Image of the familiar currently moving
	private DraggableComponent movingFamiliar;
	
	//The List of CardLabels separated by Color
	private LinkedList<CardLabel> greenTower;
	private LinkedList<CardLabel> yellowTower;
	private LinkedList<CardLabel> blueTower;
	private LinkedList<CardLabel> violetTower;
	
	//The List of positions where the Familiars can be placed
	//Starting Positions 
	private DraggableComponent neutralFamiliar;
	private DraggableComponent blackFamiliar;
	private DraggableComponent orangeFamiliar;
	private DraggableComponent whiteFamiliar;
	private DraggableComponent bonusFamiliar;
	
	//Tower positions
	private LinkedList<JLabel> greenTowerForFamiliar;
	private LinkedList<JLabel> yellowTowerForFamiliar;
	private LinkedList<JLabel> blueTowerForFamiliar;
	private LinkedList<JLabel> violetTowerForFamiliar;
	//Yield and Product Positions
	private JLabel firstYield;
	private PositionContainer secondYield;
	private JLabel firstProduce;
	private PositionContainer secondProduce;
	
	//Council Positions
	private PositionContainer council;
	private List<JLabel> market;
	
	//Window for Resource update
	private ResourceWindow resourceWindow;
	
	//Labels for the Dice Values
	private JLabel blackDice;
	private JLabel whiteDice;
	private JLabel orangeDice;
	
	//The List of Bans
	private List<CardLabel> bans;
	//The player next move
	private PlayerMove nextMove;
	//The Loader for all the images of the game
	private ImageLoader imageLoader;
	
	//The loader for the game timers
	private TimerLoader timerLoader;
	private final long MOVE_SECONDS;
	private TimerLabel timerLable;
	
	//GUI Logger
	private Logger logger = Logger.getLogger(GUIView.class);
	
	/**
	 * Constructor of the GUI Interface
	 * @throws IOException		Thrown if some files cannot be opened
	 */
	public GUIView() throws IOException {
		
		super();
		PropertyConfigurator.configure("Logger//Properties//client_log.properties");
		imageLoader = new ImageLoader("Resource//Configuration//imagePaths.json");
		timerLoader = new TimerLoader("Resource//Configuration//timers.json");
		MOVE_SECONDS = timerLoader.getPlayerMoveTimer() ;
		initialize();
		mainFrame.setVisible(true);
		
	}
	
	/**
	 * Private method used to initialize the main frame with the correct screen proportions
	 * @throws IOException	Thrown if some image file cannot be opened
	 */
	private void initialize() throws IOException{
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		//Create a full Screen main frame
		mainFrame = new JFrame(); 
		mainFrame.setBounds(0, 0,screen.width,(int)(screen.height * 0.90));
		mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setTitle("Lorenzo il Magnifico");
		//Add a JLayeredPane
		mainLayeredPane = new JLayeredPane();
		mainLayeredPane.setBounds(5, 5, mainFrame.getWidth(), mainFrame.getHeight());
		mainFrame.getContentPane().add(mainLayeredPane);
		
		Dimension leftPaneDimension = new Dimension((int)(mainLayeredPane.getWidth()*0.42), mainLayeredPane.getHeight());
		Dimension rightPanelDimension = new Dimension((int)(mainLayeredPane.getWidth()*0.58), mainLayeredPane.getHeight());
		
		//Set the Table main Image
		JLabel tableLabel = new JLabel();
		tableLabel.setSize(new Dimension((int)leftPaneDimension.getWidth(), (int)leftPaneDimension.getHeight()));
		tableLabel.setLocation(0, 0);
		ImageIcon tableIcon = resizeImage(ImageIO.read(new File("Resource//Images//TableUpperPart.png")), tableLabel.getSize());
		tableLabel.setIcon(tableIcon);
		mainLayeredPane.add(tableLabel, -1);
		//Set the Card and Table Dimension from the TableIcon and build all the CardPositions
		tableImageDimension = new Dimension(tableIcon.getIconWidth(), tableIcon.getIconHeight());
		Dimension cardDimension = new Dimension((int)(tableIcon.getIconWidth()*0.13), (int)(tableIcon.getIconHeight()*0.17));

		//Set the CardZoom Panel
		BufferedImage cardBack = ImageIO.read(new File("Resource//Images//LeaderCards//back.jpg"));
		cardZoom = new CardZoom(cardBack, new Dimension((int)(cardDimension.getWidth()*3), (int)(cardDimension.getHeight()*3)));
		cardZoom.setLocation((int)leftPaneDimension.getWidth(),0);
		mainLayeredPane.add(cardZoom);
		
		//Set the lowerPart of the Table
		JLabel lowTableLabel = new JLabel();
		lowTableDimension = new Dimension((int)(rightPanelDimension.getWidth()-cardZoom.getWidth()), (int)(cardZoom.getHeight()*0.70));
		lowTableLabel.setSize(lowTableDimension);
		lowTableLabel.setLocation((int)(leftPaneDimension.getWidth()+cardZoom.getWidth()), 0);
		lowTableLabel.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//TableDownPart.png")), lowTableLabel.getSize()));
		mainLayeredPane.add(lowTableLabel, -1);
		
		//Add the label for the Dice
		buildDicePositions(lowTableDimension);
		
		//Add the Ban's Labels 
		buildBanLabel();

		//Set the Cards taken container
		Dimension containerDimension = new Dimension((int)(rightPanelDimension.getWidth()),(int) (rightPanelDimension.getHeight()-cardZoom.getHeight()));
		Point containerLocation = new Point((int)leftPaneDimension.getWidth(), (int)cardZoom.getHeight());
		cardContainer = new CardContainer(containerDimension, containerLocation, cardZoom, ImageIO.read(new File("Resource//Images//Others//cardContainer.jpg")),ImageIO.read(new File("Resource//Images//Others//aggiuntaContainer.png")) , cardDimension);
		mainLayeredPane.add(cardContainer, -1);
		
		//Build the main Familiar positions
		buildCardPosition(cardDimension);
		
		buildFamiliarStartingPositions();
		
		buildFamiliarMovePositions();
		
		//Add the Resource Update Window
		Dimension resourceWindowDimension = new Dimension((int)(rightPanelDimension.getWidth()-cardZoom.getWidth()), (int)(rightPanelDimension.getHeight() - lowTableLabel.getHeight() - containerDimension.getHeight()));
		Point resourceWindowLocation = new Point((int)(leftPaneDimension.getWidth()+cardZoom.getWidth()), (int)lowTableLabel.getHeight());
		resourceWindow = new ResourceWindow(resourceWindowDimension, resourceWindowLocation, imageLoader );
		mainFrame.add(resourceWindow);
		
		//Build the JButton to skip a move
		buildSkipMoveButton();
		
		//Build the JButton for the LeaderCards
		buildLeaderCardsButton();
		
		//Build the timerLable
		buildTimerLable();
				
		LoginWindow login = new LoginWindow(this, "");
		login.run();
		
	
	}
	/**
	 * Initialize the Card Position Label
	 * @param cardDimension Dimension of the card that will be placed
	 * @throws IOException 	Thrown if some file cannot be opened
	 */
	private void buildCardPosition(Dimension cardDimension) throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.022);
		int deltaY = (int)(tableImageDimension.getHeight()*0.03);

		greenTower = new LinkedList<>();
		blueTower = new LinkedList<>();
		violetTower = new LinkedList<>();
		yellowTower = new LinkedList<>();
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			greenTower.addFirst(card);
			mainLayeredPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			blueTower.addFirst(card);
			mainLayeredPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			yellowTower.addFirst(card);
			mainLayeredPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			violetTower.addFirst(card);
			mainLayeredPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}

	}
	

	/**
	 * Method used to construct the familiars' starting position on the screen
	 * @throws IOException	Thrown if some picture cannot be opened
	 */
	private void buildFamiliarStartingPositions() throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.56 );
		int deltaY = (int)(tableImageDimension.getHeight()*0.93);
		
		blackFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage( 0, FamiliarColor.BLACK), FamiliarColor.BLACK);
		blackFamiliar.enableListener();
		blackFamiliar.setTable(this);
		mainLayeredPane.add(blackFamiliar, 2);
		
		int border = (int)(blackFamiliar.getWidth()*1.1); 
		deltaX += border;
		whiteFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage( 0, FamiliarColor.WHITE), FamiliarColor.WHITE);
		whiteFamiliar.enableListener();
		whiteFamiliar.setTable(this);
		mainLayeredPane.add(whiteFamiliar, 2);
		
		deltaX += border;
		orangeFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage(0, FamiliarColor.ORANGE), FamiliarColor.ORANGE);
		orangeFamiliar.enableListener();
		orangeFamiliar.setTable(this);
		mainLayeredPane.add(orangeFamiliar, 2);
		
		deltaX += border;
		neutralFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage(0, FamiliarColor.NEUTRAL), FamiliarColor.NEUTRAL);
		neutralFamiliar.enableListener();
		neutralFamiliar.setTable(this);
		mainLayeredPane.add(neutralFamiliar, 2);
		
	}
	
	/**
	 * Method used to build all the possible labels where a familiar can be positioned
	 * @throws IOException 		Thrown if some file cannot be opened
	 */
	private void buildFamiliarMovePositions() throws IOException{
		
		//Build the Tower related familiarMove positions
		greenTowerForFamiliar = new LinkedList<>();
		yellowTowerForFamiliar = new LinkedList<>();
		blueTowerForFamiliar = new LinkedList<>();
		violetTowerForFamiliar = new LinkedList<>();

		int deltaX = (int)(tableImageDimension.getWidth()*0.172);
		Dimension positionDimension = new Dimension((int)(tableImageDimension.getWidth()*0.06), (int)(tableImageDimension.getHeight()*0.05));
		placeFamiliarPosition(positionDimension, greenTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(positionDimension, blueTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(positionDimension, yellowTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.241);
		placeFamiliarPosition(positionDimension, violetTowerForFamiliar, deltaX);		
		
		//Build the Council related familiarMove positions
		Dimension positionContainerDimension = new Dimension((int)(tableImageDimension.getWidth()*0.3), (int)(tableImageDimension.getHeight()*0.12));
		Point positionContainerLocation = new Point((int)(tableImageDimension.getWidth()*0.52), (int)(tableImageDimension.getHeight()*0.76));
		council = new PositionContainer(positionContainerDimension, positionContainerLocation, new Dimension((int)(tableImageDimension.getWidth()*0.06), (int)(tableImageDimension.getHeight()*0.05)));
		mainLayeredPane.add(council, 0);
		
		//Build the Market, Yield and Product familiarMove positions
		market = new ArrayList<>();

		int deltaY = (int)(tableImageDimension.getHeight()*0.11);
		firstProduce = placeYieldAndProductPosition(positionDimension, firstProduce, deltaY);
		//Build the second Position
		deltaX = (int)(tableImageDimension.getWidth() + cardZoom.getWidth() + lowTableDimension.getWidth()*0.14);
		Dimension secondPositionDimension = new Dimension((int)(tableImageDimension.getWidth()*0.25), (int)(tableImageDimension.getHeight()*0.1));
		secondProduce = new PositionContainer(secondPositionDimension, new Point(deltaX, (int)(deltaY*0.75)), positionDimension);
		mainLayeredPane.add(secondProduce, 0);
		
		deltaY += (int)(tableImageDimension.getHeight()*0.145);
		firstYield = placeYieldAndProductPosition(positionDimension, firstYield, deltaY);
		//Build the second Position
		secondYield = new PositionContainer(secondPositionDimension, new Point(deltaX, (int)(deltaY * 0.9)), positionDimension);
		mainLayeredPane.add(secondYield, 0);
		
		deltaX = (int)(tableImageDimension.getWidth()*1.615 + cardZoom.getWidth() );
		deltaY = (int)(tableImageDimension.getHeight()*0.08);
		placeMarket(positionDimension, deltaX, deltaY);
		deltaX += (int)(positionDimension.getWidth()*1.85);
		placeMarket(positionDimension, deltaX, deltaY);
		deltaX += (int)(positionDimension.getWidth()*1.75);
		deltaY = (int)(tableImageDimension.getHeight()*0.11);
		placeMarket(positionDimension, deltaX, deltaY);
		deltaX += (int)(positionDimension.getWidth()*1.42);
		deltaY += (int)(tableImageDimension.getHeight()*0.085);
		placeMarket(positionDimension, deltaX, deltaY);
		
		mainLayeredPane.updateUI();
	}
	
	/**
	 * Method used to create all the single positions of the market
	 * @param positionDimension		The dimension of the position 
	 * @param x						The X of the screen where position the label
	 * @param y						The Y of the screen where position the label
	 */
	private void placeMarket(Dimension positionDimension, int x, int y) {
		
		JLabel position = new JLabel();
		position.setSize(positionDimension);
		position.setLocation(x, y);
		market.add(position);
		mainLayeredPane.add(position, 0);
	}
	
	/**
	 * Method used to create all the single positions of the yield and the product
	 * @param positionDimension		The dimension of the position 
	 * @param firstPosition			The label of the first position
	 * @param deltaY				Used to set the correct delta between yield and product on the screen
	 * @return						The label with the correct dimension to place the familiars
	 */
	private JLabel placeYieldAndProductPosition(Dimension positionDimension, JLabel firstPosition, int deltaY) {
		
		//Build the first position
		int deltaX = (int)(tableImageDimension.getWidth()*1.03 + cardZoom.getWidth() );
		firstPosition = new JLabel();
		firstPosition.setSize(positionDimension);
		firstPosition.setLocation(deltaX, deltaY);
		mainLayeredPane.add(firstPosition, 0);
		deltaX += positionDimension.getWidth();
		return firstPosition;
	}
	
	/**
	 * Method used to create all the 4 towers positions
	 * @param positionDimension		The dimension of the position 
	 * @param tower					The list used to contain all the tower labels
	 * @param rightShift			The parameter to set the horizontal gap between the towers
	 */
	private void placeFamiliarPosition(Dimension positionDimension, LinkedList<JLabel> tower, int rightShift) {
		
		int deltaY = (int)(tableImageDimension.getHeight()*0.081);
		for (int i=0; i<4; i++){
			JLabel familiarPosition = new JLabel();
			familiarPosition.setSize(positionDimension);
			familiarPosition.setLocation(rightShift, deltaY);
			tower.addFirst(familiarPosition);
			mainLayeredPane.add(familiarPosition, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.1858);
		}
	}
	
	/**
	 * Method used to create the dice positions on the screen
	 * @param lowerTableDimension	The dimension of the lower part of table on the screen to use the correct proportions
	 */
	private void buildDicePositions(Dimension lowerTableDimension ) {
		
		Dimension diceDimension = new Dimension((int)(lowerTableDimension.getWidth()*0.09),(int)(lowerTableDimension.getHeight()*0.21));
		blackDice = buildSingleDice(diceDimension, lowerTableDimension, 0);
		int rightShift = (int)(diceDimension.getWidth()*1.29);
		whiteDice = buildSingleDice(diceDimension, lowerTableDimension, rightShift);
		rightShift += (int)(diceDimension.getWidth()*1.3);
		orangeDice = buildSingleDice(diceDimension, lowerTableDimension, rightShift);
	}
	
	/**
	 * Method used to build the single label for the die
	 * @param diceDimension				The die dimension
	 * @param lowerTableDimension		The dimension of the lower part of table on the screen to use the correct proportions
	 * @param rightShift				The distance between one die and another one on the screen
	 * @return							The label with the image of the correct die
	 */
	private JLabel buildSingleDice(Dimension diceDimension, Dimension lowerTableDimension, int rightShift) {
		
		Point location = new Point((int) (tableImageDimension.getWidth() + cardZoom.getWidth() + lowerTableDimension.getWidth()*0.58 + rightShift),(int) (lowerTableDimension.getHeight()*0.72));
		JLabel diceLable = new JLabel();
		diceLable.setSize(diceDimension);
		diceLable.setLocation(location);
		mainLayeredPane.add(diceLable, 0);
		return diceLable;
	}
	
	/**
	 * Method used to build all the ban labels
	 */
	private void buildBanLabel() {
		
		bans = new ArrayList<>();
		int deltaX = 0;
		int deltaY = 0;
		Dimension banDimension = new Dimension((int)(tableImageDimension.getWidth()*0.088), (int)(tableImageDimension.getHeight()*0.15));
		bans.add(buildSingleBanLabel(banDimension, deltaX, deltaY));
		deltaX += (int)(tableImageDimension.getWidth() * 0.008 + banDimension.getWidth());
		deltaY += (int)(banDimension.getHeight() * 0.1);
		bans.add(buildSingleBanLabel(banDimension, deltaX, deltaY));
		deltaX += (int)(tableImageDimension.getWidth() * 0.004 + banDimension.getWidth());
		deltaY = 0;
		bans.add(buildSingleBanLabel(banDimension, deltaX, deltaY ));

	}
	
	/**
	 * Method used to build a single ban label
	 * @param banDimension		The dimesion of the ban
	 * @param rightShift		The horizontal gap between two bans on the screen
	 * @param downShift			The vertical gap between the bans
	 * @return					The CardLabel with the image of the ban
	 */
	private CardLabel buildSingleBanLabel(Dimension banDimension, int rightShift, int downShift) {
		
		CardLabel ban = new CardLabel((int)(tableImageDimension.getWidth()*0.172 + rightShift), (int)(tableImageDimension.getHeight()*0.835 + downShift), banDimension, cardZoom);
		mainLayeredPane.add(ban, 0);
		return ban;
	}
	
	/**
	 * Method used to resize the image for a JLabel ImageIcon
	 * @param imageToResize		The picture to resize with a new Dimension
	 * @param newDimension		The dimension of the new picture
	 * @return					An ImageIcon with the new dimensioned picture
	 */
	private ImageIcon resizeImage(BufferedImage imageToResize, Dimension newDimension){
		Image imageResized = null;
		if(imageToResize != null){
			int width = (int)newDimension.getWidth();
			int height = (int)newDimension.getHeight();
			int scaledHeight = height;
			int scaledWidth= (int)(width*scaledHeight/(height));
			imageResized = imageToResize.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		}
		
		return new ImageIcon(imageResized);
	}
	
	/**
	 * For each position check if contains the point (x,y), if so change the imageIcon
	 */
	@Override
	public boolean handleEvent(int x, int y, DraggableComponent familiarMoving) {
	
		int actionValue = 0;
		this.movingFamiliar = familiarMoving;
		FamiliarColor color = familiarMoving.getFamiliarColor();
		for (JLabel position : greenTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				actionValue = 1 + greenTowerForFamiliar.indexOf(position)*2;
				if( color != null)
					position.setIcon(resizeImage(familiarMoving.getImage(), position.getSize()));
				createNewMove(ActionType.TAKE_GREEN, color, greenTowerForFamiliar.indexOf(position), actionValue, familiarMoving);
				return true;
			}
		}
		for (JLabel position : yellowTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				actionValue = 1 + yellowTowerForFamiliar.indexOf(position)*2;
				if(color != null)
					position.setIcon(resizeImage(familiarMoving.getImage(), position.getSize()));
				createNewMove(ActionType.TAKE_YELLOW, color, yellowTowerForFamiliar.indexOf(position),actionValue, familiarMoving);
				return true;
			}
		}
		for (JLabel position : blueTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				actionValue = 1 + blueTowerForFamiliar.indexOf(position)*2;
				if(color != null)
					position.setIcon(resizeImage(familiarMoving.getImage(), position.getSize()));
				createNewMove( ActionType.TAKE_BLUE, color, blueTowerForFamiliar.indexOf(position),actionValue, familiarMoving);
				return true;
			}
		}
		for (JLabel position : violetTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				actionValue = 1 + violetTowerForFamiliar.indexOf(position)*2;
				if(color != null)
					position.setIcon(resizeImage(familiarMoving.getImage(), position.getSize()));
				createNewMove(ActionType.TAKE_VIOLET, color, violetTowerForFamiliar.indexOf(position),actionValue, familiarMoving);
				return true;
			}
		}
		if(containsPoint(council, x, y) && color != null){
			council.placeFamiliar(familiarMoving.getImage());
			actionValue = 1;
			createNewMove( ActionType.COUNCIL, color, council.getLastIndex(), actionValue, familiarMoving);
			return true;
		}
		
		if(containsPoint(firstProduce, x, y)){
			if(color != null)
				firstProduce.setIcon(resizeImage(familiarMoving.getImage(), firstProduce.getSize()));
			actionValue = 1;
			createNewMove(ActionType.PRODUCE, color, 0, actionValue, familiarMoving);
			return true;
		}
		
		if( secondProduce.isActivated() && containsPoint(secondProduce, x, y) && firstProduce.getIcon() != null && color != null){
			secondProduce.placeFamiliar(familiarMoving.getImage());
			actionValue = -3;
			createNewMove(ActionType.PRODUCE, color, (secondProduce.getLastIndex()+1), actionValue, familiarMoving);
			return true;
		}

		if(containsPoint(firstYield, x, y)){
			if(color != null)
				firstYield.setIcon(resizeImage(familiarMoving.getImage(), firstYield.getSize()));
			actionValue = 1;
			createNewMove(ActionType.YIELD, color, 0, actionValue, familiarMoving);
			return true;
		}
		
		if( secondYield.isActivated() && containsPoint(secondYield, x, y) && firstYield.getIcon() != null && color != null){
			secondYield.placeFamiliar(familiarMoving.getImage());
			actionValue = -3;
			createNewMove(ActionType.YIELD, color, (secondYield.getLastIndex()+1), actionValue, familiarMoving);
			return true;
		}
		
		for (JLabel position : market) {
			if( containsPoint(position, x, y) ){
				if(color != null)
					position.setIcon(resizeImage(familiarMoving.getImage(), position.getSize()));
				actionValue = 1;
				createNewMove( ActionType.MARKET, color, market.indexOf(position),actionValue , familiarMoving);
				return true;
			}
		}
		this.movingFamiliar = null;
		return false;
	}
	
	/**
	 * If the position is empty, discover if has been pointed
	 * @param label		The interested point
	 * @param x			The horizontal value
	 * @param y			The vertical value
	 * @return			True if (x,y) point is in the label, otherwise False
	 */
	private boolean containsPoint(JLabel label, int x, int y){
		if(label.getIcon() == null || player.canPositioningEverywhere() || movingFamiliar.getFamiliarColor() == null){
			Point p = label.getLocationOnScreen();
			if(p.getX() < x && x < p.getX()+label.getWidth()
					&& p.getY() < y && y < p.getY() + label.getHeight())
				return true;
		}
		return false;
	}
	
	/**
	 * Method used to add a player in this GUIView
	 */
	@Override
	public void addPlayer(String playerID) {
		super.addPlayer(playerID);
		resourceWindow.setPlayer(this.player);
		resourceWindow.update();
	}
	
	/**
	 * Method used to set to the correct player his resources
	 */
	@Override
	public void setResources(HashMap<Resource, Integer> resources, String playerID) {
		super.setResources(resources, playerID);
		if(hasToAnswer(playerID))
			resourceWindow.update();
	}
	
	/**
	 * Method used to set the black die picture value when a message come from the controller
	 */
	@Override
	public void setBlackDie(int value) {
		super.setBlackDie(value);
		//Update the image value for the blackDice
		try {
			blackDice.setIcon(resizeImage(imageLoader.loadBlackDieImage(new Integer(value)), blackDice.getSize()));
		} catch (IOException e) {
			logger.error("Black Dice Image not found!");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to set the orange die picture value when a message come from the controller
	 */
	@Override
	public void setOrangeDie(int value) {
		super.setOrangeDie(value);
		//Update the image value for the orangeDice
		try {
			orangeDice.setIcon(resizeImage(imageLoader.loadOrangeDieImage(new Integer(value)), orangeDice.getSize()));
		} catch (IOException e) {
			logger.error("Orange Dice Image not found!");
			logger.info(e);
		}

	}
	
	/**
	 * Method used to set the white die picture value when a message come from the controller
	 */
	@Override
	public void setWhiteDie(int value) {
		super.setWhiteDie(value);
		//Update the image value for the whiteDice
		try {
			whiteDice.setIcon(resizeImage(imageLoader.loadWhiteDieImage(new Integer(value)), whiteDice.getSize()));
		} catch (IOException e) {
			logger.error("White Dice Image not found!");
			logger.info(e);
		}

	}
	
	/**
	 * Ask the Player if he wants to increment the actual move and set the increment
	 * @param type				The type of the player's action
	 * @param familiarColor		The color of the moved familiar
	 * @param position			The position where the familiar is
	 * @param actionValue		The value of the player's action
	 * @param familiarMoving	The component which is moved
	 */
	private void createNewMove( ActionType type, FamiliarColor familiarColor, int position, int actionValue, DraggableComponent familiarMoving){
		this.movingFamiliar = familiarMoving;
		IncrementWindow dialog = new IncrementWindow(this, type, familiarColor, position, actionValue, player.getResource(Resource.SLAVE));
		dialog.run();
	}
	
	/**
	 * Restore the position of the moved Familiar
	 * @param type		The type of the action
	 * @param position	The position to restore
	 */
	public void cancelMove(ActionType type, int position){

		//Restore the position of the moved Familiar
		logger.debug("Starting to delete the previous move");

		if(movingFamiliar != null){
			movingFamiliar.resetFamiliar();
			if(movingFamiliar.getFamiliarColor() != null){
				switch (type) {
				case TAKE_GREEN:
					greenTowerForFamiliar.get(position).setIcon(null);
					break;
				case TAKE_YELLOW:
					yellowTowerForFamiliar.get(position).setIcon(null);
					break;
				case TAKE_BLUE:
					blueTowerForFamiliar.get(position).setIcon(null);
					break;
				case TAKE_VIOLET:
					violetTowerForFamiliar.get(position).setIcon(null);
					break;
				case MARKET:
					market.get(position).setIcon(null);
					break;
				case YIELD:
					if( !secondYield.isActivated() || secondYield.isEmpty())
						firstYield.setIcon(null);
					else 
						secondYield.resetLastFamiliar();
					break;
				case PRODUCE:
					if(!secondProduce.isActivated() || secondProduce.isEmpty()) 
						firstProduce.setIcon(null);
					else 
						secondProduce.resetLastFamiliar();
					break;
				case COUNCIL:
					council.resetLastFamiliar();
					break;
					
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * Method used to enable the player move when a message come from the Controller
	 */
	private void enableMove(){
		enableFamiliar(blackFamiliar, player.getFamiliar(FamiliarColor.BLACK), true);
		enableFamiliar(whiteFamiliar, player.getFamiliar(FamiliarColor.WHITE), true);
		enableFamiliar(orangeFamiliar, player.getFamiliar(FamiliarColor.ORANGE), true);
		enableFamiliar(neutralFamiliar, player.getFamiliar(FamiliarColor.NEUTRAL), true);
		
	}
	
	/**
	 * Method used to disable the player move when a message come from the Controller
	 */
	public void disableFamiliarMove(){

		enableFamiliar(blackFamiliar, player.getFamiliar(FamiliarColor.BLACK), false);
		enableFamiliar(whiteFamiliar, player.getFamiliar(FamiliarColor.WHITE), false);
		enableFamiliar(orangeFamiliar, player.getFamiliar(FamiliarColor.ORANGE), false);
		enableFamiliar(neutralFamiliar, player.getFamiliar(FamiliarColor.NEUTRAL), false);
	}
	
	/**
	 * Private method used to set the status to a component
	 * @param familiarIcon	The interested component
	 * @param familiar		The familiar to check
	 * @param status		the boolean that represents the status
	 */
	private void enableFamiliar(DraggableComponent familiarIcon, Familiar familiar, boolean status){
		if(!familiar.isPositioned())
			familiarIcon.setCanMove(status);
	}
	
	/**
	 * Method used to restore the status of all the familiars
	 */
	private void restoreFamiliar(){
		blackFamiliar.resetFamiliar();
		whiteFamiliar.resetFamiliar();
		orangeFamiliar.resetFamiliar();
		neutralFamiliar.resetFamiliar();
	}
	
	/**
	 * Method to reset the icon of all the familiars labels
	 * @param positions		The list of JLabel to restore the icon
	 */
	private void resetFamiliarPositions(List<JLabel> positions){
		for (JLabel position : positions) {
			position.setIcon(null);
		}
	}
	
	/**
	 * Method used to create the correct table when the match starts
	 */
	@Override
	public void createTable(List<String> playersID) {
		int playersNumber = playersID.size();
		super.createTable(playersID);
		//Hide the table parts according to the number of players
		if(playersNumber == 2){
			coverMarket();
			coverYieldAndProduct();
		}
		if(playersNumber == 3){
			coverMarket();
		}
		
		mainLayeredPane.moveToFront(blackFamiliar);
		mainLayeredPane.moveToFront(whiteFamiliar);
		mainLayeredPane.moveToFront(orangeFamiliar);
		mainLayeredPane.moveToFront(neutralFamiliar);
	}
	
	/**
	 * Method used to cover the market positions when the players aren't enough
	 */
	private void coverMarket(){
		
		market.remove(2).setEnabled(false);
		market.remove(2).setEnabled(false);
		Dimension coverDimension = new Dimension((int)(lowTableDimension.getWidth() * 0.11), (int)(lowTableDimension.getHeight() * 0.35));
		JLabel market1 = new JLabel();
		market1.setSize(coverDimension);
		market1.setLocation((int)(tableImageDimension.getWidth() + cardZoom.getSize().getWidth() + lowTableDimension.getWidth() * 0.815), (int)(lowTableDimension.getHeight() * 0.225));
		try {
			market1.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//Others//marketCover.png")) , market1.getSize()));
		} catch (IOException e) {
			logger.error("Market position covered but image not found!");
			logger.info(e);
		}
		mainLayeredPane.add(market1, 1);
		
		coverDimension = new Dimension((int)(lowTableDimension.getWidth() * 0.125), (int)(lowTableDimension.getHeight() * 0.42));
		JLabel market2 = new JLabel();
		market2.setSize(coverDimension);
		market2.setLocation((int)(tableImageDimension.getWidth() + cardZoom.getSize().getWidth() + lowTableDimension.getWidth() * 0.8955), (int)(lowTableDimension.getHeight() * 0.42));
		try {
			market2.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//Others//marketCover2.png")) , market2.getSize()));
		} catch (IOException e) {
			logger.error("Market position covered but image not found!");
			logger.info(e);
		}
		mainLayeredPane.add(market2, 1);
	}
	
	/**
	 * Method used to cover the market positions when the players aren't enough
	 */
	private void coverYieldAndProduct() {
		
		secondYield.disableContainer();
		secondYield.setEnabled(false);
		mainLayeredPane.remove(secondYield);
		secondProduce.disableContainer();
		secondProduce.setEnabled(false);
		mainLayeredPane.remove(secondProduce);
		mainLayeredPane.updateUI();
		
		Dimension coverDimension = new Dimension((int)(lowTableDimension.getWidth() * 0.269), (int)(lowTableDimension.getHeight() * 0.3495));
		JLabel product = new JLabel();
		product.setSize(coverDimension);
		product.setLocation((int)(tableImageDimension.getWidth() + cardZoom.getSize().getWidth() + lowTableDimension.getWidth() * 0.1355), (int)(lowTableDimension.getHeight() * 0.222));
		try {
			product.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//Others//yieldAndProductCover.png")) , product.getSize()));
			product.setIgnoreRepaint(true);
			
		} catch (IOException e) {
			logger.error("Yield and Product position covered but image not found!");
			logger.info(e);
		}
		mainLayeredPane.add(product, 1);
		
		coverDimension = new Dimension((int)(lowTableDimension.getWidth() * 0.3), (int)(lowTableDimension.getHeight() * 0.398));
		JLabel yield = new JLabel();
		yield.setSize(coverDimension);
		yield.setLocation((int)(tableImageDimension.getWidth() + cardZoom.getSize().getWidth() + lowTableDimension.getWidth() * 0.12), (int)(lowTableDimension.getHeight() * 0.58));
		try {
			yield.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//Others//yieldCover.png")) , yield.getSize()));
			yield.setIgnoreRepaint(true);
		} catch (IOException e) {
			logger.error("Yield and Product position covered but image not found!");
			logger.info(e);
		}
		mainLayeredPane.add(yield, 1);
	}
	
	/**
	 * Method used to reset the table when a round is finished
	 */
	@Override
	public void resetTable() {
		super.resetTable();
		restoreFamiliar();
		resetFamiliarPositions(blueTowerForFamiliar);
		resetFamiliarPositions(greenTowerForFamiliar);
		resetFamiliarPositions(violetTowerForFamiliar);
		resetFamiliarPositions(yellowTowerForFamiliar);
		council.resetPosition();
		firstYield.setIcon(null);
		if(secondYield.isActivated())
			secondYield.resetPosition();
		firstProduce.setIcon(null);
		if(secondProduce.isActivated())
			secondProduce.resetPosition();
		resetFamiliarPositions(market);
		
	}
	
	/**
	 * Method used to set a familiar in the blue tower
	 */
	@Override
	public void setFamiliarInBlueTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInBlueTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, blueTowerForFamiliar.get(position));
		}		
	}
	
	/**
	 * Method used to set a familiar in the green tower
	 */
	@Override
	public void setFamiliarInGreenTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInGreenTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, greenTowerForFamiliar.get(position));
		}		
	}
	
	/**
	 * Method used to set a familiar in the yellow tower
	 */
	@Override
	public void setFamiliarInYellowTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInYellowTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, yellowTowerForFamiliar.get(position));
		}		
	}
	
	/**
	 * Method used to set a familiar in the violet tower
	 */
	@Override
	public void setFamiliarInVioletTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInVioletTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, violetTowerForFamiliar.get(position));
		}		
	}
	
	/**
	 * Method used to set a familiar in the market
	 */
	@Override
	public void setFamiliarInMarket(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInMarket(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, market.get(position));
		}		
	}
	
	/**
	 * Method used to set a familiar in the yield positions
	 */
	@Override
	public void setFamiliarInYield(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInYield(playerID, color, position);
		if( !hasToAnswer(playerID)){
			if( firstYield.getIcon() == null )
				setOccupied(playerID, color, firstYield);
			else if( secondYield.isActivated() )
				try {
					secondYield.placeFamiliar(imageLoader.loadFamiliarImage(this.indexOfOtherPlayer(playerID)+1, color));
					mainLayeredPane.updateUI();
					logger.debug("Placing familiar in second position");
				} catch (IOException e) {
					logger.error("Image for the Familiar not found!");
					logger.info(e);
				}
		}		
	}
	
	/**
	 * Method used to set a familiar in the product positions
	 */
	@Override
	public void setFamiliarInProduce(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInProduce(playerID, color, position);
		if( !hasToAnswer(playerID)){
			if( firstProduce.getIcon() == null )
				setOccupied(playerID, color, firstProduce);
			else if(secondProduce.isActivated())
				try {
					secondProduce.placeFamiliar(imageLoader.loadFamiliarImage(this.indexOfOtherPlayer(playerID)+1, color));
					mainLayeredPane.updateUI();
					logger.debug("Placing familiar in second position");
				} catch (IOException e) {
					logger.error("Image for the Familiar not found!");
					logger.info(e);
				}		
		}		
	}
	
	/**
	 * Method used to set a familiar in the council positions
	 */
	@Override
	public void setFamiliarInCouncil(String playerID, FamiliarColor color) throws ElementNotFoundException {
		super.setFamiliarInCouncil(playerID, color);
		if( !hasToAnswer(playerID)){
			try{
				council.placeFamiliar(imageLoader.loadFamiliarImage(this.indexOfOtherPlayer(playerID)+1, color));
			} catch (IOException e) {
				logger.error("Image for "+color.toString()+" of the player: "+playerID+" not found!");
				logger.info(e);
			}
		}		
	}
	
	/**
	 * Method used to set the picture of a position when it is occupied
	 * @param playerID		The player who occupied that position
	 * @param color			The color of the familiar the player has used
	 * @param position		The JLabel of the occupied position
	 */
	private void setOccupied(String playerID, FamiliarColor color, JLabel position){
		
		try {
			position.setIcon(resizeImage(imageLoader.loadFamiliarImage(this.indexOfOtherPlayer(playerID)+1, color), position.getSize()));
		}
		catch (IOException e) {
			logger.error("Image for "+color.toString()+" of the player: "+playerID+" not found!");
			logger.info(e);
		}
		catch (ElementNotFoundException e) {
			logger.error(playerID+" not found in arrayList!");
			logger.info(e);		
			}
	}
	
	/**
	 * Method used to set the green cards in the tower when a message come from the Controller
	 */
	@Override
	public void setGreenCards(StaticList<Card> cards) {
		super.setGreenCards(cards);
		placeCards(greenTower, cards);
	}
	
	/**
	 * Method used to set the yellow cards in the tower when a message come from the Controller
	 */
	@Override
	public void setYellowCards(StaticList<Card> cards) {
		super.setYellowCards(cards);
		placeCards(yellowTower, cards);
	}
	
	/**
	 * Method used to set the blue cards in the tower when a message come from the Controller
	 */
	@Override
	public void setBlueCards(StaticList<Card> cards) {
		super.setBlueCards(cards);
		placeCards(blueTower, cards);
	}
	
	/**
	 * Method used to set the violet cards in the tower when a message come from the Controller
	 */
	@Override
	public void setVioletCards(StaticList<Card> cards) {
		super.setVioletCards(cards);
		placeCards(violetTower, cards);
	}
	
	/**
	 * Method used to set the cards in a generic tower when a message come from the Controller
	 */
	private void placeCards(List<CardLabel> tower, StaticList<Card> cards) {
		for(int i=0; i<4; i++){
			BufferedImage cardImage;
			try {
				cardImage = imageLoader.loadCardImage(cards.get(i).getName());
				tower.get(i).placeCard(cardImage);
			} catch (IOException e) {
				logger.error("Image not Found! Probably a wrong name is given or the loader has been misconfigured "+ cards.get(i).getName());
				logger.info(e);
			}
		}
	}
	
	/**
	 * Method used to set a green card to the specify player when a message come from the Controller
	 */
	@Override
	public void setGreenCard(String playerID, int position) throws ElementNotFoundException {

		Player player = searchPlayer(playerID);
		Card card = this.table.getGreenCard(position);
		player.addCard(card);
		if(hasToAnswer(playerID))
			try {
				cardContainer.addGreenCard(imageLoader.loadCardImage(card.getName()));
			} catch (IOException e) {
				logger.error("Image not Found! Probably a wrong name is given or the loader has been misconfigured");
				logger.info(e);
			}
		greenTower.get(position).removeCard();
	}
	
	/**
	 * Method used to set a yellow card to the specify player when a message come from the Controller
	 */
	@Override
	public void setYellowCard(String playerID, int position) throws ElementNotFoundException {

		Player player = searchPlayer(playerID);
		Card card = this.table.getYellowCard(position);
		player.addCard(card);
		if(hasToAnswer(playerID))
			try {
				cardContainer.addYellowCard(imageLoader.loadCardImage(card.getName()));
			} catch (IOException e) {
				logger.error("Image not Found! Probably a wrong name is given or the loader has been misconfigured");
				logger.info(e);
			}
		yellowTower.get(position).removeCard();
	}
	
	/**
	 * Method used to set a blue card to the specify player when a message come from the Controller
	 */
	@Override
	public void setBlueCard(String playerID, int position) throws ElementNotFoundException {
		
		Player player = searchPlayer(playerID);
		Card card = this.table.getBlueCard(position);
		player.addCard(card);
		if(hasToAnswer(playerID)){
			try {
				cardContainer.addBlueCard(imageLoader.loadCardImage(card.getName()));
			} catch (IOException e) {
				logger.error("Image not Found! Probably a wrong name is given or the loader has been misconfigured");
				logger.info(e);
			}
		}
		blueTower.get(position).removeCard();
	}
	
	/**
	 * Method used to set a violet card to the specify player when a message come from the Controller
	 */
	@Override
	public void setVioletCard(String playerID, int position) throws ElementNotFoundException {

		Player player = searchPlayer(playerID);
		Card card = this.table.getVioletCard(position);
		player.addCard(card);
		if(hasToAnswer(playerID))
			try {
				cardContainer.addVioletCard(imageLoader.loadCardImage(card.getName()));
			} catch (IOException e) {
				logger.error("Image not Found! Probably a wrong name is given or the loader has been misconfigured");
				logger.info(e);
			}
		violetTower.get(position).removeCard();
	}

	/**
	 * Ask the Player to choose a BonusBar from the given List
	 */
	@Override
	protected void chooseBonusBar(List<BonusBar> bonusBarList) {
		
		BonusBarRequestDialog dialog = new BonusBarRequestDialog(this, bonusBarList);
		dialog.run();
	}

	/**
	 * Method used to set the chosen bonus bar to the player
	 */
	@Override
	public void setBonusBarChoice(List<BonusBar> bonusBars, int choice) {
		try {
			cardContainer.addBonusBarLabel(imageLoader.loadBonusBarImage(bonusBars.get(choice).getName()));
		} catch (IOException e) {
			logger.error("Image for BonusBar not found!");
			logger.info(e);
		}
		super.setBonusBarChoice(bonusBars, choice);
	}
	
	/**
	 * Ask the Player to choose a LeaderCard from the given List
	 */
	@Override
	protected void chooseLeaderCard(List<LeaderCard> leaderCardList) {
		
		try {
			LeaderCardChooseDialog dialog = new LeaderCardChooseDialog(this, leaderCardList);
			dialog.run();
		} catch (IOException e) {
			logger.error("Error in open the image conversion file");
			logger.info(e);
		}
	}

	/**
	 * Method used to chose the wanted council privilege from the message
	 */
	@Override
	protected void chooseCouncilConversion(CouncilRequest message) {
		CouncilRequestDialog dialog = new CouncilRequestDialog(this, message);
		dialog.run();
	}

	/**
	 * Enable the Player to perform a new Move, if it is a retransmission cancel the precedent
	 */
	@Override
	protected void choosePlayerMove(ActionPrototype prototype, boolean isRetrasmission) {
		//Enable the Player to perform a new Move, if is a retransmission then cancel the precedent
		logger.debug("Starting procedure for a new move, old move = "+ nextMove);

		enableSkipButton();
		
		if(isRetrasmission && nextMove != null){
			logger.debug("Deleting previous move");
			cancelMove(nextMove.getActionType(), nextMove.getPosition());
		}
		
		if(prototype != null){
			logger.debug("Showing action prototype and anabling bonus move");
			ShowBonusAction dialog = new ShowBonusAction(this, prototype);
			dialog.run();
			buildBonusFamiliar();
		}
		else{
			logger.debug("Enabling move");
			enableMove();
		}
		logger.debug("Updating UI");
		mainLayeredPane.updateUI();
		
	}
	
	/**
	 * Method used to build the "skip move" button on the screen
	 */
	private void buildSkipMoveButton(){
		skipMove = new JButton("Skip Move");
		skipMove.setLocation((int)(tableImageDimension.getWidth() * 0.01), (int)(tableImageDimension.getHeight() * 0.91));
		skipMove.setSize((int)(tableImageDimension.getWidth() * 0.15), (int)(tableImageDimension.getHeight() * 0.05));
		skipMove.setFont(new Font("Papyrus", Font.ITALIC, (int)(skipMove.getWidth()*0.16)));
		mainLayeredPane.add(skipMove, 0);
		skipMove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Send to the GameLogic an emptyMove
				setEmptyMove();
				nextMove = null;
				disableMove();
			}
		});
		disableSkipButton();
	}
	
	/**
	 * Method used to build the "show leader card" button on the screen
	 */
	private void buildLeaderCardsButton(){
		leaderCardButton = new JButton("Leader Cards");
		leaderCardButton.setLocation((int)(tableImageDimension.getWidth() * 0.01), (int)(tableImageDimension.getHeight() * 0.84));
		leaderCardButton.setSize(skipMove.getSize());
		leaderCardButton.setFont(new Font("Papyrus", Font.ITALIC, (int)(leaderCardButton.getWidth()*0.13)));
		mainLayeredPane.add(leaderCardButton, 0);
		leaderCardButton.addActionListener(new MyActionListener(this));
				
		
	}
	
	/**
	 * Method used to build the "timer" label on the screen
	 */
	private void buildTimerLable(){

		Dimension timerDimension = new Dimension((int)(tableImageDimension.getWidth() * 0.15), (int)(tableImageDimension.getHeight() * 0.07));
		Point timerLocation = new Point((int)(tableImageDimension.getWidth() * 0.01), (int)(tableImageDimension.getHeight() * 0.77));
		timerLable = new TimerLabel(this, timerDimension, timerLocation, MOVE_SECONDS);
		timerLable.setVisible(true);
		mainLayeredPane.add( timerLable, 0);
	}
	
	/**
	 * Private class used to implement the actionListener when a keyboard key is pressed
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class MyActionListener implements ActionListener {

		private GUIView view;
		
		public MyActionListener(GUIView view) {
			this.view = view;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				LeaderCardShowDialog dialog = new LeaderCardShowDialog(view);
				dialog.run();
			} catch (IOException e1) {
				logger.error("Error in loading the leader cards imager");
				logger.info(e1);
			}
		}
		
	}
	
	/**
	 * Method used to enable the skip button 
	 */
	private void enableSkipButton(){
		skipMove.setEnabled(true);
		skipMove.setVisible(true);
		
	}
	
	/**
	 * Method used to disable the skip button 
	 */
	private void disableSkipButton(){
		skipMove.setEnabled(false);
		skipMove.setVisible(false);	
	}
	
	/**
	 * Method used to build the bonus familiar when the player has a bonus action
	 */
	private void buildBonusFamiliar(){
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.56 + blackFamiliar.getWidth() * 1.1 * 4);
		int deltaY = (int)(tableImageDimension.getHeight()*0.93);
		try {
			bonusFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadBonusFamiliarImage(), null);
			bonusFamiliar.enableListener();
			bonusFamiliar.setTable(this);
			bonusFamiliar.setVisible(true);
			mainLayeredPane.add(bonusFamiliar, 0);
			bonusFamiliar.setCanMove(true);
		} catch (IOException e) {
			logger.error("Bonus Familiar Image not found!");
			logger.info(e);
		}
	}
	
	/**
	 * Method used to set the new move for the player
	 */
	@Override
	public void setNewMove(PlayerMove move) {
		super.setNewMove(move);
		disableMove();
		this.nextMove = move;
	}
	
	/**
	 * Method used to load from file the picture of the 3 bans for this match
	 */
	@Override
	public void setGameBans(BanMessage message) {
		try{
			bans.get(0).placeCard(imageLoader.loadBanImage(new Integer(0), new Integer(message.getIndexOfFirstBan())));
			bans.get(1).placeCard(imageLoader.loadBanImage(new Integer(1), new Integer(message.getIndexOfSecondBan())));
			bans.get(2).placeCard(imageLoader.loadBanImage(new Integer(2), new Integer(message.getIndexOfThirdBan())));
		}
		catch (IOException e) {
			logger.error("Ban Image not Found!");
			logger.info(e);
		}
		super.setGameBans(message);
	}
	
	/**
	 * Method used to set a ban to the specify player
	 */
	@Override
	public void setBanToPlayer(String playerID, int banPeriod) throws ElementNotFoundException {
		super.setBanToPlayer(playerID, banPeriod);
		logger.debug("Setting the ban to the current player");
		if(hasToAnswer(playerID)){
			bans.get(banPeriod).setBorder(new LineBorder(Color.RED, (int)(tableImageDimension.getWidth()*0.007)));
			mainLayeredPane.updateUI();
		}
	}
	
	/**
	 * Ask to the Player if he wants to pay for the current Period Ban
	 */
	@Override
	protected void chooseIfPayBan(int banPeriod) {
		if(bans.get(banPeriod) != null){
			Dimension banWindowDimension = new Dimension((int)(mainFrame.getWidth()*0.7),(int)(mainFrame.getHeight()*0.48) );
			Point banWindowLocation = new Point((int)(tableImageDimension.getWidth()*0.4),(int)(tableImageDimension.getHeight()*0.4) );
			PayBanDialog dialog = new PayBanDialog(this, banWindowDimension ,banWindowLocation, bans.get(banPeriod).getCardImage(), banPeriod );
			dialog.run();
		}
	}

	@Override
	protected void answerCardRequest(CardRequest message) {
		// Ask to the player to choose from the given card request
		CardRequestDialog dialog = new CardRequestDialog(this, message);
		dialog.run();
	}

	/**
	 * Ask to the Player what color to choose for the leaderCard Request
	 */
	@Override
	protected void chooseFamiliarColor(LeaderFamiliarRequest message) {
		LeaderFamiliarRequestDialog dialog = new LeaderFamiliarRequestDialog(this, message);
		dialog.run();
	}

	@Override
	protected void notifyLeaderCardActivation() {
		// Nothings to do currently
		
	}

	@Override
	protected void notifyLeaderCardDiscard() { 
		// Nothings to do currently
		
	}

	/**
	 * Show a window asking the Player username
	 */
	@Override
	public void askNewPlayerID() {
		LoginWindow login = new LoginWindow(this, "Used ID yet chosen by another player");
		login.run();
	}

	/**
	 * Restore the Player cards and the player ban when he tries to reconnect
	 */
	@Override
	protected void reconnect(ReconnectMessage message) {
		try{	
			for (Card card : message.getBlue()) {
				cardContainer.addBlueCard(imageLoader.loadCardImage(card.getName()));
			}
			for (Card card : message.getYellow()) {
				cardContainer.addYellowCard(imageLoader.loadCardImage(card.getName()));
			}
			for (Card card : message.getViolet()) {
				cardContainer.addVioletCard(imageLoader.loadCardImage(card.getName()));
			}
			for (Card card : message.getGreen()) {
				cardContainer.addGreenCard(imageLoader.loadCardImage(card.getName()));
			}
		}
		catch (IOException e) {
			logger.error("Image of Card not found");
			logger.info(e);
		}
		
		//Restore the Player  ban
		try{
			bans.get(0).placeCard(imageLoader.loadBanImage(new Integer(0), new Integer(message.getFirstBanIndex())));
			bans.get(1).placeCard(imageLoader.loadBanImage(new Integer(1), new Integer(message.getSecondBanIndex())));
			bans.get(2).placeCard(imageLoader.loadBanImage(new Integer(2), new Integer(message.getThirdBanIndex())));
		}
		catch (IOException e) {
			logger.error("Ban Image not Found!");
			logger.info(e);
		}
		
		
	}
	
	/**
	 * Method used to ask the next move of the player
	 */
	@Override
	public void askPlayerMove(PlayerToken message) {
		super.askPlayerMove(message);
		if(!hasToAnswer(message.getPlayerID())){
			disableMove();
			timerLable.showPlayerPlaying(message.getPlayerID());
			mainLayeredPane.updateUI();
		}
	}
	
	/**
	 * Ask to the Player if he wants to perform a new Action and start the Timer
	 */
	@Override
	protected void askIfWantToPlay(PlayerToken moveToken) {
		this.choosePlayerMove(moveToken.getActionPrototype(), moveToken.isRetrasmission());
		if(moveToken.isRetrasmission())
			timerLable.restartTimer();
		else
			timerLable.startTimer();

	}

	/**
	 * Method used to not allow the player to move
	 */
	public void disableMove( ){
		
		disableFamiliarMove();
		disableSkipButton();
		if(bonusFamiliar != null){
			bonusFamiliar.setVisible(false);
			bonusFamiliar.setIcon(null);
			bonusFamiliar.setEnabled(false);
			mainLayeredPane.remove(bonusFamiliar);
			mainLayeredPane.updateUI();
		}
		timerLable.resetTimer();
	}
		
	/**
	 * Open a window to show the game final result
	 */
	@Override
	protected void showResult(List<String> finalChart) {
		
		Dimension resultWindowDimension = new Dimension((int)(mainFrame.getWidth()*0.7),(int)(mainFrame.getHeight()*0.48) );
		Point resultWindowLocation = new Point((int)(tableImageDimension.getWidth()*0.4),(int)(tableImageDimension.getHeight()*0.4) );
		ResultWindow dialog = new ResultWindow(this, finalChart, resultWindowDimension, resultWindowLocation );
		dialog.run();
	}
	
	/**
	 * Getter for the Main Window of the application
	 * 
	 * @return		The main JFrame of the app
	 */
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	/**
	 * Getter for the current player of this GUIView
	 * @return	The player of this GUIView
	 */
	public Player getPlayer() {
		return this.player;
	}

}
