package ru.job4j.workers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorkersCore {

    private static WorkersCore instance;
    private List<Speciality> specialityList = new ArrayList<>();
    private Worker selectedWorker;
    private SQLiteDatabase db;

    private WorkersCore() {
    }

    public static WorkersCore getInstance() {
        if (instance == null) {
            instance = new WorkersCore();
        }
        return instance;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init(Context context) {
        this.db = new WorkersBaseHelper(context).getWritableDatabase();
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
                int specialityId = specialityList.get(specialityRand).getId();
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

    public List<Speciality> getAllSpecialities() {
        return specialityList;
    }

    public List<Worker> findWorkersFromSpeciality(Context context, int selectedID) {
        List<Worker> result = new ArrayList<>();
        this.db = new WorkersBaseHelper(context.getApplicationContext()).getReadableDatabase();
        String selection = DbSchema.WorkersTable.Cols.SPECIALITY_ID + "=?";
        String[] selectionArgs = {"" + selectedID};
        Cursor cursor = this.db.query(
                DbSchema.WorkersTable.NAME,//делаем выборку из таблицы воркерс, чтобы specialityId было равно выбранной специальности
                null, selection, selectionArgs,
                null, null, null
        );
        Speciality selectedSpeciality = null; //находим в коллекции выбранную специальность, чтобы подставить ее в воркера
        for (Speciality speciality : specialityList) {
            if (speciality.getId() == selectedID) {
                selectedSpeciality = speciality;
                break;
            }
        }
        while (cursor.moveToNext()) {
            result.add(new Worker(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.BIRTH_DATE)),
                    cursor.getInt(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.PHOTO)),
                    selectedSpeciality
            ));
        }
        cursor.close();
        return result;
    }

    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }
}
