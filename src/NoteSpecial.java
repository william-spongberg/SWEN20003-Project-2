import bagel.Image;

public class NoteSpecial implements Note {
    // images
    private final Image IMAGE_DOUBLE_SCORE = new Image("res/note_special/note2x.png");
    private final Image IMAGE_BOMB = new Image("res/note_special/noteBomb.png");
    private final Image IMAGE_SPEED_UP = new Image("res/note_special/noteSpeedUp.png");
    private final Image IMAGE_SLOW_DOWN = new Image("res/note_special/noteSlowDown.png");

    // starting y position
    private static final Double START_Y = 100.0;

    // midpoint
    private static final int MIDPOINT = 32;

    // attributes with default values
    private Image image = IMAGE_DOUBLE_SCORE;
    private int dir = SPECIAL;
    private int type = DOUBLE_SCORE;
    private int delay = 0;
    private int x = 0;
    private double y = START_Y;
    private int speed = 0;
    private boolean active = true;
    private boolean visual = false;
    private boolean below_screen = false;

    public NoteSpecial(final String dir, final String type, final int delay, final int x) {
        reset(dir, type, delay, x);
    }

    public void reset(final String dir, final String type, final int delay, final int x) {
        // set image type
        switch (type) {
            case STR_DOUBLE_SCORE:
                this.image = IMAGE_DOUBLE_SCORE;
                this.type = DOUBLE_SCORE;
                break;
            case STR_BOMB:
                this.image = IMAGE_BOMB;
                this.type = BOMB;
                // if not in special lane, get lane from dir
                if (dir != "") {
                    switch (dir) {
                        case STR_LEFT:
                            this.dir = LEFT;
                            break;
                        case STR_RIGHT:
                            this.dir = RIGHT;
                            break;
                        case STR_UP:
                            this.dir = UP;
                            break;
                        case STR_DOWN:
                            this.dir = DOWN;
                            break;
                        default:
                            System.out.println("Error: invalid special note");
                    }
                }
                break;
            case STR_SPEED_UP:
                this.image = IMAGE_SPEED_UP;
                this.type = SPEED_UP;
                break;
            case STR_SLOW_DOWN:
                this.image = IMAGE_SLOW_DOWN;
                this.type = SLOW_DOWN;
                break;
            default:
                System.out.println("Error: invalid note");
        }
        // set note delay, x coord
        this.delay = delay;
        this.x = x;
    }

    // reset to default values
    // but keep original type and delay
    public void reset(final Note note) {
        this.image = note.getImage();
        this.dir = note.getDir();
        this.type = note.getType();
        this.delay = note.getDelay();
        this.y = 0;

        this.active = true;
        this.visual = false;
        this.below_screen = false;
    }

    // update note position
    public void update(final int frame) {
        // if active or visual
        if (this.active || this.visual) {
            // if note is on screen
            if (((this.delay - frame) <= 0) && (this.y < (ShadowDance.getHeight() + this.image.getHeight() / 2))) {
                // now visual
                if (!this.visual)
                    this.visual = true;

                // calculate y position
                this.y += REFRESH_60_MULTIPLIER + this.speed;

                // draw note
                this.image.draw(this.x, this.y);
            }

            // if note is below screen
            if (this.y >= (ShadowDance.getHeight() + this.image.getHeight() / 2)) {
                this.below_screen = true;
            }
        }
    }

    /* getters */
    public Image getImage() {
        return this.image;
    }

    public Integer getDir() {
        return this.dir;
    }

    public Integer getDelay() {
        return this.delay;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public Double getStartY() {
        return START_Y;
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public Integer getMidpoint() {
        return MIDPOINT;
    }

    public Boolean isActive() {
        return this.active;
    }

    public Boolean isVisual() {
        return this.visual;
    }

    public Boolean isBelowScreen() {
        return this.below_screen;
    }

    /* setters */

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public void setVisual(final Boolean visual) {
        this.visual = visual;
    }

    public void setBelowScreen(final Boolean below_screen) {
        this.below_screen = below_screen;
    }

    public void addSpeed(final Integer speed) {
        this.speed = speed;
    }
}
