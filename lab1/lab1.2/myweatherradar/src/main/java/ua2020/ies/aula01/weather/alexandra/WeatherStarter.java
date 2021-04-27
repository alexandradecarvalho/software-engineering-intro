package ua2020.ies.aula01.weather.alexandra;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua2020.ies.aula01.weather.alexandra.IpmaCityForecast;
import ua2020.ies.aula01.weather.alexandra.IpmaService;

import java.util.*;
import java.util.logging.Logger;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    private static final int CITY_ID_AVEIRO = 1010500;

    private static Map<String, Integer> codes = new HashMap<String, Integer>() {
        {
            put("Aveiro", 1010500);
            put("Beja", 1020500);
            put("Braga", 1030300);
            put("Bragança", 1040200);
            put("Castelo Branco", 1050200);
            put("Coimbra", 1060300);
            put("Évora", 1070500);
            put("Faro", 1080500);
            put("Guarda", 1090700);
            put("Leiria", 1100900);
            put("Lisboa", 1110600);
            put("Portalegre", 1121400);
            put("Porto", 1131200);
            put("Santarém", 1141600);
            put("Setúbal", 1151200);
            put("Viana do Castelo", 1160900);
            put("Vila Real", 1171400);
            put("Viseu", 1182300);
            put("Funchal", 2310300);
            put("Porto Santo", 2320100);
            put("Vila do Porto", 3410100);
            put("Ponta Delgada", 3420300);
            put("Angra do Heroísmo", 3430100);
            put("Santa Cruz da Graciosa", 3440100);
            put("Santa Cruz das Flores", 3480200);
            put("Velas", 3450200);
            put("Madalena", 3460200);
            put("Horta", 3470100);
            put("Vila do Corvo", 3490100);
        }
    };
    /*
    loggers provide a better alternative to System.out.println
    https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
     */
    private static final Logger logger = Logger.getLogger(WeatherStarter.class.getName());

    public static void  main(String[] args ) {

        /*
        get a retrofit instance, loaded with the GSon lib to convert JSON into objects
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IpmaService service = retrofit.create(IpmaService.class);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(codes.get(args[0]));

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                logger.info( "max temp for today: " + forecast.getData().
                        listIterator().next().getTMax());
            } else {
                logger.info( "No results!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}