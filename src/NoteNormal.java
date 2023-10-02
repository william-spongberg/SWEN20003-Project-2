import bagel.*;

public class NoteNormal implements Note {
    // images
    private final Image IMAGE_NOTE_LEFT = new Image("res/note_normal/noteLeft.png");
    private final Image IMAGE_NOTE_RIGHT = new Image("res/note_normal/noteRight.png");
    private final Image IMAGE_NOTE_UP = new Image("res/note_normal/noteUp.png");
    private final Image IMAGE_NOTE_DOWN = new Image("res/note_normal/noteDown.png");

    // starting y position
    private static final Double START_Y = 100.0;

    // image midpoint
    private static final int MIDPOINT = 32;

    // attributes with default values
    private Image image = IMAGE_NOTE_LEFT;
    private int dir = LEFT;
    private int delay = 0;
    private double y = 0;
    private boolean active = true;
    private boolean visual = false;
    private boolean below_screen = false;

    public NoteNormal(String dir, String type, int delay) {
        reset(dir, type, delay);
    }

    public void reset(String dir, String type, int delay) {
        // set image direction, note direction to dir
        switch (dir) {
            case TYPE_LEFT:
                this.image = IMAGE_NOTE_LEFT;
                this.dir = LEFT;
                break;
            case TYPE_RIGHT:
                this.image = IMAGE_NOTE_RIGHT;
                this.dir = RIGHT;
                break;
            case TYPE_UP:
                this.image = IMAGE_NOTE_UP;
                this.dir = UP;
                break;
            case TYPE_DOWN:
                this.image = IMAGE_NOTE_DOWN;
                this.dir = DOWN;
                break;
            default:
                System.out.println("Error: invalid note");
        }
        // set note delay
        this.delay = delay;
    }

    // reset to default values
    // but keep original direction and delay
    public void reset(Note note) {
        this.image = note.getImage();
        this.dir = note.getDir();
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

    public Integer getType() {
        return NORMAL;
    }

    public Integer getDelay() {
        return this.delay;
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
