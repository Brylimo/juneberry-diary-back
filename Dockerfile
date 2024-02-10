FROM openjdk:17

COPY juneberry-diary.jar /home/ubuntu/juneberry-diary-back/build/libs/juneberry-diary.jar
RUN chmod +x /home/ubuntu/juneberry-diary-back/build/libs/juneberry-diary.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/home/ubuntu/juneberry-diary-back/build/libs/juneberry-diary.jar"]