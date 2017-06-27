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

import javax.swing.JLayeredPane;
import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;

import it.polimi.ingsw.ps42.view.GUI.CardLabel;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUIView2 {

	private JFrame frame;
	private JLabel cardZoom;
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
	 */
	public GUIView2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//Create a full Screen main frame 
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,(int)(screen.height * 0.90));
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(5, 5, frame.getWidth(), frame.getHeight());
		splitPane.setDividerLocation(0.42);
		frame.getContentPane().add(splitPane);
		
		JLayeredPane leftLayeredPane = new JLayeredPane();
		leftLayeredPane.setSize((int)(splitPane.getWidth()*0.5), splitPane.getHeight());
		splitPane.setLeftComponent(leftLayeredPane);
		
		JLabel tableLabel = new JLabel("");
		tableLabel.setSize(new Dimension(leftLayeredPane.getWidth(), leftLayeredPane.getHeight()));
		
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
		
		JLayeredPane rightLayeredPane = new JLayeredPane();
		splitPane.setRightComponent(rightLayeredPane);
		
		cardZoom = new JLabel("");
		cardZoom.setBounds(0, 0, GreenCard4.getWidth()*3,GreenCard4.getHeight()*3 );
		rightLayeredPane.add(cardZoom);
		
		CardLabel cardLabel = new CardLabel(30, 320, cardDimension, cardZoom);
		try {
			cardLabel.placeCard(ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png")));
			//cardLabel.addZoomLabel(cardZoom);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		leftLayeredPane.add(cardLabel);
		leftLayeredPane.add(GreenCard4);
		leftLayeredPane.add(tableLabel);
		
		
		
		
	}
	
	public void zoomImage(Icon imageToZoom){
		
		cardZoom.setIcon(imageToZoom);
	}
}
