package ru.job4j.workers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectSpecialityActivity extends Fragment {
    private RecyclerView recycler;
    private OnSpecialitySelectClickListener callback;
    SQLiteDatabase db;
    private WorkersCore workersCore = WorkersCore.getInstance();
    //List<Speciality> specialityList = workersCore.getAllSpecialities();
    List<Worker> workerList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_speciality, container, false);
        this.db = new WorkersBaseHelper(getContext()).getWritableDatabase();
        Cursor cursor = this.db.query(
                DbSchema.SpecialitiesTable.NAME,
                null, null, null,
                null, null, null
        );
        if (!cursor.moveToFirst()) { //проверяем, пустая ли таблица.
            Gson gson = new GsonBuilder().setLenient().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://github.com/test-tasks/task-json/blob/master/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
            Call<List<Worker>> call = jsonPlaceHolderApi.getWorkers();
            call.enqueue(new Callback<List<Worker>>() {
                @Override
                public void onResponse(Call<List<Worker>> call, Response<List<Worker>> response) {
                    if (response.isSuccessful()) {
                        Log.i("workk",""+response.code());
                        List<Worker> workers = response.body();
                        saveWorkersToDb(workers);
                    }
                }
                @Override
                public void onFailure(Call<List<Worker>> call, Throwable t) {
                    Log.i("workk",""+t.getMessage());

                }
            });















            /*specialityList.add(new Speciality("Medic"));// если таблица пустая, то заполняем ее
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
                }*/

        } else { //иначе загружаем данные из базы данных в коллекцию
       /*     while (!cursor.isAfterLast()) {
                specialityList.add(new Speciality(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex(DbSchema.SpecialitiesTable.Cols.NAME))));
                cursor.moveToNext();
            }*/
        }
        cursor.close();



      /*  recycler = view.findViewById(R.id.recyclerViewSpecialities);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        this.recycler.setAdapter(new SpecialitiesAdapter(getContext(), specialityList));*/
        return view;
    }

    private void saveWorkersToDb(List<Worker> workers){
        workerList.addAll(workers);

    }

    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (OnSpecialitySelectClickListener) context; // назначаем активити при присоединении фрагмента к активити
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null; // обнуляем ссылку при отсоединении фрагмента от активити
    }

/*    public class SpecialitiesAdapter extends RecyclerView.Adapter<SpecialitiesAdapter.SpecialitiesHolder> {
        private List<Speciality> specialities;
        private LayoutInflater inflater;

        SpecialitiesAdapter(Context context, List<Speciality> specialities) {
            this.inflater = LayoutInflater.from(context);
            this.specialities = specialityList;
        }

        @NonNull
        @Override
        public SpecialitiesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.speciality_item, viewGroup, false);
            return new SpecialitiesHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SpecialitiesHolder specialityHolder, int i) {
            Speciality speciality = specialities.get(i);
            specialityHolder.textViewName.setText(speciality.getName());
        }

        @Override
        public int getItemCount() {
            return specialities.size();
        }

        public class SpecialitiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView textViewName;

            SpecialitiesHolder(View view) {
                super(view);
                textViewName = view.findViewById(R.id.textViewSpeciality);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                callback.onSpecialityClicked(specialities.get(getAdapterPosition()).getId());
                notifyDataSetChanged();
            }
        }
    }*/

    public interface OnSpecialitySelectClickListener {
        void onSpecialityClicked(int i);
    }

}
