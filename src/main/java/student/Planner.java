package student;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner implements IPlanner {
    /** Stores the unfiltered set of board games permanently */
    private final Set<BoardGame> originalGames;
    private Set<BoardGame> games;  // Declare an instance variable to store the board games
    private List<BoardGame> appliedFilter; // Stores the progressively filtered results

    public Planner(Set<BoardGame> games) {
        this.games = games;
        this.originalGames = games;
        this.appliedFilter = games.stream().collect(Collectors.toList()); // Start with all games
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        // Apply filter on previously filtered results instead of games.stream()
        Stream<BoardGame> filteredStream = filterSingle(filter, appliedFilter.stream());
        appliedFilter = filteredStream.collect(Collectors.toList()); // Store filtered result
        return appliedFilter.stream();
    }

    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        Operations operator = Operations.getOperatorFromStr(filter);
        if (operator == null) {
            return filteredGames;
        }

        filter = filter.replaceAll(" ", ""); // Remove spaces

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

        List<BoardGame> filteredGameList = filteredGames
                .filter(game -> Filters.filter(game, column, operator, value))
                .collect(Collectors.toList());

        return filteredGameList.stream();
    }

    @Override
    public Stream<BoardGame> filter(String filter, GameData sortOn) {
        Stream<BoardGame> filteredStream = filter(filter);
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
        Stream<BoardGame> sortedStream = filter(filter, sortOn);
        if (ascending) {
            return sortedStream;
        }
        List<BoardGame> list = sortedStream.collect(Collectors.toList());
        Collections.reverse(list);
        return list.stream();
    }

    @Override
    public void reset() {
        appliedFilter = originalGames.stream().collect(Collectors.toList()); // Reset to original
    }
}
