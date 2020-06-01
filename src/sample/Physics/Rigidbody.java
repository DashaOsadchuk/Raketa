package sample.Physics;

public class Rigidbody {
    protected double y;
    double velocity;
    double mass;

    public Rigidbody(double mass, double yPosition, double initialVelocity) {
        this.mass = mass;
        this.y = yPosition;
        this.velocity = initialVelocity;
    }

    public void addForce(double force, boolean impulse) { // добавляем силу к телу
        if (impulse) { // если толкаем тело, к примеру как двигатели толкают ракету
            this.velocity += force/this.mass;
        } else { // если добавляем ускорение, как планета притягивает ракету
            this.velocity += force;
        }
    }

    public void Update() { // обновляем позицию ракеты
        this.y += this.velocity/50;
    }

    public double Speed() {
        return this.velocity;
    }

    public double y() {
        return this.y;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}