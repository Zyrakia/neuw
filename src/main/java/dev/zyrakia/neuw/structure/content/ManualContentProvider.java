package dev.zyrakia.neuw.structure.content;

import dev.zyrakia.neuw.exception.UnmatchedPathException;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * This content provider allows for manually setting content at a given path.
 */
public class ManualContentProvider implements StructureContentProvider {

	/**
	 * The internal record of paths to their content.
	 */
	private final HashMap<Path, String> contentTable = new HashMap<>();

	/**
	 * Sets the given content at the given path.
	 *
	 * @param path    the path to set the content at
	 * @param content the content to set
	 */
	public void set(Path path, String content) {
		this.contentTable.put(path, content);
	}

	/**
	 * Clears the content at the given path.
	 *
	 * @param path the path to clear
	 */
	public void clear(Path path) {
		this.contentTable.remove(path);
	}

	@Override
	public String evaluate(Path path) throws UnmatchedPathException {
		for (Map.Entry<Path, String> entry : this.contentTable.entrySet()) {
			if (entry.getKey().equals(path))
				return entry.getValue();
		}

		throw new UnmatchedPathException(path);
	}

}
