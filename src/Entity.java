import bagel.Input;

public abstract class Entity {
    public abstract void update(int frame, Input input);
    public abstract int getX();
    public abstract int getY();
    public abstract int getSpeed();
}
