FROM anapsix/alpine-java
MAINTAINER Vitaly
RUN mkdir /home/log/
COPY nget-1.jar /home/nget-1.jar
COPY lib /home/lib
CMD ["java","-jar","/home/nget-1.jar"]