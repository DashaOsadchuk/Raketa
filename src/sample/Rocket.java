package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.Physics.Rigidbody;

public class Rocket {
    Rigidbody rb;
    double fuel;
    double engineThrust;
    double rawMass;

    public Rocket(double rawMass, double position, double initialVelocity, double fuel, double engineThrust) {
        double mass = fuel*10+rawMass;
        this.rawMass = rawMass;
        this.rb = new Rigidbody(mass, position, initialVelocity);
        this.fuel = fuel;
        this.engineThrust = engineThrust;
    }

    public void thrust() { // действия при использовании двигателей
        fuel -= 0.1;
        rb.setMass(fuel*10+rawMass);
        rb.addForce(engineThrust, true);
    }

    public void draw(GraphicsContext gc, Image rocket, double w, double h) {
        gc.drawImage(rocket, w/2-25, h/4-59+4, 50, 108);
    }

    public Rigidbody getRB() {
        return this.rb;
    }

    public double GetFuel() {
        return this.fuel;
    }
}