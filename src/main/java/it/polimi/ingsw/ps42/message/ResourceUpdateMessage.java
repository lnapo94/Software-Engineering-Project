package it.polimi.ingsw.ps42.message;

import java.util.Map;

import it.polimi.ingsw.ps42.message.visitorPattern.Visitor;
import it.polimi.ingsw.ps42.model.enumeration.Resource;

public class ResourceUpdateMessage extends Message {
	
	//Message used to update playerView's resources
	
	private Map<Resource, Integer> resources;

	public ResourceUpdateMessage(String playerID, Map<Resource,Integer> resources) {
		super(playerID);
		this.resources = resources;
	}
	
	public Map<Resource, Integer> getResources() {
		return this.resources;
	}

	@Override
	public void accept(Visitor v) {
		//Method used to start the visit
		v.visit(this);
	}

}
