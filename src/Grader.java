import bagel.Input;
import bagel.Keys;

public class Grader {
    // grading
    private static final int SPECIAL = 0;
    private static final int PERFECT = 1;
    private static final int GOOD = 2;
    private static final int BAD = 3;
    private static final int MISS = 4;

    // grading scores
    private static final int[] GRADE = { 15, 10, 5, -1, -5 };

    // grading distances
    private static final int[] DISTANCE = { 50, 15, 50, 100, 200 };

    // grade y position
    private static final int NOTE_STATIONARY_Y = 657;

    // attributes with default values
    private int grade = 0;
    private boolean specialGrade = false;

    // get score from player input
    public Boolean[] checkScore(Note note, Input input, Boolean holding) {
        // input, note_y set to defualt
        int dir = Note.NULL;
        double note_y = note.getY();

        // TODO: combine normal/special type checks here
        // if note is normal
        if (note.getType() == Note.NORMAL) {
            dir = checkInputPressed(input);
        }
        // if note is special
        else if (note.getType() > Note.HOLD) {
            dir = checkInputPressed(input);
        }
        // else if note is held and not already holding
        else if (note.getType() == Note.HOLD && !holding) {
            dir = checkInputPressed(input);
            // if valid input
            if (dir != Note.NULL) {
                // get bottom of held note, now holding
                note_y = note.getY() + note.getMidpoint();
                holding = true;
            }
        }
        // else if note is held and already holding
        else if (note.getType() == Note.HOLD && holding) {
            dir = checkInputReleased(input);
            // if valid input
            if (dir != Note.NULL) {
                // get top of held note, now not holding
                note_y = note.getY() - note.getMidpoint();
                holding = false;
            }
        }

        // if in grading range
        if ((NOTE_STATIONARY_Y - note_y <= DISTANCE[MISS])
                && (((NOTE_STATIONARY_Y - note_y >= 0) && note.getType() != Note.HOLD)
                        || ((NOTE_STATIONARY_Y - note_y + note.getMidpoint() >= 0) && note.getType() == Note.HOLD))) {
            if (note.getType() == Note.HOLD || note.getType() == Note.NORMAL) {
                // grade the input, kill note if graded and not holding
                if (gradeInput(dir, note, note_y)) {
                    if (!holding || this.grade == GRADE[MISS])
                        note.setActive(false);
                }
            }
            else if (note.getType() > Note.HOLD) {
                // grade the input, kill note if graded
                if (gradeSpecialInput(dir, note, note_y)) {
                        note.setActive(false);
                }
            }
        }
        // else if past grading range
        else if (!(NOTE_STATIONARY_Y - note_y >= 0)) {
            note.setActive(false);
        }
        return new Boolean[] { note.isActive(), holding };
    }

    // grade player input
    public Boolean gradeInput(int dir, Note note, double note_y) {
        boolean graded = false;
        // if valid input and note is on screen
        if (dir != Note.NULL && note_y <= NOTE_STATIONARY_Y) {
            // if wrong direction
            if (dir != note.getDir()) {
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
    public Boolean gradeSpecialInput(int dir, Note note, double note_y) {
        boolean graded = false;
        // if valid input and note is on screen
        if (dir != Note.NULL && note_y <= NOTE_STATIONARY_Y) {
            // if wrong direction
            if (dir != note.getDir()) {
                this.grade = GRADE[MISS];
            } else {
                // if within range
                if (graded = gradeDistance(SPECIAL, note, note_y)) {
                    this.specialGrade = true;
                }
                // else do nothing
            }
        }
        return graded;
    }

    // grade player input for range grade
    // and set note to graded + not visual
    public Boolean gradeDistance(int grade, Note note, double note_y) {
        // if in grade range
        if (NOTE_STATIONARY_Y - note_y <= DISTANCE[grade]) {
            this.grade = GRADE[grade];
            note.setVisual(false);
            return true;
        }
        return false;
    }

    // check for key released
    public Integer checkInputReleased(Input input) {
        if (input.wasReleased(Keys.LEFT)) {
            return Note.LEFT;
        } else if (input.wasReleased(Keys.RIGHT)) {
            return Note.RIGHT;
        } else if (input.wasReleased(Keys.UP)) {
            return Note.UP;
        } else if (input.wasReleased(Keys.DOWN)) {
            return Note.DOWN;
        } else if (input.wasPressed(Keys.SPACE)) {
            return Note.SPECIAL;
        } else {
            return Note.NULL;
        }
    }

    // check for key pressed
    public Integer checkInputPressed(Input input) {
        if (input.wasPressed(Keys.LEFT)) {
            return Note.LEFT;
        } else if (input.wasPressed(Keys.RIGHT)) {
            return Note.RIGHT;
        } else if (input.wasPressed(Keys.UP)) {
            return Note.UP;
        } else if (input.wasPressed(Keys.DOWN)) {
            return Note.DOWN;
        } else if (input.wasPressed(Keys.SPACE)) {
            return Note.SPECIAL;
        } else {
            return Note.NULL;
        }
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
