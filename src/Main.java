import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description: interface that interact with user.
 * TODO: should be GUI later
 */
public class Main {

    public static final int serialVersionUID = 5000;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis(); //获取开始时间

        //要测的程序或方法
        OracleLearning(new Assassin(), new Druid(), "德鲁伊85命中刺客10miss");
        long endTime = System.currentTimeMillis(); //获取结束时间

        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }

    private static void OracleLearning(Player player1, Player player2) throws Exception {
        OracleLearning(player1, player2, 5, 100, player1.getClass().getName() + new Date().getTime(), player1.getClass().getName() + new Date().getTime());
    }

    private static void OracleLearning(Player player1, Player player2, String version) throws Exception {
        OracleLearning(player1, player2, 5, 100, player1.getClass().getName() + version, player2.getClass().getName() + version);
    }

    private static void OracleLearning(Player player1, Player player2, String name1, String name2) throws Exception {
        OracleLearning(player1, player2, 5, 100, name1, name2);
    }

    private static void OracleLearning(Player player1, Player player2, int iterative, int singletimes, String version) throws Exception {
        OracleLearning(player1, player2, iterative, singletimes, player1.getClass().getName() + version, player2.getClass().getName() + version);
    }

    private static void OracleLearning(Player player1, Player player2, int iterative, int singletimes, String name1, String name2) throws Exception {
        if (iterative > 5) {
            System.out.println("迭代次数过多，强制设置为5");
            iterative = 5;
        }
        HashMap<String, HashMap<String, ArrayList<Integer>>> map1 = null;
        HashMap<String, HashMap<String, ArrayList<Integer>>> map2 = null;

        for (int i = 0; i < iterative; i++) {
            Record[] records = groupLearning(map1, map2, player1, player2, singletimes);
            map1 = records[0].save();
            map2 = records[1].save();
        }

        Record.save(map1, name1);
        Record.save(map2, name2);
    }

    private static Record[] groupLearning(HashMap<String, HashMap<String, ArrayList<Integer>>> record1, HashMap<String, HashMap<String, ArrayList<Integer>>> record2, Player p1, Player p2, int k) throws Exception {
        return new Record[]{learn(record1, record2, p1, p2, k), learn(record2, record1, p2, p1, k)};
    }

    private static Record learn(HashMap<String, HashMap<String, ArrayList<Integer>>> record1, HashMap<String, HashMap<String, ArrayList<Integer>>> record2, Player p1, Player p2) throws Exception {
        return learn(record1, record2, p1, p2, 10);
    }

    private static Record learn(HashMap<String, HashMap<String, ArrayList<Integer>>> record1, HashMap<String, HashMap<String, ArrayList<Integer>>> record2, Player p1, Player p2, int k) throws Exception {
        int count[] = new int[3];
        Record record = new Record();
        final int single = 10000;

        Constructor constructor1 = null;
        Constructor constructor2 = null;
        constructor1 = p1.getClass().getDeclaredConstructor(Bot.class);
        constructor2 = p2.getClass().getDeclaredConstructor(Bot.class);

        for (int i = 0; i < k; i++) {
            System.out.print("<");
        }
        System.out.println();

        for (int i = 0; i < k * single; i++) {
            Game game = new Game();

            game.addPlayer(0, (Player) constructor1.newInstance(new Bot(record1)));
            game.addPlayer(1, (Player) constructor2.newInstance(new Bot(record2)));
            game.recordGame(record);
            count[game.startGame() + 1]++;
            if (i % single == 0) System.out.print(">");
        }
        System.out.println();
        System.out.println(p1.getClass().getName() + "赢了" + count[0]);
        System.out.println("平局" + count[1]);
        System.out.println(p2.getClass().getName() + "赢了" + count[2]);
        return record;
    }

    static Operation askOperation(Player player) {
        while (true) {
            System.out.println("请输入你的动招");
            String input = Main.scanner.nextLine();
            Operation skill;
            if (!player.skillMap.keySet().contains(input)) {
                System.out.println("此招不存在");
                continue;
            }
            skill = player.skillMap.get(input);
            if (player.availableOperation().contains(skill)) {
                return skill;
            }
            System.out.println("你不能使用此招");
        }
    }

}
