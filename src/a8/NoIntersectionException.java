package a8;

@SuppressWarnings(value = {"serial" })
public class NoIntersectionException extends Exception {

	public NoIntersectionException() {
		super("Empty intersection");
	}
}
