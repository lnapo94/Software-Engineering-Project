package it.polimi.ingsw.ps42.view.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.JDialog;
import javax.swing.JFrame;

import it.polimi.ingsw.ps42.model.enumeration.ActionType;
import it.polimi.ingsw.ps42.model.enumeration.FamiliarColor;

public class IncrementWindow extends JDialog{

	private GUIView view;
	private JFrame mainFrame;
	private ActionType type;
	private FamiliarColor familiarColor;
	private int position;
	
	public IncrementWindow(GUIView view, ActionType type, FamiliarColor familiarColor,int position) {
		super(view.getMainFrame());
		this.view = view;
		this.mainFrame = view.getMainFrame();
		this.type = type;
		this.familiarColor = familiarColor;
		this.position = position;
		
		Dimension dimension = new Dimension((int)(mainFrame.getWidth()*0.3), (int)(mainFrame.getHeight()*0.4));
		this.setLayout(new GridLayout(2, 1));
		this.setSize(dimension);
		this.setLocation((int)(mainFrame.getWidth()*0.4),(int)(mainFrame.getHeight()*0.3));
		this.setVisible(true);
		this.setTitle("Your Move Resume");
		
		Font font = new Font("Papyrus", Font.ITALIC, (int)(dimension.getHeight()*0.08));
		//Set the Resume TextArea
		TextArea resumeArea = new TextArea();
		resumeArea.setFont(font);
		resumeArea.setText("Your current move:"
							+"\nAction type: "+ type.toString()
							+"\nAction value: "+ position
							+"\nFamiliar color:"+ familiarColor.toString());
		resumeArea.setEditable(false);
		
		this.add(resumeArea);
		
		
		
	}
}
