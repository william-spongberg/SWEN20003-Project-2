import bagel.*;

public class NoteSpecial implements Note {
    // images
    private final Image IMAGE_DOUBLE_SCORE = new Image("res/note_special/note2x.png");
    private final Image IMAGE_BOMB = new Image("res/note_special/noteBomb.png");
    private final Image IMAGE_SPEED_UP = new Image("res/note_special/holdSpeedUp.png");
    private final Image IMAGE_SLOW_DOWN = new Image("res/note_special/holdSlowDown.png");

    // starting y position
    private static final Double START_Y = 100.0;

    // midpoint
    private static final int MIDPOINT = 82;

    // attributes with default values
    private Image image = IMAGE_DOUBLE_SCORE;
    private int dir = -1;
    private int type = DOUBLE_SCORE;
    private int delay = 0;
    private double y = 0;
    private boolean active = true;
    private boolean visual = false;
    private boolean below_screen = false;

    public NoteSpecial(String dir, String type, int delay) {
        reset(dir, type, delay);
    }

    public void reset(String dir, String type, int delay) {
        // set image type
        switch (type) {
            case TYPE_DOUBLE_SCORE:
                this.image = IMAGE_DOUBLE_SCORE;
                this.type = DOUBLE_SCORE;
                break;
            case TYPE_BOMB:
                this.image = IMAGE_BOMB;
                this.type = BOMB;
                // if not in special lane, get lane from dir
                if (dir != "") {
                    switch(dir) {
                        case TYPE_LEFT:
                            this.dir = LEFT;
                            break;
                        case TYPE_RIGHT:
                            this.dir = RIGHT;
                            break;
                        case TYPE_UP:
                            this.dir = UP;
                            break;
                        case TYPE_DOWN:
                            this.dir = DOWN;
                            break;
                        default:
                            System.out.println("Error: invalid note");
                    }
                }
                break;
            case TYPE_SPEED_UP:
                this.image = IMAGE_SPEED_UP;
                this.type = SPEED_UP;
                break;
            case TYPE_SLOW_DOWN:
                this.image = IMAGE_SLOW_DOWN;
                this.type = SLOW_DOWN;
                break;
            default:
                System.out.println("Error: invalid note");
        }
        // set note delay
        this.delay = delay;
    }

    // reset to default values
    // but keep original type and delay
    public void reset(Note note) {
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
    public void update(int frame, int lane_x[]) {
        // if active or visual
        if (this.active || this.visual) {
            // calculate y position
            this.y = START_Y + (frame - this.delay) * REFRESH_60_MULTIPLIER;

            // if note is on screen
            if ((this.y > START_Y) && (this.y < (ShadowDance.getHeight() + this.image.getHeight() / 2))) {
                // now visual
                if (!this.visual)
                    this.visual = true;

                // draw note
                this.image.draw(lane_x[this.dir], this.y);
            }
            // if note is below screen
            else if (this.y > (ShadowDance.getHeight() + this.image.getHeight() / 2)) {
                this.visual = false;
                this.active = false;
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

    public Double getY() {
        return this.y;
    }

    public Double getStartY() {
        return START_Y;
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

    public Boolean canGrade(Input input) {
        return false;
    }

    /* setters */

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setVisual(Boolean visual) {
        this.visual = visual;
    }

    public void setBelowScreen(Boolean below_screen) {
        this.below_screen = below_screen;
    }


}
