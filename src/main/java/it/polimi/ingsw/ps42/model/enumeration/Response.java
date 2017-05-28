package it.polimi.ingsw.ps42.model.enumeration;

public enum Response {
	SUCCESS("Action Done with Success"), FAILURE("Action not completed"),
	LOW_LEVEL("Low Level"), CANNOT_PLAY("Player can't play");
	
	private final String message;
	
	private Response(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}
