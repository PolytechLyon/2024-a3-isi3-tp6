package fr.polytech.sim.log;

/**
 * No-op logger.
 */
public class NoopLogger implements Logger {

    public void log(String format, Object... args) {
        // Do nothing
    }
}
