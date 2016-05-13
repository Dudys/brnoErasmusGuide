package pv239.fi.muni.cz.brnoerasmusguide.service;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import pv239.fi.muni.cz.brnoerasmusguide.pojo.FacultyPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by nfabian on 10.5.16.
 */
public class FacultiesService {

    private static final String facultiesURL =
            "https://is.muni.cz/www/396035/63281828/facJSON.json";

    public static ServerService serverService;
    private static List<Building> buildings = new ArrayList<Building>();

    public static List<Building> getFaculties() {

        get().getFaculties().enqueue(new Callback<List<FacultyPojo>>() {
            @Override
            public void onResponse(Call<List<FacultyPojo>> call, Response<List<FacultyPojo>> response) {
                buildings.addAll(transformToBuilding(response.body()));
            }

            @Override
            public void onFailure(Call<List<FacultyPojo>> call, Throwable t) {

            }
        });
        return buildings;
    }

    public static  ServerService get() {
        if (serverService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(facultiesURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serverService = retrofit.create(ServerService.class);
        }

        return serverService;
    }

    public static List<Building> transformToBuilding(List<FacultyPojo> pojos) {
        List<Building> buildings = new ArrayList<>();
        for(FacultyPojo pojo : pojos) {
            Building building = new Building(
                    pojo.getName(),
                    pojo.getBuildings().get(0).getAddress(),
                    pojo.getWeb(),
                    pojo.getBuildings().get(0).getOpenHours(),
                    pojo.getBuildings().get(0).getMhdInfo());
        }
        return buildings;
    }

    public interface ServerService {
        @GET("facJSON")
        Call<List<FacultyPojo>> getFaculties();
    }
}
