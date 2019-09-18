package ru.job4j.workers;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Worker {
    private int id;
    @SerializedName("f_name")
    private String firstName;
    @SerializedName("l_name")
    private String lastName;
    @SerializedName("birthday")
    private String birthDate;
    @SerializedName("avatr_url")
    private int photo;
    @SerializedName("specialty")
    private ArrayList<Speciality> specialities;

    public Worker(int id, String firstName, String lastName, String birthDate, int photo, ArrayList<Speciality> specialities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.photo = photo;
        this.specialities = specialities;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getPhoto() {
        return photo;
    }

    public ArrayList<Speciality> getSpecialities() {
        return specialities;
    }

    public class Speciality {
        @SerializedName("specialty_id")
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
