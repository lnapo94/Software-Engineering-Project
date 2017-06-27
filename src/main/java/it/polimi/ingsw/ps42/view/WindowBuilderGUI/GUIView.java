package it.polimi.ingsw.ps42.view.WindowBuilderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Toolkit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JLayeredPane;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

public class GUIView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIView window = new GUIView();
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
	public GUIView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//Create a full Screen main frame 
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0,screen.width,screen.height - 30);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		frame.getContentPane().add(splitPane);
		splitPane.setDividerLocation((int)(frame.getWidth()*0.6));
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setSize((int)(frame.getWidth()*0.6)-10, frame.getHeight()-10);
		splitPane.setLeftComponent(layeredPane);
		
		JLabel GreenCard4 = new JLabel("cartaverde");
		GreenCard4.setHorizontalAlignment(SwingConstants.CENTER);
		GreenCard4.setBounds(70, 50, 185, 310);
		layeredPane.add(GreenCard4);
		
		try{
			BufferedImage immagineCartaVerde = ImageIO.read(GUIView.class.getResource("/Images/Cards/cartaTagliata.png"));
			int cardWidth = immagineCartaVerde.getWidth();
			int cardHeight = immagineCartaVerde.getHeight();
			int cardScaledWidth = GreenCard4.getWidth();
			int cardScaledHeight = (int)(cardHeight*cardScaledWidth/(cardWidth));
			Image cardResized = immagineCartaVerde.getScaledInstance(cardScaledWidth, cardScaledHeight, Image.SCALE_SMOOTH);
			
			GreenCard4.setIcon(new ImageIcon(cardResized));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		JLabel BlueCard4 = new JLabel("");
		BlueCard4.setBounds(0, 0, 136, 240);
		layeredPane.add(BlueCard4);
		BlueCard4.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JLabel GreenCard3 = new JLabel("");
		GreenCard3.setIcon(new ImageIcon(GUIView.class.getResource("/Images/Cards/FirstPeriod/Green/2.png")));
		GreenCard3.setBounds(24, 270, 140, 235);
		layeredPane.add(GreenCard3);
		
		JLabel GreenCard2 = new JLabel("");
		GreenCard2.setIcon(new ImageIcon(GUIView.class.getResource("/Images/Cards/FirstPeriod/Green/3.png")));
		GreenCard2.setBounds(24, 515, 140, 235);
		layeredPane.add(GreenCard2);
		
		JLabel GreenCard1 = new JLabel("");
		GreenCard1.setIcon(new ImageIcon(GUIView.class.getResource("/Images/Cards/FirstPeriod/Green/5.png")));
		GreenCard1.setBounds(24, 760, 140, 235);
		layeredPane.add(GreenCard1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 0, (int)(frame.getWidth() *0.5), frame.getHeight()-20);
		layeredPane.add(scrollPane_1);
		

		JLayeredPane layeredPane2 = new JLayeredPane();
		scrollPane_1.add(layeredPane2);
		
		JLabel label = new JLabel("");
		scrollPane_1.setViewportView(label);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, scrollPane_1.getWidth()-15, scrollPane_1.getHeight()-15);
		label.setLabelFor(layeredPane);
		
		try {
			BufferedImage table = ImageIO.read(GUIView.class.getResource("/Images/TableResized.png"));
			int width = table.getWidth();
			int height = table.getHeight();
			int scaledWidth = label.getWidth()-100;
			int scaledHeight = (int)(height*scaledWidth/(width));
			Image tableResized = table.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
			label.setIcon(new ImageIcon(tableResized));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//label.setIcon(new ImageIcon(GUIView.class.getResource("/Images/TableResized.png")));
		layeredPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{BlueCard4, GreenCard4, GreenCard3, GreenCard2, GreenCard1, scrollPane_1, label}));
	}
}
