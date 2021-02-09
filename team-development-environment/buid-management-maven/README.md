# Team Development Environment



## Maven

Maven is a “build tool”: it's a tool to create *deployable* artifacts form source code. It's also a project management tool, with a project object model, a set of standards, a project lifecycle, a dependency management system and logic to execute each project's lifecycle phases' objectives.



**How to get it?**

Download [here](https://maven.apache.org/download.cgi). Installation Instructions [here](https://maven.apache.org/install.html).

To make sure everything was done correctly: `mvn --version`

![image-20210207104904590](image-20210207104904590.png)



**Create a Project**

```bash
mvn archetype:generate -DgroupId=ies.lab1 -DartifactId=team-devenv -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.0 -DinteractiveMode=false
```

[[1](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html)] **Archetype** is a toolkit to rapidly create Maven projects through a model/template. 

According to [this guide](https://maven.apache.org/guides/mini/guide-naming-conventions.html), **groupid = ies.lab1** identifies the project in a unique way, and it's recommended that it is the reverse of our domain's name (here, the "reverse" is due to the directory structure that is created). It can contain more or less subgroups (compound names), depending on the project's granularity.

Still according to [this guide](https://maven.apache.org/guides/mini/guide-naming-conventions.html), **artifactid = team-devenv** is the JAR's name, without its version. It can be anything, as long as it's lowercase and doesn't contain any strange symbols.

Finally,  [the guide](https://maven.apache.org/guides/mini/guide-naming-conventions.html) says that the **version** can be any dotted number, representing the deployment version(1.0, 1.1, 1.0.1, ...). 



The directory structure created by Maven is the following:

```bash
`--team-devenv
    |-- pom.xml
    `-- src
        |`-- main
        |   `-- java
        |       `-- ies
        |           `-- lab1
        |               |-- App.java
         `-- test
            `-- java
                `-- ies
                    `-- lab1
                        |-- AppTest.java
```



**Build and Run**

After inserted all dependencies into the POM.xml file, you should write the command  `mvn package` to build the project. Then, you only have to execute it, with the command `mvn exec:java -Dexec.mainClass=ies.lab1.App`.



[[2](https://www.baeldung.com/maven-goals-phases)] **Maven Goals**

Maven has 3 lifecycles: 

* the main one, responsible for project deployment
* the one responsible for cleaning the project and remove all files generated from previous builds 
* the one responsible for the project's site documentation

Each of these lifecycles consists in a sequence of phases - each phase being responsible for a specific task. 

The default lifecycle is composed by 23 phases (being the largest one - the clean LC has only 3 phases and the site one has 4), but here are the most important ones:

* *validate:* check if all information necessary for the build is available
* *compile:* compile the source code
* *test-compile:* compile the test source code
* *test:* run unit tests
* *package:* package compiled source code into the distributable format (jar, war, …)
* *integration-test:* process and deploy the package if needed to run integration tests
* *install:* install the package to a local repository
* *deploy:* copy the package to the remote repository

Each phase is a sequence of goals:

* *compile phase:* compile goal - to compile the source code

* *test-compile phase:* test-compile goal - to compile the application test sources

* *test phase:* test goal - to run tests using a suitable unit testing framework

* *package phase*: jar goal - to create a jar file for the project classes inclusive resources

* *install phase:* install goal - to install the package into the local repository, for use as a dependency in other projects locally

* *deploy phase:* deploy goal - to copy the final package to the remote repository for sharing with other developers and projects

  

# Exercise: Meteorology Lookup

This exercise consists in a small Java application to invoke an API with meteorologic information.

The command `curl http://api.ipma.pt/open-data/forecast/meteorology/cities/daily/1010500.json | json_pp` allows checking the content of the API, like the following excerpt:

![image-20210207112944836](/home/alexis/snap/typora/33/.config/Typora/typora-user-images/image-20210207112944836.png)



**Project Configuration**

```bash
mvn archetype:generate -DgroupId=ies.lab1 -DartifactId=MyWeatherRadar -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.0 -DinteractiveMode=false
```

Directory structure:

|      Directory      |            Content            |
| :-----------------: | :---------------------------: |
| **`src/main/java`** | Application/Libraries sources |
| **`src/test/java`** |         Test sources          |

POM.xml file created:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    
  <modelVersion>4.0.0</modelVersion>
    
  <groupId>ies.lab1</groupId>
  <artifactId>MyWeatherRadar</artifactId>
    
  <packaging>jar</packaging>
  <version>1.0-SNAPSHOT</version>
    
  <name>MyWeatherRadar</name>
  <url>http://maven.apache.org</url>
    
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
    
</project>
```



Add the developer team's composition, before all the dependencies. In this case, it's just me:

```xml
 <developers>
        <developer>
            <id>alexa</id>
            <name>Alexandra de Carvalho</name>
            <email>alexandracarvalho@ua.pt</email>
            <url>http://www.ua.pt</url>
            <organization>Universidade de Aveiro</organization>
            <organizationUrl>http://www.ua.pt</organizationUrl>
            <roles>
                <role>developer</role>
            </roles>
            <timezone>0</timezone>
        </developer>
    </developers>
```

Also, add a version of the compiler in use(compiler plugin properties, version = 11):

```xml
<properties>
	<maven.compiler.source>11</maven.compiler.source>
	<maven.compiler.target>11</maven.compiler.target>
</properties>
```



One of the greatest advantages of Maven is its dependency management. To this project, we'll use [Retrofit](https://square.github.io/retrofit/) (to do a request to a REST API REST, in Java) and [Gson](https://github.com/google/gson) (to parse JSON to classes). With Maven, we only need to declare these in the dependencies section of the POM.xml file, as well as their version (it's best to use the most current one):

```xml
<dependency>
	<groupId>com.squareup.retrofit2</groupId>
	<artifactId>retrofit</artifactId>
	<version>2.9.0</version>
</dependency>
<dependency>
	<groupId>com.squareup.retrofit2</groupId>
	<artifactId>converter-gson</artifactId>
	<version>2.9.0</version>
</dependency>
<dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.6</version>
</dependency>
```



The four necessary base classes for this project are located [here](https://gist.github.com/icoPT/8b378e03244d07e11645a97fa1857d7c) e [here](https://gist.github.com/icoPT/a8cf15730bb201a76b228ef3cace5908).

Once we get our project free of errors, let's build it (`mvn clean package`) and execute it (`mvn exec:java -Dexec.mainClass="ies.lab1.WeatherStarter"`).



Let's change the source of the city code to a command-line argument, and print the information in a more friendly way:

```java
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
```

Once we get our project free of errors, let's build it (`mvn clean package`) and execute it with an argument (`mvn exec:java -Dexec.mainClass="ies.lab1.WeatherStarter" -Dexec.args="1010500"`).



**Generte a site**

Command: `mvn site`

If an error occurs, try specifying the plugin version (the last plugin is to also generate the JavaDoc):

```xml
<build>
  <plugins>

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.7.1</version>
    </plugin>

    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-project-info-reports-plugin</artifactId>
      <version>3.0.0</version>
    </plugin>
	
    <plugin>
    	<groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>3.2.0</version>
    </plugin>
  </plugins>
</build>
```

When the build succeeds, you can see the result in **/target/site/index.html**.
