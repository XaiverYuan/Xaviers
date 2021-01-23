import java.io.Serializable;
import java.util.*;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description: the parent class of all kinds of Players
 */
public abstract class Player implements Serializable {
    private static final long serialVersionUID = Main.serialVersionUID;
    boolean alive;
    /*
        if this is null then it is played by human, or it is played by ai
     */
    Bot bot;
    int teamId;
    int energy;
    /*
        Higher priority means the operation would be cast first
     */
    transient int priority;
    /*
        The reason health is private here is some
        kind of player has deduction on damage taken.
        i.e. Assassin take one less damage.
     */
    private int health;
    /*
        The operation they choose to cast this round
     */
    transient Operation currentOperation;
    transient ArrayList<Player> targets;
    /*
        two basic abilities that all kinds of players should be able to cast
     */
    static final Operation defend = (i, e) -> {
        i.defense = true;
        i.priority = 10;
        return i.name + StringConstant.dictionary.get(NameConstant.Defend);
    };
    static final Operation gain = (i, e) -> {
        i.energy++;
        return i.name + StringConstant.dictionary.get(NameConstant.Gain);
    };
    /*
        Contains all kinds of
     */
    static HashMap<String, Operation> skillMap;
    static {
        skillMap = new HashMap<>();
        skillMap.put("聚气", gain);
        skillMap.put("防御", defend);
    }

    boolean defense;//if someone is defending, this would be true, this will be set to false after a round complete
    String name;

    Player(String name, int startHealth, int startEnergy, SoloBot bot) {
        //create an AI player
        this.alive = true;
        this.name = name;
        this.health = startHealth;
        this.energy = startEnergy;
        this.defense = false;
        this.bot = bot;
        if (bot != null) bot.player = this;
    }

    abstract ArrayList<Operation> availableOperation();

    //return all operation that is available now
    String hash() {
        return this.getClass().getName() + getHealth() + "血" + energy + "能量";
    }

    void healthDecrease(int amount) {
        if (amount <= 0) return;
        health -= amount;
    }

    int getHealth() {
        return health;
    }

    boolean refresh() {
        //reset defense and check for alive
        if (getHealth() <= 0) alive = false;
        defense = false;
        priority = 0;
        return alive;
    }

    @Override
    public String toString() {
        return "\tTeam" + teamId + " " + name + ":" + health + "血" + energy + "能量" + operationToString(currentOperation);
    }

    static String operationToString(Operation operation) {
        if (operation == gain) return "聚气";
        if (operation == defend) return "防御";
        if (operation == null) return "空";
        return skillMap.keySet().stream().filter(e->skillMap.get(e)==operation).findAny().orElse("未知");
    }
}

@FunctionalInterface
interface Operation extends Serializable {
    String operate(Player user, ArrayList<Player> targets);
}
