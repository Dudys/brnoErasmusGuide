package pv239.fi.muni.cz.brnoerasmusguide.services;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Canteen;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Faculty;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Jan Duda on 5/20/2016.
 */
public class ServiceApiForBuldings {

    private static final String URL = "https://is.muni.cz/www/396035/63281828/";

    public static ServerService serverService;

    public static ServerService get() {
        if (serverService == null) {
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            serverService = retrofit.create(ServerService.class);
        }

        return serverService;
    }

    public interface ServerService {

        @GET("facJSON.json")
        Call<List<Faculty>> getFaculties();

        @GET("canteens.json")
        Call<List<Canteen>> getCanteens();
    }
}
