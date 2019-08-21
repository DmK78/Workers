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
    public static List<Speciality> specialityList = new ArrayList<>();
    public static List<Worker> workerList = new ArrayList<>();
    private FragmentManager fm;
    private Fragment selectSpecialityFragment;
    private Fragment selectWorkerFragment;
    private Fragment workerInfoFragment;
    private SQLiteDatabase db;
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
        this.db = new WorkersBaseHelper(this.getApplicationContext()).getWritableDatabase();
        Cursor cursor = this.db.query(
                DbSchema.SpecialitiesTable.NAME,
                null, null, null,
                null, null, null
        );
        if (!cursor.moveToFirst()) { //проверяем, пустая ли таблица.
            specialityList.add(new Speciality("Medic"));// если таблица пустая, то заполняем ее
            specialityList.add(new Speciality("Developer"));
            specialityList.add(new Speciality("Teacher"));
            specialityList.add(new Speciality("Trader"));
            specialityList.add(new Speciality("Scientifist"));
            specialityList.add(new Speciality("Fireman"));
            specialityList.add(new Speciality("Policeman"));
            for (Speciality speciality : specialityList) {//выгружаем в базу данных
                ContentValues value = new ContentValues();
                value.put(DbSchema.SpecialitiesTable.Cols.NAME, speciality.getName());
                long specialityId = db.insert(DbSchema.SpecialitiesTable.NAME, null, value);
                speciality.setId((int) specialityId);
            }
            for (int i = 0; i < 200; i++) {//заполняем ворекров и выгружаем их в базу данных (без загрузки в коллекцию
                int specialityRand = (int) (Math.random() * specialityList.size());
                Random random = new Random();
                int minDay = (int) LocalDate.of(1900, 1, 1).toEpochDay();
                int maxDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
                long randomDay = minDay + random.nextInt(maxDay - minDay);
                LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);
                String firstName = specialityList.get(specialityRand).getName();
                String lastName = specialityList.get(specialityRand).getName() + "er";
                int image = R.drawable.ic_account_box_red_24dp;
                int specialityId= specialityList.get(specialityRand).getId();
                ContentValues value = new ContentValues();
                value.put(DbSchema.WorkersTable.Cols.FIRST_NAME, firstName);
                value.put(DbSchema.WorkersTable.Cols.LAST_NAME, lastName);
                value.put(DbSchema.WorkersTable.Cols.BIRTH_DATE, randomBirthDate.toString());
                value.put(DbSchema.WorkersTable.Cols.PHOTO, image);
                value.put(DbSchema.WorkersTable.Cols.SPECIALITY_ID, specialityId);
                db.insert(DbSchema.WorkersTable.NAME, null, value);
            }
        } else { //иначе загружаем данные из базы данных в коллекцию
            while (!cursor.isAfterLast()) {
                specialityList.add(new Speciality(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(DbSchema.SpecialitiesTable.Cols.NAME))));
                cursor.moveToNext();
            }
        }
        cursor.close();
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
    public void onWorkerClicked(int workerId, int speicalityId) {
        Bundle bundle = new Bundle();
        bundle.putInt(WORKER, workerId);//передаем айди ворекра
        bundle.putInt(SPECIALITY, speicalityId);//передаем айди специальности
        workerInfoFragment = new WorkerInfoActivity();
        workerInfoFragment.setArguments(bundle);
        fm.beginTransaction()
                .replace(R.id.fragment_container, workerInfoFragment)
                .addToBackStack(null)
                .commit();
    }
}

