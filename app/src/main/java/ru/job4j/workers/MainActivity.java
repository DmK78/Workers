package ru.job4j.workers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SelectSpecialityActivity.OnSpecialitySelectClickListener, SelectWorkersActivity.OnWorkerSelectClickListener {
    private WorkersCore workersCore = WorkersCore.getInstance();
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

        workersCore.init(getApplicationContext());
        fm = getSupportFragmentManager(); // получить FragmentManager
        selectSpecialityFragment = fm.findFragmentById(R.id.fragment_container);
        if (selectSpecialityFragment == null) {
            selectSpecialityFragment = new SelectSpecialityActivity();
            fm.beginTransaction()
                    .add(R.id.fragment_container, selectSpecialityFragment) // добавить фрагмент в контейнер
                    .commit();
        }
    }

    @Override
    public void onSpecialityClicked(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt(SPECIALITY, i);//передаем айди специальности
        selectWorkerFragment = new SelectWorkersActivity();
        selectWorkerFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, selectWorkerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWorkerClicked() {
        workerInfoFragment = new WorkerInfoActivity();
        fm.beginTransaction()
                .replace(R.id.fragment_container, workerInfoFragment)
                .addToBackStack(null)
                .commit();
    }
}

