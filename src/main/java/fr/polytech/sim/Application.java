package fr.polytech.sim;

import fr.polytech.sim.cycling.BikeSimulator;

/**
 * Application's main classe.
 */
public class Application {

    /**
     * Entry point.
     * @param args  app's arguments.
     */
    public static void main(String... args) {
        new BikeSimulator().run();
    }
}
