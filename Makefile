SRC_DIR := src
OUT_DIR := out
LIB_DIR := lib

MAIN_CLASS := rv32i.Main
JAR_FILE := rvik.jar # Compiled .jar output
MANIFEST := manifest.txt

# Defines the library files necessary for testing
HAMCREST_JAR := $(LIB_DIR)/hamcrest-core-1.3.jar
JUNIT_JAR := $(LIB_DIR)/junit-4.13.1.jar

HAMCREST_URL := https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
JUNIT_URL := https://repo1.maven.org/maven2/junit/junit/4.13.1/junit-4.13.1.jar

JAVAC := javac
JAVA := java
JAR := jar
CURL := curl -L -o

# Creates the .jar file
$(JAR_FILE): compile $(MANIFEST)
	$(JAR) cfm $(JAR_FILE) $(MANIFEST) -C $(OUT_DIR) .

# Creates the manifest file
$(MANIFEST):
	echo "Main-Class: $(MAIN_CLASS)" > $(MANIFEST)
	echo "" >> $(MANIFEST)  # blank line required at end

# Downloads hamcrest if it wasn't already install
$(HAMCREST_JAR):
	mkdir -p $(LIB_DIR)
	$(CURL) $(HAMCREST_JAR) $(HAMCREST_URL)

# Downloads JUnit if it wasn't already install
$(JUNIT_JAR):
	mkdir -p $(LIB_DIR)
	$(CURL) $(JUNIT_JAR) $(JUNIT_URL)

# Default target, will just create the .jar file
all: $(JAR_FILE)

# Compiles the .java files to create its binaries
compile:
	mkdir -p $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $(shell find $(SRC_DIR) -name "*.java")

# Downloads the necessary libraries to execute the test if they weren't already in lib/
libs: $(HAMCREST_JAR) $(JUNIT_JAR)

# Cleans everything
clean:
	rm -rf $(OUT_DIR) $(LIB_DIR) $(JAR_FILE) $(MANIFEST)

.PHONY: all compile libs clean
