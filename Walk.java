package scripts.wcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSTile;
import scripts.Node;
import scripts.Util2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 26-4-2018.
 */
public class Walk extends Node {

    List<Integer> abc2WaitTimes = new ArrayList<>();

    @Override
    public boolean isValid() {

        if(Inventory.isFull() && !Util2.atBank())
            return true;
        else if(Inventory.isFull() && Util2.atBank())
            return false;
        else if(!Util2.playerNearTile(Vars.TREE_TILE,20) )
            return true;

        return false;
    }

    @Override
    public void execute() {
        abc2WaitTimes.add(General.random(1200, 2800));
        if(Util2.playerNearTile(Vars.TREE_TILE,20) && Inventory.isFull()){
            walkToBank();
        } else {
            walkToDestination(Vars.TREE_TILE);
        }
    }

    void walkToBank(){
        abc2WaitTimes = Util2.sleep(abc2WaitTimes);
        if(WebWalking.walkToBank()){
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100, 200);
                    return Util2.atBank();
                }
            }, General.random(1500,3000));
        }
    }

    void walkToDestination(RSTile destination){
        if(WebWalking.walkTo(destination)){
            abc2WaitTimes = Util2.sleep(abc2WaitTimes);
            Timing.waitCondition(new Condition() {
                @Override
                public boolean active() {
                    General.sleep(100, 200);
                    return Player.getPosition().distanceTo(destination) < 20;
                }
            }, General.random(1500,3000));
        }
    }
}

