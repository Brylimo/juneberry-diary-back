FROM openjdk:17

COPY juneberry-diary.jar /root/juneberry-diary-back/build/libs/juneberry\ diary-0.1.jar
RUN chmod +x /root/juneberry-diary-back/build/libs/juneberry\ diary-0.1.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/root/juneberry-diary-back/build/libs/juneberry\ diary-0.1.jar"]