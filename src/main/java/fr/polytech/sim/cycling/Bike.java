package fr.polytech.sim.cycling;

import fr.polytech.sim.transport.Vehicle;

/**
 * Abstract bike.
 */
public abstract class Bike extends Vehicle {

    @Override
    public double getPush() {
        return 25.0;
    }
}
