package dev.zyrakia.neuw.structure.content;

import java.nio.file.Path;

import dev.zyrakia.neuw.exception.UnmatchedPathException;

/**
 * Represents a lookup table for structure item content based on resolved
 * relative paths.
 */
public interface ContentProvider {

	/**
	 * Evaluates the given path to the content at that path.
	 *
	 * @param path the path to evaluate
	 * @return the content at the path
	 * @throws UnmatchedPathException if the given path cannot be matched to
	 * content
	 */
	public String evaluate(Path path) throws UnmatchedPathException;

}
