**Toy project illustrating the differences between (RxJava) Observable and (Java 8) Stream.**

I've written this project to provide a concrete example for the dilemma discussed on [stackoverflow.com][so]
(http://stackoverflow.com/questions/27001610/java-8-equivalent-of-rxjava-observableoncomplete)

There is a test class for each illustrating how an application will interact with a consumer in case of:

- normal flow
- empty input stream
- intermediate step throwing exeption


[so]: http://stackoverflow.com/questions/27001610/java-8-equivalent-of-rxjava-observableoncomplete