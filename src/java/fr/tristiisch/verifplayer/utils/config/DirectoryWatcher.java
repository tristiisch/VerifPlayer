package fr.tristiisch.verifplayer.utils.config;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import fr.tristiisch.verifplayer.VerifPlayer;

public class DirectoryWatcher {

	public static void start(VerifPlayer plugin) {
		// get path object pointing to the directory we wish to monitor
		Path path = plugin.getDataFolder().toPath();
		try {
			// get watch service which will monitor the directory
			WatchService watcher = path.getFileSystem().newWatchService();
			// associate watch service with the directory to listen to the event
			// types
			path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
			System.out.println("Monitoring directory for changes... " + path);
			// listen to events
			WatchKey watchKey = watcher.take();
			// get list of events as they occur
			List<WatchEvent<?>> events = watchKey.pollEvents();
			// iterate over events
			for (WatchEvent<?> event : events) {
				// check if the event refers to a new file created
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					// print file name which is newly created
					System.out.println("Created: " + event.context().toString());
				}

				System.out.println(event.kind() + ": " + event.context().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
