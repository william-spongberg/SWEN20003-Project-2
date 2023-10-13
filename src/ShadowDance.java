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

/**
 * SWEN20003 Project 2B, Semester 2, 2023
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
    private static final String[] LEVEL_FILES_60 = { "res/level/level1-60.csv", "res/level/level2-60.csv",
            "res/level/level3-60.csv" };
    private static final String[] LEVEL_FILES_120 = { "res/level/level1.csv", "res/level/level2.csv",
            "res/level/level3.csv" };
    // NOTE: change to LEVEL_FILES_120 to play 120 fps version
    private static final String[] LEVEL_FILES = LEVEL_FILES_60;

    // game logic constants
    private static final int LOCK_FRAMES = 45;
    private static final int[] WIN_SCORE = { 150, 400, 350 };

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

    // frame counter, frame counters, score, level nums
    private int frame = 0;
    private int lock_frames = 0;

    private int score = 0;
    private int high_score = 0;

    private int level_num = 0;
    private int high_level_num = 0;

    /**
     * The ShadowDance class represents the main game window for the Shadow Dance
     * game.
     * It extends the GameWindow class and initializes the game with the specified
     * window width, height, and title.
     * It also adds levels to the game by reading CSV files.
     */
    public ShadowDance() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);

        // add levels
        for (final String fileName : LEVEL_FILES) {
            this.levels.add(readCSV(fileName));
        }
    }

    /**
     * The main method of the ShadowDance class.
     * Creates a new instance of the ShadowDance game and runs it.
     * 
     * @param args The command line arguments passed to the program.
     */
    public static void main(final String[] args) {
        final ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * This method is called every frame to update the game state. It draws the
     * background, allows the user to exit or pause the game, and updates the game
     * based on the current state (paused, started, ended). If the game is paused,
     * it draws the pause screen. If the game is started, it displays the start
     * screen and allows the user to choose a level. If the game is ended, it allows
     * the user to restart the game and displays the end screen with the final score
     * and high score. If the game is in progress, it increments the frame counter,
     * activates the current level, updates the level based on user input, and
     * checks if the level has ended.
     *
     * @param input The user input received from the keyboard.
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
                chooseLevel(input);
            }
            // game ended
            else if (this.ended) {
                // allow user to restart game
                enableRestart(input);
                this.disp.drawEndScreen(score, high_score);
            }
            // game in progress
            else {
                // allow user to restart game
                enableRestart(input);
                // increment frame counter
                this.frame++;

                // get current level, update level, check if level now ended
                activateLevel();
                updateLevel(input);
                checkLevelEnded(input);
            }
        }
    }

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

    private void startScreen() {
        this.disp.drawStartScreen();
        // if completed a level, display continue option
        if (this.high_level_num > 0)
            this.disp.drawContinueOption();
        // if level locked, display level locked
        if (this.disp_lock) {
            this.lock_frames = LOCK_FRAMES;
            this.disp_lock = false;
        }
        if (this.lock_frames > 0) {
            this.disp.drawLevelLocked();
            this.lock_frames--;
        }
    }

    // allow user to select level
    private void chooseLevel(final Input input) {
        if (input.wasPressed(Keys.SPACE))
            this.started = false;

        if (input.wasPressed(Keys.NUM_1)) {
            checkLevelLock(0);
        } else if (input.wasPressed(Keys.NUM_2)) {
            checkLevelLock(1);
        } else if (input.wasPressed(Keys.NUM_3)) {
            checkLevelLock(2);
        }
    }

    // if level is unlocked, set level num and start game
    private void checkLevelLock(final int level_choice) {
        if (this.high_level_num >= level_choice) {
            this.level_num = level_choice;
            this.started = false;
        } else {
            this.disp_lock = true;
        }
    }

    // allow user to exit, pause
    private void getState(final Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if (input.wasPressed(Keys.TAB)) {
            this.paused = !this.paused;
        }
    }

    // if not in level end screen, activate level
    private void activateLevel() {
        this.currentLevel = this.levels.get(level_num);
        if (!this.level_ended) {
            this.currentLevel.setActive(true);
            this.currentLevel.setLevelNum(level_num);
        }
    }

    // if active, update level
    private void updateLevel(final Input input) {
        if (this.currentLevel.isActive()) {
            this.disp.drawScore(this.currentLevel.getScore());
            this.currentLevel.update(this.frame, input);
            // end level if now inactive
            checkLevelInactive();
        }
    }

    // if level no longer active, end level
    private void checkLevelInactive() {
        if (!this.currentLevel.isActive()) {
            this.level_ended = true;
            if (this.currentLevel.getScore() >= WIN_SCORE[currentLevel.getLevelNum()]) {
                this.currentLevel.setWin(true);
                if (this.level_num > this.high_level_num)
                    this.high_level_num = this.level_num;
                this.score += this.currentLevel.getScore();
                if (this.score > this.high_score)
                    this.high_score = this.score;
            } else {
                this.currentLevel.setWin(false);
            }
        }
    }

    // if level ended, display win/lose/end screen
    private void checkLevelEnded(final Input input) {
        if (this.level_ended) {
            if (this.currentLevel.hasWin()) {
                this.disp.drawWinScreen(this.currentLevel.getScore(), this.score);
                if (input.wasPressed(Keys.ENTER)) {
                    this.level_ended = false;
                    this.currentLevel.setScore(0);
                    this.currentLevel.reset(this.currentLevel);
                    this.started = true;
                    this.frame = 0;
                }

                if (input.wasPressed(Keys.SPACE)) {
                    this.level_ended = false;
                    // if there are more levels
                    if (this.levels.size() - this.level_num > 1) {
                        this.level_num++;
                        this.frame = 0;
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

    /**
     * @return the width of the game window
     */
    public static final int getWidth() {
        return WINDOW_WIDTH;
    }

    /**
     * @return the height of the game window
     */
    public static final int getHeight() {
        return WINDOW_HEIGHT;
    }
}
