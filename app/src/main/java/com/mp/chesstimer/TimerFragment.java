package com.mp.chesstimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class TimerFragment extends Fragment {
    private SharedPreferences pre;
    private long duration, delay;
    private TextView movesWhite, movesBlack, durationWhite, durationBlack;
    private ImageView btnPause, btnRedo, btnSettings;
    private FragmentTransaction fragmentTransaction;
    private CountDownTimer countdownTimerWhite, countdownTimerBlack;
    private Handler handler;
    private LinearLayout linearLayout;
    private CardView cardViewWhite, cardViewBlack;
    private long duration1;
    private long duration2;
    private int numberOfMovesWhite;
    private int numberOfMovesBlack;
    private boolean timerRunningBlack;
    private boolean timerRunningWhite;
    private boolean start = false;
    private boolean pause;
    int active_color, inactive_color, background_color, default_color;

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDetails();
        initView(view);
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFragment(new TimerFragment());
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pause = true;
                if (countdownTimerWhite != null) {
                    countdownTimerWhite.cancel();
                } else if (countdownTimerBlack != null) {
                    countdownTimerBlack.cancel();
                }
                cardViewBlack.setCardBackgroundColor(Color.parseColor("#E65C5A5A"));
                cardViewWhite.setCardBackgroundColor(Color.parseColor("#E65C5A5A"));
                btnSettings.setVisibility(View.VISIBLE);
                btnRedo.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.GONE);
            }
        });

        cardViewBlack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (start == false) {
                    cardViewWhite.setCardBackgroundColor(Color.RED);
                    cardViewBlack.setCardBackgroundColor(Color.parseColor("#E65C5A5A"));
                    cardViewBlack.setEnabled(false);
                    cardViewWhite.setEnabled(true);

                    reverseTimer1(cardViewWhite);
                    start = true;
                    setEnabled();
                } else {
                    if (pause == true) {
                        pause = false;
                        btnPause.setVisibility(View.VISIBLE);
                    }
                    cardViewWhite.setCardBackgroundColor(Color.RED);
                    cardViewBlack.setCardBackgroundColor(Color.parseColor("#E65C5A5A"));
                    cardViewBlack.setEnabled(false);
                    cardViewWhite.setEnabled(true);

                    reverseTimer1(cardViewWhite);
                    pauseTimer(countdownTimerBlack, timerRunningBlack);
                    numberOfMovesBlack++;
                    movesBlack.setText(Integer.toString(numberOfMovesBlack));
                }
            }
        });
        cardViewWhite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (pause == true) {
                    pause = false;
                    btnPause.setVisibility(View.VISIBLE);
                }
                cardViewBlack.setCardBackgroundColor(Color.GREEN);
                cardViewWhite.setCardBackgroundColor(Color.parseColor("#E65C5A5A"));
                cardViewWhite.setEnabled(false);
                cardViewBlack.setEnabled(true);

                reverseTimer(cardViewBlack);
                pauseTimer(countdownTimerWhite, timerRunningWhite);
                numberOfMovesWhite++;
                movesWhite.setText(Integer.toString(numberOfMovesWhite));
            }
        });

        btnRedo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToFragment(new TimerFragment());

            }
        });

    }

    private void setEnabled() {
        btnSettings.setVisibility(View.VISIBLE);
        btnRedo.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.VISIBLE);
    }

    public void reverseTimer1(CardView cardView) {
        cardView.setEnabled(true);
        duration1 += delay;
        countdownTimerWhite = new CountDownTimer(duration1, 1000) {

            public void onTick(long duration) {
                duration1 = duration;
                int seconds = (int) (duration1 / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                durationWhite.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                durationWhite.setText("Time Up!!!");
            }
        }.start();
        timerRunningWhite = true;
    }

    public void reverseTimer(final CardView cardView) {
        cardView.setEnabled(true);
        duration2 += delay;
        countdownTimerBlack = new CountDownTimer(duration2, 1000) {

            public void onTick(long duration) {
                duration2 = duration;
                int seconds = (int) (duration2 / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                durationBlack.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                durationBlack.setText("Time Up!!!");
            }
        }.start();
        timerRunningBlack = true;
    }

    private void pauseTimer(CountDownTimer timer, boolean mTimerRunning) {
        timer.cancel();
        mTimerRunning = false;
    }

    private void goToFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, null);
        fragmentTransaction.commit();
    }

    private void initView(View view) {
        movesWhite = view.findViewById(R.id.moves_white);
        movesBlack = view.findViewById(R.id.moves_black);
        durationWhite = view.findViewById(R.id.duration_white);
        durationBlack = view.findViewById(R.id.duration_black);
        btnPause = view.findViewById(R.id.pause);
        btnRedo = view.findViewById(R.id.redo);
        linearLayout = view.findViewById(R.id.relLayout);
        cardViewWhite = view.findViewById(R.id.cardViewWhite);
        cardViewBlack = view.findViewById(R.id.cardViewBlack);
        cardViewWhite.setEnabled(false);
        numberOfMovesWhite = 0;
        numberOfMovesBlack = 0;
        movesWhite.setText(Integer.toString(numberOfMovesWhite));
        movesBlack.setText(Integer.toString(numberOfMovesBlack));
        durationWhite.setText(convertLongToString(duration1 / 1000));
        durationBlack.setText(convertLongToString(duration2 / 1000));
        pause = false;
    }

    private void getDetails() {
        pre = this.getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        duration = Long.parseLong(pre.getString("duration", "")) * 60 * 1000;
        duration1 = duration;
        duration2 = duration;
        Log.d("TimerFragment", "Duration" + duration1);
        delay = Long.parseLong(pre.getString("delay", "")) * 1000;
        Log.d("TimerFragment", "Duration" + delay);
    }

    public static long convertStringToLong(String time) {

        char[] all = time.toCharArray();

        long number = ((Long.parseLong(Character.toString(all[0])) * 10) + (Long.parseLong(Character.toString(all[1])))) * 60;
        number += ((Long.parseLong(Character.toString(all[3])) * 10) + (Long.parseLong(Character.toString(all[4]))));

        return number;
    }


    public String convertLongToString(long number) {

        String timeString = null;

        if (number >= 600) {
            timeString = Long.toString(number / 60);
        } else if (number < 60) {
            timeString = "00";
        } else if ((number >= 60) && (number < 600)) {
            timeString = "0".concat(Long.toString(number / 60));
        }

        String secondHalfOfTime;

        if ((number % 60) < 10) {
            secondHalfOfTime = "0".concat(Long.toString(number % 60));
        } else {
            secondHalfOfTime = Long.toString(number % 60);
        }

        timeString += (String.format(":%s", secondHalfOfTime));

        return timeString;
    }

    @Override
    public void onPause() {

//        if (countdownTimer != null) {
//            cardViewWhite.setCardBackgroundColor(Integer.parseInt("#E65C5A5A"));
//            cardViewBlack.setCardBackgroundColor(Integer.parseInt("#E65C5A5A"));
//            countdownTimer.cancel();
//        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}