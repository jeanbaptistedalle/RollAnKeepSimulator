package com.roldaice.fallout.rollandkeepsimulator;

import java.util.StringTokenizer;

/**
 * Created by android on 11/17/15.
 */
public class OldRoll {
    private int roll;
    private int keep;


    public OldRoll() {
        this.roll = 0;
        this.keep = 0;
    }

    public OldRoll(int roll, int keep) {
        this.roll = roll;
        this.keep = keep;
    }

    @Override
    public String toString() {
        return roll + "g" + keep;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OldRoll) {
            final OldRoll other = (OldRoll) o;
            if (this.roll == other.roll && this.keep == other.keep) {
                return true;
            }
        }
        return false;
    }

    public static OldRoll build(final String oldRoll){
        final OldRoll build = new OldRoll();
        final StringTokenizer st = new StringTokenizer(oldRoll,"g");
        if(st.hasMoreElements()){
            final String roll = st.nextToken();
            build.setRoll(Integer.parseInt(roll));
            if(st.hasMoreElements()){
                final String keep = st.nextToken();
                build.setKeep(Integer.parseInt(keep));
                if(!st.hasMoreTokens()){
                    return build;
                }
            }
        }
        throw new RuntimeException("Format non géré : "+oldRoll);
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}