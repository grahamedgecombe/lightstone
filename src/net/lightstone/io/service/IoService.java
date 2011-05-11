package net.lightstone.io.service;

import java.io.IOException;

/**
 * This abstract class should be extended by classes that wish to provide some
 * way of performing I/O.
 * 
 * @author Joe Pritzel
 * 
 * @param <T>
 */
public abstract class IoService<T> {

	protected Object location;

	/**
	 * 
	 * @param key
	 *            -the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this
	 *         map contains no mapping for the key
	 * @throws IOException
	 */
	protected abstract T read(Object key) throws IOException;

	/**
	 * 
	 * @param key
	 *            - the key
	 * @param value
	 *            - the value
	 * @return the previous value of the specified key, or null if it did not
	 *         have one
	 * @throws IOException
	 */
	protected abstract T write(Object key, T value) throws IOException;

	/**
	 * 
	 * @param location
	 *            - the location of the keys/values
	 * @return returns true if the location was successfully set
	 */
	public boolean setLocation(Object location) {
		this.location = location;
		return true;
	}

	/**
	 * 
	 * @return returns the current location
	 */
	public Object getLocation() {
		return location;
	}
}
