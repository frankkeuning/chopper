package scripts.wcer;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import scripts.Node;
import scripts.Util2;

/**
 * Created by frank on 26-4-2018.
 */
public class Bank extends Node {
    @Override
    public boolean isValid() {
        return (Util2.atBank() && Inventory.isFull()) || Banking.isBankScreenOpen();
    }

    @Override
    public void execute() {
        if(!Banking.isBankScreenOpen())
            Banking.openBank();
        else
        {
            Banking.depositAllExcept(Vars.AXES);
            General.sleep(500, 1250);
        }
    }
}