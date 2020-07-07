import java.util.ArrayList;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.24
 * @Description:
 */
public class Druid extends Player {
    private static final long serialVersionUID = Main.serialVersionUID;
    private static final int START_HEALTH = 4;
    private static final int START_ENERGY = 0;
    public static final String DEFAULT_NAME = "德鲁伊";
    private static int count = 0;
    private int buff = 10;

    static {
        skillMap.put("月坠", (i, e) -> {
            assert i instanceof Druid;
            i.energy -= 3;
            ArrayList<Player> hit = new ArrayList<>();
            for (Player player : e) {
                int rand = GameRandom.random(100);
                if (rand < 75 + ((Druid) i).buff) {
                    player.healthDecrease(Math.max(player.getHealth()/2,1)+(player.defense?1:0));
                    hit.add(player);
                }

            }
            return i.name + "使用了月坠,命中了" + hit.size() + "人" + hit;
        });
        skillMap.put("影沐", (i, e) -> {
            assert i instanceof Druid;
            i.energy -= 2;
            Player player = e.get(0);
            int rand = GameRandom.random(100);
            String answer=i.name+"对"+player.name+"使用了影沐";
            if (rand < 75 + ((Druid) i).buff) {
                player.healthDecrease(player.defense ? 3 : 2);
                return answer;
            }
            return answer + "，但未命中";

        });
    }

    Druid() {
        this(DEFAULT_NAME + (++count), null);
    }

    Druid(Bot bot) {
        this(DEFAULT_NAME+ (++count), bot);
    }

    Druid(String name) {
        this(name, null);
    }

    Druid(String name, Bot bot) {
        super(name, START_HEALTH, START_ENERGY, bot);
    }

    @Override
    ArrayList<Operation> availableOperation() {
        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(Player.gain);
        operations.add(Player.defend);
        //those two operation is always available
        if (energy >= 2) operations.add(skillMap.get("影沐"));
        if (energy >= 3) operations.add(skillMap.get("月坠"));
        if (getHealth() == 4 && energy >= 3) {
            assert operations.size() == 4;
        }
        return operations;
    }




}
