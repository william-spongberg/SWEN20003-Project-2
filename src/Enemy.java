import bagel.Image;
import bagel.Input;

import java.util.Random;

/**
 * The Enemy class represents an enemy entity in the game. It extends the Entity
 * class and implements its abstract methods.
 * The enemy moves horizontally across the screen and can collide with notes.
 */
public class Enemy extends Entity {
    // enemy image
    private final Image IMAGE_ENEMY = new Image("res/enemy.png");

    // note collision range
    private final int NOTE_COLLISION = 104;

    // enemy spawn range
    private static final int MIN_X = 100;
    private static final int MAX_X = 900;
    private static final int MIN_Y = 100;
    private static final int MAX_Y = 500;

    // attributes
    private int x = 0;
    private int y = 0;
    private static final int SPEED = 1;
    private int dir = 1;

    /**
     * The Enemy class represents an enemy object in the game.
     * It has a random x and y position within a specified range, and a random direction.
     */
    public Enemy() {
        this.x = new Random().nextInt(MAX_X - MIN_X) + MIN_X;
        this.y = new Random().nextInt(MAX_Y - MIN_Y) + MIN_Y;

        // random dir backwards or forwards
        this.dir = ((int) Math.random() * 2 == 1) ? 1 : -1;
    }

    /**
     * Updates the position of the enemy based on the current frame and input.
     * Moves the enemy horizontally at a constant speed in the direction it is facing.
     * If the enemy reaches the left or right boundary, it changes direction.
     * @param frame the current frame of the game
     * @param input the input from the player
     */
    @Override
    public void update(int frame, Input input) {
        this.x += SPEED * this.dir;

        if (this.x > MAX_X || this.x < MIN_X) {
            this.dir *= -1;
        }

        IMAGE_ENEMY.draw(this.x, this.y);
    }

    /**
     * Checks if the enemy collides with a given note.
     * @param note the note to check collision with
     * @return true if the enemy collides with the note, false otherwise
     */
    public Boolean collideNote(Note note) {
        // if within collision range
        if (Math.sqrt(Math.pow(this.x - note.getX(), 2) + Math.pow(this.y - note.getY(), 2)) < NOTE_COLLISION) {
            return true;
        }
        return false;
    }

    /* getters */

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getSpeed() {
        return SPEED;
    }
}
