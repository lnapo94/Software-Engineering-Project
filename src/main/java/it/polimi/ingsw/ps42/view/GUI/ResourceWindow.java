package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import it.polimi.ingsw.ps42.model.enumeration.Resource;
import it.polimi.ingsw.ps42.model.player.Player;
import it.polimi.ingsw.ps42.parser.ImageLoader;

public class ResourceWindow extends FunctionalLabel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7926214458651089824L;
	
	private Player player;
	private HashMap<Resource, JLabel> resourceLabel;
	
	public ResourceWindow(Dimension dimension, Point location, ImageLoader loader) throws IOException {
	
		super();
		this.setSize(dimension);
		this.setLocation(location);
		this.setLayout(new GridLayout(2, 7));
		this.setIcon(resizeImage(ImageIO.read(new File("Resource//Images//Others//pergamena.png")), this.getSize()));
		this.resourceLabel = new HashMap<>();
		
		for (Resource resource: Resource.values()) {
			
			JLabel label = new JLabel();
			Dimension labelDimension = new Dimension((int)(dimension.getWidth()*0.13), (int)(dimension.getHeight() * 0.5));
			label.setSize(labelDimension);
			label.setIcon(resizeImage(loader.loadResourceImage(resource), label.getSize()));
			
			this.add(label);
			
		}

		Font numberFont = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.3));
		for (Resource resouce: Resource.values()) {

			JLabel label = new JLabel();
			label.setFont(numberFont);
			this.add(label);
			resourceLabel.put(resouce, label);
		}
				
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void update(){
		//update the player Resources
		Integer quantity;
		for (Resource resource : Resource.values()) {
			quantity = player.getResource(resource);
			JLabel label = resourceLabel.get(resource);
			label.setText("  "+quantity.toString());
			
		}
		
	}
}
