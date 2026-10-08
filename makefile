t = JaikanTests
s = jaikanRandomAnimeTest
p = dev
d = 2026-10-08

dev:
	./mvnw -Dspring-boot.run.profiles=$(p) spring-boot:run 

test:
	./mvnw test -Dtest=$(t)#$(s)

build:
	./mvnw package -Dmaven.test.skip

preview:
	java -jar -Dspring.profiles.active=$(p) -Dserver.port=8080 target/animetoanime-0.0.1-SNAPSHOT.jar

daily:
	curl -H "authorization: ${A2A_CRON_PASSWORD}" http://localhost:8080/cron?

date:
	curl -H "authorization: ${A2A_CRON_PASSWORD}" http://localhost:8080/cron?date=$(d)
