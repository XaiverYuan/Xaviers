import java.io.*;
import java.text.DateFormat;
import java.util.*;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.24
 * @Description:
 */
public class Record implements Serializable {
    private static final int serialVersionUID = Main.serialVersionUID;

    void save(String path) {
        save(save(),path);
    }
    static void save(HashMap<String, HashMap<String,ArrayList<Integer>>> forSave,String path) {
        try {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(forSave);
            System.out.println("文件已保存"+file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    HashMap<String, HashMap<String,ArrayList<Integer>>> save(){
        HashMap<String, HashMap<String,ArrayList<Integer>>> forSave=new HashMap<>();
        for (String s : learningMap.keySet()) {
            HashMap<String,ArrayList<Integer>> pointer=new HashMap<>();
            for (Operation operation : learningMap.get(s).keySet()) {
                assert learningMap.get(s).get(operation)!=null;
                ArrayList<Integer> temp=new ArrayList<Integer>();
                Arrays.stream(learningMap.get(s).get(operation)).forEach(temp::add);
                pointer.put(Player.operationToString(operation),temp);
            }
            forSave.put(s,pointer);
        }
        return forSave;
    }

    static HashMap<String, HashMap<String, ArrayList<Integer>>> load(String path) {
        return load(new File(path));
    }

    static HashMap<String, HashMap<String, ArrayList<Integer>>> load(File file) {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
            return (HashMap<String, HashMap<String, ArrayList<Integer>>>) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class roundRecord implements Serializable {
        private static final int serialVersionUID = Main.serialVersionUID;
        String player1Hash;
        String player2Hash;
        Operation operation;

        roundRecord(Player p1, Player p2) {
          /*  player1Hash = p1.hash();
            player2Hash = p2.hash();
            operation = p1.currentOperation;*/
        }

        @Override
        public String toString() {
            return player1Hash + ":" + player2Hash + Player.operationToString(operation);
        }
    }

    ArrayList<roundRecord> records;
    HashMap<String, HashMap<Operation, int[]>> learningMap;

    Record() {
        this.learningMap = new HashMap<>();
        records = new ArrayList<>();
        //Arrays.stream(map).flatMap(Arrays::stream).forEach(HashMap::new);
    }

    void addOperates(Player player1, Player player2) {
        records.add(new roundRecord(player1, player2));
    }

    void flush(int state) {//state:-1lose, 0 draw, 1 win

        for (int i = 0; i < records.size(); i++) {
            roundRecord record = records.get(i);
            String key = record.player1Hash + record.player2Hash;
            if (!learningMap.keySet().contains(key)) learningMap.put(key, new HashMap<>());
            HashMap<Operation, int[]> pointer = learningMap.get(key);
            if (pointer.keySet().contains(record.operation)) {
                int[] counts = pointer.get(record.operation);
                counts[state + 1]++;
            } else {
                int[] counts = new int[3];
                counts[state + 1]++;
                pointer.put(record.operation, counts);

            }
        }
    }

    void viewMap() {
        learningMap.keySet().forEach(this::viewMap);
    }

    void viewMap(String hash) {
        HashMap<Operation, int[]> target = learningMap.get(hash);
        System.out.println(hash);
        System.out.println(target.keySet().size());
        for (Operation operation : target.keySet()) {
            System.out.print(Player.operationToString(operation) + "\t");
            int[] data = target.get(operation);
            for (int i : data) {
                System.out.print(i + " ");
            }
            System.out.printf("\t%.2f sum%d\n", data[2] * 1.0 / (data[2] + data[0]), data[0] + data[1] + data[2]);
        }
    }
}
