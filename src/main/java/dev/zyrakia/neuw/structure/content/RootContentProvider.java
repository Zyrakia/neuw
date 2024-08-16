package dev.zyrakia.neuw.structure.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.zyrakia.neuw.exception.UnmatchedPathException;

/**
 * This content provider allows for automatic file lookup and reading when
 * evaluating a path. The path will be evaluated after being resolved with a
 * root path.
 * 
 * Any paths that cannot be accessed, will resolve via an
 * {@link UnmatchedPathException}.
 */
public class RootContentProvider implements ContentProvider {

    /**
     * Represents the root path of this provider.
     */
    private Path rootPath;

    /**
     * Creates a new content provider with the given root path to resolve all
     * content.
     * 
     * @param rootPath the root path at which to evaluate content from
     */
    public RootContentProvider(Path rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String evaluate(Path path) throws UnmatchedPathException {
        Path absPath = this.rootPath.resolve(path);
        if (!absPath.toFile().isFile()) throw new UnmatchedPathException(path);

        try {
            return Files.readString(absPath);
        } catch (IOException e) {
            throw new UnmatchedPathException(path);
        }
    }

}
