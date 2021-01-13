import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.25
 * @LastUpdated: 2021.1.11
 * @Description: A toolkit for making random decisions
 */
public class GameRandom {
    /**
     * Might be implement differently latter
     * For example, we can change the distribution... for more fun!
     * @param range (the maximum number you want to get) + 1
     * @return a random number between 0 and range-1
     */
    static int random(int range) {
        return (int) (Math.random() * range);
    }

    /**
     * This will be influenced if we put some fun in the
     * @see GameRandom#random(int)
     * @param list A list of all choices
     * @param <E> the type of the choice
     * @return one choice we made
     */
    static <E> E randomChoose(List<E> list){
        return list.get(random(list.size()));
    }

    /**
     * generate a random number between 0 and max
     * @param max the maximum of the output
     * @return a random double between max and 0
     */
    static double randomDouble(double max) {
        return Math.random() * max;
    }

    /**
     * Choose the operation based on their corresponding probability
     * a higher probability means they are more likely to be chosen
     * @param map a map contains a lot of choice and its probability
     * @param <E>  the type of the choice
     * @return the choice we made
     */
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
