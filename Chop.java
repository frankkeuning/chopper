package scripts.wcer;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import scripts.AntiBan;
import scripts.Node;
import scripts.Util2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 26-4-2018.
 */
public class Chop extends Node {
    List<Integer> waitTimes = new ArrayList<>();


    String TREE = Vars.TREE;

    @Override
    public boolean isValid() {
        return !Inventory.isFull()
                && Player.getPosition().distanceTo(Vars.TREE_TILE) < 20
                && Player.getAnimation() == -1;

    }

//    @Override
//    public boolean isValid() {
//        return true;
//
//    }

    @Override
    public void execute() {
        waitTimes = Util2.sleep(waitTimes);
        RSObject[] trees = Objects.findNearest(20,TREE);

        if(trees.length > 0){
            General.println("clicked");
            RSObject tree = AntiBan.selectNextTarget(trees);
            if(!tree.isOnScreen() && !tree.isClickable()){
                if(Util2.playerNearTile(tree.getPosition(),5))
                    Camera.turnToTile(tree.getPosition());
                else
                    walkToSpot(tree.getPosition());

            }


            if(Player.getAnimation() == -1){
                if(AntiBan.activateRun()){
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.random(100, 200);
                            return Game.isRunOn();
                        }
                    }, General.random(1000, 2000));
                }
                if(DynamicClicking.clickRSObject(tree,"Chop")){

                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.sleep(300, 600);
                            return Player.getAnimation() != -1 && !Player.isMoving();
                        }
                    }, General.random(2000, 3500));
                }
                long startTime = System.currentTimeMillis();

                doWhileWorking(trees, tree);

                if(Player.getAnimation() == -1){
                    AntiBan.generateTrackers((int) (System.currentTimeMillis() - startTime), false);
                    waitTimes.add(AntiBan.getReactionTime());
                }
            }
        }
    }

    void walkToSpot(RSTile tile){
        Walking.walkTo(tile);
        Timing.waitCondition(new Condition() {
            @Override
            public boolean active() {
                General.sleep(200,400);
                return Player.isMoving();
            }
        }, General.random(3500,5500));
    }

    void doWhileWorking(RSObject[] objects, RSObject object){
        while(Player.getAnimation() != -1){
            General.sleep(20,40);
            AntiBan.timedActions();
            if(AntiBan.getShouldHover()){
                AntiBan.hoverEntity(objects);
                AntiBan.resetShouldHover();
            }
            if(AntiBan.getShouldOpenMenu()){
                if(DynamicClicking.clickRSObject(object, 3)){
                    Timing.waitCondition(new Condition() {
                        @Override
                        public boolean active() {
                            General.sleep(100,200);
                            return ChooseOption.isOpen();
                        }
                    }, General.random(2500,3700));
                }
                AntiBan.resetShouldOpenMenu();
            }
        }
    }




}
