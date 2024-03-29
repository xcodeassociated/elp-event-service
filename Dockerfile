FROM openjdk:11

RUN apt-get update
RUN apt-get -y install curl
RUN curl -sL https://deb.nodesource.com/setup_13.x | bash -
RUN apt-get -y install nodejs
RUN npm install -g wait-on

ENV APP_HOME=/usr/local/app/
WORKDIR $APP_HOME

COPY ./wait-for.sh $APP_HOME
CMD chmod +x ./wait-for.sh

COPY ./build/libs/*service*.jar ./app.jar

# app servlet
EXPOSE 8082

ENTRYPOINT bash ./wait-for.sh "${WAIT_HOSTS}" && java -jar -Dspring.profiles.active=docker app.jar
