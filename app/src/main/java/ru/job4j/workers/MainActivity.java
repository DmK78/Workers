package ru.job4j.workers;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SelectSpecialityActivity.OnSpecialitySelectClickListener, SelectWorkersActivity.OnWorkerSelectClickListener {
    public static List<Speciality> specialityList = new ArrayList<>();
    public static List<Worker> workerList = new ArrayList<>();
    private FragmentManager fm;
    private Fragment selectSpecialityFragment;
    private Fragment selectWorkerFragment;
    private Fragment workerInfoFragment;
    public static final String SPECIALITY = "speciality";
    public static final String WORKER = "worker";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        fm = getSupportFragmentManager(); // получить FragmentManager
        selectSpecialityFragment = fm.findFragmentById(R.id.fragment_container);
        if (selectSpecialityFragment == null) {
            selectSpecialityFragment = new SelectSpecialityActivity();
            fm.beginTransaction()
                    .add(R.id.fragment_container, selectSpecialityFragment) // добавить фрагмент в контейнер
                    .commit();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        specialityList.add(new Speciality("Medic"));
        specialityList.add(new Speciality("Developer"));
        specialityList.add(new Speciality("Teacher"));
        specialityList.add(new Speciality("Trader"));
        specialityList.add(new Speciality("Scientifist"));
        specialityList.add(new Speciality("Fireman"));
        specialityList.add(new Speciality("Policman"));
        for (int i = 0; i < 200; i++) {
            int specialityRand = (int) (Math.random() * specialityList.size());
            Random random = new Random();
            int minDay = (int) LocalDate.of(1900, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
            System.out.println(randomBirthDate);
            workerList.add(new Worker(specialityList.get(specialityRand).getName(), specialityList
                    .get(specialityRand).getName() + "er", randomBirthDate.toString(), R.drawable.ic_account_box_red_24dp, specialityList.get(specialityRand)));
        }
    }

    @Override
    public void onSpecialityClicked(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(SPECIALITY, i);
        selectWorkerFragment = new SelectWorkersActivity();
        selectWorkerFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, selectWorkerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkerClicked(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(WORKER, i);
        workerInfoFragment = new WorkerInfoActivity();
        workerInfoFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, workerInfoFragment)
                .addToBackStack(null)
                .commit();
    }
}

