package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;
import it.polimi.ingsw.ps42.view.TableInterface;

/**
 * Class used to define an object that can be moved on the screen, e.g. the familiar
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class DraggableComponent extends FunctionalLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3784924483053777524L;

	private FamiliarColor familiarColor;
	private volatile int screenX;
	private volatile int screenY;
	private volatile int myX;
	private volatile int myY;
	private BufferedImage image;
	private boolean canMove;
	
	private int initialX;
	private int initialY;
	
	private TableInterface table;
	
	/**
	 * Private class used to handle a mouse event on the draggable component
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class FamiliarPressedListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			//Nothing to do
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			//Nothing to do
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			//Nothing to do
			
		}

		@Override
		public void mousePressed(MouseEvent event) {
			screenX = event.getXOnScreen();
			screenY = event.getYOnScreen();
			myX = getX();
			myY = getY();
		}

		@Override
		public void mouseReleased(MouseEvent event) {
			if(event.getSource() instanceof DraggableComponent){
				DraggableComponent source = (DraggableComponent) event.getSource();
				if( !canMove ){
					setLocation(initialX, initialY);
				}
				else{
					if(table.handleEvent(event.getXOnScreen(), event.getYOnScreen(), source)){
						setIcon(null);
					}
					else 
						setLocation(initialX, initialY);
				}
			}
		}

	
		
	}
	
	/**
	 * Private class used to know the movement of the familiar
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class FamiliarDraggedListener implements MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent event) {
			int deltaX = event.getXOnScreen() - screenX;
			int deltaY = event.getYOnScreen() - screenY;
			
			setLocation(myX + deltaX, myY + deltaY);
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			//Nothing to do
			
		}
		
	}
	
	/**
	 * Constructor of the draggable familiar
	 * @param x				The x position on screen
	 * @param y				The y position on screen
	 * @param dimension		The dimension of the component
	 * @param image			The image to show in the component
	 * @param color			The color of the familiar to load the correct picture
	 */
	public DraggableComponent(int x, int y, Dimension dimension, BufferedImage image, FamiliarColor color) {
		super();
		this.familiarColor= color;
		dimension = new Dimension((int)(dimension.getWidth()*0.06), (int)(dimension.getHeight()*0.05)); 
		setLocation(x, y);
		setSize(dimension);
		myX = x;
		myY = y;
		screenX = x;
		screenY = y;
		initialX = x;
		initialY = y;
		canMove = false;
		this.image = image;
		this.setIcon(resizeImage(image, this.getSize()));
	}
	
	/**
	 * Getter for the image of the draggable component
	 * @return		A BufferedImage of the picture shown in the component
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Method used to add the mouse event and the mouse motion to the component
	 */
	public void enableListener(){
		this.addMouseListener(new FamiliarPressedListener());
		this.addMouseMotionListener(new FamiliarDraggedListener());
	}
	
	/**
	 * Method used to enable the motion of the component
	 * @param state		The state of the component. If the state is True, then you can move the component, else you can't move the component on the screen
	 */
	public void setCanMove(boolean state){
		this.canMove = state;
	}
	
	/**
	 * Method used to set the created table
	 * @param table		The table to set to the draggable component
	 */
	public void setTable(TableInterface table) {
		this.table = table;
	}
	
	/**
	 * Method used to reset the draggable component in the initial position
	 */
	public void resetFamiliar(){
		setLocation(initialX, initialY);
		setIcon(resizeImage(image, this.getSize()));
	}
	
	/**
	 * Getter for the color of the familiar represented by the draggable component
	 * @return	The color of the represented familiar
	 */
	public FamiliarColor getFamiliarColor() {
		return familiarColor;
	}
	
}
