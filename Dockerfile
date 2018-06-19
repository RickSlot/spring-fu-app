FROM java:8
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN chmod +x ./gradlew && ./gradlew build
EXPOSE 8080
ENTRYPOINT ./gradlew run
