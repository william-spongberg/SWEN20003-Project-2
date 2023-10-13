import bagel.Image;

/**
 * The Note interface represents a musical note in the game. It contains
 * constants for directions and types of notes, as well as methods for
 * resetting, updating, and getting/setting various properties of the note.
 * 
 * Directions:
 * - NULL: represents no direction
 * - LEFT: represents a note moving to the left
 * - RIGHT: represents a note moving to the right
 * - UP: represents a note moving up
 * - DOWN: represents a note moving down
 * - SPECIAL: represents a special note
 * 
 * Types:
 * - NORMAL: represents a normal note
 * - HOLD: represents a hold note
 * - DOUBLE_SCORE: represents a note that doubles the player's score
 * - BOMB: represents a bomb note that ends the game if missed
 * - SPEED_UP: represents a note that speeds up the game
 * - SLOW_DOWN: represents a note that slows down the game
 * 
 * Refresh Rate Speed Multipliers:
 * - REFRESH_60_MULTIPLIER: represents a refresh rate of 60 fps
 * - REFRESH_120_MULTIPLIER: represents a refresh rate of 120 fps
 * - REFRESH_MULTI: represents the current refresh rate speed multiplier
 * (default is 60 fps)
 */
public interface Note {
    // directions
    public static final int NULL = -1;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    public static final int SPECIAL = 4;

    public static final String STR_LEFT = "Left";
    public static final String STR_RIGHT = "Right";
    public static final String STR_UP = "Up";
    public static final String STR_DOWN = "Down";
    public static final String STR_SPECIAL = "Special";

    // types
    public static final int NORMAL = 0;
    public static final int HOLD = 1;
    public static final int DOUBLE_SCORE = 2;
    public static final int BOMB = 3;
    public static final int SPEED_UP = 4;
    public static final int SLOW_DOWN = 5;

    public static final String STR_NORMAL = "Normal";
    public static final String STR_HOLD = "Hold";
    public static final String STR_DOUBLE_SCORE = "DoubleScore";
    public static final String STR_BOMB = "Bomb";
    public static final String STR_SPEED_UP = "SpeedUp";
    public static final String STR_SLOW_DOWN = "SlowDown";

    // refresh rate speed multipliers
    public static final int REFRESH_60_MULTIPLIER = 4;
    public static final int REFRESH_120_MULTIPLIER = 2;
    public static final int REFRESH_MULTI = REFRESH_60_MULTIPLIER;

    // methods

    /**
     * Resets the note's image direction, note direction, delay, and x-coordinate
     * based on the given parameters.
     * 
     * @param dir   the direction of the note's image and movement
     * @param type  the type of note (normal, hold, or special types)
     * @param delay the delay before the note should be played
     * @param x     the x-coordinate of the note's position
     */
    public void reset(String dir, String type, int delay, int x);

    /**
     * Resets the object with the properties of the given Note object.
     * 
     * @param note the Note object to reset with
     */
    public void reset(Note note);

    /**
     * Updates the state of the NoteSpecial object.
     * If the note is active or visual, it checks if the note is on screen.
     * If the note is on screen, it updates the y position and draws the note.
     * If the note is below the screen, it sets the below_screen flag to true.
     * 
     * @param frame the current frame of the game
     */
    public void update(int frame);

    /* getters */

    /**
     * @return the image associated with this note
     */
    public Image getImage();

    /**
     * @return the direction of the note
     */
    public Integer getDir();

    /**
     * @return the type of the note as an Integer
     */
    public Integer getType();

    /**
     * @return the delay of the note in seconds
     */
    public Integer getDelay();

    /**
     * @return the x-coordinate of the note
     */
    public Integer getX();

    /**
     * @return the y-coordinate of the note
     */
    public Double getY();

    /**
     * @return the starting y-coordinate of the note
     */
    public Double getStartY();

    /**
     * @return the speed of the note as an Integer
     */
    public Integer getSpeed();

    /**
     * @return the midpoint of the note as an Integer.
     */
    public Integer getMidpoint();

    /**
     * @return true if the note is active, false otherwise
     */
    public Boolean isActive();

    /**
     * @return true if the note is visual, false otherwise
     */
    public Boolean isVisual();

    /**
     * @return true if the note is below the screen, false otherwise.
     */
    public Boolean isBelowScreen();

    /* setters */

    /**
     * Sets the active status of the note.
     * 
     * @param active the new active status of the note
     */
    public void setActive(Boolean active);

    /**
     * Sets the visual flag for this note.
     * 
     * @param visual the new value for the visual flag
     */
    public void setVisual(Boolean visual);

    /**
     * Sets whether the note is below the screen.
     * 
     * @param below_screen true if the note is below the screen, false otherwise
     */
    public void setBelowScreen(Boolean below_screen);

    /**
     * Sets the speed of the note to the given value.
     * 
     * @param speed the speed of the note to be set
     */
    public void addSpeed(Integer speed);
}
