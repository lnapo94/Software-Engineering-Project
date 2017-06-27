package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Third and last state of the state machine used to load the cards
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class CardsThirdPeriod extends CardsCreator{
	
	//Logger
	private transient Logger logger = Logger.getLogger(CardsThirdPeriod.class);

	/**
	 * Constructor who set the correct folder name path
	 * @throws IOException
	 */
	public CardsThirdPeriod() throws IOException {
		super("Resource//Cards//ThirdPeriod//");
		logger.info("Load third period cards");
	}

	/**
	 * Method used to go to the next state. If the indexes are 0 and 4, change the indexes value
	 * to 4 and 8 because the file contains other cards, and return this state again. At this
	 * point, files are ended, so doesn't load any other file, and also close the loader of
	 * the files
	 */
	@Override
	public CardsCreator nextState() {
		if(lowIndex == 0 && highIndex == 4) {
			logger.info("Load third period cards, second part");
			lowIndex = 4;
			highIndex = 8;
			return this;
		}
		try {
			loader.close();
		} catch (IOException e) {
			logger.error("Unable to close the cards file");
			logger.info(e);
		}
		return null;
	}

}
