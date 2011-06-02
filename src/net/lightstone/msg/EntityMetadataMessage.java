package net.lightstone.msg;

import net.lightstone.util.Parameter;

public final class EntityMetadataMessage extends Message {

	private final int id;
	private final Parameter<?>[] parameters;

	public EntityMetadataMessage(int id, Parameter<?>[] parameters) {
		this.id = id;
		this.parameters = parameters;
	}

	public int getId() {
		return id;
	}

	public Parameter<?>[] getParameters() {
		return parameters;
	}

}

