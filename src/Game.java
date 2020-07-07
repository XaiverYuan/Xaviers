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
    protected static final long serialVersionUID = Main.serialVersionUID;
    protected ArrayList<Player> players;
    protected Record record;
    protected boolean print;
    protected Game() {
        players = new ArrayList<>();
    }

    protected Game(Record record) {
        this.record = record;
        players = new ArrayList<>();
    }

    protected void addPlayer(Player player, int teamId) {
        assert teamId > 0;
        player.teamId = teamId;
        players.add(player);
        if (player.bot != null) player.bot.game = this;
    }
    public void println(String a){
        if(print)
        System.out.println(a);
    }

    abstract protected int startGame();//do the necessary check here and start the game

    protected Stream<Player> getplayers(int teamId) {
        return players.stream().filter(e -> e.teamId == teamId);
    }

    protected int round() {
        ArrayList<Player> targets = alivePlayers();
        targets.forEach(e -> {
            if (e.bot == null) {
                askOperation(e);
            } else {
                e.bot.chooseOperations();
            }
        });
        if (record != null) {
            record.update();
        }
        targets.stream().sorted(Comparator.comparingInt(e -> -e.priority)).map(e -> e.currentOperation.operate(e, e.targets)).forEach(this::println);
        targets.forEach(Player::refresh);
        alivePlayers().stream().map(Player::toString).forEach(this::println);

        return end();

    }

    abstract void askOperation(Player player);

    int end() {
        if (alivePlayers().stream().findAny().isEmpty()) {
            if (record != null) record.flush(0);
            return 0;
        }//draw, no one wins
        else {
            int anyTeam = alivePlayers().stream().findFirst().get().teamId;
            if (alivePlayers().stream().allMatch(e -> e.teamId == anyTeam)) {
                record.flush(anyTeam);
                return anyTeam;
            }
            return -1;//the game is not ended yet
        }
    }

    ArrayList<Player> alivePlayers() {
        return players.stream().filter(e -> e.alive).collect(Collectors.toCollection(ArrayList::new));
    }

    abstract ArrayList<Player> getEnemy(Player target);
}
