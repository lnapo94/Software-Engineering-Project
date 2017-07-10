package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

/**
 * Class used to show the timer on the screen
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class TimerLabel extends JLabel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9216038812999139178L;
	private int seconds;
	private int minutes;
	
	private int updatedSeconds;
	private int updatedMinutes;
	
	private GUIView view;
	private Timer timer;
	private boolean status;
	
	/**
	 * Private class to create a timer task used when the timer expired. In this case, every 1000 ms the label will show a new picture that
	 * represent the time left
	 * @author Luca Napoletano, Claudio Montanari
	 *
	 */
	private class TimerLableTask extends TimerTask{

		@Override
		public void run() {
			if(updatedSeconds == 0 ){
				if( updatedMinutes != 0){
					updatedMinutes--;
					updatedSeconds = 59;
					updateLabel();
				}
				else{
					skipMove();
				}		
			}
			else{
				updatedSeconds--;
				updateLabel();
			}
		}
	}
	
	/**
	 * Constructor of the TimerLabel object
	 * @param view			A reference to the current GUIView
	 * @param dimension		The dimension of this component
	 * @param location		The locations on the screen
	 * @param secondsToWait	The start time
	 */
	public TimerLabel(GUIView view, Dimension dimension, Point location, long secondsToWait) {
		super();
		this.view = view;
		this.status = false;
		this.setSize(dimension);
		this.setLocation(location);
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getWidth()*0.2));
		
		this.setFont(font);
		
		minutes = (int)(secondsToWait / 60);
		seconds = (int)(((secondsToWait / 60) - minutes) * 60);
		
		updatedMinutes = minutes;
		updatedSeconds = seconds;
	}
	
	/**
	 * Start a timer when this method is called
	 */
	public void startTimer(){
		
		updatedMinutes = minutes;
		updatedSeconds = seconds;
		timer = new Timer();
		timer.schedule(new TimerLableTask(), 1000);
		this.status = true;
	}
	
	
	/**
	 * Show the current player when it isn't the GUIView player the current player
	 * @param name		The name of the player who is playing
	 */
	public void showPlayerPlaying(String name){
		this.setText(name+ " is playing");
	}
	
	/**
	 * Method used to skip the move when the timer expired
	 */
	private void skipMove(){
		
		view.disableMove();
	}
		
	/**
	 * Method used to update the shown time
	 */
	private void updateLabel(){
		if(status){
			timer.cancel();
			this.setText("  " + updatedMinutes + " : " + updatedSeconds);
			timer = new Timer();
			timer.schedule(new TimerLableTask(), 1000);
		}
		
	}
		
	/**
	 * Method used to reset the timer
	 */
	public void resetTimer(){
		if(timer != null)
			timer.cancel();
		this.status = false;
		this.setText("");
	}

}
