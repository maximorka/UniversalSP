package comUniversal.BitLevel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class InfAdd {
    private List<RadiogramPercent> radiogramPercent = new ArrayList<>();
    public void addRadiogramPercentListener(RadiogramPercent listener){radiogramPercent.add(listener);}
    public void clearRadiogramPercentListener(){radiogramPercent.clear();}
    private void toRadiogramPercentListener(int percent) {
        if (!radiogramPercent.isEmpty())
            for (RadiogramPercent listener : radiogramPercent)
                listener.percent(percent);
    }
    private Queue<Integer> bits = new ArrayDeque<Integer>();

    private int totalSize = 0;

    public void add(String string) {

    }
    private int oldPercent = 0;

    private void updatePercent(){
        if (totalSize != 0) {
            int currentPercent = (100 * bits.size()) / totalSize;
            int different = oldPercent - currentPercent;
            if (Math.abs(different) >= 1) {
                oldPercent = currentPercent;
                //System.out.println(currentPercent + " %");
                toRadiogramPercentListener(currentPercent);
            }
        }
    }

    public int getBit(){
        if(bits.size() == 0 || bits == null) {
            return -1;
        } else {
            updatePercent();
            return bits.poll();
        }
    }
}
