# YuukaFinder: Rush Hour Path Solver

## Description
YuukaFinder is a path finding visualization tool developed specifically for solving the Rush Hour puzzle game. It implements various search algorithms to find the optimal sequence of moves to solve Rush Hour puzzles. This project was developed as part of the Algorithm Strategies course (Tucil 3).

## Authors
- 13523123 - Rhio Bimo Prakoso Sugiyanto
- 13523147 - Frederiko Eldad Mugiyono

## Features
- Four search algorithms:
  - A* Search (A*)
  - Uniform Cost Search (UCS)
  - Greedy Best-First Search (GBFS)
  - Iterative Deepening A* Search (IDA*)
- Multiple heuristic options:
  - Composite (default)
  - Manhattan Distance
  - Pattern Database
  - Enhanced Blocking
- Graphical User Interface with:
  - Animated solution playback
  - Interactive board visualization
  - Colorful piece representation
  - Video background
- Colorized board visualization in terminal (CLI mode)
- Solution path tracking and visualization
- Performance metrics (execution time, nodes expanded)
- Save solution paths to text and image files

## Dependencies
- Java Development Kit (JDK) 11 or higher
- JavaFX SDK 21+ (for GUI version)
- Terminal with ANSI color support (for CLI version)

## Setup & Running

### Using Makefile (Linux/macOS)
```bash
# Compile the code
make compile

# Run the GUI version
make gui

# Run the CLI version
make cli PUZZLE=test/test0.txt
```

### Manual Compilation
```bash
# Create bin directory
mkdir -p bin

# Compile the code
javac -d bin -cp src src/com/java/Main.java

# Run the GUI version
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.swing,javafx.media --enable-native-access=javafx.graphics,javafx.media -cp bin com.java.Main gui

# Run the CLI version
java -cp bin com.java.Main cli test/test0.txt
```

## Puzzle Format
Puzzles are defined in text files with the following format:
```
rows cols
num_pieces
[board configuration]
K
```

Example:
```
6 6
11
AAB..F
..BCDF
GPPCDFK
GH.III
GHJ...
LLJMM.
```

Where:
- First line: Board dimensions (rows × columns)
- Second line: Number of non-primary pieces
- Next lines: Board configuration with pieces represented by letters (P is primary)
- K: Exit position (can be inline or separate)

## Algorithm Comparison

| Algorithm | Advantages | Disadvantages |
|-----------|------------|---------------|
| A* | Finds optimal solutions | Higher memory usage |
| UCS | Guarantees shortest path | Slower for complex puzzles |
| GBFS | Fast solution finding | May not find optimal path |
| IDA* | Memory efficient, finds optimal solutions | Can be slower on complex puzzles |

## Heuristic Comparison

| Heuristic | Description | Advantage |
|-----------|-------------|-----------|
| Composite | Distance + blocking pieces | Good general performance |
| Manhattan | Simple distance to exit | Fast calculation, works well for simple puzzles |
| Pattern Database | Pre-computed pattern costs | Can be very efficient for known patterns |
| Enhanced Blocking | Advanced blocker detection | Better performance on complex puzzles |

## Project Structure
- java - Source code
  - `model/` - Board and piece representations
  - `searching/` - Search algorithms implementation
  - `checker/` - Solution validation
  - `exception/` - Custom exceptions
  - `gui/` - Graphical user interface components
- test - Test puzzles
- bin - Compiled class files
- resources - Images, videos, and fonts
- test/solutions - Saved solution outputs

## Algorithm Implementations
- **A* Search**: Combines path cost and heuristic for evaluation
- **UCS**: Evaluates nodes based only on path cost
- **GBFS**: Uses heuristic to prioritize most promising nodes
- **IDA***: Memory-efficient version of A* using iterative deepening

The default heuristic combines:
1. Manhattan distance from primary piece to exit
2. Number of blocking pieces in the path