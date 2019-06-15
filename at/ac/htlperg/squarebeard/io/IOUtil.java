package at.ac.htlperg.squarebeard.io;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.media.Media;

public class IOUtil {

	private IOUtil() {
	}

	public static URI resourceURI(String subFolder) throws URISyntaxException {
		URL url = IOUtil.class.getResource("../../../../../" + subFolder);
		if (url == null) {
			return null;
		}
		return url.toURI();
	}

	public static Image loadImage(String name) {
		try {
			URI uri = resourceURI(name);
			if (uri == null) {
				return null;
			}
			return new Image(uri.toString());
		} catch (URISyntaxException e) {
		}
		return null;
	}

	public static File resourceFile(String subFolder) {
		try {
			return new File(resourceURI(subFolder));
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public static Media loadSound(String path) {
		Media media = new Media(IOUtil.class.getResource("../../../../../" + path).toString());
		return media;
	}

}
