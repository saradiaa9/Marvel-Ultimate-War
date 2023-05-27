package exceptions;

public class NotEnoughResourcesException extends GameActionException {

	public NotEnoughResourcesException() {
		super();
	}
	
	public NotEnoughResourcesException(String s) {
		super(s);
		
	}
}
//Not enough resources for this action//