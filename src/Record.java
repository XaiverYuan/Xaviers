import java.io.Serializable;

public abstract class Record implements Serializable {
    protected String description;
    static final long serialVersionUID=Main.serialVersionUID;
    abstract void start();
    abstract void update();
    abstract void flush(int teamId);
    abstract String hash();
    abstract void save(String file);
    abstract void load(String file);//would set current Record to the loaded one
}
