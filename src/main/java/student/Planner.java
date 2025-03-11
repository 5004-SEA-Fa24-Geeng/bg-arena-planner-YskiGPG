package student;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages a collection of board games and applies filtering operations.
 */
public class Planner implements IPlanner {
    /**
     * Stores the unfiltered set of board games permanently.
     */
    private final Set<BoardGame> originalGames;

    /**
     * Stores the progressively filtered results.
     */
    private List<BoardGame> appliedFilter;

    /**
     * Constructs a Planner with the given set of board games.
     *
     * @param games The initial set of board games.
     */
    public Planner(Set<BoardGame> games) {
        this.originalGames = games;
        this.appliedFilter = games.stream().collect(Collectors.toList()); // Start with all games
    }

    /**
     * Applies a filter to the list of board games.
     *
     * @param filter The filter condition to apply.
     * @return A stream of board games after applying the filter.
     */
    @Override
    public Stream<BoardGame> filter(String filter) {
        Stream<BoardGame> filteredStream = filterSingle(filter, appliedFilter.stream());
        appliedFilter = filteredStream
                .sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
        return appliedFilter.stream();
    }

    /**
     * Filters a stream of board games based on multiple conditions.
     *
     * @param filter The filter condition string.
     * @param filteredGames The stream of board games to filter.
     * @return A filtered stream of board games.
     */
    private Stream<BoardGame> filterSingle(String filter, Stream<BoardGame> filteredGames) {
        String[] conditions = filter.split(",");

        for (String condition : conditions) {
            condition = condition.trim();
            Operations operator = Operations.getOperatorFromStr(condition);
            if (operator == null) {
                continue;
            }

            String[] parts = condition.split(operator.getOperator());
            if (parts.length != 2) {
                continue;
            }

            GameData column;
            try {
                column = GameData.fromString(parts[0].trim());
            } catch (IllegalArgumentException e) {
                System.out.println("Filter Column: " + parts[0]);
                continue;
            }

            String value = parts[1].trim();
            filteredGames = filteredGames.filter(game -> Filters.filter(game, column, operator, value));
        }

        return filteredGames
                .sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList())
                .stream();
    }

    /**
     * Applies a filter and sorts the results based on a specific column.
     *
     * @param filter The filter condition string.
     * @param sortOn The column to sort by.
     * @return A sorted stream of board games.
     */
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

    /**
     * Applies a filter, sorts the results, and orders them based on the specified direction.
     *
     * @param filter The filter condition string.
     * @param sortOn The column to sort by.
     * @param ascending Whether the sort order should be ascending.
     * @return A sorted and ordered stream of board games.
     */
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

    /**
     * Resets the filtered list to the original set of board games.
     */
    @Override
    public void reset() {
        appliedFilter = originalGames.stream().collect(Collectors.toList());
    }
}
