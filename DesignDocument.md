# Board Game Arena Planner Design Document


This document is meant to provide a tool for you to demonstrate the design process. You need to work on this before you code, and after have a finished product. That way you can compare the changes, and changes in design are normal as you work through a project. It is contrary to popular belief, but we are not perfect our first attempt. We need to iterate on our designs to make them better. This document is a tool to help you do that.


## (INITIAL DESIGN): Class Diagram 

Place your class diagrams below. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. If it is not, you will need to fix it. As a reminder, here is a link to tools that can help you create a class diagram: [Class Resources: Class Design Tools

### Provided Code

Provide a class diagram for the provided code as you read through it.  For the classes you are adding, you will create them as a separate diagram, so for now, you can just point towards the interfaces for the provided code diagram.

```mermaid
classDiagram
    IGameList <|-- GameList
    IPlanner <|-- Planner
    BoardGame --> GameData
    GameList --> BoardGame : stores
    ConsoleApp --> IGameList : uses
    ConsoleApp --> IPlanner : uses
    GamesLoader --> BoardGame : loads
    Planner --> BoardGame : filters
    Planner --> Operations : uses
    Operations --> GameData : operates on
    GameData : <<enum>> columns
class IGameList{
    <<interface>>
    +List<String> getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
}

class IPlanner{
    <<interface>>
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
}

class BoardGame{
    -String name
    -int id
    -int minPlayers
    -int maxPlayers
    -int minPlayTime
    -int maxPlayTime
    -double difficulty
    -int rank
    -double averageRating
    -int yearPublished
    +BoardGame(String name, int id, int minPlayers, int maxPlayers, int minPlayTime, int maxPlayTime, double difficulty, int rank, double averageRating, int yearPublished)
    +String getName()
    +int getId()
    +int getMinPlayers()
    +int getMaxPlayers()
    +int getMinPlayTime()
    +int getMaxPlayTime()
    +double getDifficulty()
    +int getRank()
    +double getRating()
    +int getYearPublished()
    +String toString()
    +boolean equals(Object obj)
    +int hashCode()
}

class GamesLoader{
    +Set<BoardGame> loadGamesFile(String filename)
    +BoardGame toBoardGame(String line, Map<GameData, Integer> columnMap)
    +Map<GameData, Integer> processHeader(String header)
}

class ConsoleApp{
    +ConsoleApp(IGameList gameList, IPlanner planner)
    +void start()
    +void processHelp()
    +void processFilter()
    +void printCurrentList()
    +void processListCommands()
    +void randomNumber()
    +ConsoleText nextCommand()
    +String remainder()
}

class Planner{
		-Set<BoardGame> allGames
		-List<Stream<BoardGame>> appliedFilters
    +Planner(Set<BoardGame> games)
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
    -Stream<BoardGame> applyFilter(String filter, GameData sortOn, boolean ascending)
}

class GameList{
		-Set<BoardGame> selectedGames
		+List<String> getGameNames()
		+void clear()
		+int count()
		+void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
}

class Operations{
    <<enum>>
    +EQUALS
    +NOT_EQUALS
    +GREATER_THAN
    +LESS_THAN
    +GREATER_THAN_EQUALS
    +LESS_THAN_EQUALS
    +CONTAINS
    +String getOperator()
    +Operations fromOperator(String operator)
    +Operations getOperatorFromStr(String str)
}

class GameData{
    <<enum>>
    +NAME
    +ID
    +RATING
    +DIFFICULTY
    +RANK
    +MIN_PLAYERS
    +MAX_PLAYERS
    +MIN_TIME
    +MAX_TIME
    +YEAR
    +String getColumnName()
    +GameData fromString(String name)
    +GameData fromColumnName(String columnName)
}
```

### Your Plans/Design

Create a class diagram for the classes you plan to create. This is your initial design, and it is okay if it changes. Your starting points are the interfaces. 

```mermaid
classDiagram
    IGameList <|-- GameList
    IPlanner <|-- Planner

class IPlanner{
    <<interface>>
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
}

class IGameList{
    <<interface>>
    +List<String> getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
}

class Planner{
		-Set<BoardGame> allGames
		-List<Stream<BoardGame>> appliedFilters
    +Planner(Set<BoardGame> games)
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
    -Stream<BoardGame> applyFilter(String filter, GameData sortOn, boolean ascending)
}

class GameList{
		-Set<BoardGame> selectedGames
		+List<String> getGameNames()
		+void clear()
		+int count()
		+void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
}
```

## (INITIAL DESIGN): Tests to Write - Brainstorm

Write a test (in english) that you can picture for the class diagram you have created. This is the brainstorming stage in the TDD process. 

> [!TIP]
> As a reminder, this is the TDD process we are following:
> 1. Figure out a number of tests by brainstorming (this step)
> 2. Write **one** test
> 3. Write **just enough** code to make that test pass
> 4. Refactor/update  as you go along
> 5. Repeat steps 2-4 until you have all the tests passing/fully built program

You should feel free to number your brainstorm. 

For Planner：

1. Test 1: Filters games by name ("Go").
1. Test 2: Filters games based on a minimum rating of `8.0`.
1. Test 3: Filters games based on their year of release (greater than or equal to 2005).
1. Test 4: Sorts games by rating in ascending order.
1. Test 5: Filters games by multiple conditions using AND logic (e.g., minPlayers >= 2 and maxPlayers <= 6).
1. Test 6: Resets all filters and ensures no filters are applied.
1. Test 7: Handles invalid filters and throws an exception.
1. Test 8: Filters and sorts games by difficulty in descending order.

For GameList:

1. Test 1: Ensures that a game is correctly added to the list.
2. Test 2: Verifies that a game can be removed from the list.
3. Test 3: Ensures the list is cleared when `clear()` is called.
4. Test 4: Verifies that the list can be saved correctly to a file.
5. Test 5: Ensures that `count()` returns the correct number of games in the list.
6. Test 6: Ensures that removing a non-existent game throws an exception.
7. Test 7: Ensures that no duplicate games are added to the list.


## (FINAL DESIGN): Class Diagram

Go through your completed code, and update your class diagram to reflect the final design. Make sure you check the file in the browser on github.com to make sure it is rendering correctly. It is normal that the two diagrams don't match! Rarely (though possible) is your initial design perfect. 

For the final design, you just need to do a single diagram that includes both the original classes and the classes you added. 

> [!WARNING]
> If you resubmit your assignment for manual grading, this is a section that often needs updating. You should double check with every resubmit to make sure it is up to date.

```mermaid
classDiagram
    IGameList <|-- GameList
    IPlanner <|-- Planner
    

class IPlanner{
    <<interface>>
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
}

class IGameList{
    <<interface>>
    +List<String> getGameNames()
    +void clear()
    +int count()
    +void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
}

class Planner{
		-Set<BoardGame> originalGames
		-List<Stream<BoardGame>> appliedFilter
    +Planner(Set<BoardGame> games)
    +Stream<BoardGame> filter(String filter)
    +Stream<BoardGame> filter(String filter, GameData sortOn)
    +Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending)
    +void reset()
}

class Filters{
    +static boolean filter(BoardGame game, GameData column, Operations op, String value)
    +static boolean filterString(String gameData, Operations op, String value)
    +static boolean filterNumeric(Comparable gameData, Operations op, String value)
    +static Comparable<?> getColumnValue(BoardGame game, GameData column)
}

class GameList{
		-List<BoardGame> filteredGames
		+List<String> getGameNames()
		+void clear()
		+int count()
		+void saveGame(String filename)
    +void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException
    +void removeFromList(String str) throws IllegalArgumentException
    -List<BoardGame> parseGamesFromString(String str, List<BoardGame> gameList) throws IllegalArgumentException
}
```





## (FINAL DESIGN): Reflection/Retrospective

> [!IMPORTANT]
> The value of reflective writing has been highly researched and documented within computer science, from learning to information to showing higher salaries in the workplace. For this next part, we encourage you to take time, and truly focus on your retrospective.

Take time to reflect on how your design has changed. Write in *prose* (i.e. do not bullet point your answers - it matters in how our brain processes the information). Make sure to include what were some major changes, and why you made them. What did you learn from this process? What would you do differently next time? What was the most challenging part of this process? For most students, it will be a paragraph or two. 

While working on the Board Game Arena Planner, my design changed a lot as I figured out better ways to make everything work together. At first, I just focused on getting the basic features in place, but as I tested things, I saw that some parts didn’t work as well as I expected. One big change was in the `Planner` class. At first, every time I applied a filter, it started from the full game list again. This was a problem because filters were supposed to stack on top of each other. I fixed this by keeping track of the current filtered games so that each new filter only applied to the previous results.

Another change happened in `GameList`. At first, I stored the selected games in a `Set`, but that didn’t keep the games in order. Since the program needed to list games alphabetically, I switched to using a sorted list instead. This made sure that adding, removing, and displaying games worked in a way that made sense to the user. I also improved the `Filters` class by combining similar methods, so the code was cleaner and easier to understand.

Through this process, I learned that planning ahead saves a lot of time. If I had thought more about how filtering should work before coding, I wouldn’t have had to go back and rewrite big parts of my program. Next time, I would spend more time designing and testing small parts before building everything at once. The hardest part was making sure that filtering and sorting behaved exactly as expected. Some bugs, like filters not stacking properly or games appearing in the wrong order, were tricky to find and fix.

In the end, I’m happy with how much I improved the design. I realized that coding is not just about making something work, but making it work in a way that’s clear, efficient, and easy to maintain. Even though I had to make a lot of changes, each one made my program better.
