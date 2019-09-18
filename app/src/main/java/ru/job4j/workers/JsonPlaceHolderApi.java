package ru.job4j.workers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("task.json")
    Call<List<Worker>> getWorkers();
}
