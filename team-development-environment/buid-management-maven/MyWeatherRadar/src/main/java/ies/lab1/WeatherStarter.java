package ies.lab1;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    public static void main(String[] args){

        int city_id = 0;
        try {
            city_id = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            System.out.println("Sorry! Invalid City ID! Shutting down...");
            exit(1);
        }

        //get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        IpmaService service = retrofit.create(IpmaService.class);

        Call<IpmaCityForecast> callSync = service.getForecastForACity(city_id);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null)
                System.out.println("max temp for today: " + forecast.getData().listIterator().next().gettMax());
            else{
                System.out.println("Sorry! No results for this City ID! Shutting down...");
                exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
