import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description:
 */
public class Game implements Serializable {
    private static final int serialVersionUID = Main.serialVersionUID;
    private ArrayList<ArrayList<Player>> teams;
    //2 teams, each contains a player for now
    private ArrayList<Player> alivePlayer;
    private int winnerTeam;
    private GameState gameState;
    Record record;

    enum GameState {
        NotEnd, Draw, WinnerIs;
    }

    ArrayList<Player> enemyPlayer(Player player) {
        //called by players to get the alive enemy players
        return new ArrayList<>(alivePlayer.stream().filter(e -> e.teamId != player.teamId).collect(Collectors.toList()));
    }

    Game() {
        gameState = GameState.NotEnd;
        teams = new ArrayList<>();
        teams.add(new ArrayList<>());
        teams.add(new ArrayList<>());
        alivePlayer = new ArrayList<>();
    }

    void addPlayer(int teamId, Player player) {
        teams.get(teamId).add(player);
        alivePlayer.add(player);
        //link the player with correct team and game
        player.teamId = teamId;
        player.game = this;
    }

    void recordGame(Record record) {
        this.record = record;
        record.records = new ArrayList<>();
    }

    int startGame() {
        for (; gameState == GameState.NotEnd; round()) ;

        if (gameState == GameState.Draw) {
            //System.out.println("平局");
            if (record != null) {
                record.flush(0);
            }

            return 0;
        } else {
            //System.out.println(winnerTeam + "赢了");
            if (record != null) record.flush(winnerTeam == 0 ? 1 : -1);
            return winnerTeam * 2 - 1;
        }
    }

    void round() {
        alivePlayer.forEach(Player::getOperation);//ask for operation

        alivePlayer.stream().filter(e -> e.currentOperation == Player.defend).forEach(e -> e.defense = true);//set defense first
        if (record != null) {
            //alivePlayer.forEach(System.out::println);
            //System.out.println("~");
            record.addOperates(teams.get(0).get(0), teams.get(1).get(0));
        }
        String[] skillInfo = alivePlayer.stream().map(e -> e.currentOperation.operate(e, e.targets)).toArray(String[]::new);//do the operation and get returned string

        alivePlayer.forEach(Player::refresh);//refresh them and set alive

        alivePlayer = new ArrayList<>(alivePlayer.stream().filter(e -> e.alive).collect(Collectors.toList()));//delete dead players
        if (alivePlayer.size() == 0) {
            //no one alive
            gameState = GameState.Draw;
        } else {
            int aliveTeam = alivePlayer.get(0).teamId;//assume only one team alive
            if (alivePlayer.stream().allMatch(e -> e.teamId == aliveTeam)) {
                //no other alive team
                gameState = GameState.WinnerIs;
                winnerTeam = aliveTeam;
            }
        }
        if (record == null) print(skillInfo);
    }

    void print(String[] skillInfo) {

        //TODO: should be GUI
        Arrays.stream(skillInfo).forEach(System.out::println);
        alivePlayer.forEach(System.out::println);
    }
}
