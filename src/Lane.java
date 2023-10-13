import java.util.ArrayList;
import java.util.List;

import bagel.Input;

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

    // construct lane
    public Lane(final int dir, final int x) {
        this.dir = dir;
        this.x = x;
        this.active = true;
    }

    // reset by lane object data
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

    // add note to self
    public void addNote(final Note note) {
        this.notes.add(note);
        if (this.currentNote == null)
            this.currentNote = this.notes.get(0);
    }

    // update lane
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
    public int getDir() {
        return this.dir;
    }

    public int getX() {
        return this.x;
    }

    public boolean isActive() {
        return this.active;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public Note getCurrentNote() {
        return this.currentNote;
    }

    public Boolean isHolding() {
        return this.holding;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public Integer getSpecialType() {
        return this.specialType;
    }

    public Integer getSpeed() {
        return this.speed;
    }

    /* setters */

    public void setSpeed(final int speed) {
        this.speed = speed;
    }
}
