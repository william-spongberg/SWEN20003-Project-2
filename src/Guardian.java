import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Guardian extends Entity {
    // guardian final visual data
    private final Image IMAGE_GUARDIAN = new Image("res/guardian.png");
    public static final int X = 800;
    public static final int Y = 600;

    // attributes
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    // constuct guardian
    public Guardian() {
        reset();
    }

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
            if (projectile.getHitEnemy()) {
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

    public void addEnemy() {
        this.enemies.add(new Enemy());
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


    public List<Enemy> getEnemies() {
        return this.enemies;
    }

    /* setters */
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
}
