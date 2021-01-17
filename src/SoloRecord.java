import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class SoloRecord extends Record {
    Class<? extends Player> class1;
    Class<? extends Player> class2;
    /*
        key:player1.hash and player2.hash(int array length is always 2)
        value:an array of hashmap size 2;
        the hashmap:
            key:String of operation
            value:an array of hashmap size 3;
                corresponding to their lose/draw/win;
     */
    HashMap<String, HashMap<String, int[]>[]> learnMap;
    /*
        The int array here corresponding to the operation players took
        during the game. It is somehow used like a pointer.
        When the game ends, all these array would be updated due to result
        of the game (win, draw, lose)
     */
    transient ArrayList<int[]>[] inGameOperations;
    transient Player p1;
    transient Player p2;
    private static final long serialVersionUID = Main.serialVersionUID;


    SoloRecord(String address) {
        load(address);
    }

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
        if (p1.getClass().getName().compareTo(p2.getClass().getName()) > 0) {
            this.p1 = p2;
            this.p2 = p1;
        } else {
            this.p1 = p1;
            this.p2 = p2;
        }
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
        target.forEach((k, v) -> anonVal.put(k, v.clone()));
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
        e2.forEach((k,v)->{
            if (e.containsKey(k)) {
                int[] raw = e.get(k).clone();
                int[] addOn = v.clone();
                for (int i = 0; i < 3; i++) {
                    raw[i] += addOn[i];
                }
                e.put(k, raw);
            } else {
                e.put(k, v.clone());
            }
        });
        return e;
    }

    static SoloRecord add(ArrayList<SoloRecord> soloRecords) {
        Class<? extends Player> class1 = soloRecords.get(0).class1;
        Class<? extends Player> class2 = soloRecords.get(0).class2;
        assert soloRecords.stream().allMatch(e -> e.class1 == class1 && e.class2 == class2);
        SoloRecord answer = new SoloRecord(class1, class2);
        for (SoloRecord soloRecord : soloRecords) {
            for (String string : soloRecord.learnMap.keySet()) {
                if (!answer.learnMap.containsKey(string)) {
                    answer.learnMap.put(string, hashMapClone(soloRecord.learnMap.get(string)));
                } else {
                    answer.learnMap.put(string, hashMapAdd(answer.learnMap.get(string), soloRecord.learnMap.get(string)));
                }
            }
        }
        return answer;
    }

    String hash() {
        return p1.hash() + p2.hash();
    }

    HashMap<String, int[]> getMap(Player target, Player enemy) {
        boolean reverse = target.getClass().getName().compareTo(enemy.getClass().getName()) > 0;
        if (reverse) {
            p1 = enemy;
            p2 = target;
        } else {
            p1 = target;
            p2 = enemy;
        }
        HashMap<String, int[]>[] raw = learnMap.get(hash());

        return raw == null ? null : raw[reverse ? 1 : 0];
    }

    @Override
    void save(String file) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(file)))) {
            objectOutputStream.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Override
    void load(String file) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(file)))) {
            Object anon = objectInputStream.readObject();
            assert anon instanceof SoloRecord;
            this.learnMap = ((SoloRecord) anon).learnMap;
            this.class1 = ((SoloRecord) anon).class1;
            this.class2 = ((SoloRecord) anon).class2;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Override
    void start() {
        assert this.p1 != null && this.p2 != null;
        inGameOperations = new ArrayList[2];
        inGameOperations[0] = new ArrayList<>();
        inGameOperations[1] = new ArrayList<>();
    }
    /*
        Put all the operation players made in this round into the
        inGameOperations
     */
    void update() {
        HashMap<String, int[]>[] target = learnMap.get(hash());
        if (target == null) {
            HashMap<String, int[]> anon1 = new HashMap<>();
            HashMap<String, int[]> anon2 = new HashMap<>();
            learnMap.put(hash(), new HashMap[]{anon1, anon2});
            target = learnMap.get(hash());
        }
        int[] anon1 = target[0].get(Player.operationToString(p1.currentOperation));
        if (anon1 == null) {
            target[0].put(Player.operationToString(p1.currentOperation), new int[3]);
            anon1 = target[0].get(Player.operationToString(p1.currentOperation));
        }
        inGameOperations[0].add(anon1);
        int[] anon2 = target[1].get(Player.operationToString(p2.currentOperation));
        if (anon2 == null) {
            target[1].put(Player.operationToString(p2.currentOperation), new int[3]);
            anon2 = target[1].get(Player.operationToString(p2.currentOperation));
        }
        inGameOperations[1].add(anon2);
    }

    @Override
    void flush(int teamId) {
        //if it is 0, then it means draw
        int adds = teamId == 0 ? 1 : teamId == p1.teamId ? 2 : teamId == p2.teamId ? 0 : -100;
        assert adds != -100;
        for (int[] ints : inGameOperations[0]) {
            ints[adds]++;
        }
        for (int[] ints : inGameOperations[1]) {
            ints[2 - adds]++;
        }
    }
}
