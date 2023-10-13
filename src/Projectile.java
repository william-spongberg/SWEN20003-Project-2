import java.util.List;

import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;

public class Projectile extends Entity {
    private final Image IMAGE_PROJECTILE = new Image("res/arrow.png");

    private static final int ENEMY_COLLISION = 62;

    // attributes with default values
    private int x = Guardian.X;
    private int y = Guardian.Y;
    private int speed = 6;
    private double rotation = -1;
    private Enemy currentEnemy = null;
    private DrawOptions drawOptions = new DrawOptions();
    private boolean hitEnemy = false;

    // construct projectile
    public Projectile(List<Enemy> enemies) {
        if (enemies.size() > 0) {
            // fire projectile towards closest enemy
            double minDistance = Double.MAX_VALUE;
            for (final Enemy enemy : enemies) {
                final double distance = Math.sqrt(Math.pow(this.x - enemy.getX(), 2) + Math.pow(this.y - enemy.getY(), 2));
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

    // update projectile position
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

    public Enemy getCurrentEnemy() {
        return this.currentEnemy;
    }

    public Boolean getHitEnemy() {
        return this.hitEnemy;
    }
}
