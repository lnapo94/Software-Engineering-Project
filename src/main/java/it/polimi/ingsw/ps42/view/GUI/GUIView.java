package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
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
import javax.swing.JSplitPane;

import it.polimi.ingsw.ps42.message.CardRequest;
import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.message.leaderRequest.LeaderFamiliarRequest;
import it.polimi.ingsw.ps42.model.action.ActionPrototype;
import it.polimi.ingsw.ps42.model.effect.Obtain;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.model.leaderCard.LeaderCard;
import it.polimi.ingsw.ps42.model.player.BonusBar;
import it.polimi.ingsw.ps42.view.View;

public class GUIView extends View {

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
		//Add a SplitPane 
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(5, 5, mainFrame.getWidth(), mainFrame.getHeight());
		splitPane.setDividerLocation(0.42);
		mainFrame.getContentPane().add(splitPane);
		
		Dimension leftPaneDimension = new Dimension((int)(splitPane.getWidth()*0.42), splitPane.getHeight());
		Dimension rightPanelDimension = new Dimension((int)(splitPane.getWidth()*0.58), splitPane.getHeight());
		
		//Set up the Left Layered Pane
		JLayeredPane leftLayeredPane = new JLayeredPane();
		leftLayeredPane.setSize(leftPaneDimension);
		leftLayeredPane.setMinimumSize(leftPaneDimension);
		splitPane.setLeftComponent(leftLayeredPane);
		//Setup the Right Layered Pane
		JLayeredPane rightLayeredPane = new JLayeredPane();
		rightLayeredPane.setSize(rightPanelDimension);
		rightLayeredPane.setMinimumSize(rightPanelDimension);
		splitPane.setRightComponent(rightLayeredPane);
		
		//Set the Table main Image
		JLabel tableLabel = new JLabel();
		tableLabel.setSize(new Dimension(leftLayeredPane.getWidth(), leftLayeredPane.getHeight()));
		ImageIcon tableIcon = resizeImage(ImageIO.read(GUIView.class.getResource("/Images/TableUpperPart.png")), tableLabel.getSize());
		tableLabel.setIcon(tableIcon);
		leftLayeredPane.add(tableLabel, -1);
		//Set the Card and Table Dimension from the TableIcon and build all the CardPositions
		tableImageDimension = new Dimension(tableIcon.getIconWidth(), tableIcon.getIconHeight());
		Dimension cardDimension = new Dimension((int)(tableIcon.getIconWidth()*0.13), (int)(tableIcon.getIconHeight()*0.17));

		//Set the CardZoom Panel
		BufferedImage cardBack = ImageIO.read(GUIView.class.getResource("/Images/LeaderCards/back.jpg"));
		cardZoom = new CardZoom(cardBack, new Dimension((int)(cardDimension.getWidth()*3), (int)(cardDimension.getHeight()*3)));
		cardZoom.setLocation(0, 0);
		rightLayeredPane.add(cardZoom);
		
		buildCardPosition(cardDimension, leftLayeredPane);
		
		buildFamiliarStartingPositions();
	}
	/**
	 * 
	 * @param cardDimension
	 * @param leftPane
	 * @throws IOException
	 */
	private void buildCardPosition(Dimension cardDimension, JLayeredPane leftPane) throws IOException{
		
		int deltaX = (int)(tableImageDimension.getWidth()*0.022);
		int deltaY = (int)(tableImageDimension.getHeight()*0.03);
		greenTower = new ArrayList<>();
		blueTower = new ArrayList<>();
		violetTower = new ArrayList<>();
		yellowTower = new ArrayList<>();
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			greenTower.add(card);
			leftPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.24);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			yellowTower.add(card);
			leftPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.24);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			blueTower.add(card);
			leftPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}
		deltaY = (int)(tableImageDimension.getHeight()*0.03);
		deltaX += (int) (tableImageDimension.getWidth()*0.24);
		
		for(int i=0; i<4; i++){
			CardLabel card = new CardLabel(deltaX, deltaY, cardDimension, cardZoom); 
			violetTower.add(card);
			leftPane.add(card, 0);
			deltaY += (int)(tableImageDimension.getHeight()*0.015) + cardDimension.getHeight();
		}

		blueTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		violetTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		greenTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		yellowTower.get(0).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
		greenTower.get(1).placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/FirstPeriod/Green/1.png")));
	}
	
	private void buildFamiliarStartingPositions(){
		
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
		// TODO Auto-generated method stub
		return null;
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
		return null;
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
