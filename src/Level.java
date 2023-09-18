import java.util.ArrayList;
import java.util.List;

import bagel.Image;
import bagel.Input;

public class Level {
    // images
    private final Image IMAGE_LANE_LEFT = new Image("res/laneLeft.png");
    private final Image IMAGE_LANE_RIGHT = new Image("res/laneRight.png");
    private final Image IMAGE_LANE_UP = new Image("res/laneUp.png");
    private final Image IMAGE_LANE_DOWN = new Image("res/laneDown.png");

    // position
    private static final int LANE_Y = 384;

    // csv file indices
    private static final int INDEX_LANE = 0;
    private static final int INDEX_TYPE = 1;
    private static final int INDEX_DELAY = 2;

    // directions
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    // attributes with default values
    private List<Note> notes = new ArrayList<Note>();
    private int[] lane_x = new int[4];
    private int score = 0;
    private int grade = 0;
    private boolean holding = false;
    private boolean active = false;
    private Boolean win = false;
    private Note currentNote;

    // construct level
    public Level(List<List<String>> file) {
        reset(file);
    }

    // reset level by csv file data
    public void reset(List<List<String>> file) {
        // create objects from csv file
        int i = 0;
        for (List<String> record : file) {
            // get lane x coords and create list of notes
            if (record.get(INDEX_LANE).equals("Lane")) {
                this.lane_x[i] = Integer.parseInt(record.get(INDEX_DELAY));
                i++;
            } else {
                this.notes.add(new Note(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                        Integer.parseInt(record.get(INDEX_DELAY))));
            }
        }
        // get first note
        this.currentNote = this.notes.get(0);
    }

    // reset level by level object data
    public void reset(Level level) {
        this.notes = level.getNotes();
        for (Note note : this.notes) {
            note.reset(note);
        }
        this.currentNote = this.notes.get(0);

        this.score = 0;
        this.grade = 0;
        this.win = false;
        this.holding = false;
        this.active = false;
    }

    // update level
    public void update(int frame, Input input) {
        Grade grade = new Grade();
        if (this.currentNote.isActive()) {
            // check score while current note is active
            Boolean[] array = grade.checkScore(this.currentNote, input, this.holding);
            // update grade and score
            this.grade = grade.getGrade();
            this.score += this.grade;
            // set new holding and active states
            this.currentNote.setActive(array[0]);
            this.holding = array[1];
        }

        // if current note now inactive
        if (!this.currentNote.isActive()) {
            // get next note if more left
            if (this.notes.indexOf(this.currentNote) < this.notes.size() - 1) {
                this.currentNote = this.notes.get(this.notes.indexOf(this.currentNote) + 1);
                this.holding = false;
                // if last note isn't visual end level
            } else {
                if (!currentNote.isVisual())
                    this.active = false;
            }
        }

        // check for notes below screen
        for (Note note : this.notes) {
            if (note.isBelowScreen()) {
                this.grade = Grade.getMissGrade();
                this.score += this.grade;
                note.setBelowScreen(false);
            }
        }

        // draw lanes
        IMAGE_LANE_LEFT.draw(lane_x[LEFT], LANE_Y);
        IMAGE_LANE_RIGHT.draw(lane_x[RIGHT], LANE_Y);
        IMAGE_LANE_UP.draw(lane_x[UP], LANE_Y);
        IMAGE_LANE_DOWN.draw(lane_x[DOWN], LANE_Y);

        // draw notes
        for (Note note : this.notes) {
            note.update(frame, lane_x);
        }
    }

    /* getters */
    public Boolean isActive() {
        return this.active;
    }

    public List<Note> getNotes() {
        return this.notes;
    }

    public Integer getScore() {
        return this.score;
    }

    public Integer getGrade() {
        return this.grade;
    }

    public Boolean hasWin() {
        return this.win;
    }

    /* setters */
    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setWin(Boolean win) {
        this.win = win;
    }
}
