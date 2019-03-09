# hello-perf
High performance client that can be used as a skeleton to create the Microservices. It produces a series of messages and publishes to a topic, consumes the same from a persistent queue, and then sends the response to another topic. 

The number of messages, message size and the steps in the pipeline (publish/consume/respond) are configurable. By default the three steps run in the same JVM, but it can be started separately as well.

# How to run the client
Clone the repository and use gradle to build and run it.
The program uses command line arguments

````usage: App
 -h,--hostname <arg>       host FQDN
 -j,--publishTopic <arg>   publishTopic
 -o,--publishCount <arg>   publishCount
 -p,--password <arg>       password
 -u,--username <arg>       username
 -v,--vpn <arg>            vpn name
 ````
 
 Sample command line
 ````
 ./gradlew run --args="-h='192.168.1.55:55555' -u=testuser -p=solace123 -v=testvpn -publishCount=20000 -publishTopic=a/b"
 ````
 [hello-perf ]$ ./gradlew run --args="-h='192.168.1.55:55555' -u=username -p=password -v=testvpn -publishCount=20000 -publishTopic=a/b"

> Task :run
01:31:32 INFO  c.e.s.h.App(23) - App Starting [main]
Mar 05, 2019 1:31:32 AM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connecting to host 'orig=192.168.1.55:55555, host=192.168.1.55, port=55555' (host 1 of 1, smfclient 2, attempt 1 of 1, this_host_attempt: 1 of 1)
Mar 05, 2019 1:31:32 AM com.solacesystems.jcsmp.protocol.impl.TcpClientChannel call
INFO: Connected to host 'orig=192.168.1.55:55555, host=192.168.1.55, port=55555' (smfclient 2)
01:31:32 INFO  c.e.s.h.PublishThread(65) - publish-thread - Started. [publish-thread]
01:31:33 INFO  c.e.s.h.PublishThread(69) - Processing Time:Stopwatch:Publish 20000 messages:655.0ms [publish-thread]
01:31:33 INFO  c.e.s.h.PublishThread(70) - publish-thread - Terminated. [publish-thread]
