import bagel.Font;
import bagel.Image;
import bagel.Window;

/**
 * The DISPLAY class is responsible for rendering the game's graphics and text
 * on the screen.
 * It contains methods for drawing the game's background, start screen, pause
 * screen, score, and grade.
 * It also contains constants for the game's dimensions, font, and strings used
 * for rendering text.
 */
public final class DISPLAY {
    // dimensions
    private final static int WINDOW_WIDTH = ShadowDance.getWidth();
    private final static int WINDOW_HEIGHT = ShadowDance.getHeight();

    // font
    private final static String FILE_FONT = "res/FSO8BITR.TTF";
    private final Font FONT = new Font(FILE_FONT, 64);
    private final Font FONT_SMALL = new Font(FILE_FONT, 24);
    private final Font FONT_SCORE = new Font(FILE_FONT, 30);
    private final Font FONT_GRADE = new Font(FILE_FONT, 40);

    /* strings */
    // title
    private final static String GAME_TITLE = "SHADOW DANCE";
    // start screen
    private static final String START_1 = "SELECT LEVELS WITH";
    private static final String START_2 = "NUMBER KEYS";
    private static final String START_3 = "1    2    3";
    private static final String CONTINUE = "PRESS SPACE TO CONTINUE";
    private static final String NOT_CLEARED = "THE PREVIOUS LEVEL MUST BE CLEARED";
    // game play
    private static final String SCORE = "SCORE ";
    private static final String PAUSED = "PAUSED";
    private static final String STR_PERFECT = "PERFECT";
    private static final String STR_GOOD = "GOOD";
    private static final String STR_BAD = "BAD";
    private static final String STR_MISS = "MISS";
    private static final String STR_DOUBLE = "DOUBLE SCORE";
    private static final String STR_BOMB = "LANE CLEAR";
    private static final String STR_SPEED = "SPEED UP";
    private static final String STR_SLOW = "SLOW DOWN";
    // level win
    private static final String CLEAR = "CLEAR!";
    private static final String TOTAL_SCORE = "TOTAL SCORE ";
    // level lose
    private static final String LOSE = "TRY AGAIN";
    private static final String NEED_SCORE_1 = "NEED SCORE ";
    private static final String NEED_SCORE_2 = " TO CONTINUE";
    private static final String LEVEL_SELECT = "PRESS ENTER TO RETURN TO LEVEL SELECTION";
    // game over
    private static final String GAME_OVER = "GAME OVER";
    private static final String HIGH_SCORE = "HIGH SCORE ";
    private static final String GAME_RESTART = "PRESS R TO RESTART";

    /* draw passive screens */

    /**
     * Draws the background image at the center of the window.
     * 
     * @param image the image to be drawn
     */
    public final void drawBackground(final Image image) {
        image.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }

    /**
     * Draws the start screen with the game title and start options.
     * Uses the FONT and FONT_SMALL objects to draw the text.
     */
    public final void drawStartScreen() {
        FONT.drawString(GAME_TITLE, WINDOW_WIDTH / 2 - FONT.getWidth(GAME_TITLE) / 2, WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(START_1, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(START_1) / 2, WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(START_2, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(START_2) / 2,
                WINDOW_HEIGHT / 2 + 40);
        FONT_SMALL.drawString(START_3, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(START_3) / 2,
                WINDOW_HEIGHT / 2 + 100);
    }

    /**
     * Draws the "Continue" option on the display.
     * The option is centered horizontally and positioned 200 pixels below the
     * center of the display.
     */
    public final void drawContinueOption() {
        FONT_SMALL.drawString(CONTINUE, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(CONTINUE) / 2,
                WINDOW_HEIGHT / 2 + 200);
    }

    /**
     * Draws the "Level Locked" message on the screen.
     * The option is centered horizontally and positioned 200 pixels below the
     * center of the display.
     */
    public final void drawLevelLocked() {
        FONT_SMALL.drawString(NOT_CLEARED, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(NOT_CLEARED) / 2,
                WINDOW_HEIGHT / 2 + 200);
    }

    /**
     * Draws the pause screen with the text "PAUSED" centered on the screen.
     */
    public final void drawPauseScreen() {
        FONT.drawString(PAUSED, WINDOW_WIDTH / 2 - FONT.getWidth(PAUSED) / 2,
                WINDOW_HEIGHT / 2);
    }

    /* draw scoring */

    /**
     * Draws the score on the display.
     * 
     * @param score the score to be drawn
     */
    public final void drawScore(final int score) {
        FONT_SCORE.drawString(SCORE + score, 35, 35);
    }

    /**
     * Draws the appropriate image on the screen based on the given grade.
     * If the grade is a perfect grade, the perfect image is drawn.
     * If the grade is a good grade, the good image is drawn.
     * If the grade is a bad grade, the bad image is drawn.
     * If the grade is a miss grade, the miss image is drawn.
     * If the grade is a special grade, no image is drawn.
     * If the grade is invalid, an error message is printed and the program exits.
     * 
     * @param grade the grade to draw an image for
     */
    public final void drawGrade(final int grade) {
        if (grade == Grader.getPerfectGrade()) {
            drawPerfect();
        } else if (grade == Grader.getGoodGrade()) {
            drawGood();
        } else if (grade == Grader.getBadGrade()) {
            drawBad();
        } else if (grade == Grader.getMissGrade()) {
            drawMiss();
        } else if (grade == Grader.getSpecialGrade()) {
        } else {
            System.out.println("Error: invalid grade");
            System.exit(-1);
        }
    }

    /**
     * Draws the referred grade message on the display using the FONT_GRADE font.
     * The messages are centered horizontally and vertically on the display.
     */
    public final void drawPerfect() {
        FONT_GRADE.drawString(STR_PERFECT, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_PERFECT) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawGood() {
        FONT_GRADE.drawString(STR_GOOD, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_GOOD) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawBad() {
        FONT_GRADE.drawString(STR_BAD, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_BAD) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawMiss() {
        FONT_GRADE.drawString(STR_MISS, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_MISS) / 2,
                WINDOW_HEIGHT / 2);
    }

    /**
     * Draws special note grade on the centre of the screen based on the given type.
     * 
     * @param type the type of special note grade to draw
     */
    public final void drawSpecial(final int type) {
        if (type == Note.DOUBLE_SCORE) {
            FONT_GRADE.drawString(STR_DOUBLE, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_DOUBLE) / 2,
                    WINDOW_HEIGHT / 2);
        } else if (type == Note.BOMB) {
            FONT_GRADE.drawString(STR_BOMB, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_BOMB) / 2,
                    WINDOW_HEIGHT / 2);
        } else if (type == Note.SPEED_UP) {
            FONT_GRADE.drawString(STR_SPEED, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_SPEED) / 2,
                    WINDOW_HEIGHT / 2);
        } else if (type == Note.SLOW_DOWN) {
            FONT_GRADE.drawString(STR_SLOW, WINDOW_WIDTH / 2 - FONT_GRADE.getWidth(STR_SLOW) / 2,
                    WINDOW_HEIGHT / 2);
        } else {
            System.out.println("Error: invalid special type");
            System.exit(-1);
        }
    }

    /* draw win/lose/end screen */

    /**
     * Draws the win screen with the given score and total score.
     * 
     * @param score       The score achieved in the game.
     * @param total_score The total score achieved in all games played.
     */
    public final void drawWinScreen(final int score, final int total_score) {
        FONT.drawString(CLEAR, WINDOW_WIDTH / 2 - FONT.getWidth(CLEAR) / 2,
                WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(SCORE + score, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(SCORE + score) / 2,
                WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(TOTAL_SCORE + total_score,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(TOTAL_SCORE + total_score) / 2,
                WINDOW_HEIGHT / 2 + 50);
        FONT_SMALL.drawString(CONTINUE,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(CONTINUE) / 2,
                WINDOW_HEIGHT / 2 + 150);
        FONT_SMALL.drawString(LEVEL_SELECT,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(LEVEL_SELECT) / 2,
                WINDOW_HEIGHT / 2 + 200);

    }

    /**
     * Draws the lose screen with the given score and needed score.
     * 
     * @param score      the player's score
     * @param need_score the score needed to win
     */
    public final void drawLoseScreen(final int score, final int need_score) {
        FONT.drawString(LOSE, WINDOW_WIDTH / 2 - FONT.getWidth(LOSE) / 2,
                WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(SCORE + score, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(SCORE + score) / 2,
                WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(NEED_SCORE_1 + need_score + NEED_SCORE_2,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(NEED_SCORE_1 + need_score + NEED_SCORE_2) / 2,
                WINDOW_HEIGHT / 2 + 50);
        FONT_SMALL.drawString(CONTINUE,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(CONTINUE) / 2,
                WINDOW_HEIGHT / 2 + 150);
        FONT_SMALL.drawString(LEVEL_SELECT,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(LEVEL_SELECT) / 2,
                WINDOW_HEIGHT / 2 + 200);
    }

    /**
     * Draws the end screen with the total score, high score, and option to restart
     * the game.
     * 
     * @param total_score the total score achieved in the game
     * @param high_score  the highest score achieved in the game
     */
    public final void drawEndScreen(final int total_score, final int high_score) {
        FONT.drawString(GAME_OVER, WINDOW_WIDTH / 2 - FONT.getWidth(GAME_OVER) / 2,
                WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(TOTAL_SCORE + total_score,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(TOTAL_SCORE + total_score) / 2,
                WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(HIGH_SCORE + high_score,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(HIGH_SCORE + high_score) / 2,
                WINDOW_HEIGHT / 2 + 50);
        FONT_SMALL.drawString(GAME_RESTART,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(GAME_RESTART) / 2,
                WINDOW_HEIGHT / 2 + 150);
    }
}
