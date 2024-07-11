package dev.zyrakia.neuw.exception;

import java.nio.file.Path;

/**
 * This exception is thrown when a content provider cannot match a given path to
 * content.
 */
public class UnmatchedPathException extends Exception {

	/**
	 * Creates a new exception with the given path.
	 *
	 * @param path the path that could not be matched to content
	 */
	public UnmatchedPathException(Path path) {
		super("The given path \"" + path + "\" cannot be matched to content.");
	}

}
