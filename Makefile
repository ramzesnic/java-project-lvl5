clean:
	./gradlew clean

.PHONY: build
build: 
	./gradlew clean build

install: clean
	./gradlew installDist

run-dist:
	./build/install/app/bin/app

start:
	APP_ENV=development	./gradlew bootRun

check-updates:
	APP_ENV=production ./gradlew dependencyUpdates

lint:
	./gradlew checkstyleMain

test:
	APP_ENV=test ./gradlew test

