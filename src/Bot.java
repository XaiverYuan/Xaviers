import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description:
 */
public class Bot implements Serializable {

    private static final int serialVersionUID = Main.serialVersionUID;
    Player player;

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

    Operation chooseOperations() {

        if (learningMap == null) return player.randomOperation();
        String key = player.hash();
        key += player.game.enemyPlayer(player).get(0).hash();
        HashMap<String, Double> pointerSaved = winRate.get(key);
        if (pointerSaved == null) {
            pointerSaved = new HashMap<>();
            HashMap<String, ArrayList<Integer>> pointer = learningMap.get(key);
            if(pointer!=null) {
                double sum=0;
                for (String operationName : pointer.keySet()) {
                    double share=(1.0 * pointer.get(operationName).get(2) / (pointer.get(operationName).get(2) + pointer.get(operationName).get(0)));
                    sum+=share;
                    pointerSaved.put(operationName, share);
                }
                for (String s : pointer.keySet()) {
                    pointerSaved.put(s,pointerSaved.get(s)/sum);
                }
            }else {
                //System.out.println("No such a key"+key);
                return player.randomOperation();
            }
        }
        return player.skillMap.get(GameRandom.random(pointerSaved));
    }
}
