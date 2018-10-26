package scripts.wcer;

import org.tribot.api2007.Inventory;
import scripts.Node;

/**
 * Created by frank on 26-10-2018.
 */
public class Drop extends Node {
    @Override
    public boolean isValid() {
        return Inventory.isFull();
    }

    @Override
    public void execute() {
        Inventory.dropAllExcept(Vars.AXES);
    }
}
