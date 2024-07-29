package dev.zyrakia.neuw.structure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dev.zyrakia.neuw.evaluation.ContentEvaluator;
import dev.zyrakia.neuw.exception.UnmatchedPathException;
import dev.zyrakia.neuw.structure.content.ContentProvider;

/**
 * Represents a writable file/folder structure at a given location.
 */
public class Structure {

    /**
     * The root path of the structure.
     */
    private Path root;

    /**
     * The root items of the structure.
     */
    private List<StructureItem> rootItems;

    /**
     * Creates a new structure at the given root with the given item.
     * 
     * @param root      the root path of the structure
     * @param rootItems the items at the root path
     */
    public Structure(Path root, List<StructureItem> rootItems) {
        this.root = root;
        this.rootItems = rootItems;
    }

    /**
     * Maps the given items to their absolute paths based on the given root path.
     * 
     * @param root      the root path to map the items at
     * @param items     the items to map to the path
     * @param evaluator the evaluator to evaluate the item names
     * @return the absolute paths mapped to each item
     */
    private static Map<StructureItem, Path> mapToAbsolutes(Path root, List<StructureItem> items,
            ContentEvaluator evaluator) {
        Map<StructureItem, Path> absolutes = new LinkedHashMap<>();

        for (StructureItem item : items) {
            String name = evaluator.evaluate(item.getName(), item.getName());
            if (name.isEmpty())
                continue;

            Path itemRoot = root.resolve(name);
            absolutes.put(item, itemRoot);

            if (item.isDirectory())
                absolutes.putAll(Structure.mapToAbsolutes(itemRoot, item.getChildren(), evaluator));
        }

        return absolutes;
    }

    /**
     * Writes the structure at the root path, with content from the given provider.
     * 
     * @param provider  the provider of the content for each item
     * @param evaluator the evaluator to evaluate item names and content
     * @return the write results for each item of this structure
     */
    public Map<Path, WriteResult> write(ContentProvider provider, ContentEvaluator evaluator) {
        Map<StructureItem, Path> absolutes = Structure.mapToAbsolutes(this.root, this.rootItems, evaluator);
        Map<Path, WriteResult> results = new LinkedHashMap<>(absolutes.size());

        for (Map.Entry<StructureItem, Path> entry : absolutes.entrySet()) {
            StructureItem item = entry.getKey();
            Path path = entry.getValue();

            if (item.isDirectory()) {
                boolean created = path.toFile().mkdir();
                results.put(path, created ? WriteResult.WRITTEN : WriteResult.EXISTS);
                continue;
            }

            try {
                boolean created = path.toFile().createNewFile();
                if (!created)
                    results.put(path, WriteResult.EXISTS);
            } catch (IOException e) {
                results.put(path, WriteResult.ERROR);
                continue;
            }

            try {
                String content = evaluator.evaluate(provider.evaluate(path), item.getName());
                Files.writeString(path, content);
            } catch (UnmatchedPathException e) {
                results.put(path, WriteResult.NO_CONTENT);
            } catch (IOException e) {
                results.put(path, WriteResult.ERROR);
            }
        }

        return results;
    }

}
