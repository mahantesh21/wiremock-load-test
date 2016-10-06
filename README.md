# pto-durable-services-mock
Wiremock implementation for running data service perf tests against the mock server
How to run

* mvn clean install
* java -cp "wmock-extn/target/wmock-extn-1.0-SNAPSHOT.jar:wiremock-standalone-2.1.12.jar"  com.github.tomakehurst.wiremock.standalone.WireMockServerRunner   --verbose --extensions=com.intuit.ExampleTransformer


* java -cp "wiremock-standalone-2.1.12.jar"  com.github.tomakehurst.wiremock.standalone.WireMockServerRunner   --verbose --extensions=com.intuit.ExampleTransformer
