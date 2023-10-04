import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int dir = Note.LEFT;
    private int x = 0;
    private boolean empty = true;
    private List<Note> notes = new ArrayList<Note>();


    public Lane(int dir, int x) {
        this.dir = dir;
        this.x = x;
        this.empty = false;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public int getDir() {
        return this.dir;
    }

    public int getX() {
        return this.x;
    }

    public boolean isEmpty() {
        return this.empty;
    }
}
