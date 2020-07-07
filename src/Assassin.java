import java.util.ArrayList;

/**
 * @Author: Yizhen Yuan
 * @Date: 2020.05.23
 * @Description:
 */
public class Assassin extends Player {
    private static final long serialVersionUID = Main.serialVersionUID;
    private static final int START_HEALTH =1;
    private static final int START_ENERGY =0;
    public static final String DEFAULT_NAME ="刺客";
    private static final String LOSERS_NAME ="被秒杀的";//killing the player with Shunsha might change their name
    private static int count=0;
    private int countForBeiCi;
    static {
        skillMap.put("背刺",(i,e)->{
            /*
                Beici;
                Can be used only once, draw no energy(free).
                deal 2 damage if enemy is not defend
             */
            assert i instanceof Assassin&&e.size()==1;
            ((Assassin) i).countForBeiCi--;
            Player target=e.get(0);
            if(!target.defense)target.healthDecrease(2);
            return i.name+"对"+target.name+"使用了背刺";
        });
        skillMap.put("瞬杀",(i,e)->{
            /*
                Shunsha;
                1% kill the enemy regardless of anything, and change their name
                9% deal 10 damage if enemy not defend, 6 if enemy does
                90% deal 4/1 damage if target is also Assasin it deal 2 damage even with defend(which finally become 1 damage
                due to the Assasin`s damage reduce)
             */
            assert e.size()==1&&i instanceof Assassin;
            int rand= GameRandom.random(100);
            Player target=e.get(0);
            i.energy--;
            String answer=i.name+"对"+target.name+"使用了瞬杀";
            if(rand==0){
                target.healthDecrease(Short.MAX_VALUE);
                target.name= LOSERS_NAME +target.name;
                return answer+"，并触发了秒杀";
            }else if (rand<10){
                target.healthDecrease(target.defense?6:10);
                return answer+"，并触发了暴击";
            }else if (rand<90){
                target.healthDecrease(target.defense?target instanceof Assassin?2:1:4);
                return answer;
            }else {
                return answer+"，miss了";
            }
        });
    }

    Assassin(){
        this(DEFAULT_NAME +(++count),null);
    }
    Assassin(Bot bot){
        this(DEFAULT_NAME+(++count),bot);
    }
    Assassin(String name){
        this(name,null);
    }
    Assassin(String name,Bot bot){
        super(name, START_HEALTH, START_ENERGY,bot);

        countForBeiCi=1;
    }


    @Override
    void healthDecrease(int amount) {
        //Assasin take one less damage
        super.healthDecrease(amount-1);
    }
    @Override
    ArrayList<Operation> availableOperation() {
        ArrayList<Operation> operations=new ArrayList<>();
        operations.add(skillMap.get("聚气"));
        operations.add(Player.defend);
        //those two operation is always available
        if(this.countForBeiCi>0)operations.add(skillMap.get("背刺"));
        if(this.energy>=1)operations.add(skillMap.get("瞬杀"));

        return operations;
    }

    @Override
    String hash() {
        return super.hash()+countForBeiCi+"背刺";
    }
}
