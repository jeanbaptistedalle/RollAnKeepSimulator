package com.roldaice.fallout.rollandkeepsimulator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 11/17/15.
 */
public class OldRollAdapter extends ArrayAdapter<OldRoll> {

    private LayoutInflater inflater;

    private List<OldRoll> oldRolls;

    public OldRollAdapter(final Activity activity, int resource, final List<OldRoll> objects) {
        super(activity, resource, objects);
        if(objects != null && objects.size() > 0){
            oldRolls = new ArrayList<OldRoll>(objects);
        }else {
            oldRolls = new ArrayList<OldRoll>();
        }
        inflater = activity.getLayoutInflater();
    }

    @Override
    public void add(final OldRoll object) {
        if(!oldRolls.contains(object)) {
            oldRolls.add(object);
            super.add(object);
        }
    }

    @Override
    public View getView(int position, final View convertView, final ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = inflater.inflate(R.layout.row, parent, false);
        } else {
            view = convertView;
        }

        final OldRoll oldRoll = getItem(position);

        final Button button = (Button) view.findViewById(R.id.old_roll_button);
        button.setText(oldRoll.toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RollFragment.roll(v.getRootView(), oldRoll.getRoll(), oldRoll.getKeep());
            }
        });

        return view;
    }

    public List<OldRoll> getOldRolls() {
        return oldRolls;
    }

    public void setOldRolls(List<OldRoll> oldRolls) {
        this.oldRolls = oldRolls;
    }
}
