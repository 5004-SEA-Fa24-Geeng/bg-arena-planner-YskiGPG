package student;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Planner implements IPlanner {

    private final Set<BoardGame> games;  // Declare an instance variable to store the board games

    public Planner(Set<BoardGame> games) {
        // TODO Auto-generated method stub
//        throw new UnsupportedOperationException("Unimplemented constructor 'Planner'");
        this.games = games;  // Initialize the games set in the constructor
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        // return Stream<BoardGame>
        // "name == Go"
        Stream<BoardGame> filteredStream = filterSingle(filter, games.stream());
        return filteredStream;
    }

    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames){
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }
        // remove spaces
        filter = filter.replaceAll(" ", "");

        String[] parts = filter.split(operator.getOperator());
        if (parts.length != 2) {
            return filteredGames;
        }

        GameData column;
        try {
            column = GameData.fromString(parts[0]);
        } catch (IllegalArgumentException e) {
            return filteredGames;

        }
        String value;
        try {
            value = parts[1].trim();
        } catch (IllegalArgumentException e) {
            return filteredGames;
        }
        System.out.println("Operator is :" + operator);
        System.out.println("GameData is :" + column);
        System.out.println("Value is :" + value);
        // more work here to filter the games
        // we found creating a String filter and a Number filter to be useful.
        // both of the them take in both the GameData enum, Operator Enum, and the value to parse and filter on.
        List<BoardGame> filteredGameList = filteredGames.filter(game ->
                Filters.filter(game, column, operator, value)).toList();
        System.out.println("FilteredGameList is :" + filteredGameList);
        return filteredGameList.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        Stream<BoardGame> filteredStream = filterSingle(filter, games.stream());

        // Sort based on whether it's a name (String) or a numeric attribute
        return filteredStream.sorted((game1, game2) -> {
            if (sortOn == GameData.NAME) {
                return game1.getName().compareToIgnoreCase(game2.getName());
            } else {
                Comparable<Object> value1 = (Comparable<Object>) Filters.getColumnValue(game1, sortOn);
                Comparable<Object> value2 = (Comparable<Object>) Filters.getColumnValue(game2, sortOn);
                return value1.compareTo(value2);
            }
        });
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        Stream<BoardGame> filteredStream = filter(filter, sortOn);
        if (ascending) {
            return filteredStream;
        }
        List<BoardGame> list = filteredStream.collect(Collectors.toList());
        Collections.reverse(list);
        return list.stream();
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }


}
