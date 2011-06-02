package net.lightstone.msg.handler;

import java.util.HashMap;
import java.util.Map;

import net.lightstone.msg.ChatMessage;
import net.lightstone.msg.HandshakeMessage;
import net.lightstone.msg.IdentificationMessage;
import net.lightstone.msg.KickMessage;
import net.lightstone.msg.Message;
import net.lightstone.msg.PositionMessage;
import net.lightstone.msg.PositionRotationMessage;
import net.lightstone.msg.RotationMessage;
import net.lightstone.msg.DiggingMessage;
import net.lightstone.msg.AnimateEntityMessage;
import net.lightstone.msg.EntityActionMessage;
import net.lightstone.msg.BlockPlacementMessage;
import net.lightstone.msg.ActivateItemMessage;

/**
 * A class used to look up message handlers.
 * @author Graham Edgecombe
 */
public final class HandlerLookupService {

	/**
	 * A table which maps messages to their handles.
	 */
	private static final Map<Class<? extends Message>, MessageHandler<?>> handlers = new HashMap<Class<? extends Message>, MessageHandler<?>>();

	/**
	 * Populates the table with message handlers.
	 */
	static {
		try {
			bind(IdentificationMessage.class, IdentificationMessageHandler.class);
			bind(HandshakeMessage.class, HandshakeMessageHandler.class);
			bind(ChatMessage.class, ChatMessageHandler.class);
			bind(PositionMessage.class, PositionMessageHandler.class);
			bind(RotationMessage.class, RotationMessageHandler.class);
			bind(PositionRotationMessage.class, PositionRotationMessageHandler.class);
			bind(KickMessage.class, KickMessageHandler.class);
			bind(DiggingMessage.class, DiggingMessageHandler.class);
			bind(AnimateEntityMessage.class, AnimateEntityMessageHandler.class);
			bind(EntityActionMessage.class, EntityActionMessageHandler.class);
			bind(BlockPlacementMessage.class, BlockPlacementMessageHandler.class);
			bind(ActivateItemMessage.class, ActivateItemMessageHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Binds a handler by adding entries for it in the table.
	 * @param <T> The type of message.
	 * @param clazz The message's class.
	 * @param handlerClass The handler's class.
	 * @throws InstantiationException if the handler could not be instantiated.
	 * @throws IllegalAccessException if the handler could not be instantiated
	 * due to an access violation.
	 */
	private static <T extends Message> void bind(Class<T> clazz, Class<? extends MessageHandler<T>> handlerClass) throws InstantiationException, IllegalAccessException {
		MessageHandler<T> handler = handlerClass.newInstance();
		handlers.put(clazz, handler);
	}

	/**
	 * Finds a handler by message class.
	 * @param <T> The type of message.
	 * @param clazz The message's class.
	 * @return The message's handler, or {@code null} if no handler exists.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Message> MessageHandler<T> find(Class<T> clazz) {
		return (MessageHandler<T>) handlers.get(clazz);
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private HandlerLookupService() {

	}

}

