package ru.job4j.workers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
    private WorkersCore workersCore = WorkersCore.getInstance();
    private SQLiteDatabase db;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_worker_info, container, false);
        textViewFirstName = view.findViewById(R.id.textViewInfoFirstName);
        textViewLastName = view.findViewById(R.id.textViewInfoLastName);
        textViewBirthDate = view.findViewById(R.id.textViewInfoBirthDate);
        textViewSpeciality = view.findViewById(R.id.textViewInfoSpeciality);
        imageViewPhoto = view.findViewById(R.id.imageViewInfoPhoto);
        Worker worker = workersCore.getSelectedWorker();
        textViewFirstName.setText(worker.getFirstName());
        textViewLastName.setText(worker.getLastName());
        textViewBirthDate.setText(worker.getBirthDate());
        imageViewPhoto.setImageResource(worker.getPhoto());
        return view;
    }
}
