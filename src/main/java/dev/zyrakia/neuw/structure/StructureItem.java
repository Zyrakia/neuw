package dev.zyrakia.neuw.structure;

import java.util.List;

/**
 * Represents a part of the project structure, either writable file or folder.
 */
public class StructureItem {

    /**
     * The name of the structure item, this could potentially include an expression,
     * so before it is used it should be evaluated.
     */
    private final String name;

    /**
     * The children of this structure item, this will only be defined if the given
     * structure item is a directory.
     */
    private final List<StructureItem> children;

    /**
     * Creates a new structure item.
     * 
     * @param name     the name of the item
     * @param children the children, null if this is a file
     */
    private StructureItem(String name, List<StructureItem> children) {
        this.name = name;
        this.children = children;
    }

    /**
     * Creates a new file structure item.
     * 
     * @param name the name of the item
     * @return the created file structure item
     */
    public static StructureItem file(String name) {
        return new StructureItem(name, null);
    }

    /**
     * Creates a new directory structure item.
     * 
     * @param name     the name of the item
     * @param children the children items of the directory
     * @return the created directory structure item
     */
    public static StructureItem dir(String name, StructureItem... children) {
        return new StructureItem(name, List.of(children));
    }

    /**
     * Returns whether this structure item is a directory.
     * 
     * @return true if it is a directory, false otherwise
     */
    public boolean isDirectory() {
        return this.children != null;
    }

    /**
     * Returns whether this structure item is a file.
     * 
     * @return true if it is a file, false otherwise
     */
    public boolean isFile() {
        return this.children == null;
    }

    /**
     * Returns the children of this structure item. This will only be defined if
     * this item is a directory.
     * 
     * @return the children, or null
     */
    public List<StructureItem> getChildren() {
        return this.children;
    }

    /**
     * Returns the name of this structure item.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

}
