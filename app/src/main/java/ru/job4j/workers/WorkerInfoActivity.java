package ru.job4j.workers;

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_worker_info, container, false);
        textViewFirstName = view.findViewById(R.id.textViewInfoFirstName);
        textViewLastName = view.findViewById(R.id.textViewInfoLastName);
        textViewBirthDate = view.findViewById(R.id.textViewInfoBirthDate);
        textViewSpeciality = view.findViewById(R.id.textViewInfoSpeciality);
        imageViewPhoto=view.findViewById(R.id.imageViewInfoPhoto);

        if (getArguments() != null) {
            worker = MainActivity.workerList.get(getArguments().getInt(MainActivity.WORKER));
        } else {
            worker = new Worker("1", "2", "3", 1, new Speciality("Unknown"));
        }
        textViewFirstName.setText(worker.getFirstName());
        textViewLastName.setText(worker.getLastName());
        textViewBirthDate.setText(worker.getBirthDate());
        textViewSpeciality.setText(worker.getSpeciality().getName());
        imageViewPhoto.setImageResource(worker.getPhoto());


        return view;
    }
}
