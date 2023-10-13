import java.util.ArrayList;
import java.util.List;

import bagel.Input;

/**
 * The Lane class represents a single lane in the game. A lane contains a list
 * of notes that the player must hit in time with the music.
 * The class provides methods for updating the state of the lane, adding notes
 * to the lane, and resetting the lane to its initial state.
 */
public class Lane {
    // attributes with default values
    private List<Note> notes = new ArrayList<Note>();
    private Note currentNote = null;
    private int specialType = 0;
    private int dir = Note.LEFT;
    private int x = 0;
    private int speed = 0;
    private int grade = 0;
    private boolean active = false;
    private boolean holding = false;

    // grader object
    private final Grader grader = new Grader();

    /**
     * Constructs a new Lane object with the given direction and x-coordinate.
     * 
     * @param dir the direction of the lane
     * @param x   the x-coordinate of the lane
     */
    public Lane(final int dir, final int x) {
        this.dir = dir;
        this.x = x;
        this.active = true;
    }

    /**
     * Resets the current lane to match the given lane. This includes resetting all
     * notes in the lane, setting the current note to the first note in the lane,
     * and resetting various other properties such as speed and grade.
     *
     * @param lane the lane to reset to
     */
    public void reset(final Lane lane) {
        // reset notes
        this.notes = lane.getNotes();
        for (final Note note : this.notes) {
            note.reset(note);
        }
        this.currentNote = this.notes.get(0);
        this.specialType = 0;

        this.dir = lane.getDir();
        this.x = lane.getX();
        this.speed = 0;
        this.grade = 0;
        this.active = true;
        this.holding = false;
    }

    /**
     * Adds a note to the lane's list of notes and sets it as the current note if
     * there is no current note.
     * 
     * @param note the note to be added to the lane's list of notes
     */
    public void addNote(final Note note) {
        this.notes.add(note);
        if (this.currentNote == null)
            this.currentNote = this.notes.get(0);
    }

    /**
     * Updates the state of the Lane object for each frame of the game.
     * 
     * @param frame the current frame of the game
     * @param input the user input for the current frame
     */
    public void update(final int frame, final Input input) {
        this.specialType = 0;
        this.grade = 0;

        if (this.active) {
            // if current note is active
            if (this.currentNote.isActive()) {
                // grade note
                final Boolean[] temp = grader.checkScore(this.currentNote, input, this.dir, this.holding);
                this.currentNote.setActive(temp[0]);
                this.holding = temp[1];

                // update grade
                this.grade = grader.getGrade();
            }

            // if current note now inactive
            if (!this.currentNote.isActive()) {
                // if graded special
                if (this.currentNote.getType() > Note.HOLD && this.grader.isSpecialGrade()) {
                    // no miss penalty
                    if (this.grade == Grader.getMissGrade()) {
                        this.grade = 0;
                    }
                    this.specialType = this.currentNote.getType();
                }

                // get next note if more left
                if (this.notes.indexOf(this.currentNote) < this.notes.size() - 1) {
                    this.currentNote = this.notes.get(this.notes.indexOf(this.currentNote) + 1);
                    this.holding = false;
                }
                // if last note isn't visual (off screen), lane is inactive
                else {
                    if (!currentNote.isVisual() || !currentNote.isActive()) {
                        this.active = false;
                    }
                }
            }
        }

        // check for notes below screen
        for (final Note note : this.notes) {
            if (note.isBelowScreen() && note.isVisual()) {
                note.setVisual(false);
                // if not special, missed note
                if (!(note.getType() > Note.HOLD))
                    this.grade = Grader.getMissGrade();
            }
        }

        // update/draw notes
        for (final Note note : this.notes) {
            note.addSpeed(this.speed);
            note.update(frame);
        }
    }

    /* getters */

    /**
     * @return an integer representing the direction of the lane
     */
    public int getDir() {
        return this.dir;
    }

    /**
     * @return the x-coordinate of the Lane object.
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return true if the lane is active, false otherwise.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @return a list of notes in the lane
     */
    public List<Note> getNotes() {
        return this.notes;
    }

    /**
     * @return the current note in the lane
     */
    public Note getCurrentNote() {
        return this.currentNote;
    }

    /**
     * @return true if the user is currently holding down the key for a note
     */
    public Boolean isHolding() {
        return this.holding;
    }

    /**
     * @return the grade of this Lane
     */
    public Integer getGrade() {
        return this.grade;
    }

    /**
     * @return the current special note type of this lane
     */
    public Integer getSpecialType() {
        return this.specialType;
    }

    /**
     * @return the speed of the lane
     */
    public Integer getSpeed() {
        return this.speed;
    }

    /* setters */

    /**
     * Sets the speed of the lane.
     * 
     * @param speed the new speed of the lane
     */
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
}
