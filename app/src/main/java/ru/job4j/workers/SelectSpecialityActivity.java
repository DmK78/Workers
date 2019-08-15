package ru.job4j.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SelectSpecialityActivity extends Fragment {
    private RecyclerView recycler;
    private OnSpecialitySelectClickListener callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_select_speciality, container, false);
        recycler = view.findViewById(R.id.recyclerViewSpecialities);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recycler.setAdapter(new SpecialitiesAdapter(getContext(), MainActivity.specialityList));
        return view;
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

    public class SpecialitiesAdapter extends RecyclerView.Adapter<SpecialitiesAdapter.SpecialitiesHolder> {
        private List<Speciality> specialities;
        private LayoutInflater inflater;
        SpecialitiesAdapter(Context context, List<Speciality> specialities) {
            this.inflater = LayoutInflater.from(context);
            this.specialities = MainActivity.specialityList;
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
                callback.onSpecialityClicked(getAdapterPosition());
                notifyDataSetChanged();
            }
        }
    }

    public interface OnSpecialitySelectClickListener {
        void onSpecialityClicked(int i);
    }
}
