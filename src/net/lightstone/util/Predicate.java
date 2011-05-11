package net.lightstone.util;

/**
 * An interface used for filtering various collections, among other things.
 * @author Joe Pritzel
 *
 * @param <T>
 */
public interface Predicate<T> {
	/**
	 * 
	 * @param input
	 * @return Returns the result of applying this predicate to input. 
	 */
	public boolean apply(T input);
}
