# Team Development Environment



### Docker

Docker utilizes containers with a pre-configured virtual environment, in order to provide a deployment without compatibility issues, for example.  



##### [[1](https://www.youtube.com/watch?v=iqqDU2crIEQ)]**How to get it?**

Download [here](https://docs.docker.com/engine/install/) and more instructions [here](https://docs.docker.com/engine/install/linux-postinstall/).

##### **Cheat Sheet**

![image-20210207211453126](/home/alexis/snap/typora/33/.config/Typora/typora-user-images/image-20210207211453126.png)

##### [[2](https://docs.docker.com/get-started/02_our_app/)] Build and Run your Image

1 - Download [this](https://github.com/docker/getting-started) project.

2 - In the "app" folder, create a file named Dockerfile (without extension!) with the following content:

```dockerfile
 FROM node:12-alpine				# uses this image as a base 
 WORKDIR /app						# sets /app folder as our working directory
 COPY . .							# copies our application
 RUN yarn install --production		# installs dependencies 
 CMD ["node", "src/index.js"]		# this is the command it will always run when starting a container from this image
```

3 - In that same directory, run the following command to build the container image:

```bash
 docker build -t getting-started .	
 # -t allows us to name (tag) our image to "getting-started"
 # the dot at the end tells that the dockerfile used to build this image is in the current directory
```

 4 - Start the container

```bash
docker run -dp 3000:3000 getting-started # -d runs in "detached" mode (bckgrd) and -p maps the ports from 3000 to 3000
```

5 - Open the browser in [http://localhost:3000.](http://localhost:3000/)



##### Portainer

Even though Docker can be used through the command line, you can also install a graphic environment such as Portainer (["Deploy Portainer in Docker"](https://documentation.portainer.io/v2.0/deploy/linux/)), to be avaliable at http://localhost:9000/.

 

### [[3](https://docs.docker.com/engine/examples/postgresql_service/)]Example : Creating an Image of PostgreSQL

1 - Create the Dockerfile:

```dockerfile
#
# example Dockerfile for https://docs.docker.com/engine/examples/postgresql_service/
#

FROM ubuntu:16.04

# Add the PostgreSQL PGP key to verify their Debian packages.
# It should be the same key as https://www.postgresql.org/media/keys/ACCC4CF8.asc
RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8

# Add PostgreSQL's repository. It contains the most recent stable release
#  of PostgreSQL.
RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list

# Install ``python-software-properties``, ``software-properties-common`` and PostgreSQL 9.3
#  There are some warnings (in red) that show up during the build. You can hide
#  them by prefixing each apt-get statement with DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y python-software-properties software-properties-common postgresql-9.3 postgresql-client-9.3 postgresql-contrib-9.3

# Note: The official Debian and Ubuntu images automatically ``apt-get clean``
# after each ``apt-get``

# Run the rest of the commands as the ``postgres`` user created by the ``postgres-9.3`` package when it was ``apt-get installed``
USER postgres

# Create a PostgreSQL role named ``docker`` with ``docker`` as the password and
# then create a database `docker` owned by the ``docker`` role.
# Note: here we use ``&&\`` to run commands one after the other - the ``\``
#       allows the RUN command to span multiple lines.
RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -O docker docker

# Adjust PostgreSQL configuration so that remote connections to the
# database are possible.
RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf

# And add ``listen_addresses`` to ``/etc/postgresql/9.3/main/postgresql.conf``
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf

# Expose the PostgreSQL port
EXPOSE 5432

# Add VOLUMEs to allow backup of config, logs and databases
VOLUME  ["/etc/postgresql", "/var/log/postgresql", "/var/lib/postgresql"]

# Set the default command to run when starting the container
CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]
```

2 - Build the image from the Dockerfile

```bash
docker build -t eg_postgresql .
```

3 - Run the container

```bash
docker run --rm -P --name pg_test eg_postgresql
```

4 - Connect from you host system

```bash
docker ps
psql -h localhost -p 49153 -d docker -U docker --password
```

### [[4](https://docs.docker.com/compose/gettingstarted/)] Docker Compose 

1 - Create a new directory

```bash
mkdir composetest
cd composetest
```

2 - Create a file called app.py with the following content:

```python
import time

import redis
from flask import Flask

app = Flask(__name__)
cache = redis.Redis(host='redis', port=6379)

def get_hit_count():
    retries = 5
    while True:
        try:
            return cache.incr('hits')
        except redis.exceptions.ConnectionError as exc:
            if retries == 0:
                raise exc
            retries -= 1
            time.sleep(0.5)

@app.route('/')
def hello():
    count = get_hit_count()
    return 'Hello World! I have been seen {} times.\n'.format(count)
```

3 - Create a file called requirements.txt with the following content:

```
flask
redis
```

4 - Create the following Dockerfile:

```dockerfile
FROM python:3.7-alpine
WORKDIR /code
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
RUN apk add --no-cache gcc musl-dev linux-headers
COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt
EXPOSE 5000
COPY . .
CMD ["flask", "run"]
```

5 - Create a file called docker-compose.yml with the following content:

```yaml
version: "3.9"
services:
  web:
    build: .
    ports:
      - "5000:5000"
  redis:
    image: "redis:alpine"
```

This file defines both the web and the redis service.

6 - Start the application by running the following command:

```bash
docker-compose up
```

7 - Go to http://localhost:5000/ in the browser and refresh the page

8 - In a new terminal, run the following command:

```bash
docker image ls
```

9 - To stop the application, run:

```bash
docker-compose down
```

10 - To list all docker containers, run:

```bash
docker container ls –all
```

Volumes are the preferred mechanism for persisting data generated by and used by Docker containers. While [bind mounts](https://docs.docker.com/storage/bind-mounts/) are dependent on the directory structure and OS of the host machine, volumes are completely managed by Docker. Volumes have several advantages over bind mounts:

- Volumes are easier to back up or migrate than bind mounts.
- You can manage volumes using Docker CLI commands or the Docker API.
- Volumes work on both Linux and Windows containers.
- Volumes can be more safely shared among multiple containers.
- Volume drivers let you store volumes on remote hosts or cloud providers, to encrypt the contents of volumes, or to add other functionality.
- New volumes can have their content pre-populated by a container.
- Volumes on Docker Desktop have much higher performance than bind mounts from Mac and Windows hosts.

In addition, volumes are often a better choice than persisting data in a container’s writable layer, because a volume does not increase the size of the containers using it, and the volume’s contents exist outside the lifecycle of a given container.

![volumes on the Docker host](https://docs.docker.com/storage/images/types-of-mounts-volume.png)

If your container generates non-persistent state data, consider using a [tmpfs mount](https://docs.docker.com/storage/tmpfs/) to avoid storing the data anywhere permanently, and to increase the container’s performance by avoiding writing into the container’s writable layer.