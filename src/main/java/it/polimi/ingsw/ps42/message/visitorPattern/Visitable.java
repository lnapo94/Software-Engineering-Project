package it.polimi.ingsw.ps42.message.visitorPattern;

/**
 * Common interface for all the visitable object, such as the messages
 * @author Luca Napoletano, Claudio Montanari
 *
 */
public interface Visitable {
	public void accept(Visitor v);
}
