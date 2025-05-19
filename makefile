# Makefile for Rush Hour Solver

# Compiler and flags
JAVAC = javac
JAVA = java
JFLAGS = -d bin -cp src
SRCDIR = src
BINDIR = bin
MAIN = com.java.Main

# Default target
all: clean build

all-gui: clean build-gui
# Clean the bin directory
clean:
	@echo "Cleaning previous build..."
	@rm -rf $(BINDIR)/*.class $(BINDIR)/com
	@mkdir -p $(BINDIR)

# Compile the source code
build:
	@echo "Compiling Java files..."
	@$(JAVAC) $(JFLAGS) $(SRCDIR)/com/java/Main.java
	@echo "Compilation successful!"
	@echo ""
	@echo "To run the program with a puzzle file:"
	@echo "make run PUZZLE=test/your-puzzle-file.txt"

# Compile the GUI source code
build-gui:
	@echo "Compiling GUI Java files..."
	@$(JAVAC) $(JFLAGS) $(SRCDIR)/com/java/gui/RushHourGUI.java
	@echo "GUI compilation successful!"

# Run the program
run:
	@if [ "$(PUZZLE)" = "" ]; then \
		echo "Usage: make run PUZZLE=<puzzle-file>"; \
		echo "Example: make run PUZZLE=test/test0.txt"; \
		exit 1; \
	fi
	@$(JAVA) -cp $(BINDIR) $(MAIN) $(PUZZLE)

run-gui:
	@java -cp $(BINDIR) com.java.gui.RushHourGUI
	
# Help message
help:
	@echo "Rush Hour Solver Build System"
	@echo ""
	@echo "Available targets:"
	@echo "  make all     - Clean and compile"
	@echo "  make clean   - Remove compiled files"
	@echo "  make compile - Compile the source code"
	@echo "  make run PUZZLE=<file> - Run with specified puzzle file"
	@echo "  make help    - Show this help message"

.PHONY: all clean compile run help