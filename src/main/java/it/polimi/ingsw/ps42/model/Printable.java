package it.polimi.ingsw.ps42.model;

import java.io.Serializable;

@FunctionalInterface
public interface Printable extends Serializable{
	public String print();
}
