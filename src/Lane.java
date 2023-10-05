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

    /* testing */
    private boolean display = true;


    public Lane(final int dir, final int x) {
        this.dir = dir;
        this.x = x;
        this.active = true;
    }

    public void reset(final Lane lane) {
        this.notes = lane.getNotes();
        // reset notes
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

    public void addNote(final Note note) {
        this.notes.add(note);
        if (this.currentNote == null)
            this.currentNote = this.notes.get(0);
    }

    public void update(final int frame, final Input input) {
        this.specialType = 0;
        this.grade = 0;

        if (this.active) {

            /* testing */
            // private final DISPLAY disp = new DISPLAY();
            // display note data
            // disp.drawNoteData(this.currentNote);

            // draw current note data to system
            //System.out.println("Current note: " + this.currentNote.getDir() + " " + this.currentNote.getType() + " "
            //        + this.currentNote.getY());

            // print note data
            if (this.display) {
                System.out.println("dir|type|delay");
                for (final Note note : this.notes)
                    System.out.println(note.getDir() + " " + note.getType() + " " + note.getDelay());
                this.display = false;
            }

            // if current note is active
            if (this.currentNote.isActive()) {
                final Boolean[] temp = grader.checkScore(this.currentNote, input, this.dir, this.holding);
                
                // update grade
                this.grade = grader.getGrade();

                /* testing */
                if (this.grade == Grader.getMissGrade())
                    System.out.println(dir + " miss");
                if (this.holding != temp[1])
                    System.out.println(dir + " holding: " + this.holding + " -> " + temp[1]);
                
                this.currentNote.setActive(temp[0]);
                this.holding = temp[1];
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
