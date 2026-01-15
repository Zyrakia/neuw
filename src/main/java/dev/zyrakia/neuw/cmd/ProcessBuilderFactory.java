package dev.zyrakia.neuw.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jline.utils.OSUtils;

/**
 * This class facilitates creation of {@link ProcessBuilders} by having the
 * ability to automatically detect the shell prefix for certain platforms.
 */
public class ProcessBuilderFactory {

    /**
     * The shell prefix of every builder created with this factory.
     */
    private final List<String> shellPrefix;

    /**
     * The CWD of every builder created with this factory.
     */
    private final File cwd;

    /**
     * The added customized ENV of every builder created with this factory.
     */
    private final Map<String, String> env;

    /**
     * Creates a new factory with a customized shell prefix, CWD and added
     * customized ENV.
     * 
     * @param shellPrefix the commands used to prefix any commands run, this is
     * to launch the shell (i.e. "cmd", "/c" or "bin.sh", "-c")
     * @param cwd the working directory of any builders created
     * @param env the added ENV of any builders created
     */
    public ProcessBuilderFactory(List<String> shellPrefix, File cwd,
            Map<String, String> env) {
        this.shellPrefix = shellPrefix;
        this.cwd = cwd;
        this.env = env;
    }

    /**
     * Creates a new factory with an auto-detected shell prefix (based on
     * current OS).
     * 
     * @param cwd the working directory of any builders created
     * @param env the added ENV of any builders created
     */
    public ProcessBuilderFactory(File cwd, Map<String, String> env) {
        this(ProcessBuilderFactory.autoDetectShellPrefix(), cwd, env);
    }

    /**
     * Creates a new builder of the given command. This builder will be dressed
     * with the shell prefix, CWD and ENV of this factory.
     * 
     * @param cmd the command that the builder will hold
     * @return the created builder
     */
    public ProcessBuilder createBuilder(String cmd) {
        ProcessBuilder pb = new ProcessBuilder();

        List<String> command = new ArrayList<>(this.shellPrefix);
        command.add(cmd);

        pb.command(command);
        pb.directory(this.cwd);
        pb.environment().putAll(this.env);

        return pb;
    }

    /**
     * Detects the current OS, and returns the generally applicable shell
     * prefix.
     * 
     * @return the shell prefix (a list of commands preprended to all other
     * commands)
     */
    private static List<String> autoDetectShellPrefix() {
        List<String> cmds = new ArrayList<>();

        if (OSUtils.IS_WINDOWS) {
            cmds.add("cmd.exe");
            cmds.add("/c");
        } else if (OSUtils.IS_LINUX) {
            cmds.add("/bin/sh");
            cmds.add("-c");
        }

        return cmds;
    }

}
