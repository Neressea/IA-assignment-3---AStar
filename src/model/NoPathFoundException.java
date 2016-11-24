package model;

/**
 * 
 * Thrown if the empty list is empty.
 *
 */
public class NoPathFoundException extends Exception {
	public NoPathFoundException(String mess) {
		super(mess);
	}
}
