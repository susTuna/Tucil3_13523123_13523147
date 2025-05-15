# YuukaFinder: Rush Hour Path Solver

## Description
YuukaFinder is a path finding visualization tool developed specifically for solving the Rush Hour puzzle game. It implements various search algorithms to find the optimal sequence of moves to solve Rush Hour puzzles. This project was developed as part of the Algorithm Strategies course (Tucil3).

## Directory Structure
```
YuukaFinder/
├── src/             # Source code files
│   └── main/
│       ├── java/    # Java source files
│       └── resources/ # Application resources
├── bin/             # Compiled class files
├── doc/             # Documentation files
└── test/            # Test files
```

## Features
- Multiple path finding algorithms for Rush Hour puzzle solutions:
  - Uniform Cost Search (UCS)
  - Greedy Best-First Search
  - A* (A-Star) Search
- Visualization of the search process and solution moves
- Puzzle editor to create custom Rush Hour scenarios
- Performance comparison between algorithms
- Ability to solve puzzles of varying difficulty

## Rush Hour Game
Rush Hour is a sliding block puzzle where the goal is to get a specific vehicle (usually the red car) out of a grid-based parking lot filled with other vehicles. Vehicles can only move forward or backward along their orientation (horizontal or vertical), and the player must find the sequence of moves that allows the target vehicle to exit.

## Requirements
- Java 21 or higher
- Gradle 8.13 or higher

## Installation

Clone the repository:
```bash
git clone https://github.com/yourusername/YuukaFinder.git
cd YuukaFinder
```

Build the project:
```bash
./gradlew build
```

Create the executable JAR:
```bash
./gradlew fatJar
```

## Usage

Run the application:
```bash
java -jar build/libs/YuukaFinder-all.jar
```

Or use Gradle to run:
```bash
./gradlew run
```

## How to Use
1. Launch the application
2. Load an existing Rush Hour puzzle or create a new one
3. Select a search algorithm
4. Run the search and observe the solution steps
5. Compare algorithm performance for different puzzles

## Authors
- 13523123 - [Your Name]
- 13523147 - [Your Partner's Name]

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Thanks to our course instructors for their guidance
- Special thanks to the Java and Gradle communities for their excellent tools
- Inspired by the classic Rush Hour puzzle game by ThinkFun