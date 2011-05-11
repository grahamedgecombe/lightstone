package net.lightstone.io.service;

import net.lightstone.io.service.impl.FlatFileIoService;

public class IoServiceFactory {
	public static IoService<?> getIoService(String type) {
		if(type == null) {
			throw new IllegalArgumentException("Can not have a null IoService.");
		}
		type = type.toUpperCase();
		if(type.equals("FLAT")) {
			return new FlatFileIoService();
		}
		return null;
	}
}
