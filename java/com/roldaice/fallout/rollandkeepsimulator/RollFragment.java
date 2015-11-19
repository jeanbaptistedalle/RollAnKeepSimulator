package com.roldaice.fallout.rollandkeepsimulator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by android on 11/16/15.
 */
public class RollFragment extends Fragment {

    private Activity callBack;
    private ArrayAdapter<Integer> keepAdapter;
    private ArrayAdapter<Integer> rollAdapter;
    private OldRollAdapter oldRollAdapter;

    private static final String NB_DICE_ROLL_KEY = "nbRoll";
    private static final String NB_DICE_KEEP_KEY = "nbKeep";
    private static final String RESULT_KEY = "result";
    private static final String ROLL_DICE_KEY = "rollDice";
    private static final String KEEP_DICE_KEY = "keepDice";
    private static final String OLD_ROLLS_KEY = "oldRolls";

    @Override
    public void onAttach(Activity activity) {
        this.callBack = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.roll_fragment, container, false);
        final List<Integer> availableNbRoll= new ArrayList<Integer>();
        for (Integer i = 1; i <= 10; i++) {
            availableNbRoll.add(i);
        }
        final List<Integer> availableNbKeep = new ArrayList<Integer>();
        if(savedInstanceState != null){
            int nbRoll = savedInstanceState.getInt(NB_DICE_ROLL_KEY);
            for (Integer i = 1; i <= nbRoll; i++) {
                availableNbKeep.add(i);
            }
        }else {
            availableNbKeep.add(1);
        }
        final Spinner spinnerRoll = (Spinner) view.findViewById(R.id.nb_dice_roll);
        rollAdapter = new ArrayAdapter<Integer>(callBack, android.R.layout.simple_spinner_item, availableNbRoll);
        rollAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerRoll.setAdapter(rollAdapter);
        final Spinner spinnerKeep = (Spinner) view.findViewById(R.id.nb_dice_keep);
        keepAdapter = new ArrayAdapter<Integer>(callBack, android.R.layout.simple_spinner_item, availableNbKeep);
        keepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerKeep.setAdapter(keepAdapter);
        if(savedInstanceState != null){
            spinnerRoll.setSelection(savedInstanceState.getInt(NB_DICE_ROLL_KEY)-1);
            spinnerKeep.setSelection(savedInstanceState.getInt(NB_DICE_KEEP_KEY)-1);
        }else {
            spinnerRoll.setSelection(0);
            spinnerKeep.setSelection(0);
        }

        final ListView oldRolls = (ListView) view.findViewById(R.id.old_rolls);
        final List<OldRoll> oldRollsList = new ArrayList<OldRoll>();
        if (savedInstanceState != null) {
            final ArrayList<String> oldRollsStringList = savedInstanceState.getStringArrayList(OLD_ROLLS_KEY);
            if(oldRollsStringList != null) {
                for (final String oldRoll : oldRollsStringList) {
                    oldRollsList.add(OldRoll.build(oldRoll));
                }
            }
        }

        oldRollAdapter = new OldRollAdapter(callBack, R.layout.row, oldRollsList);
        oldRolls.setAdapter(oldRollAdapter);
        if(savedInstanceState != null) {
            if (savedInstanceState.getString(ROLL_DICE_KEY) != null) {
                final TextView dicesText = (TextView) view.findViewById(R.id.dices);
                dicesText.setText(savedInstanceState.getString(ROLL_DICE_KEY));
            }
            if (savedInstanceState.getString(KEEP_DICE_KEY) != null) {
                final TextView dicesKeepText = (TextView) view.findViewById(R.id.dices_keep);
                dicesKeepText.setText(savedInstanceState.getString(KEEP_DICE_KEY));
            }
            if (savedInstanceState.getString(RESULT_KEY) != null) {
                final TextView resultText = (TextView) view.findViewById(R.id.resultat);
                resultText.setText(savedInstanceState.getString(RESULT_KEY));
            }
        }

        final Button rollButton = (Button) view.findViewById(R.id.roll_button);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareRoll(getView());
            }
        });

        spinnerRoll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, int i, long l) {
                int roll = (int) spinnerRoll.getSelectedItem();
                final Spinner spinnerKeep = (Spinner) getView().findViewById(R.id.nb_dice_keep);
                if (spinnerKeep != null) {
                    int oldValue = (int) spinnerKeep.getSelectedItem();
                    int index = 0;
                    final List<Integer> availableNb = new ArrayList<Integer>();
                    for (Integer j = 1; j <= roll; j++) {
                        availableNb.add(j);
                        if (j == oldValue) {
                            index = j - 1;
                        }
                    }
                    final ArrayAdapter<Integer> keepAdapter = new ArrayAdapter<Integer>(callBack, android.R.layout.simple_spinner_item, availableNb);
                    keepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spinnerKeep.setAdapter(keepAdapter);
                    spinnerKeep.setSelection(index);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        final Spinner spinnerRoll = (Spinner) getView().findViewById(R.id.nb_dice_roll);
        outState.putInt(NB_DICE_ROLL_KEY, (int) spinnerRoll.getSelectedItem());

        final Spinner spinnerKeep = (Spinner) getView().findViewById(R.id.nb_dice_keep);
        outState.putInt(NB_DICE_KEEP_KEY, (int)spinnerKeep.getSelectedItem());

        final TextView dicesText = (TextView) getView().findViewById(R.id.dices);
        if (dicesText.getText().toString() != null) {
            outState.putString(ROLL_DICE_KEY, dicesText.getText().toString());
        }else{
            outState.putString(ROLL_DICE_KEY, "");
        }

        final TextView dicesKeepText = (TextView) getView().findViewById(R.id.dices_keep);
        if (dicesKeepText.getText().toString() != null) {
            outState.putString(KEEP_DICE_KEY, dicesKeepText.getText().toString());
        }else{
            outState.putString(KEEP_DICE_KEY, "");
        }

        final TextView resultat = (TextView) getView().findViewById(R.id.resultat);
        if (resultat.getText().toString() != null) {
            outState.putString(RESULT_KEY, resultat.getText().toString());
        }else{
            outState.putString(RESULT_KEY, "");
        }

        if (oldRollAdapter.getOldRolls() != null && oldRollAdapter.getOldRolls().size() > 0) {
            final ArrayList<String> oldRollsString = new ArrayList<String>();
            for (final OldRoll oldRoll : oldRollAdapter.getOldRolls()) {
                oldRollsString.add(oldRoll.toString());
            }
            outState.putStringArrayList(OLD_ROLLS_KEY, oldRollsString);
        }
    }

    public static void prepareRoll(final View view) {
        final Spinner spinnerRoll = (Spinner) view.findViewById(R.id.nb_dice_roll);
        final Spinner spinnerKeep = (Spinner) view.findViewById(R.id.nb_dice_keep);
        int roll = (int) spinnerRoll.getSelectedItem();
        int keep = (int) spinnerKeep.getSelectedItem();
        roll(view, roll, keep);
    }

    public static void roll(final View view, int roll, int keep) {
        final List<Integer> dices = new ArrayList<Integer>(roll);
        if (keep > roll) {
            keep = roll;
        }
        int result = 0;
        final Random rand = new Random();
        for (int i = 0; i < roll; i++) {
            int total = 0;
            int dice = 0;
            do {
                dice = rand.nextInt(10) + 1;
                total += dice;
            } while (dice == 10);
            dices.add(total);
        }
        Collections.sort(dices, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1 - integer;
            }
        });
        for (int i = 0; i < keep; i++) {
            result += dices.get(i);
        }
        final TextView resultat = (TextView) view.findViewById(R.id.resultat);
        resultat.setText(result + "");
        final TextView dicesText = (TextView) view.findViewById(R.id.dices);
        final StringBuilder builder = new StringBuilder();
        for (int aValue : dices) {
            builder.append(aValue).append(", ");
        }

        if (dices.size() > 0) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        dicesText.setText(builder.toString());
        final TextView dicesKeepText = (TextView) view.findViewById(R.id.dices_keep);
        final StringBuilder builder2 = new StringBuilder();
        for (int i = 0; i < keep; i++) {
            builder2.append(dices.get(i)).append(", ");
        }
        if (dices.size() > 0) {
            builder2.deleteCharAt(builder2.length() - 1);
            builder2.deleteCharAt(builder2.length() - 1);
        }
        dicesKeepText.setText(builder2.toString());

        final ListView oldRolls = (ListView) view.findViewById(R.id.old_rolls);
        OldRollAdapter oldRollAdapter = (OldRollAdapter) oldRolls.getAdapter();
        oldRollAdapter.add(new OldRoll(roll, keep));
        oldRollAdapter.notifyDataSetChanged();
    }
}
