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
public class SoloBot extends Bot {

    private static final long serialVersionUID = Main.serialVersionUID;
    Player enemy;
    SoloRecord record;
    SoloBot(SoloRecord record) {
        this.record=record;
    }
    SoloBot(){

    }
    void chooseOperations() {
        final int assumeBasic = 2;
        /*
            when an operation is always lose, it does not means this operation will never be chosen
            It might because our data is few. And there is hope.
            we give them 2 wins for free, and 2 additional draws and lose
            example:
            if an operation is 0 win, 3 draw, 10 lose, the probability this operation is chosen is
            (0+2)/(3+10+2*3)=2/19
         */
        if (enemy == null) enemy = game.getEnemy(player).get(0);
        player.targets = game.getEnemy(player);
        if (record != null) {
            HashMap<String, int[]> hashMap = record.getMap(player, enemy);
            if(hashMap!=null) {
                HashMap<Operation, Double> answerMap = new HashMap<>();
                for (String s : hashMap.keySet()) {
                    int[] anonData = hashMap.get(s);
                    answerMap.put(Player.skillMap.get(s), 1.0 * (anonData[2] + assumeBasic) / (3 * assumeBasic + anonData[0] + anonData[1] + anonData[2]));
                }
                player.currentOperation = GameRandom.random(answerMap);
            }else {
                player.currentOperation = GameRandom.randomChoose(player.availableOperation());
            }
        } else {
            player.currentOperation = GameRandom.randomChoose(player.availableOperation());
        }
    }
}
