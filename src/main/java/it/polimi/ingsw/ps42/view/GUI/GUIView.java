package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
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
import it.polimi.ingsw.ps42.view.GUI.dialog.BonusBarRequestDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.CardRequestDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.CouncilRequestDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.LeaderCardChooseDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.LeaderCardShowDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.PayBanDialog;
import it.polimi.ingsw.ps42.view.GUI.dialog.ResultWindow;
import it.polimi.ingsw.ps42.view.GUI.dialog.ShowBonusAction;

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
	
	public GUIView() throws IOException {
		
		super();
		PropertyConfigurator.configure("Logger//Properties//client_log.properties");
		imageLoader = new ImageLoader("Resource//Configuration//imagePaths.json");
		timerLoader = new TimerLoader("Resource//Configuration//timers.json");
		MOVE_SECONDS = timerLoader.getPlayerMoveTimer() ;
		initialize();
		mainFrame.setVisible(true);
		
	}
	
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
		buildDicePositions(mainLayeredPane, lowTableDimension);
		
		//Add the Ban's Labels 
		buildBanLabel(mainLayeredPane);

		//Set the Cards taken container
		Dimension containerDimension = new Dimension((int)(rightPanelDimension.getWidth()),(int) (rightPanelDimension.getHeight()-cardZoom.getHeight()));
		Point containerLocation = new Point((int)leftPaneDimension.getWidth(), (int)cardZoom.getHeight());
		cardContainer = new CardContainer(containerDimension, containerLocation, cardZoom, ImageIO.read(new File("Resource//Images//Others//cardContainer.jpg")), cardDimension);
		mainLayeredPane.add(cardContainer, -1);
		
		//Build the main Familiar positions
		buildCardPosition(cardDimension, mainLayeredPane);
		
		buildFamiliarStartingPositions(mainLayeredPane);
		
		buildFamiliarMovePositions(mainLayeredPane);
		
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
	 * @param mainPane Pane where the Card will be placed
	 * @throws IOException 
	 */
	private void buildCardPosition(Dimension cardDimension, JLayeredPane mainPane) throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.022);
		int deltaY = (int)(tableImageDimension.getHeight()*0.03);

		greenTower = new LinkedList<>();
		blueTower = new LinkedList<>();
		violetTower = new LinkedList<>();
		yellowTower = new LinkedList<>();
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			greenTower.addFirst(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			blueTower.addFirst(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			yellowTower.addFirst(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			violetTower.addFirst(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}

	}
	

	/**
	 * 
	 * @param mainPane
	 * @throws IOException
	 */
	private void buildFamiliarStartingPositions(JLayeredPane mainPane) throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.56 );
		int deltaY = (int)(tableImageDimension.getHeight()*0.93);
		
		blackFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage( 0, FamiliarColor.BLACK), FamiliarColor.BLACK);
		blackFamiliar.enableListener();
		blackFamiliar.setTable(this);
		mainPane.add(blackFamiliar, 2);
		
		int border = (int)(blackFamiliar.getWidth()*1.1); 
		deltaX += border;
		whiteFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage( 0, FamiliarColor.WHITE), FamiliarColor.WHITE);
		whiteFamiliar.enableListener();
		whiteFamiliar.setTable(this);
		mainPane.add(whiteFamiliar, 2);
		
		deltaX += border;
		orangeFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage(0, FamiliarColor.ORANGE), FamiliarColor.ORANGE);
		orangeFamiliar.enableListener();
		orangeFamiliar.setTable(this);
		mainPane.add(orangeFamiliar, 2);
		
		deltaX += border;
		neutralFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadFamiliarImage(0, FamiliarColor.NEUTRAL), FamiliarColor.NEUTRAL);
		neutralFamiliar.enableListener();
		neutralFamiliar.setTable(this);
		mainPane.add(neutralFamiliar, 2);
		
	}
	
	/**
	 * 
	 * @param mainPane
	 * @throws IOException 
	 */
	private void buildFamiliarMovePositions(JLayeredPane mainPane) throws IOException{
		
		//Build the Tower related familiarMove positions
		greenTowerForFamiliar = new LinkedList<>();
		yellowTowerForFamiliar = new LinkedList<>();
		blueTowerForFamiliar = new LinkedList<>();
		violetTowerForFamiliar = new LinkedList<>();

		int deltaX = (int)(tableImageDimension.getWidth()*0.172);
		Dimension positionDimension = new Dimension((int)(tableImageDimension.getWidth()*0.06), (int)(tableImageDimension.getHeight()*0.05));
		placeFamiliarPosition(mainPane, positionDimension, greenTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(mainPane, positionDimension, blueTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(mainPane, positionDimension, yellowTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.241);
		placeFamiliarPosition(mainPane, positionDimension, violetTowerForFamiliar, deltaX);		
		
		//Build the Council related familiarMove positions
		Dimension positionContainerDimension = new Dimension((int)(tableImageDimension.getWidth()*0.3), (int)(tableImageDimension.getHeight()*0.12));
		Point positionContainerLocation = new Point((int)(tableImageDimension.getWidth()*0.52), (int)(tableImageDimension.getHeight()*0.76));
		council = new PositionContainer(positionContainerDimension, positionContainerLocation, new Dimension((int)(tableImageDimension.getWidth()*0.06), (int)(tableImageDimension.getHeight()*0.05)));
		mainPane.add(council, 0);
		
		//Build the Market, Yield and Product familiarMove positions
		market = new ArrayList<>();

		int deltaY = (int)(tableImageDimension.getHeight()*0.11);
		firstProduce = placeYieldAndProductPosition(mainPane, positionDimension, firstProduce, deltaY);
		//Build the second Position
		deltaX = (int)(tableImageDimension.getWidth() + cardZoom.getWidth() + lowTableDimension.getWidth()*0.14);
		Dimension secondPositionDimension = new Dimension((int)(tableImageDimension.getWidth()*0.25), (int)(tableImageDimension.getHeight()*0.1));
		secondProduce = new PositionContainer(secondPositionDimension, new Point(deltaX, (int)(deltaY*0.75)), positionDimension);
		mainPane.add(secondProduce, 0);
		
		deltaY += (int)(tableImageDimension.getHeight()*0.145);
		firstYield = placeYieldAndProductPosition(mainPane, positionDimension, firstYield, deltaY);
		//Build the second Position
		secondYield = new PositionContainer(secondPositionDimension, new Point(deltaX, (int)(deltaY * 0.9)), positionDimension);
		mainPane.add(secondYield, 0);
		
		deltaX = (int)(tableImageDimension.getWidth()*1.615 + cardZoom.getWidth() );
		deltaY = (int)(tableImageDimension.getHeight()*0.08);
		placeMarket(positionDimension, deltaX, deltaY, mainPane);
		deltaX += (int)(positionDimension.getWidth()*1.85);
		placeMarket(positionDimension, deltaX, deltaY, mainPane);
		deltaX += (int)(positionDimension.getWidth()*1.75);
		deltaY = (int)(tableImageDimension.getHeight()*0.11);
		placeMarket(positionDimension, deltaX, deltaY, mainPane);
		deltaX += (int)(positionDimension.getWidth()*1.42);
		deltaY += (int)(tableImageDimension.getHeight()*0.085);
		placeMarket(positionDimension, deltaX, deltaY, mainPane);
		
		mainLayeredPane.updateUI();
	}
	
	private void placeMarket(Dimension positionDimension, int x, int y, JLayeredPane mainPane) throws IOException{
		
		JLabel position = new JLabel();
		position.setSize(positionDimension);
		position.setLocation(x, y);
		market.add(position);
		mainPane.add(position, 0);
	}
	
	private JLabel placeYieldAndProductPosition(JLayeredPane mainPane, Dimension positionDimension, JLabel firstPosition, int deltaY) throws IOException{
		
		//Build the first position
		int deltaX = (int)(tableImageDimension.getWidth()*1.03 + cardZoom.getWidth() );
		firstPosition = new JLabel();
		firstPosition.setSize(positionDimension);
		firstPosition.setLocation(deltaX, deltaY);
		mainPane.add(firstPosition, 0);
		deltaX += positionDimension.getWidth();
		return firstPosition;
	}
	
	private void placeFamiliarPosition(JLayeredPane mainPane, Dimension positionDimension, LinkedList<JLabel> tower, int rightShift) throws IOException{
		
		int deltaY = (int)(tableImageDimension.getHeight()*0.081);
		for (int i=0; i<4; i++){
			JLabel familiarPosition = new JLabel();
			familiarPosition.setSize(positionDimension);
			familiarPosition.setLocation(rightShift, deltaY);
			tower.addFirst(familiarPosition);
			mainPane.add(familiarPosition, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.1858);
		}
	}
	
	private void buildDicePositions(JLayeredPane mainPane, Dimension lowerTableDimension ) {
		
		Dimension diceDimension = new Dimension((int)(lowerTableDimension.getWidth()*0.09),(int)(lowerTableDimension.getHeight()*0.21));
		blackDice = buildSingleDice(mainPane, diceDimension, lowerTableDimension, 0);
		int rightShift = (int)(diceDimension.getWidth()*1.29);
		whiteDice = buildSingleDice(mainPane, diceDimension, lowerTableDimension, rightShift);
		rightShift += (int)(diceDimension.getWidth()*1.3);
		orangeDice = buildSingleDice(mainPane, diceDimension, lowerTableDimension, rightShift);
	}
	
	private JLabel buildSingleDice(JLayeredPane mainPane, Dimension diceDimension, Dimension lowerTableDimension, int rightShift) {
		
		Point location = new Point((int) (tableImageDimension.getWidth() + cardZoom.getWidth() + lowerTableDimension.getWidth()*0.58 + rightShift),(int) (lowerTableDimension.getHeight()*0.72));
		JLabel diceLable = new JLabel();
		diceLable.setSize(diceDimension);
		diceLable.setLocation(location);
		mainPane.add(diceLable, 0);
		return diceLable;
	}
	
	private void buildBanLabel(JLayeredPane mainPane) {
		
		bans = new ArrayList<>();
		int deltaX = 0;
		int deltaY = 0;
		Dimension banDimension = new Dimension((int)(tableImageDimension.getWidth()*0.088), (int)(tableImageDimension.getHeight()*0.15));
		bans.add(buildSingleBanLabel(mainPane,banDimension, deltaX, deltaY));
		deltaX += (int)(tableImageDimension.getWidth() * 0.008 + banDimension.getWidth());
		deltaY += (int)(banDimension.getHeight() * 0.1);
		bans.add(buildSingleBanLabel(mainPane,banDimension, deltaX, deltaY));
		deltaX += (int)(tableImageDimension.getWidth() * 0.004 + banDimension.getWidth());
		deltaY = 0;
		bans.add(buildSingleBanLabel(mainPane,banDimension, deltaX, deltaY ));

	}
	
	private CardLabel buildSingleBanLabel(JLayeredPane mainPane, Dimension banDimension, int rightShift, int downShift) {
		
		CardLabel ban = new CardLabel((int)(tableImageDimension.getWidth()*0.172 + rightShift), (int)(tableImageDimension.getHeight()*0.835 + downShift), banDimension, cardZoom);
		mainPane.add(ban, 0);
		return ban;
	}
	
	//Method used to resize the image for a JLabel ImageIcon
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
	
	@Override
	public boolean handleEvent(int x, int y, DraggableComponent familiarMoving) {
	
		//For each position check if contains the point (x,y), if so change the imageIcon
		int actionValue = 0;
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
		return false;
	}
	
	private boolean containsPoint(JLabel label, int x, int y){
		if(label.getIcon() == null){
			//If the position is empty, discover if has been pointed
			Point p = label.getLocationOnScreen();
			if(p.getX() < x && x < p.getX()+label.getWidth()
					&& p.getY() < y && y < p.getY() + label.getHeight())
				return true;
		}
		return false;
	}
	
	@Override
	public void addPlayer(String playerID) {
		super.addPlayer(playerID);
		resourceWindow.setPlayer(this.player);
		resourceWindow.update();
	}
	
	@Override
	public void setResources(HashMap<Resource, Integer> resources, String playerID) {
		super.setResources(resources, playerID);
		if(hasToAnswer(playerID))
			resourceWindow.update();
	}
	
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
	
	private void createNewMove( ActionType type, FamiliarColor familiarColor, int position, int actionValue, DraggableComponent familiarMoving){
		//Ask the Player if he wants to increment the actual move and set the increment
		this.movingFamiliar = familiarMoving;
		new IncrementWindow(this, type, familiarColor, position, actionValue, player.getResource(Resource.SLAVE));
		
	}
	
	public void cancelMove(ActionType type, int position){
		//Restore the position of the moved Familiar
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
	
	private void enableMove(){
		enableFamiliar(blackFamiliar, player.getFamiliar(FamiliarColor.BLACK), true);
		enableFamiliar(whiteFamiliar, player.getFamiliar(FamiliarColor.WHITE), true);
		enableFamiliar(orangeFamiliar, player.getFamiliar(FamiliarColor.ORANGE), true);
		enableFamiliar(neutralFamiliar, player.getFamiliar(FamiliarColor.NEUTRAL), true);
		
	}
	
	public void disableFamiliarMove(){

		enableFamiliar(blackFamiliar, player.getFamiliar(FamiliarColor.BLACK), false);
		enableFamiliar(whiteFamiliar, player.getFamiliar(FamiliarColor.WHITE), false);
		enableFamiliar(orangeFamiliar, player.getFamiliar(FamiliarColor.ORANGE), false);
		enableFamiliar(neutralFamiliar, player.getFamiliar(FamiliarColor.NEUTRAL), false);
	}
	
	private void enableFamiliar(DraggableComponent familiarIcon, Familiar familiar, boolean status){
		if(!familiar.isPositioned())
			familiarIcon.setCanMove(status);
	}
	
	private void restoreFamiliar(){
		blackFamiliar.resetFamiliar();
		whiteFamiliar.resetFamiliar();
		orangeFamiliar.resetFamiliar();
		neutralFamiliar.resetFamiliar();
	}
	
	private void resetFamiliarPositions(List<JLabel> positions){
		for (JLabel position : positions) {
			position.setIcon(null);
		}
	}
	
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
	
	@Override
	public void setFamiliarInBlueTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInBlueTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, blueTowerForFamiliar.get(position));
		}		
	}
	
	@Override
	public void setFamiliarInGreenTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInGreenTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, greenTowerForFamiliar.get(position));
		}		
	}
	
	@Override
	public void setFamiliarInYellowTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInYellowTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, yellowTowerForFamiliar.get(position));
		}		
	}
	
	@Override
	public void setFamiliarInVioletTower(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInVioletTower(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, violetTowerForFamiliar.get(position));
		}		
	}
	
	@Override
	public void setFamiliarInMarket(String playerID, FamiliarColor color, int position) throws ElementNotFoundException {
		super.setFamiliarInMarket(playerID, color, position);
		if( !hasToAnswer(playerID)){
			setOccupied(playerID, color, market.get(position));
		}		
	}
	
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
	
	@Override
	public void setGreenCards(StaticList<Card> cards) {
		super.setGreenCards(cards);
		placeCards(greenTower, cards);
	}
	
	@Override
	public void setYellowCards(StaticList<Card> cards) {
		super.setYellowCards(cards);
		placeCards(yellowTower, cards);
	}
	@Override
	public void setBlueCards(StaticList<Card> cards) {
		super.setBlueCards(cards);
		placeCards(blueTower, cards);
	}
	@Override
	public void setVioletCards(StaticList<Card> cards) {
		super.setVioletCards(cards);
		placeCards(violetTower, cards);
	}
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

	@Override
	protected void chooseBonusBar(List<BonusBar> bonusBarList) {
		//Ask the Player to choose a BonusBar from the given List
		BonusBarRequestDialog dialog = new BonusBarRequestDialog(this, bonusBarList);
		dialog.run();
	}

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
	
	@Override
	protected void chooseLeaderCard(List<LeaderCard> leaderCardList) {
		// Ask the Player to choose a LeaderCard from the given List
		try {
			LeaderCardChooseDialog dialog = new LeaderCardChooseDialog(this, leaderCardList);
			dialog.run();
		} catch (IOException e) {
			logger.error("Error in open the image conversion file");
			logger.info(e);
		}
	}

	@Override
	protected void chooseCouncilConversion(CouncilRequest message) {
		CouncilRequestDialog dialog = new CouncilRequestDialog(this, message);
		dialog.run();
	}

	@Override
	protected void choosePlayerMove(ActionPrototype prototype, boolean isRetrasmission) {
		//Enable the Player to perform a new Move, if is a retrasmission then cancel the precedent
		
		enableSkipButton();
		
		if(isRetrasmission && nextMove != null)
			cancelMove(nextMove.getActionType(), nextMove.getPosition());
		if(prototype != null){
			ShowBonusAction dialog = new ShowBonusAction(this, prototype);
			dialog.run();
			buildBonusFamiliar();
		}
		else{
			enableMove();
		}
		
	}
	
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
				disableMove();
			}
		});
		disableSkipButton();
	}
	
	private void buildLeaderCardsButton(){
		leaderCardButton = new JButton("Leader Cards");
		leaderCardButton.setLocation((int)(tableImageDimension.getWidth() * 0.01), (int)(tableImageDimension.getHeight() * 0.84));
		leaderCardButton.setSize(skipMove.getSize());
		leaderCardButton.setFont(new Font("Papyrus", Font.ITALIC, (int)(leaderCardButton.getWidth()*0.13)));
		mainLayeredPane.add(leaderCardButton, 0);
		leaderCardButton.addActionListener(new MyActionLister(this));
				
		
	}
	
	private void buildTimerLable(){

		Dimension timerDimension = new Dimension((int)(tableImageDimension.getWidth() * 0.15), (int)(tableImageDimension.getHeight() * 0.07));
		Point timerLocation = new Point((int)(tableImageDimension.getWidth() * 0.01), (int)(tableImageDimension.getHeight() * 0.77));
		timerLable = new TimerLabel(this, timerDimension, timerLocation, MOVE_SECONDS);
		timerLable.setVisible(true);
		mainLayeredPane.add( timerLable, 0);
	}
	
	private class MyActionLister implements ActionListener {

		private GUIView view;
		
		public MyActionLister(GUIView view) {
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
	
	private void enableSkipButton(){
		skipMove.setEnabled(true);
		skipMove.setVisible(true);
	}
	
	private void disableSkipButton(){
		skipMove.setEnabled(false);
		skipMove.setVisible(false);	
	}
	
	private void buildBonusFamiliar(){
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.56 + blackFamiliar.getWidth() * 1.1 * 4);
		int deltaY = (int)(tableImageDimension.getHeight()*0.93);
		try {
			bonusFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), imageLoader.loadBonusFamiliarImage(), null);
			bonusFamiliar.enableListener();
			bonusFamiliar.setTable(this);
			mainLayeredPane.add(bonusFamiliar, 0);
			bonusFamiliar.setCanMove(true);
		} catch (IOException e) {
			logger.error("Bonus Familiar Image not found!");
			logger.info(e);
		}
	}
	
	@Override
	public void setNewMove(PlayerMove move) {
		super.setNewMove(move);
		disableMove();
		this.nextMove = move;
	}
	
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
	
	@Override
	public void setBanToPlayer(String playerID, int banPeriod) throws ElementNotFoundException {
		super.setBanToPlayer(playerID, banPeriod);
		logger.debug("Setting the ban to the current player");
		if(hasToAnswer(playerID)){
			bans.get(banPeriod).setBorder(new LineBorder(Color.RED, (int)(tableImageDimension.getWidth()*0.007)));
			mainLayeredPane.updateUI();
		}
	}
	
	@Override
	protected void chooseIfPayBan(int banPeriod) {
		// Ask to the Player if he wants to pay for the current Period Ban
		if(bans.get(banPeriod) != null){
			Dimension banWindowDimension = new Dimension((int)(mainFrame.getWidth()*0.7),(int)(mainFrame.getHeight()*0.48) );
			Point banWindowLocation = new Point((int)(tableImageDimension.getWidth()*0.4),(int)(tableImageDimension.getHeight()*0.4) );
			new PayBanDialog(this, banWindowDimension ,banWindowLocation, bans.get(banPeriod).getCardImage(), banPeriod );
		}
	}

	@Override
	protected void answerCardRequest(CardRequest message) {
		// Ask to the player to choose from the given card request
		CardRequestDialog dialog = new CardRequestDialog(this, message);
		dialog.run();
	}

	@Override
	protected void chooseFamiliarColor(LeaderFamiliarRequest message) {
		// Ask to the Player what color to choose for the leaderCard Request
		
	}

	@Override
	protected void notifyLeaderCardActivation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void notifyLeaderCardDiscard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askNewPlayerID() {
		//Show a window asking the Player username
		LoginWindow login = new LoginWindow(this, "Used ID yet chosen by another player");
		login.run();
	}

	@Override
	protected void reconnect(ReconnectMessage message) {
		//Restore the Player cards
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
	
	@Override
	public void askPlayerMove(PlayerToken message) {
		super.askPlayerMove(message);
		if(!hasToAnswer(message.getPlayerID()))
			timerLable.showPlayerPlaying(message.getPlayerID());
	}
	
	@Override
	protected void askIfWantToPlay(PlayerToken moveToken) {
		//Ask to the Player if he wants to perform a new Action and start the Timer
		timerLable.startTimer();
		
		//If so ask a new PlayerMove
		this.choosePlayerMove(moveToken.getActionPrototype(), moveToken.isRetrasmission());
		
	}

	public void disableMove( ){
		
		nextMove = null;
		disableFamiliarMove();
		disableSkipButton();
		if(bonusFamiliar != null){
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
		new ResultWindow(this, finalChart, resultWindowDimension, resultWindowLocation );
		
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(GUIView.class);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIView window = new GUIView();
				} catch (Exception e) {
					logger.error("Problems in GUI View creation");
					logger.info(e);
				}
			}
		});
	}


}
