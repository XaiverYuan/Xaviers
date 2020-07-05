import java.util.stream.Stream;

public class SoloGame extends Game{
    @Override
    Stream<Player> getEnemy(Player target) {
        Stream<Player> answer=alivePlayers().stream().filter(e->e!=target);
        assert answer.count()==1;
        return answer;
    }

    @Override
    void addPlayer(Player player, int teamId) {
        super.addPlayer(player, teamId);
        assert alivePlayers().stream().filter(e->e.teamId==teamId).count()==1;
        assert alivePlayers().stream().map(e->e.teamId).distinct().count()<=2;
    }

    @Override
    void startGame() {
        assert players.size()==2;
        assert players.get(0).teamId!=players.get(1).teamId;
        int result;
        while ((result=round())<0);
        if(result!=0){
            System.out.println(getplayers(result).findFirst().get().name+"赢了");
        }else {
            System.out.println("平局");
        }
    }
}
