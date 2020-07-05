import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SoloRecord implements Serializable {
    static final long serialVersionUID = Main.serialVersionUID;
    Class<? extends Player> class1;
    Class<? extends Player> class2;
    HashMap<Integer, HashMap<String, int[]>[]> learnMap;
    transient ArrayList<int[]>[] inGameOperations;
    transient Player p1;
    transient Player p2;

    /*
        key:player1.hash and player2.hash(int array length is always 2)
        value:an array of hashmap size 2;
        the hashmap:
            key:String of operation
            value:an array of hashmap size 3;
                corresponding to their lose/draw/win;
     */

    SoloRecord(Class<? extends Player> class1, Class<? extends Player> class2) {
        if (class1.getName().compareTo(class2.getName()) > 0) {
            Class<? extends Player> anon = class1;
            class1 = class2;
            class2 = anon;
        }
        this.class1 = class1;
        this.class2 = class2;
        learnMap = new HashMap<>();
    }

    SoloRecord(Player p1, Player p2) {
        this(p1.getClass(), p2.getClass());
    }

    private static HashMap<String, int[]>[] hashMapClone(HashMap<String, int[]>[] target) {
        assert target.length == 2;
        HashMap<String, int[]>[] answer = new HashMap[2];
        answer[0] = hashMapClone(target[0]);
        answer[1] = hashMapClone(target[1]);
        return answer;
    }

    private static HashMap<String, int[]> hashMapClone(HashMap<String, int[]> target) {
        HashMap<String, int[]> anonVal = new HashMap<>();
        for (String s : target.keySet()) {
            anonVal.put(s, target.get(s).clone());
        }
        return anonVal;
    }

    private static HashMap<String, int[]>[] hashMapAdd(HashMap<String, int[]>[] e1, HashMap<String, int[]>[] e2) {
        assert e1.length == 2 && e2.length == 2;
        HashMap<String, int[]>[] answer = new HashMap[2];
        answer[0] = hashMapAdd(e1[0], e2[0]);
        answer[1] = hashMapAdd(e1[1], e2[1]);
        return answer;
    }

    private static HashMap<String, int[]> hashMapAdd(HashMap<String, int[]> e1, HashMap<String, int[]> e2) {
        HashMap<String, int[]> e = hashMapClone(e1);
        for (String s : e2.keySet()) {
            if (e.containsKey(s)) {
                int[] raw = e.get(s).clone();
                int[] addOn = e2.get(s).clone();
                for (int i = 0; i < 3; i++) {
                    raw[i] += addOn[i];
                }
                e.put(s, raw);
            } else {
                e.put(s, e2.get(s).clone());
            }
        }
        return e;
    }

    static SoloRecord add(ArrayList<SoloRecord> soloRecords) {
        Class<? extends Player> class1 = soloRecords.get(0).class1;
        Class<? extends Player> class2 = soloRecords.get(0).class2;
        assert soloRecords.stream().allMatch(e -> e.class1 == class1 && e.class2 == class2);
        SoloRecord answer = new SoloRecord(class1, class2);
        for (SoloRecord soloRecord : soloRecords) {
            for (Integer ints : soloRecord.learnMap.keySet()) {
                if (!answer.learnMap.containsKey(ints)) {
                    answer.learnMap.put(ints, hashMapClone(soloRecord.learnMap.get(ints)));
                } else {
                    answer.learnMap.put(ints,hashMapAdd(answer.learnMap.get(ints), soloRecord.learnMap.get(ints)));
                }
            }
        }
        return answer;
    }

    void start(Player p1, Player p2) {
        inGameOperations = new ArrayList[2];
        inGameOperations[0] = new ArrayList<>();
        inGameOperations[1] = new ArrayList<>();
        this.p1 = p1;
        this.p2 = p2;
    }

    int hash() {
        return p1.hashCode() << 15 + p2.hashCode();
    }

    void update() {
        HashMap<String, int[]>[] target = learnMap.get(hash());
        if(target==null){
            HashMap<String, int[]> anon1=new HashMap<>();
            HashMap<String, int[]> anon2=new HashMap<>();
            learnMap.put(hash(),new HashMap[]{anon1,anon2});
            target = learnMap.get(hash());
        }
        int[] anon1=target[0].get(Player.operationToString(p1.currentOperation));
        if(anon1==null){
            target[0].put(Player.operationToString(p1.currentOperation),new int[3]);
        }
        inGameOperations[0].add(anon1);
        int[] anon2=target[0].get(Player.operationToString(p1.currentOperation));
        if(anon2==null){
            target[0].put(Player.operationToString(p1.currentOperation),new int[3]);
        }
        inGameOperations[0].add(anon2);
    }

    void flush(Player player) {
        //if it is null, then it means draw
        int adds = player == null ? 1 : player == p1 ? 0 : player == p2 ? 2 : -100;
        assert adds != -100;
        for (int[] ints : inGameOperations[0]) {
            ints[adds]++;
        }
        for (int[] ints : inGameOperations[1]) {
            ints[2 - adds]++;
        }
    }
}
