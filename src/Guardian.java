import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

/**
 * The Guardian class represents the player-controlled entity in the game. It
 * extends the Entity class and contains attributes for enemies and projectiles.
 * The Guardian can spawn projectiles and defeat enemies by hitting them with
 * projectiles. It also has a reset method to clear the enemies and projectiles.
 */
public class Guardian extends Entity {
    // guardian image
    private final Image IMAGE_GUARDIAN = new Image("res/guardian.png");

    // guardian spawn position
    public static final int X = 800;
    public static final int Y = 600;

    // attributes
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Constructs Guardian object.
     * Resets the guardian's state.
     */
    public Guardian() {
        reset();
    }

    /**
     * Updates the state of the Guardian object.
     * Draws the Guardian image, spawns projectiles on player input, updates
     * projectiles and removes them if they hit an enemy, and updates the state of
     * all enemies.
     * 
     * @param frame the current frame of the game
     * @param input the input object containing player input
     */
    @Override
    public void update(int frame, Input input) {
        IMAGE_GUARDIAN.draw(X, Y);

        // player input here, spawn projectiles, etc
        if (input.wasPressed(Keys.LEFT_SHIFT)) {
            this.projectiles.add(new Projectile(this.enemies));
        }

        // update projectiles
        Iterator<Projectile> iterator = this.projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update(frame, input);
            if (projectile.hasHitEnemy()) {
                this.enemies.remove(projectile.getCurrentEnemy());
                iterator.remove();
            }
        }

        // update enemies
        for (final Enemy enemy : this.enemies) {
            if (enemy != null)
                enemy.update(frame, input);
        }
    }

    private void reset() {
        this.enemies.clear();
        this.projectiles.clear();
    }

    /* getters */

    @Override
    public int getX() {
        return X;
    }

    @Override
    public int getY() {
        return Y;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    /**
     * @return a list of Enemy objects representing the enemies in the game
     */
    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    /* setters */

    /**
     * Sets the list of enemies for this Guardian.
     * 
     * @param enemies the list of enemies to set
     */
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Adds a new enemy to the list of enemies.
     */
    public void addEnemy() {
        this.enemies.add(new Enemy());
    }
}
