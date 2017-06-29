package it.polimi.ingsw.ps42.model;

import java.io.Serializable;

/**
 * Common interface for all the object can be visualized in the view
 * @author Luca Napoletano, Claudio Montanari
 *
 */
@FunctionalInterface
public interface Printable extends Serializable{
	public String print();
}
