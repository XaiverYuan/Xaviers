import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description:
 */
public abstract class Game implements Serializable {
    protected static final int serialVersionUID = Main.serialVersionUID;
    protected ArrayList<Player> players;

    Game() {
        players = new ArrayList<>();
    }

    void addPlayer(Player player, int teamId) {
        player.teamId = teamId;
        players.add(player);
        if(player.bot!=null)player.bot.game=this;
    }

    abstract void startGame();//do the necessary check here and start the game

    Stream<Player> getplayers(int teamId) {
        return players.stream().filter(e -> e.teamId == teamId);
    }

    int round() {
        ArrayList<Player> targets = alivePlayers();
        targets.forEach(e -> {
            if (e.bot == null) {
                askOperation(e);
            } else {
                e.bot.chooseOperations();
            }
        });
        targets.stream().sorted(Comparator.comparingInt(e -> -e.priority)).map(e -> e.currentOperation.operate(e, e.targets)).forEach(System.out::println);
        targets.forEach(Player::refresh);
        alivePlayers().forEach(System.out::println);
        return end();

    }

    void askOperation(Player player) {
        System.out.println("您现在的能量是" + player.energy);
        System.out.println("敌人的能量是" + getEnemy(player).findFirst().get().energy);
        System.out.println("您的可选操作有:");
        for (Operation operation : player.availableOperation()) {
            System.out.println(Player.operationToString(operation));
        }
        player.targets = getEnemy(player).collect(Collectors.toCollection(ArrayList::new));
        String in;
        Scanner scanner = Main.scanner;
        in = scanner.nextLine();
        in=in.replace("\n","");
        player.currentOperation = Player.skillMap.get(in);
        if (player.currentOperation == null) {
            System.out.println("没有这个操作");
            assert false;
        }
    }

    int end() {
        if (alivePlayers().stream().findAny().isEmpty()) return 0;//draw, no one wins
        else {
            int anyTeam = alivePlayers().stream().findFirst().get().teamId;
            if (alivePlayers().stream().allMatch(e -> e.teamId == anyTeam)) return anyTeam;
            return -1;//the game is not ended yet
        }
    }

    ArrayList<Player> alivePlayers() {
        return players.stream().filter(e -> e.alive).collect(Collectors.toCollection(ArrayList::new));
    }

    abstract Stream<Player> getEnemy(Player target);
}
