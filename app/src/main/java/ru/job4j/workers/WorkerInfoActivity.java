package ru.job4j.workers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkerInfoActivity extends Fragment {
    private TextView textViewFirstName, textViewLastName, textViewBirthDate, textViewSpeciality;
    private ImageView imageViewPhoto;
    private Worker worker;
    SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_worker_info, container, false);
        this.db = new WorkersBaseHelper(this.getContext()).getReadableDatabase();
        textViewFirstName = view.findViewById(R.id.textViewInfoFirstName);
        textViewLastName = view.findViewById(R.id.textViewInfoLastName);
        textViewBirthDate = view.findViewById(R.id.textViewInfoBirthDate);
        textViewSpeciality = view.findViewById(R.id.textViewInfoSpeciality);
        imageViewPhoto = view.findViewById(R.id.imageViewInfoPhoto);

        if (getArguments() != null) {
            int workerID = getArguments().getInt(MainActivity.WORKER);
            int specialityId = getArguments().getInt(MainActivity.SPECIALITY);
            Speciality selectedSpeciality = null; //находим в коллекции выбранную специальность, чтобы подставить ее в воркера
            for (Speciality speciality : MainActivity.specialityList) {
                if (speciality.getId() == specialityId) {
                    selectedSpeciality = speciality;
                    break;
                }
            }
            String selection = "id=?";
            String[] selectionArgs = {String.valueOf(workerID)};
            Cursor cursor = this.db.query(
                    DbSchema.WorkersTable.NAME,//делаем выборку из таблицы воркерс, чтобы specialityId было равно выбранной специальности
                    null, selection, selectionArgs,
                    null, null, null
            );
            cursor.moveToFirst();
            worker = new Worker(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.FIRST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.LAST_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.BIRTH_DATE)),
                    cursor.getInt(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.PHOTO)),
                    selectedSpeciality
            );
            cursor.close();
        }
        textViewFirstName.setText(worker.getFirstName());
        textViewLastName.setText(worker.getLastName());
        textViewBirthDate.setText(worker.getBirthDate());
        textViewSpeciality.setText(worker.getSpeciality().getName());
        imageViewPhoto.setImageResource(worker.getPhoto());
        return view;
    }
}
