package it.polimi.ingsw.ps42.view.WindowBuilderGUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;

import javax.swing.JLayeredPane;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;

import it.polimi.ingsw.ps42.message.PlayerMove;
import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.view.TableInterface;
import it.polimi.ingsw.ps42.view.GUI.CardLabel;
import it.polimi.ingsw.ps42.view.GUI.CardZoom;
import it.polimi.ingsw.ps42.view.GUI.DraggableComponent;
import it.polimi.ingsw.ps42.view.GUI.GUIView;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import java.awt.Choice;

public class GUIView2 implements TableInterface{

	private JFrame frame;
	private CardZoom cardZoom;
	private BufferedImage movingImage;
	private JLabel towerFamPosition;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIView2 window = new GUIView2();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public GUIView2() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		//Create a full Screen main frame 
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,(int)(screen.height * 0.90));
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		/*
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(5, 5, frame.getWidth(), frame.getHeight());
		splitPane.setDividerLocation(0.42);
		frame.getContentPane().add(splitPane);
		*/
		
		
		JLayeredPane splitPane = new JLayeredPane();
		splitPane.setBounds(5, 5, frame.getWidth(), frame.getHeight());
		frame.getContentPane().add(splitPane);
				
		
		JLayeredPane leftLayeredPane = new JLayeredPane();
		leftLayeredPane.setSize((int)(splitPane.getWidth()), splitPane.getHeight());
		leftLayeredPane.setLocation(0, 0);
//		splitPane.setLeftComponent(leftLayeredPane);
splitPane.add(leftLayeredPane);
		
		JLabel tableLabel = new JLabel("");
		tableLabel.setSize(new Dimension((int)(leftLayeredPane.getWidth()*0.5), leftLayeredPane.getHeight()));
		
		Dimension cardDimension = null;
		try {
			BufferedImage tableUp = ImageIO.read(GUIView.class.getResource("/Images/TableUpperPart.png"));
			int width = tableUp.getWidth();
			int height = tableUp.getHeight();
			int scaledHeight = tableLabel.getHeight();
			int scaledWidth= (int)(width*scaledHeight/(height));
			Image tableResized = tableUp.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			tableLabel.setIcon(new ImageIcon(tableResized));
		
			cardDimension = new Dimension((int)(scaledWidth*0.13), (int)(scaledHeight*0.17));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel GreenCard4 = new JLabel("");
		/*GreenCard4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				zoomImage(GreenCard4.getIcon());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				zoomImage(GreenCard4.getIcon());
			}
			@Override
			public void mouseExited(MouseEvent e) {
				zoomImage(null);
			}
		});
		*/
		GreenCard4.setBounds(30, 50, (int)cardDimension.getWidth(), (int)cardDimension.getHeight());
		
		try {
			BufferedImage cardImage = ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png"));
			int width = GreenCard4.getWidth();
			int height = GreenCard4.getHeight();
			int scaledHeight = GreenCard4.getHeight();
			int scaledWidth= (int)(width*scaledHeight/(height));
			Image cardResized = cardImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			GreenCard4.setIcon(new ImageIcon(cardResized));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*JLayeredPane rightLayeredPane = new JLayeredPane();
		rightLayeredPane.setSize((int)(splitPane.getWidth()*0.5), splitPane.getHeight());
		rightLayeredPane.setLocation((int)(splitPane.getWidth()*0.5), 0);
		splitPane.add(rightLayeredPane);
		*/
		
		BufferedImage cardBack = ImageIO.read(GUIView.class.getResource("/Images/LeaderCards/back.jpg"));
		cardZoom = new CardZoom(cardBack, new Dimension(GreenCard4.getWidth()*3,GreenCard4.getHeight()*3));
		cardZoom.setLocation((int)(leftLayeredPane.getWidth()*0.5), 0);
		leftLayeredPane.add(cardZoom);
		
		CardLabel cardLabel = new CardLabel(30, 320, cardDimension, cardZoom);
		try {
			cardLabel.placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
			//cardLabel.addZoomLabel(cardZoom);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try{
			DraggableComponent familiarStart = new DraggableComponent(750, 1510, tableLabel.getSize(), ImageIO.read(GUIView2.class.getResource("/Images/Others/BluFamiliareNero.png")), FamiliarColor.ORANGE);
			familiarStart.enableListener();
			familiarStart.setCanMove(true);
			towerFamPosition = new JLabel("");
			towerFamPosition.setBounds(545, 120, (int)(tableLabel.getWidth()*0.06), (int)(tableLabel.getHeight()*0.06));
			
			familiarStart.setTable(this);
			
			
			JButton btnNewButton = new JButton("resetFamiliar");
			btnNewButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					familiarStart.resetFamiliar();
					towerFamPosition.setIcon(null);
				}
			});
			
			Choice choice = new Choice();
			choice.setBounds(100, 1000, 300, 300);
			//PopupMenu menu = new PopupMenu("menu delle scelte");
			//menu.add("item11");
			//choice.add(menu);
			choice.add("scelta1");
			choice.add("Scelta 2");
			leftLayeredPane.add(choice);
			
			btnNewButton.setBounds(0, 0, 250, 41);
			leftLayeredPane.add(btnNewButton);
			
			
			leftLayeredPane.add(towerFamPosition);
			leftLayeredPane.add(familiarStart);
			this.movingImage = familiarStart.getImage();
			
		}
		catch (IOException e) {
			
		}
		
		leftLayeredPane.add(cardLabel);
		leftLayeredPane.add(GreenCard4);
		leftLayeredPane.add(tableLabel);
		
	}
	
	public void zoomImage(Icon imageToZoom){
		
		cardZoom.setIcon(imageToZoom);
	}
	
	private ImageIcon resizeImage(BufferedImage imageToResize, Dimension newDimension){
		Image cardResized = null;
		if(imageToResize != null){
			int width = (int)newDimension.getWidth();
			int height = (int)newDimension.getHeight();
			int scaledHeight = height;
			int scaledWidth= (int)(width*scaledHeight/(height));
			cardResized = imageToResize.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
		}
		
		return new ImageIcon(cardResized);
	}
	
	public BufferedImage getMovingImage(){
		return this.movingImage;
	}
	
	public void setMovingImage(BufferedImage image){
		this.movingImage = image;
	}

	public boolean handleEvent(int x, int y, BufferedImage image, FamiliarColor color) {
		System.out.println("x: "+x+"; y: "+y);
		if( containsPoint(towerFamPosition, x, y) )
		{
			towerFamPosition.setIcon(resizeImage(image, towerFamPosition.getSize()));
			return true;
		}
		else return false;
	}
	
	private boolean containsPoint(JLabel label, int x, int y){
		Point p = label.getLocationOnScreen();
		if(p.getX() < x && x < p.getX()+label.getWidth()
				&& p.getY() < y && y < p.getY() + label.getHeight())
			return true;
		else return false;
	}


	
}
