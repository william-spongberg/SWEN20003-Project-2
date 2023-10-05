import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bagel.AbstractGame;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

// TODO: remove /* testing */, give to play testers to check for final bugs

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
    private static final String[] LEVEL_FILES = { "res/level/level1-60.csv", "res/level/level2-60.csv",
            "res/level/level3-60.csv" };

    // game logic constants
    private static final int LOCK_FRAMES = 45;
    private static final int[] WIN_SCORE = {150, 400, 350};

    // display object
    private final DISPLAY disp = new DISPLAY();

    // array list of levels, current level
    private final List<Level> levels = new ArrayList<Level>();
    private Level currentLevel;

    // game logic booleans
    private boolean paused = false;
    private boolean started = true;
    private boolean ended = false;
    private boolean level_ended = false;
    private boolean disp_lock = false;

    // frame counter, frame counters, current grade/special, score, level number
    private int frame = 0;
    private int lock_frames = 0;

    private int score = 0;
    private int high_score = 0;

    private int level_num = 0;
    private int high_level_num = 3; // allows any level choice for testing

    public ShadowDance() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);

        // add levels
        for (final String fileName : LEVEL_FILES) {
            this.levels.add(readCSV(fileName));
        }
    }

    // reads CSV file and returns a Level object
    private Level readCSV(final String fileName) {
        // read csv level files from res into arraylist of arrays of strings
        final List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // read until end of file
            while ((line = br.readLine()) != null) {
                final String[] values = line.split(",");
                records.add(List.of(values));
            }
            // catch and print errors
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return new Level(records);
    }

    // program entry point
    public static void main(final String[] args) {
        final ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * This method updates the game state and draws the appropriate screen based on
     * the current state.
     * It draws the background and allows the user to exit or pause the game. If the
     * game is paused, it draws the pause screen.
     * If the game is playing, it allows the user to start the game and draws the
     * start screen. If the game has ended, it allows the user to restart the game
     * and draws the end screen.
     * If the game is in progress, it allows the user to restart the game,
     * increments the frame counter, gets the current level, updates the level if it
     * is running, and checks if the level has ended to display the appropriate
     * screen.
     * This method is called REFRESH_RATE times every second by the Bagel engine
     *
     * @param input The input object used to get user input.
     */
    @Override
    protected void update(final Input input) {
        // draw background
        this.disp.drawBackground(IMAGE_BACKGROUND);

        // allow user to exit, pause
        getState(input);

        // if paused
        if (this.paused) {
            this.disp.drawPauseScreen();
        }
        // if playing
        else {
            // game started
            if (this.started) {
                startScreen();
                // allow user to select level
                chooseLevel(input);
            }
            // game ended
            else if (this.ended) {
                // allow user to restart game
                enableRestart(input);
                // draw end screen
                this.disp.drawEndScreen(score, high_score);
            }
            // game in progress
            else {
                // allow user to restart game
                enableRestart(input);

                // increment frame counter
                this.frame++;

                // get current level, activate if not ended
                activateLevel();

                // if current level is running, update
                updateLevel(input);

                // if current level has now ended, display win/lose/end screen
                checkLevelEnded(input);
            }
        }
    }

    private void startScreen() {
        this.disp.drawStartScreen();
        if (this.high_level_num > 0)
            this.disp.drawContinueOption();

        if (this.disp_lock) {
            this.lock_frames = LOCK_FRAMES;
            this.disp_lock = false;
        }

        if (this.lock_frames > 0) {
            this.disp.drawLevelLocked();
            this.lock_frames--;
        }
    }

    private void chooseLevel(final Input input) {
        // allow user to continue game with space
        if (input.wasPressed(Keys.SPACE))
            this.started = false;

        // allow user to select level with number keys 1 2 3
        if (input.wasPressed(Keys.NUM_1)) {
            checkLevelLock(0);
        } else if (input.wasPressed(Keys.NUM_2)) {
            checkLevelLock(1);
        } else if (input.wasPressed(Keys.NUM_3)) {
            checkLevelLock(2);
        }
    }

    private void checkLevelLock(final int level_choice) {
        if (this.high_level_num >= level_choice) {
            this.level_num = level_choice;
            this.started = false;
        } else {
            this.disp_lock = true;
        }
    }

    private void getState(final Input input) {
        // allow user to exit
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // allow user to pause
        if (input.wasPressed(Keys.TAB)) {
            this.paused = !this.paused;
        }
    }

    private void activateLevel() {
        this.currentLevel = this.levels.get(level_num);
        if (!this.level_ended) {
            this.currentLevel.setActive(true);
            this.currentLevel.setLevelNum(level_num);
        }
    }

    private void updateLevel(final Input input) {
        if (this.currentLevel.isActive()) {
            this.disp.drawScore(this.currentLevel.getScore());
            this.currentLevel.update(this.frame, input);

            // end level if now inactive
            checkLevelInactive();
        }
    }

    private void checkLevelInactive() {
        // if level no longer active
        if (!this.currentLevel.isActive()) {
            // end level
            this.level_ended = true;

            // calculate if player won level, add score
            if (this.currentLevel.getScore() >= WIN_SCORE[currentLevel.getLevelNum()]) {
                this.currentLevel.setWin(true);
                this.high_level_num = this.level_num;
                this.score += this.currentLevel.getScore();
                if (this.score > this.high_score)
                    this.high_score = this.score;
            } else {
                this.currentLevel.setWin(false);
            }
        }
    }

    private void checkLevelEnded(final Input input) {
        // if level ended, display win/lose/end screen
        if (this.level_ended) {
            if (this.currentLevel.hasWin()) {
                this.disp.drawWinScreen(this.currentLevel.getScore(), this.score);
                // allow user to go back to level selection
                if (input.wasPressed(Keys.ENTER)) {
                    this.level_ended = false;
                    this.currentLevel.setScore(0);
                    this.currentLevel.reset(this.currentLevel);
                    this.started = true;
                    this.frame = 0;
                }

                // allow user to go to next level
                if (input.wasPressed(Keys.SPACE)) {
                    this.level_ended = false;
                    // if there are more levels
                    if (this.levels.size() - this.level_num > 1) {
                        this.level_num++;
                        this.frame = 0;
                        // if no more levels, game ended
                    } else
                        this.ended = true;
                }
            } else {
                this.disp.drawLoseScreen(this.currentLevel.getScore(), WIN_SCORE[currentLevel.getLevelNum()]);
                // allow user to go back to level selection
                if (input.wasPressed(Keys.ENTER)) {
                    this.level_ended = false;
                    this.currentLevel.setScore(0);
                    this.currentLevel.reset(this.currentLevel);
                    this.started = true;
                    this.frame = 0;
                }

                // allow user to restart level
                if (input.wasPressed(Keys.SPACE)) {
                    this.level_ended = false;
                    this.currentLevel.setScore(0);
                    this.currentLevel.reset(this.currentLevel);
                    this.frame = 0;
                }
            }
        }
    }

    // allow user to restart game on key R
    private void enableRestart(final Input input) {
        if (input.wasPressed(Keys.R)) {
            this.started = true;
            this.ended = false;
            this.level_ended = false;

            this.frame = 0;
            this.score = 0;
            this.level_num = 0;
            this.high_level_num = 0;

            for (final Level level : this.levels) {
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
}
