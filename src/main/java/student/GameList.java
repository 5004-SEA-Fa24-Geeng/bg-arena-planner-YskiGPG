package student;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {
    private final Set<BoardGame> games;

    public GameList() {
        this.games = new HashSet<>();
    }

    @Override
    public List<String> getGameNames() {
        return games.stream()
                .map(BoardGame::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public int count() {
        return games.size();
    }

    @Override
    public void saveGame(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (String name : getGameNames()) {
                writer.println(name);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving game list to file", e);
        }
    }

    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> filteredGames = filtered.collect(Collectors.toList());
        games.addAll(parseGamesFromString(str, filteredGames));
    }

    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        // Retrieve sorted list of games (case-insensitive)
        List<BoardGame> sortedGames = games.stream()
                .sorted(Comparator.comparing(BoardGame::getName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        // Parse the games to remove based on the sorted order
        List<BoardGame> toRemove = parseGamesFromString(str, sortedGames);

        // Remove selected games from the original set
        games.removeAll(toRemove);
    }


    /**
     * Parses a string to extract a list of games based on a name, index, or range.
     * @param str The input string (e.g., "1", "1-3", "Go").
     * @param gameList The list of games to search in.
     * @return The list of board games matching the parsed criteria.
     * @throws IllegalArgumentException If the string format is invalid.
     */
    private List<BoardGame> parseGamesFromString(String str, List<BoardGame> gameList) throws IllegalArgumentException {
        if (str.equalsIgnoreCase(ADD_ALL)) {
            return new ArrayList<>(gameList);
        }

        if (str.matches("\\d+")) { // Single index
            int index = Integer.parseInt(str) - 1;
            if (index < 0 || index >= gameList.size()) throw new IllegalArgumentException("Invalid index");
            return List.of(gameList.get(index));
        }

        if (str.matches("\\d+-\\d+")) { // Range
            String[] parts = str.split("-");
            int start = Integer.parseInt(parts[0]) - 1;
            int end = Integer.parseInt(parts[1]) - 1;
            if (start < 0 || end >= gameList.size() || start > end)
                throw new IllegalArgumentException("Invalid range");
            return gameList.subList(start, end + 1);
        }

        // Assume it's a name
        Optional<BoardGame> game = gameList.stream()
                .filter(g -> g.getName().equalsIgnoreCase(str))
                .findFirst();
        if (game.isEmpty()) throw new IllegalArgumentException("Game not found in list");

        return List.of(game.get());
    }
}
