package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.LineBorder;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.view.TableInterface;
import it.polimi.ingsw.ps42.view.View;

public class GUIView extends View implements TableInterface{

	//The GUI main frame, where all the others Components are placed
	private JFrame mainFrame;
	private Dimension tableImageDimension;
	
	//The Label where the zoom of the Card is displayed
	private CardZoom cardZoom;
	
	//The Image of the familiar currently moving
	private BufferedImage movingImage;
	
	//The List of CardLabels separated by Color
	private List<CardLabel> greenTower;
	private List<CardLabel> yellowTower;
	private List<CardLabel> blueTower;
	private List<CardLabel> violetTower;
	
	//The List of positions where the Familiars can be placed
	//Starting Positions 
	private DraggableComponent neutralFamiliar;
	private DraggableComponent blackFamiliar;
	private DraggableComponent orangeFamiliar;
	private DraggableComponent whiteFamiliar;
	//Tower positions
	private List<JLabel> greenTowerForFamiliar;
	private List<JLabel> yellowTowerForFamiliar;
	private List<JLabel> blueTowerForFamiliar;
	private List<JLabel> violetTowerForFamiliar;
	//Yield and Product Positions
	private List<JLabel> yield;
	private List<JLabel> produce;
	//Council Positions
	private List<JLabel> council;
	
	//New Order 
	private List<JLabel> newOrder;
	//The List of Bans
	private List<CardLabel> bans;
	//The player next move
	private PlayerMove nextMove;
	
	public GUIView() throws IOException {
		super();
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
		//Add a JLayeredPane
		JLayeredPane mainLayeredPane = new JLayeredPane();
		mainLayeredPane.setBounds(5, 5, mainFrame.getWidth(), mainFrame.getHeight());
		mainFrame.getContentPane().add(mainLayeredPane);
		
		Dimension leftPaneDimension = new Dimension((int)(mainLayeredPane.getWidth()*0.42), mainLayeredPane.getHeight());
		Dimension rightPanelDimension = new Dimension((int)(mainLayeredPane.getWidth()*0.58), mainLayeredPane.getHeight());
		
		//Set up the LeftPart of the Layered Pane
/*		JLayeredPane leftLayeredPane = new JLayeredPane();
		leftLayeredPane.setSize(leftPaneDimension);
		leftLayeredPane.setMinimumSize(leftPaneDimension);
		splitPane.setLeftComponent(leftLayeredPane);
		//Setup the Right Layered Pane
		JLayeredPane rightLayeredPane = new JLayeredPane();
		rightLayeredPane.setSize(rightPanelDimension);
		rightLayeredPane.setMinimumSize(rightPanelDimension);
		splitPane.setRightComponent(rightLayeredPane);
*/
		
		//Set the Table main Image
		JLabel tableLabel = new JLabel();
		tableLabel.setSize(new Dimension((int)leftPaneDimension.getWidth(), (int)leftPaneDimension.getHeight()));
		tableLabel.setLocation(0, 0);
//		tableLabel.setBorder(new LineBorder(Color.BLACK, 10));
		ImageIcon tableIcon = resizeImage(ImageIO.read(GUIView.class.getResource("/Images/TableUpperPart.png")), tableLabel.getSize());
		tableLabel.setIcon(tableIcon);
		mainLayeredPane.add(tableLabel, -1);
		//Set the Card and Table Dimension from the TableIcon and build all the CardPositions
		tableImageDimension = new Dimension(tableIcon.getIconWidth(), tableIcon.getIconHeight());
		Dimension cardDimension = new Dimension((int)(tableIcon.getIconWidth()*0.13), (int)(tableIcon.getIconHeight()*0.17));

		//Set the CardZoom Panel
		BufferedImage cardBack = ImageIO.read(GUIView.class.getResource("/Images/LeaderCards/back.jpg"));
		cardZoom = new CardZoom(cardBack, new Dimension((int)(cardDimension.getWidth()*3), (int)(cardDimension.getHeight()*3)));
		cardZoom.setLocation((int)leftPaneDimension.getWidth(),0);
		mainLayeredPane.add(cardZoom);
		
		//Set the lowerPart of the Table
		JLabel lowTableLabel = new JLabel();
		lowTableLabel.setSize((int)(rightPanelDimension.getWidth()-cardZoom.getWidth()), (int)(cardZoom.getHeight()*0.70));
		lowTableLabel.setLocation((int)(leftPaneDimension.getWidth()+cardZoom.getWidth()), 0);
		lowTableLabel.setIcon(resizeImage(ImageIO.read(GUIView.class.getResource("/Images/TableDownPart.png")), lowTableLabel.getSize()));
		mainLayeredPane.add(lowTableLabel, -1);
		
		//Set the Cards taken container
		JLabel cardContainer = new JLabel();
		cardContainer.setSize((int)(rightPanelDimension.getWidth()*0.8),(int) (rightPanelDimension.getHeight()-cardZoom.getHeight()));
		cardContainer.setLocation((int)leftPaneDimension.getWidth(), (int)cardZoom.getHeight());
		cardContainer.setIcon(resizeImage(ImageIO.read(GUIView.class.getResource("/Images/Others/cardContainer.jpg")), cardContainer.getSize()));
		mainLayeredPane.add(cardContainer, -1);
		
		//Build the main Familiar positions
		buildCardPosition(cardDimension, mainLayeredPane);
		
		buildFamiliarStartingPositions(mainLayeredPane);
		
		buildFamiliarMovePositions(mainLayeredPane);
		
		enableMove();
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
		greenTower = new ArrayList<>();
		blueTower = new ArrayList<>();
		violetTower = new ArrayList<>();
		yellowTower = new ArrayList<>();
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			greenTower.add(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			yellowTower.add(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			blueTower.add(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.241);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			violetTower.add(card);
			mainPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}

		blueTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		violetTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		greenTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		yellowTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		greenTower.get(1).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/FirstPeriod/Green/1.png")));
	}
	
	/**
	 * 
	 * @param mainPane
	 * @throws IOException
	 */
	private void buildFamiliarStartingPositions(JLayeredPane mainPane) throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.56 );
		int deltaY = (int)(tableImageDimension.getHeight()*0.93);
		
		blackFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")));
		blackFamiliar.enableListener();
		blackFamiliar.setTable(this);
		mainPane.add(blackFamiliar, 0);
		
		int border = (int)(blackFamiliar.getWidth()*1.1); 
		deltaX += border;
		whiteFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")));
		whiteFamiliar.enableListener();
		whiteFamiliar.setTable(this);
		mainPane.add(whiteFamiliar, 0);
		
		deltaX += border;
		orangeFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")));
		orangeFamiliar.enableListener();
		orangeFamiliar.setTable(this);
		mainPane.add(orangeFamiliar, 0);
		
		deltaX += border;
		neutralFamiliar = new DraggableComponent(deltaX, deltaY, tableImageDimension.getSize(), ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")));
		neutralFamiliar.enableListener();
		neutralFamiliar.setTable(this);
		mainPane.add(neutralFamiliar, 0);
		
	}
	
	/**
	 * 
	 * @param mainPane
	 * @throws IOException 
	 */
	private void buildFamiliarMovePositions(JLayeredPane mainPane) throws IOException{
		
		//Build the Tower related familiarMove positions
		greenTowerForFamiliar = new ArrayList<>();
		yellowTowerForFamiliar = new ArrayList<>();
		blueTowerForFamiliar = new ArrayList<>();
		violetTowerForFamiliar = new ArrayList<>();

		int deltaX = (int)(tableImageDimension.getWidth()*0.172);
		Dimension positionDimension = new Dimension((int)(tableImageDimension.getWidth()*0.06), (int)(tableImageDimension.getHeight()*0.05));
		placeFamiliarPosition(mainPane, positionDimension, greenTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(mainPane, positionDimension, yellowTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.242);
		placeFamiliarPosition(mainPane, positionDimension, blueTowerForFamiliar, deltaX);
		deltaX += (int)(tableImageDimension.getWidth()*0.241);
		placeFamiliarPosition(mainPane, positionDimension, violetTowerForFamiliar, deltaX);		
		
		//Build the Council related familiarMove positions
		council = new ArrayList<>();
		JLabel firstCouncil = new JLabel();
		firstCouncil.setSize(positionDimension);
		firstCouncil.setLocation((int)(tableImageDimension.getWidth()*0.7), (int)(tableImageDimension.getHeight()*0.8));
		//firstCouncil.setIcon(resizeImage(ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")), positionDimension));
		council.add(firstCouncil);
		mainPane.add(firstCouncil, 0);
	}
	
	private void placeFamiliarPosition(JLayeredPane mainPane, Dimension positionDimension, List<JLabel> tower, int rightShift) throws IOException{
		
		int deltaY = (int)(tableImageDimension.getHeight()*0.081);
		for (int i=0; i<4; i++){
			JLabel familiarPosition = new JLabel();
			familiarPosition.setSize(positionDimension);
			familiarPosition.setLocation(rightShift, deltaY);
			//familiarPosition.setIcon(resizeImage(ImageIO.read(GUIView.class.getResource("/Images/Others/BluFamiliareNero.png")), positionDimension));
			tower.add(familiarPosition);
			mainPane.add(familiarPosition, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.1858);
		}
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
	public boolean handleEvent(int x, int y, BufferedImage image) {
		System.out.println("x: "+x+"; y: "+y);
		//For each position check if contains the point (x,y), if so change the imageIcon
		for (JLabel position : greenTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				position.setIcon(resizeImage(image, position.getSize()));
				return true;
			}
		}
		for (JLabel position : yellowTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				position.setIcon(resizeImage(image, position.getSize()));
				return true;
			}
		}
		for (JLabel position : blueTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				position.setIcon(resizeImage(image, position.getSize()));
				return true;
			}
		}
		for (JLabel position : violetTowerForFamiliar) {
			if( containsPoint(position, x, y) ){
				position.setIcon(resizeImage(image, position.getSize()));
				return true;
			}
		}
		for (JLabel position : council) {
			if( containsPoint(position, x, y) ){
				position.setIcon(resizeImage(image, position.getSize()));
				return true;
			}
		}
		return false;
	}
	
	private boolean containsPoint(JLabel label, int x, int y){
		
			Point p = label.getLocationOnScreen();
			if(p.getX() < x && x < p.getX()+label.getWidth()
					&& p.getY() < y && y < p.getY() + label.getHeight())
				return true;
			else return false;
	
	}

	@Override
	public void askIncrement(ActionType type, int position, FamiliarColor color) {
		//Ask the player if he wants to increase the Action value with some of his slaves
		int increaseValue = 0;
		
		this.nextMove = new PlayerMove(player.getPlayerID(), type, color, position, increaseValue);
		
	}
	private void enableMove(){
		blackFamiliar.setCanMove(true);
		whiteFamiliar.setCanMove(true);
		orangeFamiliar.setCanMove(true);
		neutralFamiliar.setCanMove(true);
	}
	private void restoreFamiliar(){
		blackFamiliar.resetFamiliar();
		whiteFamiliar.resetFamiliar();
		orangeFamiliar.resetFamiliar();
		neutralFamiliar.resetFamiliar();
	}
	
	
	@Override
	protected int chooseBonusBar(List<BonusBar> bonusBarList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int chooseLeaderCard(List<LeaderCard> leaderCardList) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int chooseCouncilConversion(List<Obtain> possibleConversions) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected PlayerMove choosePlayerMove(ActionPrototype prototype) {
		ActionType type = null;
		FamiliarColor familiarColor = null;
		int position = 0;
		int increaseValue = 0;
		//Enable the draggableFamiliar to move
		enableMove();
		while(nextMove != null){
/*			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
*/
		}
		return nextMove;
	}

	@Override
	protected boolean chooseIfPayBan(int banPeriod) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int answerCardRequest(CardRequest message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected FamiliarColor chooseFamiliarColor(LeaderFamiliarRequest message) {
		// TODO Auto-generated method stub
		return null;
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
	protected String askPlayerID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String askIfWantToPlay() {
		// TODO Auto-generated method stub
		return "si";
	}

	@Override
	protected void showResult(List<String> finalChart) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIView window = new GUIView();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
