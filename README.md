# YuukaFinder: Rush Hour Path Solver

## Description
YuukaFinder is a path finding visualization tool developed specifically for solving the Rush Hour puzzle game. It implements various search algorithms to find the optimal sequence of moves to solve Rush Hour puzzles. This project was developed as part of the Algorithm Strategies course (Tucil 3).

## Authors
- 13523123 - Rhio Bimo Prakoso Sugiyanto
- 13523147 - Frederiko Eldad Mugiyono

## Features
- Three search algorithms:
  - A* Search (A*)
  - Uniform Cost Search (UCS)
  - Greedy Best-First Search (GBFS)
- Colorized board visualization in terminal
- Solution path tracking and visualization
- Performance metrics (execution time, nodes expanded)
- Save solution paths to text files

## Dependencies
- Java Development Kit (JDK) 11 or higher
- Terminal with ANSI color support

## How to Run

### Using Makefile (Linux/macOS)
```bash
# Compile the code
make compile

# Run the program with a test file
make run PUZZLE=test/test0.txt
```

### Manual Compilation
```bash
# Create bin directory
mkdir -p bin

# Compile the code
javac -d bin -cp src src/com/java/Main.java

# Run the program
java -cp bin com.java.Main test/test0.txt
```

## Puzzle Format
Puzzles are defined in text files with the following format:
```
rows cols
num_pieces
[board configuration]
X
```

Example:
```
6 6
11
AAB..F
..BCDF
GPPCDFX
GH.III
GHJ...
LLJMM.
```

Where:
- First line: Board dimensions (rows × columns)
- Second line: Number of non-primary pieces
- Next lines: Board configuration with pieces represented by letters (P is primary)
- X: Exit position (can be inline or separate)

## Algorithm Comparison

| Algorithm | Advantages | Disadvantages |
|-----------|------------|---------------|
| A* | Finds optimal solutions | Higher memory usage |
| UCS | Guarantees shortest path | Slower for complex puzzles |
| GBFS | Fast solution finding | May not find optimal path |

## Project Structure
- java - Source code
  - `model/` - Board and piece representations
  - `searching/` - Search algorithms implementation
  - `checker/` - Solution validation
  - `exception/` - Custom exceptions
- test - Test puzzles
- bin - Compiled class files
- solutions - Saved solution outputs

## Algorithm Implementations
- **A* Search**: Combines path cost and heuristic for evaluation
- **UCS**: Evaluates nodes based only on path cost
- **GBFS**: Uses heuristic to prioritize most promising nodes

The heuristic combines:
1. Manhattan distance from primary piece to exit
2. Number of blocking pieces in the path