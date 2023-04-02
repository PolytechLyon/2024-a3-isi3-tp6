package fr.polytech.sim.transport;

/**
 * Any object with velocity and mass.
 */
public interface MobileObject {

    /**
     * The velocity of the moving object.
     *
     * @return  linear speed in Km/h
     */
    double getVelocity();

    /**
     * The mass of the object.
     *
     * @return  mass in Kg
     */
    double getMass();
}
