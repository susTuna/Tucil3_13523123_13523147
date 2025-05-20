# Makefile for Rush Hour Solver (Swing + JavaFX GUI)

# ── Java / JavaFX toolchain
JAVAC       := javac
JAVA        := java
# update this to point at your local JavaFX SDK lib folder
JAVAFX_LIB  := /path/to/javafx-sdk/lib
JAVAFX_MODS := javafx.swing,javafx.media

# ── Directory layout
SRCDIR  := src
BINDIR  := bin
RESDIR  := resources

# ── Main entry point
MAIN    := com.java.Main

# ── Compiler & runtime flags
JFLAGS  := --module-path $(JAVAFX_LIB) \
           --add-modules $(JAVAFX_MODS) \
           -d $(BINDIR)
RFLAGS  := --module-path $(JAVAFX_LIB) \
           --add-modules $(JAVAFX_MODS) \
           -cp $(BINDIR)

# ── Phony targets
.PHONY: all clean compile resources run help

# Default: clean, compile everything, copy resources
all: clean compile resources

# Remove and recreate bin/
clean:
	@echo "Cleaning $(BINDIR)..."
	@rm -rf $(BINDIR)
	@mkdir -p $(BINDIR)

# Compile every .java under src/
compile:
	@echo "Compiling Java sources..."
	@find $(SRCDIR) -name '*.java' > .sources
	@$(JAVAC) $(JFLAGS) @.sources
	@rm -f .sources
	@echo "Compilation complete."

# Copy fonts/, videos/, etc. so VideoBackground can load them
resources:
	@echo "Copying resources → $(BINDIR)/resources/"
	@cp -r $(RESDIR) $(BINDIR)/
	@echo "Resources copied."

# Launch the GUI
run:
	@echo "Starting Rush Hour Solver GUI…"
	@$(JAVA) $(RFLAGS) $(MAIN)

# Help screen
help:
	@echo "Rush Hour Solver Build"
	@echo ""
	@echo "  make all        # clean, compile, copy resources"
	@echo "  make clean      # remove and recreate $(BINDIR)"
	@echo "  make compile    # compile all Java sources"
	@echo "  make resources  # copy resources into bin/"
	@echo "  make run        # launch the GUI"
	@echo "  make help       # this message"
