mvn clean install
docker service rm mock_prf_env1
docker build -t test/mock:1.0.8.1 .
docker service create --with-registry-auth --name mock_prf_env1 --replicas 1 -p 12015:8080 -p 12815:8009 --reserve-cpu 1 --update-delay 10s --update-parallelism 1 --limit-memory 3G --limit-cpu 1 test/mock:1.0.8.1