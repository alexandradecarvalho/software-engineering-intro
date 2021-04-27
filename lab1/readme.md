ALEXANDRA DE CARVALHO - 93346

--------------------------------------------------------------------------------------------------------------

EXERCÍCIO 1

Apache Maven 3.6.3
Maven home: /usr/share/maven
Java version: 11.0.8, vendor: Ubuntu, runtime: /usr/lib/jvm/java-11-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "5.4.0-51-generic", arch: "amd64", family: "unix"

ARCHETYPE - É um template / modelo de projeto, usado pelo Maven para agilizar a criação dos projetos dos utilizadores. Garante que estes projetos seguem a mesma estrutura e as consideradas boas práticas pelo Maven
GROUPID - Este é o identificador único de quem criou o projeto e, por consequinte, do próprio projeto. Basicamente parece que o projeto fica estruturado por pastas dentro de pastas de acordo com as várias componentes (separadas por pontos) deste id
Naming convention: é costume ser escrito de trás para a frente?
ARTIFACTID - Este consiste num único nome para o artefacto original do projeto (i.e. o nome do projeto) Naming convention: minusculas e sem simbolos estranhos

curl http://api.ipma.pt/open-data/forecast/meteorology/cities/daily/1010500.json| json_pp

Criar: mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

Construir na primeira vez: mvn package

Construir nas restantes vezes: mvn clear package
Correr: mvn exec:java -Dexec.mainClass="com.mycompany.app.App (opcional: -Dexec.args="arg")

POM - ficheiro principal do projeto, que contém todas as suas configurações Maven, isto é, as informações necessárias para ser construído corretamente

Personalização do POM : 
<developers>
    <developer>
        <id>Alexa</id>
        <name>Alexandra de Carvalho</name>
        <email>alexandracarvalho@ua.pt</email>
        <url>http://www.ua.pt</url>
        <organization>Universidade de Aveiro</organization>
        <organizationUrl>http://www.ua.pt</organizationUrl>
        <roles>
            <role>developer</role>
        </roles>
        <timezone>+1</timezone>
    </developer>
</developers>
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
</properties>

Dependências:
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

O main desta aplicação usa o retrofit para aceder ao link com os dados do ipma, usando (através da classe IpmaService) o método get e (através da classe IpmaCityForecast) o código de Aveiro para aceder à informação em JSON

Gerar site: mvn site

    A elaboração de um projeto Maven está desenhada para englobar várias fases. Algumas delas são:
VALIDAÇÃO - validação do projeto e da disponibilidade de toda a informação necessária
COMPILAÇÃO - compilação do código-fonte do projeto
TESTE - teste do código compilado 
EMPACOTAMENTO - empacotamento do código no seu formato disponibilizável (ex: JAR)
VERIFICAÇÃO - correr testes e validação dos seus resultados para garantir o alcance  dos critérios qualidade 
INSTALAÇÃO - instalação do pacote no repsitório local, para ser usado como dependência noutros projetos, localmente
DISPONIBILIZAÇÃO - cópia do pacote final para o repositório remoto para ser partilhado com outros developers e projetos
Como vemos, cada uma destas fases está responsável por realizar algo em específico ao longo do projeto. Para isso, cada fase recorre a uma sequência de "goals". Estes são tarefas mais pequenas, que podem estar relacionadas com várias fases, com apenas uma das fases ou até com nenhuma das fase. 
    Quando uma fase corre, os goals são invocados por uma ordem específica. 
Fase                    |       Goals:             
---------------------------------------_
process-resources       |    resources:resources    
compile                 |     compiler:compile   
process-test-resources  |       resources:testResources    
test-compile            |     compiler:testCompile    
test                    |     surefire:test  
package                 |          jar:jar     
package                 |          war:war     
install                 |      install:install     
deploy                  |       deploy:deploy    

--------------------------------------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------------------------

EXERCÍCIO 2

https://github.com/alexandradecarvalho/IES.git 

Este exercício foi feito individualmente, pelo que o ficheiro de log não tem assim nada de especial (3 commits)

cd IES
git init
git remote add origin *link here*
git add *
git commit -m "Initial commit"
git push -u origin master

git clone *link here*

<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.13.3</version>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.13.3</version>
</dependency>

Alterações para introduzir nome da cidade como argumento:
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
E na função main:
Call<IpmaCityForecast> callSync = service.getForecastForACity(codes.get(args[0]));

--------------------------------------------------------------------------------------------------------------------------

-------------------------------------------------------------------------------------------------------------

EXERCÍCIO 3

Installation here: https://docs.docker.com/engine/install/ubuntu/

Para listar todos os comandos : docker ou docker container --help

BUILD : to create -> docker build -t myimage:1.0 
        to list -> docker image ls
        to remove -> docker image rm alpine:3.4
SHARE : to pull from registry -> docker pull myimage:1.0 
        to retag -> docker tag myimage:1.0 myrepo/myimage:2.0
        to push to a registry -> docker push myrepo/myimage:2.0 
RUN :   to run -> docker container run --name web -p 5000:80 alpine:3.9
        to stop running -> docker container stop (ou kill) web
        to list networks -> docker network ls 
        to list running containers -> docker container ls 
        to delete all containers -> docker container rm -f $(docker ps -aq) 
        to print last 100 lines of the containers log -> docker container logs --tail 100 web

docker container ls -all mostra todos os containers (em execução, parados ou mortos)

Volumes são o mecanismo preferencial para manter a persistência dos dados, independentes de SO e da estrutura do diretório. 