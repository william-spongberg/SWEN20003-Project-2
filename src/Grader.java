import bagel.Input;
import bagel.Keys;

public class Grader {
    // grading
    private static final int PERFECT = 0;
    private static final int GOOD = 1;
    private static final int BAD = 2;
    private static final int MISS = 3;
    private static final int SPECIAL = 4;

    // grading scores
    private static final int[] GRADE = { 10, 5, -1, -5, 15 };

    // grading distances
    private static final int[] DISTANCE = { 15, 50, 100, 200, 50 };

    // grade y position
    private static final int NOTE_STATIONARY_Y = 657;

    // attributes with default values
    private int grade = 0;
    private boolean specialGrade = false;

    // get score from player input
    public Boolean[] checkScore(final Note note, final Input input, final int lane_dir, boolean holding) {
        // reset values
        this.grade = 0;
        this.specialGrade = false;

        // input, note_y set to defualt
        boolean dir[] = new boolean[5];
        double note_y = note.getY();

        // if note is normal/special
        if (note.getType() != Note.HOLD) {
            dir = checkInputPressed(input);
        }
        // else if note is held
        else {
            if (!holding) {
                // check press, correct dir
                dir = checkInputPressed(input);
                if (dir[lane_dir]) {
                    // get bottom of held note, now holding
                    note_y = note.getY() + note.getMidpoint();
                    holding = true;
                }
            } else {
                // check release, correct dir
                dir = checkInputReleased(input);
                if (dir[lane_dir]) {
                    // get top of held note, now not holding
                    note_y = note.getY() - note.getMidpoint();
                    holding = false;
                }
            }
        }

        // if in grading range
        if ((NOTE_STATIONARY_Y - note_y <= DISTANCE[MISS])
                && (((NOTE_STATIONARY_Y - note_y >= 0) && note.getType() != Note.HOLD)
                        || ((NOTE_STATIONARY_Y - note_y + note.getMidpoint() >= 0) && note.getType() == Note.HOLD))) {
            // check correct dir
            if (dir[lane_dir]) {
                // if normal or hold
                if (note.getType() == Note.NORMAL || note.getType() == Note.HOLD) {
                    // grade the input, kill note if graded and not holding
                    if (gradeInput(dir, note, note_y)) {
                        if (!holding || this.grade == GRADE[MISS])
                            note.setActive(false);
                    }
                    // if special
                } else if (note.getType() > Note.HOLD) {
                    // grade the input, kill note if graded
                    if (gradeSpecialInput(dir, note, note_y))
                        note.setActive(false);
                }
            }
        }
        // else if past grading range
        else if (!(NOTE_STATIONARY_Y - note_y >= 0)) {
            note.setActive(false);
        }

        return new Boolean[] {note.isActive(), holding };
    }

    // grade player input
    public Boolean gradeInput(final boolean dir[], final Note note, final double note_y) {
        boolean graded = false;
        // if note is on screen
        if (note_y <= NOTE_STATIONARY_Y) {
            // if wrong direction
            if (!dir[note.getDir()]) {
                this.grade = GRADE[MISS];
            } else {
                // if within range
                if (graded = gradeDistance(PERFECT, note, note_y)) {
                } else if (graded = gradeDistance(GOOD, note, note_y)) {
                } else if (graded = gradeDistance(BAD, note, note_y)) {
                } else if (graded = gradeDistance(MISS, note, note_y)) {
                }
            }
        }
        return graded;
    }

    // grade player input for special note
    public Boolean gradeSpecialInput(final boolean dir[], final Note note, final double note_y) {
        boolean graded = false;
        // if valid input and note is on screen
        if (note_y <= NOTE_STATIONARY_Y) {
            // if wrong direction
            if (!dir[note.getDir()]) {
                // this.grade = GRADE[MISS];
            } else {
                // if within range
                if (graded = gradeDistance(SPECIAL, note, note_y)) {
                    this.specialGrade = true;

                    /* testing */
                    System.out.println("grade: " + this.grade + " special: " + this.specialGrade);
                    System.out.println(
                            "dir: " + note.getDir() + " type: " + note.getType() + " delay: " + note.getDelay());
                }
            }
        }
        return graded;
    }

    // grade player input for range grade
    // and set note to graded + not visual
    public Boolean gradeDistance(final int grade, final Note note, final double note_y) {
        // if in grade range
        if (NOTE_STATIONARY_Y - note_y <= DISTANCE[grade]) {
            this.grade = GRADE[grade];
            note.setVisual(false);
            return true;
        }
        return false;
    }

    // check for key pressed
    public boolean[] checkInputPressed(final Input input) {
        final boolean dir[] = { false, false, false, false, false };
        if (input.wasPressed(Keys.LEFT))
            dir[Note.LEFT] = true;
        if (input.wasPressed(Keys.RIGHT))
            dir[Note.RIGHT] = true;
        if (input.wasPressed(Keys.UP))
            dir[Note.UP] = true;
        if (input.wasPressed(Keys.DOWN))
            dir[Note.DOWN] = true;
        if (input.wasPressed(Keys.SPACE))
            dir[Note.SPECIAL] = true;

        return dir;
    }

    // check for key released
    public boolean[] checkInputReleased(final Input input) {
        final boolean dir[] = { false, false, false, false, false };
        if (input.wasReleased(Keys.LEFT))
            dir[Note.LEFT] = true;
        if (input.wasReleased(Keys.RIGHT))
            dir[Note.RIGHT] = true;
        if (input.wasReleased(Keys.UP))
            dir[Note.UP] = true;
        if (input.wasReleased(Keys.DOWN))
            dir[Note.DOWN] = true;

        return dir;
    }

    /* getters */
    public int getGrade() {
        return this.grade;
    }

    public static int getPerfectGrade() {
        return GRADE[PERFECT];
    }

    public static int getGoodGrade() {
        return GRADE[GOOD];
    }

    public static int getBadGrade() {
        return GRADE[BAD];
    }

    public static int getMissGrade() {
        return GRADE[MISS];
    }

    public static int getSpecialGrade() {
        return GRADE[SPECIAL];
    }

    public Boolean isSpecialGrade() {
        return this.specialGrade;
    }
}
