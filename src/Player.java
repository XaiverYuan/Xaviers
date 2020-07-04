import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description: the parent class of all kinds of Players(职业)
 */
public abstract class Player implements Serializable{
    private static final int serialVersionUID = Main.serialVersionUID;
    boolean alive;
    Game game;//the game this player is in
    Bot bot;//if this is null then it is played by human, or it is played by ai
    int teamId;//the id of the team
    int energy;//use to cast abilities
    private int health;//died when it is <=0
    /* the reason health is private here is Some kind of player has deduction on damage taken.
        i.e. Assasin take one less damage.
     */
    Operation currentOperation;//the operation this round
    ArrayList<Player> targets;//the target this round
    static final Operation defend = (i, e) -> {
        //everyone should be able to defend
        i.defense = true;
        return i.name + "防御了";
    };
    static final Operation gain = (i, e) -> {
        //gaining energy
        i.energy++;
        return i.name + "使用了聚气";
    };
    static HashMap<String, Operation> skillMap;//contains all skills that kind of player has
    static {
        skillMap=new HashMap<>();
        skillMap.put("聚气",gain);
        skillMap.put("防御",defend);
    }
    boolean defense;//if someone is defending, this would be true, this will be set to false after a round complete
    String name;


    void getOperation() {
        //get operation for this round either from AI or interface

        ArrayList<Player> enemy=new ArrayList<>();
        //TODO: this is hard code, should be changed
        enemy.add(this.game.enemyPlayer(this).get(0));
        targets=enemy;
        if (bot!=null){
            //get from Bot
            this.currentOperation=bot.chooseOperations();
        }else {
            //get from Main interface.
            this.currentOperation=Main.askOperation(this);
        }
    }
    Player(String name, int startHealth, int startEnergy) {
        //create a human player
        this(name,startHealth,startEnergy,null);
    }
    Player(String name, int startHealth, int startEnergy,Bot bot) {
        //create an AI player
        this.alive = true;
        this.name = name;
        this.health = startHealth;
        this.energy = startEnergy;
        this.defense = false;
        this.bot=bot;
        if(bot!=null)bot.player=this;
    }

    abstract ArrayList<Operation> availableOperation();
    //return all operation that is available now

    Operation randomOperation() {
        //return a random operation that is available
        ArrayList<Operation> operations = availableOperation();
        int rand = GameRandom.random(operations.size());
        return operations.get(rand);

    }
    abstract String hash();
    //return a String that describe the state of player

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
        return alive;
    }


    @Override
    public String toString() {
        return "\tTeam"+teamId+" "+name + ":" + health + "血"+energy + "能量" +operationToString(currentOperation);
    }
    static String operationToString(Operation operation){
        if(operation==gain)return "聚气";
        if(operation==defend)return "防御";
        if(operation==null)return "空";
        if(Assassin.skillMap.containsValue(operation)){
            for (String s : Assassin.skillMap.keySet()) {
                if(Assassin.skillMap.get(s)==operation)return s;
            }
        }
        if(Druid.skillMap.containsValue(operation)){
            for (String s : Druid.skillMap.keySet()) {
                if(Druid.skillMap.get(s)==operation)return s;
            }
        }
        return "未知";
    }
}
@FunctionalInterface
interface Operation extends Serializable{
    String operate(Player user, ArrayList<Player> targets);
}
