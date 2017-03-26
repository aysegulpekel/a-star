# a-star

Just an A* search implementation based on [wiki's pseudocode](https://en.wikipedia.org/wiki/A*_search_algorithm). 

### Overview
Basically an approach to find the shortest path for a given goal in a maze. 

Two dimensional grid represents the maze.

The first clicked cell indicates the starting point (green colored) while the second one indicates the goal cell (red colored).

More clicked cells (blue colored) place obstacles and user can control the board with Go! and Clear.

### Requirements
* Java 8+
* Maven 3+

### How to run?

`mvn package -DskipTests`

`java -jar target/a-star-1.0.jar`
