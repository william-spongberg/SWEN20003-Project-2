import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SWEN20003 Project 1, Semester 2, 2023
 * 
 * @author William Spongberg
 */
public class ShadowDance extends AbstractGame {
    // window dimensions, refresh rate, title and background
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image IMAGE_BACKGROUND = new Image("res/background.png");

    // file level names
    private static final String[] FILE_LEVEL = { "res/level/test1.csv", "res/level/test2.csv", "res/level/test3.csv" };

    // game logic constants
    private static final int GRADE_FRAMES = 30;
    private static final int WIN_SCORE = 150;

    // display object
    DISPLAY disp = new DISPLAY();

    // array list of levels, current level
    private List<Level> levels = new ArrayList<Level>();
    private Level currentLevel;

    // game logic booleans
    private boolean paused = false;
    private boolean started = true;
    private boolean ended = false;
    private boolean level_ended = false;
    private static boolean refresh_60 = false;

    // frame counter, grade frames counter, current grade, score, level number
    private int frame = 0;
    private int frames_grading = 0;
    private int current_grade = 0;
    private int score = 0;
    private int high_score = 0;
    private int level_num = 0;

    public ShadowDance() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);

        // add levels
        for (String fileName : FILE_LEVEL) {
            this.levels.add(readCSV(fileName));
        }
    }

    // read csv files and create level objects
    private Level readCSV(String fileName) {
        // read csv level files from res into arraylist of arrays of strings
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // read until end of file
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(Arrays.asList(values));
            }
            // catch and print errors
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Level(records);
    }

    // program entry point
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    // performs a state update REFRESH_RATE times every second
    @Override
    protected void update(Input input) {
        // draw background
        this.disp.drawBackground(IMAGE_BACKGROUND);

        // allow user to exit
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // pause logic
        if (input.wasPressed(Keys.TAB)) {
            if (!this.paused)
                this.paused = true;
            else
                this.paused = false;
        }

        /* game state logic */
        // if paused
        if (this.paused) {
            this.disp.drawPauseScreen();
        } else {
            // allow user to start game
            if (input.wasPressed(Keys.SPACE))
                this.started = false;

            // if game started
            if (this.started) {
                this.disp.drawStartScreen();
                // if game ended
            } else if (this.ended) {
                enableRestart(input);
                this.disp.drawEndScreen(score, high_score);
                // else in progress
            } else {
                enableRestart(input);

                // increment frame counter
                this.frame++;

                // get current level, activate if not ended
                this.currentLevel = this.levels.get(level_num);
                if (!this.level_ended)
                    this.currentLevel.setActive(true);

                // if current level is running
                if (this.currentLevel.isActive()) {
                    this.disp.drawScore(this.currentLevel.getScore());
                    this.currentLevel.update(this.frame, input);

                    // if there is a new grade, get new grade
                    if (this.currentLevel.getGrade() != 0) {
                        this.frames_grading = GRADE_FRAMES;
                        this.current_grade = this.currentLevel.getGrade();
                    }

                    // if there are frames left to display grade, display it
                    if (this.frames_grading > 0) {
                        this.disp.drawGrade(this.current_grade);
                        this.frames_grading--;
                        // else reset grade
                    } else
                        this.current_grade = 0;

                    // if level no longer active
                    if (!this.currentLevel.isActive()) {
                        // end level
                        this.level_ended = true;

                        // calculate if player won level, add score
                        if (this.currentLevel.getScore() >= WIN_SCORE) {
                            this.currentLevel.setWin(true);
                            this.score += this.currentLevel.getScore();
                            if (this.score > this.high_score)
                                this.high_score = this.score;
                        } else {
                            this.currentLevel.setWin(false);
                        }
                    }
                }
                // if level ended, display win/lose/end screen
                if (this.level_ended) {
                    if (this.currentLevel.hasWin()) {
                        this.disp.drawWinScreen(this.currentLevel.getScore(), this.score);
                        // allow user to go to next level
                        if (input.wasPressed(Keys.SPACE)) {
                            this.level_ended = false;
                            // if there are more levels
                            if (this.levels.size() - this.level_num > 1) {
                                this.level_num++;
                                this.frame = 0;
                                this.frames_grading = 0;
                                // if no more levels, game ended
                            } else
                                this.ended = true;
                        }
                    } else {
                        this.disp.drawLoseScreen(this.currentLevel.getScore());
                        // allow user to restart level
                        if (input.wasPressed(Keys.SPACE)) {
                            this.level_ended = false;
                            this.currentLevel.setScore(0);
                            this.frame = 0;
                            this.frames_grading = 0;
                            this.currentLevel.reset(this.currentLevel);
                        }
                    }
                }
            }
        }
    }

    // allow user to restart game on key R
    private void enableRestart(Input input) {
        if (input.wasPressed(Keys.R)) {
            this.started = true;
            this.ended = false;
            this.level_ended = false;

            this.frame = 0;
            this.frames_grading = 0;
            this.current_grade = 0;
            this.score = 0;
            this.level_num = 0;

            for (Level level : this.levels) {
                level.reset(level);
            }
        }
    }

    /* getters */
    public static final int getWidth() {
        return WINDOW_WIDTH;
    }

    public static final int getHeight() {
        return WINDOW_HEIGHT;
    }

    public static final Boolean hasRefresh60() {
        return refresh_60;
    }
}
