import java.io.Serializable;

public abstract class Bot implements Serializable {
    static final long serialVersionUID=Main.serialVersionUID;
    protected Player player;
    protected Game game;
    abstract void chooseOperations();

}
