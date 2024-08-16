package dev.zyrakia.neuw.structure;

import java.nio.file.Path;

/**
 * Represents the result of a write operation of a {@link StructureItem}.
 * 
 * @param item the item that was written
 * @param absPath the absolute path of the item, where the writing occurred
 * @param success whether the item was able to be written
 * @param overwritten whether the item was created over an already existing item
 * @param contentWritten whether any content was written to the file, if the
 * result is about a file
 */
public record WriteResult(StructureItem item, Path absPath, boolean success,
        boolean overwritten, boolean contentWritten) {

    /**
     * Creates a new write result based off of the given item. The created
     * result will indicate that the given item was written successfully, with
     * content if applicable.
     * 
     * @param item the item that was written
     * @param absPath the absolute path of where the item was written
     * @param overwritten whether the item already existed on the disk at the
     * time of writing
     * @return the created result
     */
    public static WriteResult written(StructureItem item, Path absPath,
            boolean overwritten) {
        return new WriteResult(item, absPath, true, overwritten, item
                .isDirectory() ? false : true);
    }

    /**
     * Creates a new result based off of the given item. The created result will
     * indicate that the given item was written successfully, with content if
     * applicable.
     * 
     * @param item the item that was written
     * @param absPath the absolute path of where the item was written
     * @return the created result
     */
    public static WriteResult written(StructureItem item, Path absPath) {
        return WriteResult.written(item, absPath, false);
    }

    /**
     * Creates a new result based off of the given item. The created result will
     * indicate that the given item was written successfully, but no content was
     * found or available for it.
     * 
     * @param item the item that was written
     * @param absPath the absolute path of where the item was written
     * @param overwritten whether the item already existed on the disk at the
     * time of writing
     * @return the created result
     */
    public static WriteResult writtenEmpty(StructureItem item, Path absPath,
            boolean overwritten) {
        return new WriteResult(item, absPath, true, overwritten, false);
    }

    /**
     * Creates a new result based off of the given item. The created result will
     * indicate that the given item was written successfully, but no content was
     * found or available for it.
     * 
     * @param item the item that was written
     * @param absPath the absolute path of where the item was written
     * @return the created result
     */
    public static WriteResult writtenEmpty(StructureItem item, Path absPath) {
        return WriteResult.writtenEmpty(item, absPath, false);
    }

    /**
     * Creates a new result based off of the given item. The created result will
     * indicate that the write operation was not successful.
     * 
     * @param item the item that was attempted to be written
     * @param absPath the absolute path of where the item should have been
     * written
     * @return the created result
     */
    public static WriteResult err(StructureItem item, Path absPath) {
        return new WriteResult(item, absPath, false, false, false);
    }

}