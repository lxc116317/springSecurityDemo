FROM java:8
VOLUME /tmp
ADD demo-0.0.1-SNAPSHOT.jar test.jar
#可以使用"-Djava.security.egd=file:/dev/./urandom"加快随机数产生过程。
#/dev/urandom是一个非阻塞的发生器
#egd表示熵收集守护进程(entropy gathering daemon)
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/test.jar"]