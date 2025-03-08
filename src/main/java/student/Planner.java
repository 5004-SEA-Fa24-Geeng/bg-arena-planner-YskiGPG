package student;


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
//        throw new UnsupportedOperationException("Unimplemented method 'filter'");
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
        System.out.print("Operator is :" + operator);
        System.out.print("GameData is :" + column);
        System.out.print("Value is :" + value);
        // more work here to filter the games
        // we found creating a String filter and a Number filter to be useful.
        // both of the them take in both the GameData enum, Operator Enum, and the value to parse and filter on.
        List<BoardGame> filteredGameList = filteredGames.filter(game ->
                Filters.filter(game, column, operator, value)).toList();
        return filteredGameList.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn, boolean ascending) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filter'");
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }


}
