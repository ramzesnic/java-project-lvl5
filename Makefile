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
	APP_ENV=development ./gradlew bootRun

check-updates:
	./gradlew dependencyUpdates

lint:
	./gradlew checkstyleMain

report:
	./gradlew jacocoTestReport

generate-migrations:
	./gradlew diffChangeLog

db-migrate:
	./gradlew update

test:
	APP_ENV=test ./gradlew test

