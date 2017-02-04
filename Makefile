build:
	@echo "Building..."
	@mkdir -p out/production/behave-app
	@javac -d out/production/behave-app src/com/company/*


run: build
	@echo "Running..."
	@java -classpath out/production/behave-app com.company.Main config.properties
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
