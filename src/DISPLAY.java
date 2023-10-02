import bagel.*;

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
    private static final String START_1 = "PRESS SPACE TO START";
    private static final String START_2 = "USE ARROW KEYS TO PLAY";
    // game play
    private static final String SCORE = "SCORE ";
    private static final String PAUSED = "PAUSED";
    private static final String STRING_PERFECT = "PERFECT";
    private static final String STRING_GOOD = "GOOD";
    private static final String STRING_BAD = "BAD";
    private static final String STRING_MISS = "MISS";
    // level win
    private static final String CLEAR = "CLEAR!";
    private static final String TOTAL_SCORE = "TOTAL SCORE ";
    // level lose
    private static final String LOSE = "TRY AGAIN";
    private static final String NEED_SCORE = "NEED 150 TO CONTINUE";
    // game over
    private static final String GAME_OVER = "GAME OVER";
    private static final String HIGH_SCORE = "HIGH SCORE ";
    private static final String GAME_RESTART = "PRESS R TO RESTART";

    /* methods */
    // centered text, small font for subtitles

    /* draw passive screens */
    public final void drawBackground(Image image) {
        image.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
    }

    public final void drawStartScreen() {
        FONT.drawString(GAME_TITLE, WINDOW_WIDTH / 2 - FONT.getWidth(GAME_TITLE) / 2, WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(START_1, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(START_1) / 2, WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(START_2, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(START_2) / 2,
                WINDOW_HEIGHT / 2 + 50);
    }

    public final void drawPauseScreen() {
        FONT.drawString(PAUSED, WINDOW_WIDTH / 2 - FONT.getWidth(PAUSED) / 2,
                WINDOW_HEIGHT / 2);
    }

    // draw score
    public final void drawScore(int score) {
        FONT_SCORE.drawString(SCORE + score, 35, 35);
    }

    /* draw grade methods */
    public final void drawPerfect() {
        FONT_GRADE.drawString(STRING_PERFECT, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(STRING_PERFECT) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawGood() {
        FONT_GRADE.drawString(STRING_GOOD, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(STRING_GOOD) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawBad() {
        FONT_GRADE.drawString(STRING_BAD, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(STRING_BAD) / 2,
                WINDOW_HEIGHT / 2);
    }

    public final void drawMiss() {
        FONT_GRADE.drawString(STRING_MISS, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(STRING_MISS) / 2,
                WINDOW_HEIGHT / 2);
    }

    // draw grade according to grade
    public final void drawGrade(int grade) {
        if (grade == Grade.getPerfectGrade()) {
            drawPerfect();
        } else if (grade == Grade.getGoodGrade()) {
            drawGood();
        } else if (grade == Grade.getBadGrade()) {
            drawBad();
        } else if (grade == Grade.getMissGrade()) {
            drawMiss();
        } else {
            System.out.println("Error: invalid grade");
        }
    }

    /* draw win/lose/end screen */
    public final void drawWinScreen(int score, int total_score) {
        FONT.drawString(CLEAR, WINDOW_WIDTH / 2 - FONT.getWidth(CLEAR) / 2,
                WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(SCORE + score, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(SCORE + score) / 2,
                WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(TOTAL_SCORE + total_score,
                WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(TOTAL_SCORE + total_score) / 2,
                WINDOW_HEIGHT / 2 + 50);
    }

    public final void drawLoseScreen(int score) {
        FONT.drawString(LOSE, WINDOW_WIDTH / 2 - FONT.getWidth(LOSE) / 2,
                WINDOW_HEIGHT / 2 - 100);
        FONT_SMALL.drawString(SCORE + score, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(SCORE + score) / 2,
                WINDOW_HEIGHT / 2);
        FONT_SMALL.drawString(NEED_SCORE, WINDOW_WIDTH / 2 - FONT_SMALL.getWidth(NEED_SCORE) / 2,
                WINDOW_HEIGHT / 2 + 50);
    }

    public final void drawEndScreen(int total_score, int high_score) {
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
