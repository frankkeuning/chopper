package scripts.wcer;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Login;
import org.tribot.api2007.Skills;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import scripts.AntiBan;
import scripts.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by frank on 26-4-2018.
 */
public class progressiveWcer extends Script implements Painting, MessageListening07 {

    public ArrayList<Node> nodes = new ArrayList<>();
    long startTime;

    @Override
    public void run() {
        doGui();
        init();
        startTime = System.currentTimeMillis();
        General.useAntiBanCompliance(true);
        ThreadSettings.get().setClickingAPIUseDynamic(true);
        while(true){
            if(Login.getLoginState().equals(Login.STATE.INGAME)){
                for(final Node n : nodes){
                    if(n.isValid())
                        n.execute();
                    else
                        AntiBan.timedActions();
                    General.sleep(20, 35);
                }
            } else {
                if(Login.getLoginState().equals(Login.STATE.LOGINSCREEN)){
                    General.println(Login.getLoginResponse());
                }
            }
            sleep(20,40);
        }
    }

    public void init(){
        if(Vars.BANK){
            nodes.add(new scripts.wcer.Bank());
        } else {
            nodes.add(new scripts.wcer.Drop());
        }
        nodes.add(new Chop());
        nodes.add(new scripts.wcer.Walk());
    }

    public final Gui gui = new Gui();
    public void doGui(){
        General.println("running ui");
        try{
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        gui.setVisible(true);
                    } catch (Exception e) {
                        General.println("error1");
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){
            General.println("error2");
            e.printStackTrace();
        }
//        if(gui.active)
//            gui.loadSettings();
        while(gui.active){
            sleep(100);
        }





        General.println("done");
    }

    @Override
    public void playerMessageReceived(String s, String s1) {

    }

    @Override
    public void serverMessageReceived(String s) {
        if(s.contains("get some"))
            Vars.LOG_COUNT++;
        General.println(s);
    }

    @Override
    public void tradeRequestReceived(String s) {

    }

    @Override
    public void duelRequestReceived(String s, String s1) {

    }

    @Override
    public void personalMessageReceived(String s, String s1) {

    }

    @Override
    public void clanMessageReceived(String s, String s1) {

    }

    final int startXP = Skills.getXP(Skills.SKILLS.WOODCUTTING);
    @Override
    public void onPaint(Graphics g) {
        long runTime = System.currentTimeMillis() - startTime;

        int currentXP = Skills.getXP(Skills.SKILLS.WOODCUTTING);
        int gainedXp = currentXP - startXP;
        long xpPerHr = (gainedXp) * (3600000 / runTime);

        g.drawString("Runtime: "+ Timing.msToString(runTime),300,360);
        g.drawString("Logs: "+ Vars.LOG_COUNT+ " ("+(Vars.LOG_COUNT * (3600000 / runTime))+")",300,375);
        g.drawString("XP Gained: "+ gainedXp +" ("+xpPerHr+")",300,390);
    }
}



