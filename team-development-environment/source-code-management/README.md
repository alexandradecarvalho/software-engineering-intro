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

This exercise consists in taking a small Java application to invoke an API with meteorologic information and adding logging support. For that, we'll use the logging library [Log4j 2](https://www.baeldung.com/java-logging-intro):

```
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
    
    //loggers provide a better alternative to System.out.println https://rules.sonarsource.com/java/tag/bad-practice/RSPEC-106
    private static final Logger logger = Logger.getLogger(WeatherStarter.class.getName());

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