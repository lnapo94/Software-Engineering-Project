package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class TimerLabel extends JLabel{
	
	private int seconds;
	private int minutes;
	
	private int updatedSeconds;
	private int updatedMinutes;
	
	private GUIView view;
	private Timer timer;
	
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
	
	public TimerLabel(GUIView view, Dimension dimension, Point location, long secondsToWait) {
		super();
		this.view = view;
		this.setSize(dimension);
		this.setLocation(location);
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getWidth()*0.2));
		
		this.setFont(font);
		
		minutes = (int)(secondsToWait / 60);
		seconds = (int)(((secondsToWait / 60) - minutes) * 60);
		
		updatedMinutes = minutes;
		updatedSeconds = seconds;
	}
	
	public void startTimer(){
		
		updatedMinutes = minutes;
		updatedSeconds = seconds;
		timer = new Timer();
		timer.schedule(new TimerLableTask(), 1000);
	}
	
	
	
	public void showPlayerPlaying(String name){
		this.setText(name+ " is playing");
	}
	
	private void skipMove(){
		
		view.disableMove();
	}
		
	private void updateLabel(){
		
		this.setText("  " + updatedMinutes + " : " + updatedSeconds);
		timer = new Timer();
		timer.schedule(new TimerLableTask(), 1000);
	}
	
	public void resetTimer(){
		timer.cancel();
		this.setText("");
	}

}
