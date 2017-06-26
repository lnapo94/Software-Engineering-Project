package it.polimi.ingsw.ps42.controller.cardCreator;

import java.io.IOException;

import org.apache.log4j.Logger;

public class CardsThirdPeriod extends CardsCreator{
	
	//Logger
	private transient Logger logger = Logger.getLogger(CardsThirdPeriod.class);

	public CardsThirdPeriod() throws IOException {
		super("Resource//Cards//ThirdPeriod//");
		logger.info("Load third period cards");
	}

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
