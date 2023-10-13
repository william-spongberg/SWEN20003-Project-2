import java.util.List;

import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;

/**
 * The Projectile class represents a projectile that is fired towards the
 * closest enemy in a given list of enemies.
 * If the list is empty, the projectile will not fire. The class extends the
 * Entity class and overrides its update method to update the position of the
 * projectile and check for collision with enemy.
 * The class also provides getters for the x-coordinate, y-coordinate, speed,
 * current enemy, and hit enemy flag of the projectile.
 */
public class Projectile extends Entity {
    // projectile image
    private final Image IMAGE_PROJECTILE = new Image("res/arrow.png");

    // enemy collision range
    private static final int ENEMY_COLLISION = 62;

    // attributes with default values
    private int x = Guardian.X;
    private int y = Guardian.Y;
    private int speed = 6;
    private double rotation = -1;
    private Enemy currentEnemy = null;
    private DrawOptions drawOptions = new DrawOptions();
    private boolean hitEnemy = false;

    /**
     * Constructs a new Projectile object that fires towards the closest enemy in
     * the given list of enemies.
     * If the list is empty, the projectile will not fire.
     * 
     * @param enemies the list of enemies to target
     */
    public Projectile(List<Enemy> enemies) {
        if (enemies.size() > 0) {
            // fire projectile towards closest enemy
            double minDistance = Double.MAX_VALUE;
            for (final Enemy enemy : enemies) {
                final double distance = Math
                        .sqrt(Math.pow(this.x - enemy.getX(), 2) + Math.pow(this.y - enemy.getY(), 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    this.currentEnemy = enemy;
                }
            }

            // find direction to fire projectile
            final double deltaX = this.currentEnemy.getX() - this.x;
            final double deltaY = this.currentEnemy.getY() - this.y;
            this.rotation = Math.atan2(deltaY, deltaX);
            drawOptions.setRotation(this.rotation);
        }
    }

    /**
     * Updates the position of the projectile and checks for collision with enemy.
     * If the projectile collides with an enemy, sets the hitEnemy flag to true.
     * 
     * @param frame the current frame number
     * @param input the input object containing user input
     */
    @Override
    public void update(int frame, Input input) {
        // if not rotated, no enemies to fire at
        if ((this.rotation == -1))
            return;

        // move projectile
        this.x += this.speed * Math.cos(this.rotation);
        this.y += this.speed * Math.sin(this.rotation);

        // check for collision with enemy
        if (this.currentEnemy != null) {
            final double distance = Math.sqrt(
                    Math.pow(this.x - this.currentEnemy.getX(), 2) + Math.pow(this.y - this.currentEnemy.getY(), 2));
            if (distance < ENEMY_COLLISION) {
                this.hitEnemy = true;
            } else
                this.hitEnemy = false;
        }

        // draw projectile
        IMAGE_PROJECTILE.draw(this.x, this.y, drawOptions);
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
        return this.speed;
    }

    /**
     * @return the current enemy that the projectile is targeting.
     */
    public Enemy getCurrentEnemy() {
        return this.currentEnemy;
    }

    /**
     * @return true if the projectile has hit an enemy, false otherwise.
     */
    public Boolean hasHitEnemy() {
        return this.hitEnemy;
    }
}
