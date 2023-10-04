import java.util.ArrayList;
import java.util.List;

import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Level {
    // images
    private final Image IMAGE_LANE_LEFT = new Image("res/lane/laneLeft.png");
    private final Image IMAGE_LANE_RIGHT = new Image("res/lane/laneRight.png");
    private final Image IMAGE_LANE_UP = new Image("res/lane/laneUp.png");
    private final Image IMAGE_LANE_DOWN = new Image("res/lane/laneDown.png");
    private final Image IMAGE_LANE_SPECIAL = new Image("res/lane/laneSpecial.png");

    // position
    private static final int LANE_Y = 384;

    // csv file indices
    private static final int INDEX_LANE = 0;
    private static final int INDEX_TYPE = 1;
    private static final int INDEX_DELAY = 2;

    // display object
    private final DISPLAY disp = new DISPLAY();

    // attributes with default values
    private List<List<Note>> notes = new ArrayList<List<Note>>();
    private int[] lane_x = {0, 0, 0, 0, 0};
    private Lane[] lanes = new Lane[5];
    private int score = 0;
    private int grade = 0;
    private boolean holding = false;
    private boolean active = false;
    private Boolean win = false;
    private Note currentNote[] = new Note[5];
    private int currentSpecialType = 0;

    // TODO: enemy, guardian, arrow, entity

    // construct level
    public Level(List<List<String>> file) {
        reset(file);
    }

    // reset level by csv file data
    public void reset(List<List<String>> file) {
        // if != null
        List<Note> lane_left = new ArrayList<Note>();
        List<Note> lane_right = new ArrayList<Note>();
        List<Note> lane_up = new ArrayList<Note>();
        List<Note> lane_down = new ArrayList<Note>();
        List<Note> lane_special = new ArrayList<Note>();

        // create objects from csv file
        for (List<String> record : file) {
            // get lane x coords and create list of notes
            if (record.get(INDEX_LANE).equals("Lane")) {
                switch (record.get(INDEX_TYPE)) {
                    case Note.STR_LEFT:
                        lanes[Note.LEFT] = new Lane(Note.LEFT, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_RIGHT:
                        lanes[Note.RIGHT] = new Lane(Note.RIGHT, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_UP:
                        lanes[Note.UP] = new Lane(Note.UP, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_DOWN:
                        lanes[Note.DOWN] = new Lane(Note.DOWN, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_SPECIAL:
                        lanes[Note.SPECIAL] = new Lane(Note.SPECIAL, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    default: System.out.println("Error: invalid lane");
                }
            } else {
                switch(record.get(INDEX_LANE)) {
                    case Note.STR_LEFT:
                        lane_left.add(new NoteNormal(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY))));
                        break;
                    case Note.STR_RIGHT:
                        lane_right.add(new NoteNormal(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY))));
                        break;
                    case Note.STR_UP:
                        lane_up.add(new NoteNormal(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY))));
                        break;
                    case Note.STR_DOWN:
                        lane_down.add(new NoteNormal(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY))));
                        break;
                    case Note.STR_SPECIAL:
                        lane_special.add(new NoteSpecial(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY))));
                        break;
                    default: System.out.println("Error: invalid note");
                }
            }
        }
        // initalise list of lanes and get first note from each lane
        if (lane_left.size() > 0) {
            notes.add(lane_left);
            this.currentNote[Note.LEFT] = notes.get(Note.LEFT).get(0);
        }
        if (lane_right.size() > 0) {
            notes.add(lane_right);
            this.currentNote[Note.RIGHT] = notes.get(Note.RIGHT).get(0);
        }
        if (lane_up.size() > 0) {
            notes.add(lane_up);
            this.currentNote[Note.UP] = notes.get(Note.UP).get(0);
        }
        if (lane_down.size() > 0) {
            notes.add(lane_down);
            this.currentNote[Note.DOWN] = notes.get(Note.DOWN).get(0);
        }
        if (lane_special.size() > 0) {
            notes.add(lane_special);
            this.currentNote[Note.SPECIAL] = notes.get(Note.SPECIAL).get(0);
        }
    }

    // reset level by level object data
    public void reset(Level level) {
        this.notes = level.getNotes();
        // reset notes
        for (List<Note> lane : this.notes) {
            for (Note note : lane) {
                note.reset(note);
            }
        }
        // get first note from each lane
        if (this.notes.get(Note.LEFT).size() > 0)
            this.currentNote[Note.LEFT] = this.notes.get(Note.LEFT).get(0);
        if (this.notes.get(Note.RIGHT).size() > 0)
            this.currentNote[Note.RIGHT] = this.notes.get(Note.RIGHT).get(0);
        if (this.notes.get(Note.UP).size() > 0)
            this.currentNote[Note.UP] = this.notes.get(Note.UP).get(0);
        if (this.notes.get(Note.DOWN).size() > 0)
            this.currentNote[Note.DOWN] = this.notes.get(Note.DOWN).get(0);
        if (this.notes.get(Note.SPECIAL).size() > 0)
            this.currentNote[Note.SPECIAL] = this.notes.get(Note.SPECIAL).get(0);

        this.score = 0;
        this.grade = 0;
        this.win = false;
        this.holding = false;
        this.active = false;  
    }

    // update level
    public void update(int frame, Input input) {
        // TODO: new grader object per lane, check notes lane by lane
            // (diff current note per lane, still same currentNote variable?)
        // grader object
        List<Grader> grader = new ArrayList<Grader>();

        // reset special type
        this.currentSpecialType = 0;

        for (List<Note> lane : this.notes) {
            // if not empty, update
            if (lane.size() > 0) {
                /* testing */
                // display notes
                disp.drawNoteData(this.currentNote[i]);
                // print note data
                System.out.println(note.getDir() + " " + note.getType() + " " + note.getDelay());
                // display input
                printInput(input);


                // if current note is active
                if (this.currentNote[i].isActive()) {
                    Boolean[] array = grader[i].checkScore(this.currentNote[i], input, this.holding);
                    // update grade and score
                    this.grade = grader[i].getGrade();
                    this.score += this.grade;
                    // set new holding and active states
                    this.currentNote[i].setActive(array[0]);
                    this.holding = array[1];
                }

                // if current note now inactive
                if (!this.currentNote[i].isActive()) {
                    // if was special, do special move
                    if (grader[i].isSpecialGrade()) {
                        if (this.grade == Grader.getMissGrade()) {
                            this.grade = 0;
                        }
                        this.currentSpecialType = this.currentNote[i].getType();
                        switch (this.currentNote[i].getType()) {
                            // TODO: DISPLAY all messages
                            case Note.DOUBLE_SCORE:
                                // this.grade *= 2;
                                //disp.drawSpecial(this.currentSpecialType);
                                System.out.println("Double score!");
                                break;
                            case Note.BOMB:
                                // TODO: change to all notes on screen
                                for (Note note : this.notes[i]) {
                                    if (note.isVisual()) {
                                        note.setActive(false);
                                    }
                                }
                                //disp.drawSpecial(this.currentSpecialType);
                                System.out.println("Lane Clear!");
                                break;
                            case Note.SPEED_UP:
                                for (Note note : this.notes[i]) {
                                    note.setSpeed(note.getSpeed() + 1);
                                }
                                //disp.drawSpecial(this.currentSpecialType);
                                System.out.println("Speed up!");
                                break;
                            case Note.SLOW_DOWN:
                                for (Note note : this.notes[i]) {
                                    note.setSpeed(note.getSpeed() - 1);
                                }
                                //disp.drawSpecial(this.currentSpecialType);
                                System.out.println("Slow down!");
                                break;
                            default:
                                this.currentSpecialType = 0;
                                System.out.println("Error: invalid special type");
                        }
                    }
                }

                // get next note if more left
                if (this.notes[i].indexOf(this.currentNote[i]) < this.notes[i].size() - 1) {
                    this.currentNote[i] = this.notes[i].get(this.notes[i].indexOf(this.currentNote[i]) + 1);

                    // if next note is special, set special type
                    if (this.currentNote[i].getType() > Note.HOLD)
                        this.currentSpecialType = this.currentNote[i].getType();
                    else
                        this.currentSpecialType = Note.NORMAL;
                    
                    this.holding = false;
                    // if last note isn't visual (off screen) end level
                } else {
                    if (!currentNote[i].isVisual())
                        this.active = false;
                }

                // check for notes below screen
                for (Note note : this.notes[i]) {
                    if (note.isBelowScreen()) {
                        this.grade = Grader.getMissGrade();
                        this.score += this.grade;
                        note.setBelowScreen(false);
                    }
                }

                // draw lanes if exist
                if (lane_x[Note.SPECIAL] != 0)
                    IMAGE_LANE_SPECIAL.draw(lane_x[Note.SPECIAL], LANE_Y);
                if (lane_x[Note.LEFT] != 0)
                    IMAGE_LANE_LEFT.draw(lane_x[Note.LEFT], LANE_Y);
                if (lane_x[Note.RIGHT] != 0)
                    IMAGE_LANE_RIGHT.draw(lane_x[Note.RIGHT], LANE_Y);
                if (lane_x[Note.UP] != 0)
                    IMAGE_LANE_UP.draw(lane_x[Note.UP], LANE_Y);
                if (lane_x[Note.DOWN] != 0)
                    IMAGE_LANE_DOWN.draw(lane_x[Note.DOWN], LANE_Y);

                // update/draw notes
                for (Note note : this.notes[i]) {
                    note.update(frame, lane_x);
                }
            }
        }
    }

    /* testing */
    // print input for testing
    public void printInput(Input input) {
        if (input.wasPressed(Keys.LEFT))
            System.out.println("Left D");
        if (input.wasReleased(Keys.LEFT))
            System.out.println("Left U");
        if (input.wasPressed(Keys.RIGHT))
            System.out.println("Right D");
        if (input.wasReleased(Keys.RIGHT))
            System.out.println("Right U");
        if (input.wasPressed(Keys.UP))
            System.out.println("Up D");
        if (input.wasReleased(Keys.UP))
            System.out.println("Up U");
        if (input.wasPressed(Keys.DOWN))
            System.out.println("Down D");
        if (input.wasReleased(Keys.DOWN))
            System.out.println("Down U");
        if (input.wasPressed(Keys.SPACE))
            System.out.println("Space D");
        if (input.wasReleased(Keys.SPACE))
            System.out.println("Space U");
    }

    /* getters */
    public Boolean isActive() {
        return this.active;
    }

    public List<List<Note>> getNotes() {
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

    public Integer getSpecialType() {
        return this.currentSpecialType;
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
