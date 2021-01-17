import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SoloGame extends Game{

    public SoloGame(){
        super();
    }
    public SoloGame(SoloRecord soloRecord) {
        super(soloRecord);
    }

    @Override
    ArrayList<Player> getEnemy(Player target) {
        ArrayList<Player> answer=alivePlayers().stream().filter(e->e!=target).collect(Collectors.toCollection(ArrayList::new));
        assert answer.size()==1;
        return answer;
    }

    @Override
    protected void addPlayer(Player player, int teamId) {
        super.addPlayer(player, teamId);
        assert alivePlayers().stream().filter(e->e.teamId==teamId).count()==1;
        assert alivePlayers().stream().map(e->e.teamId).distinct().count()<=2;
    }

    @Override
    protected int startGame() {
        assert players.size()==2;
        assert players.get(0).teamId!=players.get(1).teamId;
        if(record==null){
            record=new SoloRecord(players.get(0),players.get(1));
        }
        assert record instanceof SoloRecord;
        record.start();
        int result;
        while ((result=round())<0);
        if(result!=0){
            this.println(getplayers(result).findFirst().get().name+"赢了");
        }else {
            this.println("平局");
        }
        return result;
    }

    @Override
    void askOperation(Player player) {
        System.out.println("您现在的能量是" + player.energy);
        System.out.println("敌人的能量是" + getEnemy(player).get(0).energy);
        System.out.println("您的可选操作有:");
        for (Operation operation : player.availableOperation()) {
            System.out.println(Player.operationToString(operation));
        }
        player.targets = getEnemy(player);
        String in;
        Scanner scanner = Main.scanner;
        in = scanner.nextLine();
        in = in.replace("\n", "");
        player.currentOperation = Player.skillMap.get(in);
        if (player.currentOperation == null) {
            System.out.println("没有这个操作");
            assert false;
        }
    }
}
