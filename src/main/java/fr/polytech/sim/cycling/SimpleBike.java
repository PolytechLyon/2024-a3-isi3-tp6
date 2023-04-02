package fr.polytech.sim.cycling;

import fr.polytech.sim.transport.Wheel;

/**
 * Two-wheel bike.
 */
public class SimpleBike extends Bike {

    /**
     * Constructor.
     */
    public SimpleBike() {
        components.add(new Wheel(this));
        components.add(new Wheel(this));
    }
}
