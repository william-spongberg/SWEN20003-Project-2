import bagel.Image;

public class NoteHold implements Note {
    // images
    private final Image IMAGE_NOTEHOLD_LEFT = new Image("res/note_hold/holdNoteLeft.png");
    private final Image IMAGE_NOTEHOLD_RIGHT = new Image("res/note_hold/holdNoteRight.png");
    private final Image IMAGE_NOTEHOLD_UP = new Image("res/note_hold/holdNoteUp.png");
    private final Image IMAGE_NOTEHOLD_DOWN = new Image("res/note_hold/holdNoteDown.png");

    // starting y position
    private static final Double START_Y = 24.0;

    // hold image midpoint
    private static final int MIDPOINT = 82;

    // attributes with default values
    private Image image = IMAGE_NOTEHOLD_LEFT;
    private int dir = LEFT;
    private int delay = 0;
    private int x = 0; // testing
    private double y = START_Y;
    private int speed = 0;
    private boolean active = true;
    private boolean visual = false;
    private boolean below_screen = false;

    public NoteHold(final String dir, final String type, final int delay, final int x) {
        reset(dir, type, delay, x);
    }

    public void reset(final String dir, final String type, final int delay, final int x) {
        // set image direction, note direction to dir
        switch (dir) {
            case STR_LEFT:
                this.image = IMAGE_NOTEHOLD_LEFT;
                this.dir = LEFT;
                break;
            case STR_RIGHT:
                this.image = IMAGE_NOTEHOLD_RIGHT;
                this.dir = RIGHT;
                break;
            case STR_UP:
                this.image = IMAGE_NOTEHOLD_UP;
                this.dir = UP;
                break;
            case STR_DOWN:
                this.image = IMAGE_NOTEHOLD_DOWN;
                this.dir = DOWN;
                break;
            default:
                System.out.println("Error: invalid hold note");
        }
        // set note delay, x coord
        this.delay = delay;
        this.x = x;
    }

    // reset to default values
    // but keep original direction and delay
    public void reset(final Note note) {
        this.image = note.getImage();
        this.dir = note.getDir();
        this.delay = note.getDelay();
        this.y = START_Y;

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

    public Integer getType() {
        return HOLD;
    }

    public Integer getDelay() {
        return this.delay;
    }

    public Integer getX() { // testing
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

    public Boolean isActive() {
        return this.active;
    }

    public Boolean isVisual() {
        return this.visual;
    }

    public Boolean isBelowScreen() {
        return this.below_screen;
    }

    public Integer getMidpoint() {
        return MIDPOINT;
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
