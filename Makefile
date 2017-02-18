BUILD=out/production/behave-app
CLASSPATH=.:lib/*:$(BUILD)

SRC= \
		 src/com/company/*.java

deps:
	mvn dependency:copy-dependencies -DoutputDirectory=lib
	npm install

build:
	@echo "Building..."
	@mkdir -p $(BUILD)
	@javac -d $(BUILD) -classpath $(CLASSPATH) $(SRC)

run: build
	@echo "Running..."
	@java -classpath $(CLASSPATH) com.company.Main config.properties
	@echo "\nRun ended."

autorun: run
	@echo "Waiting for source files to change..."
	@fswatch -o src -o config.properties | xargs -n1 -I{} make run

test: build
	@echo "Running..."
	@bash test.sh
	@echo "\nRun ended."

autotest: test
	@echo "Waiting for source files to change..."
	@fswatch -o src -o test.sh | xargs -n1 -I{} make test

ts: build
	@node build-history.js
	@java -classpath $(CLASSPATH) com.company.Main timeseries.properties
