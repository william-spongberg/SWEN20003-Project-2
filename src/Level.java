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

    // frame constants
    private static final int GRADE_FRAMES_60 = 15;
    private static final int GRADE_FRAMES_120 = 30;
    private static final int DOUBLE_FRAMES = 480;
    private static final int ENEMY_FRAMES = 600;

    // attributes with default values
    private Lane[] lanes = new Lane[5];
    private int level_num = 0;
    private int score = 0;
    private int grade = 0;
    private int specialType = 0;
    private boolean active = false;
    private Boolean win = false;

    // grading attributes
    private int grade_frames = 0;
    private int special_frames = 0;
    private int double_frames = 0;
    private int enemy_frames = ENEMY_FRAMES;

    private int current_grade = 0;
    private int current_special = 0;
    private boolean double_score = false;

    // guardian object
    private Guardian guardian = new Guardian();

    // display object
    private final DISPLAY disp = new DISPLAY();

    // construct level
    public Level(final List<List<String>> file) {
        reset(file);
    }

    // reset level by csv file data
    public void reset(final List<List<String>> file) {
        // create objects from csv file
        for (final List<String> record : file) {
            // get lane x coords and create lanes
            if (record.get(INDEX_LANE).equals("Lane")) {
                switch (record.get(INDEX_TYPE)) {
                    case Note.STR_LEFT:
                        this.lanes[Note.LEFT] = new Lane(Note.LEFT, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_RIGHT:
                        this.lanes[Note.RIGHT] = new Lane(Note.RIGHT, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_UP:
                        this.lanes[Note.UP] = new Lane(Note.UP, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_DOWN:
                        this.lanes[Note.DOWN] = new Lane(Note.DOWN, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    case Note.STR_SPECIAL:
                        this.lanes[Note.SPECIAL] = new Lane(Note.SPECIAL, Integer.parseInt(record.get(INDEX_DELAY)));
                        break;
                    default:
                        System.out.println("Error: invalid lane");
                }
                // add notes to lanes
            } else {
                switch (record.get(INDEX_LANE)) {
                    case Note.STR_LEFT:
                        addNoteToLane(record, Note.LEFT);
                        break;
                    case Note.STR_RIGHT:
                        addNoteToLane(record, Note.RIGHT);
                        break;
                    case Note.STR_UP:
                        addNoteToLane(record, Note.UP);
                        break;
                    case Note.STR_DOWN:
                        addNoteToLane(record, Note.DOWN);
                        break;
                    case Note.STR_SPECIAL:
                        this.lanes[Note.SPECIAL].addNote(new NoteSpecial(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                                Integer.parseInt(record.get(INDEX_DELAY)), this.lanes[Note.SPECIAL].getX()));
                        break;
                    default:
                        System.out.println("Error: invalid note");
                }
            }
        }
    }

    private void addNoteToLane(final List<String> record, final int dir) {
        switch (record.get(INDEX_TYPE)) {
            case Note.STR_BOMB:
                this.lanes[dir].addNote(new NoteSpecial(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                        Integer.parseInt(record.get(INDEX_DELAY)), this.lanes[dir].getX()));
                break;
            case Note.STR_NORMAL:
                this.lanes[dir].addNote(new NoteNormal(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                        Integer.parseInt(record.get(INDEX_DELAY)), this.lanes[dir].getX()));
                break;
            case Note.STR_HOLD:
                this.lanes[dir].addNote(new NoteHold(record.get(INDEX_LANE), record.get(INDEX_TYPE),
                        Integer.parseInt(record.get(INDEX_DELAY)), this.lanes[dir].getX()));
                break;
            default:
                System.out.println("Error: invalid note type");
        }
    }

    // reset level by level object data
    public void reset(final Level level) {
        this.lanes = level.getLanes();
        // reset notes
        for (final Lane lane : this.lanes) {
            if (lane != null)
                lane.reset(lane);
        }

        this.grade = 0;
        this.score = 0;
        this.win = false;
        this.active = false;
    }

    // update level
    public void update(final int frame, final Input input) {
        // level 3 guardian, enemy, projectiles
        if (this.level_num == 2) {
            if (this.enemy_frames == 0) {
                this.enemy_frames = ENEMY_FRAMES;
                this.guardian.addEnemy();
            }
            else
                this.enemy_frames--;
            
            this.guardian.update(frame, input);

            for (final Enemy enemy : this.guardian.getEnemies()) {
                // collide with notes
                for (Lane lane : this.lanes) {
                    if (lane != null) {
                        for (Note note : lane.getNotes()) {
                            if (note.isActive() && note.isVisual()) {
                                if (enemy.collideNote(note)) {
                                    note.setActive(false);
                                    note.setVisual(false);
                                    System.out.println("Enemy hit note");
                                }
                            }
                        }
                    }
                }
            }
        }        

        // reset special type
        this.specialType = 0;

        // display input
        printInput(input);

        boolean lanes_active = false;
        for (final Lane lane : this.lanes) {
            this.grade = 0;

            // if not empty, update
            if (lane != null) {
                lane.update(frame, input);

                this.grade = lane.getGrade();
                this.specialType = lane.getSpecialType();

                // if all lanes inactive, level is inactive
                if (lane.isActive()) {
                    lanes_active = true;
                }

                // if special type
                // if speed, go through all notes and update speed
                // if bomb, go through all visual notes and set to inactive + invisible
                // if double score, return boolean to super class

                if (this.specialType != 0) {
                    System.out.println("Special type: " + this.specialType);
                    switch (this.specialType) {
                        case Note.DOUBLE_SCORE:
                            this.double_score = true;
                            this.double_frames = DOUBLE_FRAMES;
                            // don't add grade 
                            this.grade = 0;
                            System.out.println("Double score!");
                            break;
                        case Note.BOMB:
                            for (final Lane lane_s : this.lanes) {
                                // if not empty, update
                                if (lane_s != null) {
                                    for (final Note note : lane_s.getNotes()) {
                                        if (note.isVisual()) {
                                            note.setVisual(false);
                                            note.setActive(false);
                                        }
                                    }
                                }
                            }
                            // don't add grade
                            this.grade = 0;
                            System.out.println("Lane Clear!");
                            break;
                        case Note.SPEED_UP:
                            for (final Lane lane_s : this.lanes) {
                                // if not empty, update
                                if (lane_s != null)
                                    lane_s.setSpeed(lane_s.getSpeed() + 1);
                            }
                            System.out.println("Speed up!");
                            break;
                        case Note.SLOW_DOWN:
                            for (final Lane lane_s : this.lanes) {
                                // if not empty, update
                                if (lane_s != null)
                                    lane_s.setSpeed(lane_s.getSpeed() - 1);
                            }
                            System.out.println("Slow down!");
                            break;
                        default:
                            System.out.println("Error: invalid special type");
                            System.out.println("Special type: " + this.specialType);
                    }
                }
                // display/add grade
                displayGrade();

                if (this.double_score) {
                    this.grade *= 2;
                }
                this.score += this.grade;
                displaySpecial();
            }

            // draw lanes if active
            if (lanes[Note.LEFT] != null)
                IMAGE_LANE_LEFT.draw(lanes[Note.LEFT].getX(), LANE_Y);
            if (lanes[Note.RIGHT] != null)
                IMAGE_LANE_RIGHT.draw(lanes[Note.RIGHT].getX(), LANE_Y);
            if (lanes[Note.UP] != null)
                IMAGE_LANE_UP.draw(lanes[Note.UP].getX(), LANE_Y);
            if (lanes[Note.DOWN] != null)
                IMAGE_LANE_DOWN.draw(lanes[Note.DOWN].getX(), LANE_Y);
            if (lanes[Note.SPECIAL] != null)
                IMAGE_LANE_SPECIAL.draw(lanes[Note.SPECIAL].getX(), LANE_Y);
        }

        if (!lanes_active)
            this.active = false;
    }

    private void displayGrade() {
        // if there is a new grade, replace current
        if (this.grade != 0) {
            this.grade_frames = GRADE_FRAMES_60;
            this.current_grade = this.grade;
        }

        // if there are frames left to display grade, display it
        if (this.grade_frames > 0 && this.current_grade != 0) {
            this.disp.drawGrade(this.current_grade);
            this.grade_frames--;
        }
        // else reset grade
        else
            this.current_grade = 0;
    }

    private void displaySpecial() {
        // if there is a new special grade, replace current
        if (this.specialType != 0) {
            this.special_frames = GRADE_FRAMES_60;
            this.current_special = this.specialType;
        }

        // if there are frames left to display special grade, display it
        if (this.special_frames > 0 && this.current_special != 0) {
            this.disp.drawSpecial(this.current_special);
            this.special_frames--;

            /* testing */
            System.out.println("Special type: " + current_special);
        }
        // else reset special grade
        else
            this.current_special = 0;

        // if double score
        if (this.double_frames > 0) {
            this.double_frames--;
        }
        // else reset double score
        else
            this.double_score = false;
    }

    /* testing */
    // print input for testing
    public void printInput(final Input input) {
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

    public Lane[] getLanes() {
        return this.lanes;
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
        return this.specialType;
    }

    public Integer getLevelNum() {
        return this.level_num;
    }

    /* setters */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public void setWin(final Boolean win) {
        this.win = win;
    }

    public void setLevelNum(final int level_num) {
        this.level_num = level_num;
    }
}
