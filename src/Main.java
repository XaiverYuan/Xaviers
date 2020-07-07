import java.util.ArrayList;
import java.util.Scanner;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description: interface that interact with user.
 * TODO: should be GUI later
 */
public class Main {

    public static final long serialVersionUID = 6716681636L;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        trainMore();
    }
    public static void trainMore() {
        SoloRecord keepSoloRecord =null;
        for (int total = 0; total < 10; total++) {
            int[] ends = new int[3];
            ArrayList<SoloRecord> records = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                records.add(train(ends, keepSoloRecord));

            }
            keepSoloRecord = SoloRecord.add(records);
            System.out.println(total+" 平"+ends[0]+"德鲁伊赢"+ends[1]+"刺客赢"+ends[2]);
        }
        int[] check = keepSoloRecord.learnMap.get("Assassin1血0能量1背刺Druid4血2能量")[1].get("影沐");

        keepSoloRecord.description="运行10次，每次10000次对练，德鲁伊10命中加成，刺客瞬杀10miss";
        keepSoloRecord.save("./good");
    }
    private static SoloRecord train(int[] ends,final SoloRecord record){
        Game soloGame=new SoloGame(null);
        Player p1=new Druid(new Bot(record));
        Player p2=new Assassin(new Bot(record));
        soloGame.addPlayer(p1,1);
        soloGame.addPlayer(p2,2);
        ends[soloGame.startGame()]++;
        return (SoloRecord) soloGame.record;

    }
    private static void play(){
        SoloGame soloGame=new SoloGame();
        soloGame.print=true;
        SoloRecord record=new SoloRecord("./first100000");
        Player p1=new Druid();
        Player p2=new Assassin(new Bot(record));
        soloGame.addPlayer(p1,1);
        soloGame.addPlayer(p2,2);
        soloGame.startGame();
    }
    private static String test(){
        return "test";
    }

}
