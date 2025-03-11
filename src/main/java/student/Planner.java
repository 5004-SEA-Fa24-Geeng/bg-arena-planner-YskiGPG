package student;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Planner implements IPlanner {
    /**
     * Stores the unfiltered set of board games permanently
     */
    private final Set<BoardGame> originalGames;
    /**
     * Stores the progressively filtered results
     */
    private List<BoardGame> appliedFilter; // Stores the progressively filtered results

    public Planner(Set<BoardGame> games) {
//        this.games = games;
        this.originalGames = games;
        this.appliedFilter = games.stream().collect(Collectors.toList()); // Start with all games
    }

    @Override
    public Stream<BoardGame> filter(String filter) {
        // Apply filter on previously filtered results instead of games.stream()
        Stream<BoardGame> filteredStream = filterSingle(filter, appliedFilter.stream());
        appliedFilter = filteredStream
                .sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        return appliedFilter.stream();
    }

    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        // Split by ',' to handle multiple filters
        String[] conditions = filter.split(",");

        for (String condition : conditions) {
            condition = condition.trim();  // Trim each filter condition

            Operations operator = Operations.getOperatorFromStr(condition);
            if (operator == null) {
                continue;  // Skip invalid filters
            }

            String[] parts = condition.split(operator.getOperator());
            if (parts.length != 2) {
                continue;  // Skip malformed filters
            }

            GameData column;
            try {
                column = GameData.fromString(parts[0].trim());
            } catch (IllegalArgumentException e) {
                System.out.println("Filter Column: " + parts[0]);
                continue;  // Skip invalid column names
            }

            String value = parts[1].trim();

            // Debugging Output
//        System.out.println("Filter Operation: " + operator);
//        System.out.println("Filtering Column: " + column);
//        System.out.println("Filter Value: " + value);

            // Apply the filter to the stream
            filteredGames = filteredGames.filter(game -> Filters.filter(game, column, operator, value));
        }

        // Ensure sorting before returning results
        return filteredGames
                .sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList())  // Collect back to list
                .stream(); // Convert back to Stream
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
