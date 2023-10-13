import java.util.List;

import bagel.Image;
import bagel.Input;

/**
 * The Level class represents a level in the game. It contains information about
 * the level's images, positions, csv file indices, frame constants, attributes,
 * grading attributes, guardian object, and display object. It also has methods
 * to reset the level with new file data and add notes to lanes.
 */
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

    /**
     * Constructs a new Level object with the given file data.
     * 
     * @param file the file data to use for the level
     */
    public Level(final List<List<String>> file) {
        reset(file);
    }

    /**
     * Resets the level with the given file data.
     * 
     * @param file The data for the level, in the form of a list of lists of
     *             strings.
     */
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
                        System.exit(-1);
                }
            }
            // add notes to lanes
            else {
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
                        System.exit(-1);
                }
            }
        }
    }

    // given note info add note to its lane
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
                System.exit(-1);
        }
    }

    /**
     * Resets the level to the initial state.
     * 
     * @param level the level to reset to
     */
    public void reset(final Level level) {
        // reset lanes
        this.lanes = level.getLanes();
        for (final Lane lane : this.lanes) {
            if (lane != null)
                lane.reset(lane);
        }
        this.specialType = 0;

        this.grade = 0;
        this.score = 0;
        this.win = false;
        this.active = false;

        this.grade_frames = 0;
        this.special_frames = 0;
        this.double_frames = 0;
        this.enemy_frames = ENEMY_FRAMES;

        this.current_grade = 0;
        this.current_special = 0;
        this.double_score = false;

        this.guardian = new Guardian();
    }

    /**
     * Updates the level by updating all lanes, checking for special types, and
     * updating the score.
     * 
     * @param frame the current frame number
     * @param input the user input
     */
    public void update(final int frame, final Input input) {
        // level 3 update (if level 3)
        level3(frame, input);

        // reset special type
        this.specialType = 0;

        // update lanes
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
                if (this.specialType != 0) {
                    switch (this.specialType) {
                        case Note.DOUBLE_SCORE:
                            this.double_score = true;
                            this.double_frames = DOUBLE_FRAMES;
                            // don't add grade
                            this.grade = 0;
                            break;
                        case Note.BOMB:
                            // go through all visual notes and set to inactive + invisible
                            for (final Lane lane_s : this.lanes) {
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
                            break;
                        case Note.SPEED_UP:
                            for (final Lane lane_s : this.lanes) {
                                // if not empty, update lane speed
                                if (lane_s != null)
                                    lane_s.setSpeed(lane_s.getSpeed() + 1);
                            }
                            break;
                        case Note.SLOW_DOWN:
                            for (final Lane lane_s : this.lanes) {
                                // if not empty, update lane speed
                                if (lane_s != null)
                                    lane_s.setSpeed(lane_s.getSpeed() - 1);
                            }
                            break;
                        default:
                            System.out.println("Error: invalid special type");
                            System.exit(-1);
                    }
                }
                // display grade
                displayGrade();

                // double the score
                if (this.double_score) {
                    this.grade *= 2;
                }
                this.score += this.grade;

                // display special grade
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
        // if no more notes, level inactive
        if (!lanes_active)
            this.active = false;
    }

    // level 3 logic
    private void level3(final int frame, final Input input) {
        if (this.level_num == 2) {
            if (this.enemy_frames == 0) {
                this.enemy_frames = ENEMY_FRAMES;
                this.guardian.addEnemy();
            } else
                this.enemy_frames--;

            this.guardian.update(frame, input);

            // collide enemies with notes
            for (final Enemy enemy : this.guardian.getEnemies()) {
                for (Lane lane : this.lanes) {
                    if (lane != null) {
                        for (Note note : lane.getNotes()) {
                            if (note.isActive() && note.isVisual()) {
                                if (enemy.collideNote(note)) {
                                    note.setActive(false);
                                    note.setVisual(false);
                                }
                            }
                        }
                    }
                }
            }
        }
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
        } else
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
        } else
            this.current_special = 0;

        // if double score
        if (this.double_frames > 0) {
            this.double_frames--;
        } else
            this.double_score = false;
    }

    /* getters */

    /**
     * @return true if the level is active, false otherwise.
     */
    public Boolean isActive() {
        return this.active;
    }

    /**
     * @return an array of Lane objects representing the lanes in the level
     */
    public Lane[] getLanes() {
        return this.lanes;
    }

    /**
     * @return the score of the level as an Integer
     */
    public Integer getScore() {
        return this.score;
    }

    /**
     * @return the grade of the level as an Integer
     */
    public Integer getGrade() {
        return this.grade;
    }

    /**
     * @return true if the level has been won, false otherwise
     */
    public Boolean hasWin() {
        return this.win;
    }

    /**
     * @return the special type of the level as an Integer
     */
    public Integer getSpecialType() {
        return this.specialType;
    }

    /**
     * @return the level number as an Integer.
     */
    public Integer getLevelNum() {
        return this.level_num;
    }

    /* setters */

    /**
     * Sets the active state of the level.
     * 
     * @param active the new active state of the level
     */
    public void setActive(final Boolean active) {
        this.active = active;
    }

    /**
     * Sets the score for the level.
     * 
     * @param score the score to set
     */
    public void setScore(final int score) {
        this.score = score;
    }

    /**
     * Sets the win status of the level.
     * 
     * @param win the win status to set
     */
    public void setWin(final Boolean win) {
        this.win = win;
    }

    /**
     * Sets the level number for the current level.
     * 
     * @param level_num the level number to set
     */
    public void setLevelNum(final int level_num) {
        this.level_num = level_num;
    }
}
