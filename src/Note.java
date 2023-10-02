import bagel.*;
public interface Note {
    // directions
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    public static final int SPECIAL = 4;

    // types
    public static final int NORMAL = 0;
    public static final int HOLD = 1;
    public static final int DOUBLE_SCORE = 2;
    public static final int BOMB = 3;
    public static final int SPEED_UP = 4;
    public static final int SLOW_DOWN = 5;

    public static final String TYPE_LEFT = "Left";
    public static final String TYPE_RIGHT = "Right";
    public static final String TYPE_UP = "Up";
    public static final String TYPE_DOWN = "Down";
    public static final String TYPE_SPECIAL = "Special";
    public static final String TYPE_HOLD = "Hold";
    public static final String TYPE_DOUBLE_SCORE = "DoubleScore";
    public static final String TYPE_BOMB = "Bomb";
    public static final String TYPE_SPEED_UP = "SpeedUp";
    public static final String TYPE_SLOW_DOWN = "SlowDown";

    // refresh rate speed multipliers
    public static final int REFRESH_60_MULTIPLIER = 4;
    public static final int REFRESH_120_MULTIPLIER = 2;

    // methods
    public void reset(Note note);
    public void reset(String dir, String type, int delay);
    public void update(int frame, int lane_x[]);
    public Boolean canGrade(Input input);

    /* getters */
    public Image getImage();    
    public Integer getDir();
    public Integer getType();
    public Integer getDelay();
    public Double getY();
    public Double getStartY();
    public Integer getMidpoint();
    public Boolean isActive();
    public Boolean isVisual();
    public Boolean isBelowScreen();

    /* setters */
    public void setActive(Boolean active);
    public void setVisual(Boolean visual);
    public void setBelowScreen(Boolean below_screen);
}
