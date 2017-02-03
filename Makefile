build:
	@echo "Building..."
	@mkdir -p out/production/behave-app
	@javac -d out/production/behave-app src/com/company/*


run: build
	@echo "Running..."
	@java -classpath out/production/behave-app com.company.Main
	@echo "\nRun ended."

autorun:
	@echo "Waiting for source files to change..."
	@fswatch -o src | xargs -n1 -I{} make run
