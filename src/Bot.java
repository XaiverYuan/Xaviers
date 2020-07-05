import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description:
 */
public class Bot implements Serializable {

    private static final int serialVersionUID = Main.serialVersionUID;
    Player player;
    Game game;
    static int rand = 0;
    HashMap<String, HashMap<String, ArrayList<Integer>>> learningMap;

    Bot(Player player) {
        this();
        this.player = player;
        player.bot = this;
    }

    Bot(HashMap<String, HashMap<String, ArrayList<Integer>>> learningMap) {
        this();
        this.learningMap = learningMap;
    }

    Bot() {
        winRate = new HashMap<>();
    }

    HashMap<String, HashMap<String, Double>> winRate;

    void chooseOperations() {
        player.targets=game.getEnemy(player).collect(Collectors.toCollection(ArrayList::new));
        player.currentOperation=GameRandom.randomChoose(player.availableOperation());    }
}
