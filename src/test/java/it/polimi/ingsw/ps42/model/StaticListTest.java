package it.polimi.ingsw.ps42.model;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.ps42.model.StaticList;

/**
 * This class tests the main functionalities of the StaticList class, 
 * so it creates a StaticList of Integer, tries to add and remove some elements and verify the fixed  
 * maximum dimension of the StaticList 
 * 
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public class StaticListTest {
	
	private Logger logger = Logger.getLogger(StaticListTest.class);
	
	@BeforeClass
	public static void classSetUp() {
		PropertyConfigurator.configure("Logger//Properties//test_log.properties");
	}

	@Test
	public void test() {
		//Create a StaticList and add some elements
		StaticList<Integer> list = new StaticList<>(3);
		list.add(2);
		logger.info(list.get(0));
		list.add(5);
		list.add(1);
		for(Integer i : list) {
			logger.info("Number: " + i.toString());
		}
		
		//Remove some elements
		list.remove(2);
		assertEquals("Now list has 2 elements", 2, list.size());
		for(Integer i : list) {
			logger.info("Number: " + i.toString());
		}
		
		//Remove all the elements and verify it is empty
		list.removeAll();
		System.out.println(list.size());
		for(Integer i : list) {
			logger.info("Number: " + i.toString());
		}
		assertEquals("Now list is empty", 0, list.size());
	}
	

}
