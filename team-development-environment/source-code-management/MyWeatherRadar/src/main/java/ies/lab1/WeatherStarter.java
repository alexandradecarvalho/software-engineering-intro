package ies.lab1;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import static java.lang.System.exit;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {
    private static int cityToID(HashMap<String, Integer> city_codes, String city_name){
        for(String city : city_codes.keySet())
            if(city.equals(city_name))
                return city_codes.get(city);

        return 0;
    }

    //loggers provide a better alternative to System.out.println https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
    private static final Logger logger = LogManager.getLogger(WeatherStarter.class.getName());

    public static void main(String[] args){
        HashMap<String, Integer> city_codes = new HashMap<>();
        city_codes.put("Aveiro",1010500);
        city_codes.put("Beja",1020500);
        city_codes.put("Braga",1030300);
        city_codes.put("Bragança",1040200);
        city_codes.put("Castelo Branco",1050200);
        city_codes.put("Coimbra",1060300);
        city_codes.put("Évora",1070500);
        city_codes.put("Faro",1080500);
        city_codes.put("Guarda",1090700);
        city_codes.put("Leiria",1100900);
        city_codes.put("Lisboa",1110600);
        city_codes.put("Portalegre",1121400);
        city_codes.put("Porto",1131200);
        city_codes.put("Santarém",1141600);
        city_codes.put("Setúbal",1151200);
        city_codes.put("Viana do Castelo",1160900);
        city_codes.put("Vila Real",1171400);
        city_codes.put("Viseu",1182300);
        city_codes.put("Funchal",2310300);
        city_codes.put("Porto Santo",2320100);
        city_codes.put("Vila do Porto",3410100);
        city_codes.put("Ponta Delgada",3420300);
        city_codes.put("Angra do Heroísmo",3430100);
        city_codes.put("Santa Cruz da Graciosa",3440100);
        city_codes.put("Velas",3450200);
        city_codes.put("Madalena",3460200);
        city_codes.put("Horta",3470100);
        city_codes.put("Santa Cruz das Flores",3480200);
        city_codes.put("Vila do Corvo",3490100);

        int city_id;

        try {
            city_id = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            logger.debug("Passed a city name");
            city_id = cityToID(city_codes, args[0]);
        }

        //get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        IpmaService service = retrofit.create(IpmaService.class);

        Call<IpmaCityForecast> callSync = service.getForecastForACity(city_id);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if(forecast != null){
                System.out.println("max temp for today: " + forecast.getData().listIterator().next().gettMax());
                logger.debug("valid query");
                exit(0);
            }
            else{
                logger.debug("No results for city ID");
                System.out.println("Sorry! No results for this City ID! Shutting down...");
                exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
