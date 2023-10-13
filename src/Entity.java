import bagel.Input;

/**
 * The Entity class is an abstract class that represents a game entity.
 * It provides methods for updating the entity's state and getting its position
 * and speed.
 */
public abstract class Entity {

    /**
     * Updates the entity's state based on the current frame and input.
     * 
     * @param frame The current frame number.
     * @param input The input object containing user input.
     */
    public abstract void update(int frame, Input input);

    /**
     * @return The x-coordinate of the entity's position.
     */
    public abstract int getX();

    /**
     * @return The y-coordinate of the entity's position.
     */
    public abstract int getY();

    /**
     * @return The speed of the entity.
     */
    public abstract int getSpeed();
}
