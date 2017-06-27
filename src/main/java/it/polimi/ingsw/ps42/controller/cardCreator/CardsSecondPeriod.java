package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Second state of the state machine used to load the cards
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardsSecondPeriod extends CardsCreator{
	
	//Logger
		private transient Logger logger = Logger.getLogger(CardsThirdPeriod.class);


	/**
	* Constructor who set the correct folder name path
	* @throws IOException
	*/
	public CardsSecondPeriod() throws IOException {
		super("Resource//Cards//SecondPeriod//");
		logger.info("Load second period cards");
	}

	/**
	 * Method used to go to the next state. If the indexes are 0 and 4, change the indexes value
	 * to 4 and 8 because the file contains other cards, and return this state again; else go to the other state
	 */
	@Override
	public CardsCreator nextState() throws IOException {
		if(lowIndex == 0 && highIndex == 4) {
			logger.info("Load second period cards, second part");
			lowIndex = 4;
			highIndex = 8;
			return this;
		}
		return new CardsThirdPeriod();
	}

}
