package dev.zyrakia.neuw.construction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class enables object instantiation from a set of predefined properties.
 * Each object is inspected to determine which propeties are required, and if
 * available in this creator, they are used to instantiate the object.
 */
public class PropertyClassCreator {

    /**
     * This is a hard-coded map of primitive types to their wrapper classes.
     * This is required to ensure that the instance check works correctly when
     * validating the types of parameters.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS = Map
            .ofEntries(Map.entry(boolean.class, Boolean.class),
                    Map.entry(byte.class, Byte.class),
                    Map.entry(char.class, Character.class),
                    Map.entry(double.class, Double.class),
                    Map.entry(float.class, Float.class),
                    Map.entry(int.class, Integer.class),
                    Map.entry(long.class, Long.class),
                    Map.entry(short.class, Short.class),
                    Map.entry(void.class, Void.class));

    /**
     * The properties that are available to this creator for instantiation.
     */
    private final Map<String, ?> properties;

    /**
     * Creates a new creator with the given properties.
     * 
     * @param properties the properties that will be available for instantiation
     */
    public PropertyClassCreator(Map<String, ?> properties) {
        this.properties = properties;
    }

    /**
     * Creates an instance of the given class using the properties available to
     * this creator.
     * 
     * @param <T>   the type of the class to create
     * @param clazz the class to create
     * @return the created instance
     * @throws InstantiationException if, for any reason, the class could not be
     *                                created
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> clazz) throws InstantiationException {
        Creatable<?> creatable = this.getCreatable(clazz);
        if (creatable == null)
            throw new InstantiationException("No constructor found on \""
                    + clazz.getSimpleName() + "\" that can be created with:\n"
                    + this.getTypeAssociationString());

        Object instance;
        try {
            instance = creatable.create();
        } catch (IllegalAccessException e) {
            throw new InstantiationError("The class \"" + clazz.getSimpleName()
                    + "\" has a matching constructor, but is not accessible.");
        } catch (IllegalArgumentException e) {
            throw new InstantiationError("The class \"" + clazz.getSimpleName()
                    + "\" has a matching constructor, but is not compatible.");
        } catch (InvocationTargetException e) {
            throw new InstantiationError("The class \"" + clazz.getSimpleName()
                    + "\" has a matching constructor, but threw an exception during instantiation:\n"
                    + e.getCause().getMessage());
        }

        if (!clazz.isInstance(instance))
            throw new InstantiationError("The class \""
                    + instance.getClass().getSimpleName()
                    + "\" was created, but is not an instance of initial class \""
                    + clazz.getSimpleName() + "\".");

        return (T) instance;
    }

    /**
     * Gets the first constructor that can be used to create an instance of the
     * given class.
     * 
     * @param clazz the class to create
     * @return the first constructor that can be used to create an instance of
     *         the given class, wrapped inside a {@link Creatable} object, or
     *         null
     */
    private Creatable<?> getCreatable(Class<?> clazz) {
        List<Creatable<?>> candidates = this.getCreatableCandidates(clazz);

        int mostArgs = 0;
        Creatable<?> mostArgsCandidate = null;

        for (Creatable<?> candidate : candidates) {
            int args = candidate.arguments().length;
            if (args < mostArgs)
                continue;

            mostArgs = args;
            mostArgsCandidate = candidate;
        }

        return mostArgsCandidate;
    }

    private List<Creatable<?>> getCreatableCandidates(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();
        List<Creatable<?>> candidates = new ArrayList<>();

        for (Constructor<?> constructor : constructors) {
            Object[] params = this.compileParameters(constructor);
            if (params == null)
                continue;

            candidates.add(new Creatable<>(constructor, params));
        }

        return candidates;
    }

    /**
     * Compiles the parameters of the given constructor into an array of objects
     * that can be passed to the constructor.
     * 
     * This will extract the requested properties from the
     * {@link PropertyCreatable} annotation into arguments, and ensure that the
     * parameters of the constructor are compatible with the available
     * arguments.
     * 
     * @param constructor the constructor to compile parameters for
     * @return the compiled parameters, or null
     */
    private Object[] compileParameters(Constructor<?> constructor) {
        PropertyCreatable annotation = constructor
                .getAnnotation(PropertyCreatable.class);
        if (annotation == null)
            return null;

        Object[] requestedArgs = this.extractRequestedArguments(annotation);
        if (requestedArgs == null)
            return null;

        if (!doesConstructorMatchArguments(constructor, requestedArgs))
            return null;

        return requestedArgs;
    }

    /**
     * Extracts the requested properties from the given
     * {@link PropertyCreatable} annotation, and returns them as an array of
     * objects to be passed to the constructor as arguments.
     * 
     * @param annotation the annotation to extract arguments from
     * @return the extracted arguments, or null
     */
    private Object[] extractRequestedArguments(PropertyCreatable annotation) {
        String[] requestedArguments = annotation.value();
        Object[] params = new Object[requestedArguments.length];

        for (int i = 0; i < requestedArguments.length; i++) {
            if (!this.properties.containsKey(requestedArguments[i]))
                return null;

            params[i] = this.properties.get(requestedArguments[i]);
        }

        return params;
    }

    /**
     * Returns a string representation of the properties available to this
     * creator.
     * 
     * @return a JSON-like string representation of the properties
     */
    private String getTypeAssociationString() {
        return "{\n" + this.properties.entrySet().stream().map((e) -> {
            return "\t" + e.getKey() + " ("
                    + e.getValue().getClass().getSimpleName() + ")";
        }).collect(Collectors.joining(",\n")) + "\n}";
    }

    /**
     * Ensures that the given constructors parameters match the given arguments.
     * The result of this function will indicate whether the arguments can be
     * used to invoke the constructor.
     * 
     * @param constructor the constructor to check
     * @param args        the arguments to check
     * @return whether the arguments can be used to invoke the constructor
     */
    private static boolean doesConstructorMatchArguments(
            Constructor<?> constructor, Object[] args) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        if (paramTypes.length != args.length)
            return false;

        for (int i = 0; i < paramTypes.length; i++) {
            Class<?> requiredType = paramTypes[i];
            if (PRIMITIVE_WRAPPERS.containsKey(requiredType))
                requiredType = PRIMITIVE_WRAPPERS.get(requiredType);

            if (!requiredType.isInstance(args[i]))
                return false;
        }

        return true;
    }

}
