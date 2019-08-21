package ru.job4j.workers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectWorkersActivity extends Fragment {
    private List<Worker> workers = new ArrayList<>();//используем нестатическую коллекцию, которая будет каждый раз создаваться при создании класса
    // и будет подгружать записи из базы данных по айди специальности
    private RecyclerView recycler;
    SQLiteDatabase db;
    private OnWorkerSelectClickListener callback;
    private boolean isBlue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_workers, container, false);
        this.db = new WorkersBaseHelper(this.getContext()).getReadableDatabase();
        recycler = view.findViewById(R.id.recyclerViewWorkers);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null) {
            int selectedID = getArguments().getInt(MainActivity.SPECIALITY);
            String selection = DbSchema.WorkersTable.Cols.SPECIALITY_ID + "=?";
            String[] selectionArgs = {"" + selectedID};
            Cursor cursor = this.db.query(
                    DbSchema.WorkersTable.NAME,//делаем выборку из таблицы воркерс, чтобы specialityId было равно выбранной специальности
                    null, selection, selectionArgs,
                    null, null, null
            );
            Speciality selectedSpeciality = null; //находим в коллекции выбранную специальность, чтобы подставить ее в воркера
            for (Speciality speciality : MainActivity.specialityList) {
                if (speciality.getId() == selectedID) {
                    selectedSpeciality = speciality;
                    break;
                }
            }
            while (cursor.moveToNext()) {
                workers.add(new Worker(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.BIRTH_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DbSchema.WorkersTable.Cols.PHOTO)),
                        selectedSpeciality
                ));
            }
            cursor.close();
        }
        this.recycler.setAdapter(new WorkersAdapter(getContext(), workers));
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnWorkerSelectClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

    public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkersHolder> {
        private List<Worker> workers;
        private LayoutInflater inflater;

        WorkersAdapter(Context context, List<Worker> workers) {
            this.inflater = LayoutInflater.from(context);
            this.workers = workers;
        }

        @NonNull
        @Override
        public WorkersHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.worker_item, viewGroup, false);
            return new WorkersHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WorkersHolder workersHolder, int i) {
            Worker worker = workers.get(i);
            workersHolder.textViewFirstName.setText(worker.getFirstName());
            workersHolder.textViewLastName.setText(worker.getLastName());
            workersHolder.textViewAge.setText(worker.getBirthDate());
            workersHolder.textViewSpeciality.setText(worker.getSpeciality().getName());
            workersHolder.imageViewPhoto.setImageResource(worker.getPhoto());
            if (isBlue) {
                workersHolder.itemView.setBackgroundColor(getResources().getColor(R.color.colorGray));
                isBlue = false;
            } else {
                workersHolder.itemView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                isBlue = true;
            }

        }

        @Override
        public int getItemCount() {
            return workers.size();
        }

        public class WorkersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView textViewFirstName, textViewLastName, textViewAge, textViewSpeciality;
            private ImageView imageViewPhoto;

            WorkersHolder(View view) {
                super(view);
                textViewFirstName = view.findViewById(R.id.textViewFirstName);
                textViewLastName = view.findViewById(R.id.textViewLastName);
                textViewAge = view.findViewById(R.id.textViewBirth);
                textViewSpeciality = view.findViewById(R.id.textViewSpeciality);
                imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Worker worker = workers.get(getAdapterPosition());
                int result = workers.get(getAdapterPosition()).getId();
                callback.onWorkerClicked(result, worker.getSpeciality().getId());
                notifyDataSetChanged();
            }
        }
    }

    public interface OnWorkerSelectClickListener {
        void onWorkerClicked(int vorkerId, int specialityId);
    }
}
