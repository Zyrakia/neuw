package dev.zyrakia.neuw.structure;

import java.util.List;

/**
 * Represents a part of the project structure, either writable file or folder.
 */
public class StructureItem {

	/**
	 * The name of the structure item, this will be the file name created.
	 */
	private final String name;

	/**
	 * The expression that is evaluated when building this item to determine if it
	 * should be included in the current build.
	 */
	private final String conditionExpression;

	/**
	 * The children of this structure item, this will only be defined if the given
	 * structure item is a directory.
	 */
	private final List<StructureItem> children;

	/**
	 * Creates a new structure item.
	 * 
	 * @param name                the name of the item
	 * @param conditionExpression the condition of the item
	 * @param children            the children, null if this is a file
	 */
	private StructureItem(String name, String conditionExpression, List<StructureItem> children) {
		this.name = name;
		this.conditionExpression = conditionExpression;
		this.children = children;
	}

	/**
	 * Creates a new file structure item.
	 * 
	 * @param name                the name of the item
	 * @param conditionExpression the condition of the item
	 * @return the created file structure item
	 */
	public static StructureItem file(String name, String conditionExpression) {
		return new StructureItem(name, conditionExpression, null);
	}

	/**
	 * Creates a new file structure item.
	 * 
	 * @param name the name of the item
	 * @return the created file structure item
	 */
	public static StructureItem file(String name) {
		return file(name, "");
	}

	/**
	 * Creates a new directory structure item.
	 * 
	 * @param name                the name of the item
	 * @param conditionExpression the condition of the item
	 * @param children            the children items of the directory
	 * @return the created directory structure item
	 */
	public static StructureItem dir(String name, String conditionExpression, StructureItem... children) {
		return new StructureItem(name, conditionExpression, List.of(children));
	}

	/**
	 * Creates a new directory structure item.
	 * 
	 * @param name     the name of the item
	 * @param children the children items of the directory
	 * @return the created directory structure item
	 */
	public static StructureItem dir(String name, StructureItem... children) {
		return dir(name, "", children);
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

	/**
	 * Returns the condition expression of this structure item.
	 * 
	 * @return the condition expression
	 */
	public String getConditionExpression() {
		return this.conditionExpression;
	}

}
