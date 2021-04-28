package com.mp.chesstimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private Spinner spinner_time, spinner_mode;
    private Button btnSubmit,btnAddNewMode;
    private ArrayList<GameMode> allModes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> allModesName, allTimes;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_mode = view.findViewById(R.id.spinner1);
        spinner_time = view.findViewById(R.id.spinner2);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnAddNewMode=view.findViewById(R.id.btnAddNewMode);
        initEverything();
        initTime();
        initModes();
        ArrayAdapter allModeAdapter = new ArrayAdapter(getContext(), R.layout.spinner_header, allModesName);
        allModeAdapter.setDropDownViewResource(R.layout.dropdown);
        spinner_mode.setAdapter(allModeAdapter);

        ArrayAdapter timerModeAdapter = new ArrayAdapter(getContext(), R.layout.spinner_header, allTimes);
        timerModeAdapter.setDropDownViewResource(R.layout.dropdown);
        spinner_time.setAdapter(timerModeAdapter);
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    spinner_time.setEnabled(false);
                } else {
                    spinner_time.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddNewMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(new AddNewModeFragment());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_mode.getSelectedItem().equals("Normal")) {
                    editor.putString("duration", ((String) spinner_time.getSelectedItem()).split(" ")[0]);
                    editor.putString("delay", "0");
                    editor.apply();
                    goToFragment(new TimerFragment());
                } else {
                    editor.putString("duration", allModes.get(spinner_mode.getSelectedItemPosition()).getDuration());
                    Log.d("HOMEFRAGMENT", "GET:" + spinner_mode.getSelectedItemPosition());
                    editor.putString("delay", allModes.get(spinner_mode.getSelectedItemPosition()).getDelay());
                    editor.apply();
                    goToFragment(new TimerFragment());
                }
            }
        });


    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, null);
        fragmentTransaction.commit();
    }

    @SuppressLint("CommitPrefEdits")
    private void initEverything() {
        allModes = new ArrayList<>();
        allModesName = new ArrayList<>();
        allTimes = new ArrayList<>();
        sharedPreferences = this.getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }


    @SuppressLint("DefaultLocale")
    private void initTime() {
        for (int i = 1; i < 100; i++) {
            allTimes.add(String.format("%d min", i));
        }
    }

    private void initModes() {
        allModes.add(new GameMode("Normal", "0", "0"));
        allModesName.add("Normal");

        File[] files = getActivity().getFilesDir().listFiles();

        for(File i: files){
            try{
                GameMode gameMode = (GameMode) (new ObjectInputStream(getActivity().openFileInput(i.getName())).readObject());
                allModes.add(gameMode);
                allModesName.add(String.format("%s %s|%s", gameMode.getName(), gameMode.getDuration(), gameMode.getDelay()));
            }

            catch(Exception e){

            }
        }


        allModes.add(new GameMode("Fischer Blitz", "0", "5"));
        allModes.add(new GameMode("Delay Bullet", "2", "1"));
        allModes.add(new GameMode("Fischer", "5", "5"));

        allModesName.add("Fischer Blitz 5|0");
        allModesName.add("Delay Bullet 1|2");
        allModesName.add("Fischer 5|5");
    }

}