# Team Development Environment



### Source Code

##### How to get git?

Download and install [here](https://www.atlassian.com/git/tutorials/install-git).

To make sure everything was done correctly: `git config --list`

![image-20210207151607718](/home/alexis/snap/typora/33/.config/Typora/typora-user-images/image-20210207151607718.png)

You could also install graphic clients, such as [SmartGit](https://www.syntevo.com/smartgit/), [SourceTree](https://www.sourcetreeapp.com/), or my favourite [GitKraken](https://www.gitkraken.com/).



##### [[1](https://www.atlassian.com/git/tutorials/learn-git-with-bitbucket-cloud)] Basic Overview

* Each repository belongs to a user account or a team.

- The repository owner (individual person or team's admin) is the only person who can delete the repository.
- A code project can consist of multiple repositories across multiple accounts but can also be a single repository from a single account.
- Each repository has a 2 GB size limit, but it's recommended to keep it no larger than 1 GB.

Creating a repository means creating a central location for your files to be stored, that can be accessed by others if you allow it (it can be public - visible to everyone - or private - visible only to the ones you allow). The repository name will be used to create its URL.

<img src="https://wac-cdn.atlassian.com/dam/jcr:a226d62e-3f0f-4c7e-8d99-c3c73188f9f6/01.svg?cdnVersion=1446" alt="Central Repo to Local Repo" style="zoom: 200%;" />

Now that you have a place to add and share your files, you need a way to access it from your local system. To set that up, copy the remote repository to your system - "clone" it. When you clone a repository, you create a connection between the server ("origin") and your local system. 

```bash
cd project_folder
git init
git remote add origin remote_url
git add .
git commit -m "Initial commit"
git push -u origin master
```

**`git add`** command moves changes from the working directory to the Git staging area. The staging area is where you prepare a snapshot of a set of changes before committing them to the official history.

![Git add staging](https://wac-cdn.atlassian.com/dam/jcr:dbf0c59f-848d-4814-bfd5-6b190a092963/03.svg)

 **`git commit`** takes the staged snapshot and commits it to the project history. Combined with `git add`, this process defines the basic workflow for all Git users.

 **`git push origin master`** specifies that you are pushing to the master branch on origin.

Some files don't need to be passed to the remote repository. We can create a .gitignore file (some templates [here](https://github.com/github/gitignore/blob/master/Python.gitignore)) on the root of the project, so that if it is in the file's list, it is ignored by git. 

To pull changes done by others in git: 

**`git pull`** command merges the file from your remote repository into your local repository.



### Logging Exercise: Meteorology Lookup

This exercise consists in taking a small Java application to invoke an API with meteorologic information (original here) and adding logging support. For that, we'll use the logging library [Log4j 2](https://www.baeldung.com/java-logging-intro):

```xml
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-api</artifactId>
	<version>2.6.1</version>	
</dependency>
<dependency>
	<groupId>org.apache.logging.log4j</groupId>
	<artifactId>log4j-core</artifactId>
	<version>2.6.1</version>
</dependency>
```

If we don't want to use default configuration, we can create our own configuration file, just like the following [log4j2.xml](https://howtodoinjava.com/log4j2/log4j-2-xml-configuration-example/):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="debug">
    <Properties>
        <Property name="basePath">~/Desktop/testIES/team-development-environment/source-code-management</Property>
    </Properties>
    <Appenders>
    <File name="fileLogger" fileName="${basePath}/app-info.log" filePattern="${basePath}/app-info-%d{yyyy-MM-dd}.log">
        <PatternLayout>
            <pattern>[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n</pattern>
        </PatternLayout>
        <Policies>
            <TimeBasedTriggeringPolicy interval="1" modulate="true" />
        </Policies>
    </File>

</Appenders>
<Loggers>
    <Root level="INFO">
        <appender-ref ref="fileLogger" />
    </Root>
</Loggers>
</Configuration>
```

This config creates a file named app-info.log in ~/Desktop/testIES/team-development-environment/source-code-management. The xml config file itself should be in src/main/resources.

Applying a logger to our project:

```java
public class WeatherStarter {

    //loggers provide a better alternative to System.out.println https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
    private static final Logger logger = LogManager.getLogger(WeatherStarter.class.getName());

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

            if (forecast != null){
                System.out.println("max temp for today: " + forecast.getData().listIterator().next().gettMax());
                logger.debug("valid query");
            }
            else{
                logger.debug("No results for city ID");
                System.out.println("Sorry! No results for this City ID! Shutting down...");
                exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```

To test our logger, let's change something in the project, like adding support to query not by city id - but by city name (as a result, return the previsions for that city for the next 5 days).

```java
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

        int city_id = 0;

        try {
            city_id = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            logger.debug("Passed a city name");
            city_id = cityToID(city_codes, args[0]);
        }
        (...)
            if (forecast != null){
                System.out.println("max temp for today: " + forecast.getData().listIterator().next().gettMax());
                logger.debug("valid query");
            }
            else{
                logger.debug("No results for city ID");
                System.out.println("Sorry! No results for this City ID! Shutting down...");
                exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```