import java.util.HashMap;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

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

    static <E> E random(HashMap<E, Double> map) {
        double total=map.values().stream().reduce(0.0,Double::sum);
        double rands=Math.random()*total;
        for (E e : map.keySet()) {
            if(map.get(e)>=rands)
                return e;
            rands-=map.get(e);
        }
        assert false;
        return null;
    }


}
