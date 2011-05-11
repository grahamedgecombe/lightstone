package net.lightstone.io.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import net.lightstone.io.service.IoService;

/**
 * This is an IoService that is used for reading and writing keys, and values to
 * flat files via the {@link Properties} class.<br>
 * <br>
 * It makes use of delayed writing, to reduce the amount of times it writes the
 * keys/values to disk.
 * 
 * 
 * @author Joe Pritzel
 * 
 */
public class FlatFileIoService extends IoService<Object> {

	private static final int DEFAULT_WRITE_DELAY = 1000;

	private static final Logger logger = Logger
			.getLogger(FlatFileIoService.class.getName());

	private final Properties p = new Properties();
	private File location;

	private volatile long lastModified = 0L;
	private volatile boolean scheduledStore = false;

	private Timer timer = new Timer();
	private TimerTask storeTask = new TimerTask() {

		@Override
		public void run() {
			if (scheduledStore) {
				store();
			}
		}

	};

	@Override
	protected Object read(Object key) throws IOException {
		if (key == null) {
			return null;
		}
		reloadIfChanged();
		return p.get(key);
	}

	@Override
	protected Object write(Object key, Object value) throws IOException {
		scheduleStore();
		return p.put(key, value);
	}

	@Override
	public boolean setLocation(Object location) {
		if (location instanceof String) {
			this.location = new File((String) location);
		} else if (location instanceof File) {
			this.location = (File) location;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Reloads the file if there has been a change of the file since the last
	 * write to disk.
	 * 
	 * @throws IOException
	 */
	private void reloadIfChanged() throws IOException {
		if (lastModified != location.lastModified()) {
			p.load(new FileInputStream(location));
		}
	}

	/**
	 * Schedules a write to disk after the default write delay.
	 */
	private void scheduleStore() {
		scheduleStore(DEFAULT_WRITE_DELAY);
	}

	/**
	 * Schedules a write to disk after the specified write delay.
	 */
	private void scheduleStore(long delay) {
		if (!scheduledStore) {
			timer.schedule(storeTask, delay);
			scheduledStore = true;
		}
	}

	/**
	 * Writes the properties to disk.
	 */
	void store() {
		try {
			p.store(new FileWriter(location), null);
		} catch (IOException e) {
			logger.warning(e.getLocalizedMessage());
		}
		lastModified = location.lastModified();
		scheduledStore = false;
	}
}
