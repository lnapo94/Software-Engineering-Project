package it.polimi.ingsw.ps42.parser;

import it.polimi.ingsw.ps42.model.effect.Effect;

/**
 * Class used to serialize the Ban in a Gson file
 *
 * @author Luca Napoletano, Claudio Montanari
 */
	public class BanSerializerSet {

	private Effect ban;
	
	/**
	 * Setter for the Ban in the set
	 * @param ban the Ban to add
	 */
	public void setBan(Effect ban) {
		this.ban = ban;
	}
	
	/**
	 * Getter for the Ban in the set
	 * @return the Effect in the set
	 */
	public Effect getBan() {
		return this.ban;
	}
}
