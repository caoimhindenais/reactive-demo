# Parralel, Async, Reactive Programming

Sample code for a discussion on 

#### Reactive programming

>In a nutshell reactive programming is about non-blocking, event-driven applications that scale with a small number of threads with backpressure as a key ingredient that aims to ensure producers do not overwhelm consumers. 


#### Optimal # of Threads

CPU Intensive Code 
``` bash
>= #Cores
```

I/O Intensive Code 
``` bash
1 / ( 1-BF) 
where 0>= Blocking Factor <1
```

Amdahl's law can be formulated in the following way:

https://en.wikipedia.org/wiki/Amdahl%27s_law

#### And Gaps to a reactive System
https://www.reactivemanifesto.org
- Resilence
- Elasticity

#### Start
``` bash
mvn clean install
mvn spring-boot:run
curl localhost:8080/event-stream
```

