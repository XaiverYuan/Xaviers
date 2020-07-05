import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.25
 * @Description:
 */
public class GameRandom {
    static int random(int range) {
        return (int) (Math.random() * range);
    }
    static <E> E randomChoose(List<E> list){
        int u=(int)(Math.random()*list.size());
        return list.get(u);
    }
    static double randomDouble(double max) {
        return Math.random() * max;
    }

    static String random(HashMap<String, Double> map) {
        return random(map,1);
    }

    static String random(HashMap<String, Double> map, double max) {
        double rand = randomDouble(max);
        for (String s : map.keySet()) {
            final double temp = map.get(s);
            if (temp > rand) return s;
            else rand -= temp;
        }
        return map.keySet().iterator().next();
    }
}
