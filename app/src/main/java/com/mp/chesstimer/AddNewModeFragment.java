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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


public class AddNewModeFragment extends Fragment {
    private Spinner spinner_duration, spinner_delay;
    private EditText editName;
    private TextView save;
    private ArrayList<String> allDuration, allDelay;

    public AddNewModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_duration = view.findViewById(R.id.duration);
        spinner_delay = view.findViewById(R.id.delay);
        save = view.findViewById(R.id.save);
        editName = view.findViewById(R.id.name);
        allDuration = new ArrayList<>();
        allDelay = new ArrayList<>();
        initDelay();
        initDuration();
        ArrayAdapter durationAdapter = new ArrayAdapter(getContext(), R.layout.spinner_header, allDuration);
        durationAdapter.setDropDownViewResource(R.layout.dropdown);
        spinner_duration.setAdapter(durationAdapter);

        ArrayAdapter delayAdapter = new ArrayAdapter(getContext(), R.layout.spinner_header, allDelay);
        delayAdapter.setDropDownViewResource(R.layout.dropdown);
        spinner_delay.setAdapter(delayAdapter);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GameMode gameMode = new GameMode(editName.getText().toString().trim(), ((String) spinner_delay.getSelectedItem()).split(" ")[0], ((String) spinner_duration.getSelectedItem()).split(" ")[0]);
                Random random = new Random();
                int code = random.nextInt(9000);
                try{
                    FileOutputStream fos = getActivity().openFileOutput(Integer.toString(code), Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(gameMode);
                    oos.close();
                    fos.close();
                    Toast.makeText(getActivity().getApplicationContext(), "Saved successfully", Toast.LENGTH_LONG).show();
                }
                catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "Failed, try again later", Toast.LENGTH_LONG).show();
                }
                goToFragment(new HomeFragment());
            }
        });


    }
    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, null);
        fragmentTransaction.commit();
    }


    @SuppressLint("DefaultLocale")
    private void initDuration() {
        for (int i = 1; i < 100; i++) {
            allDuration.add(String.format("%d min", i));
        }
    }

    @SuppressLint("DefaultLocale")
    private void initDelay() {
        for (int i = 1; i < 31; i++) {
            allDelay.add(String.format("%d sec", i));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}