package student;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a list of board games with functionality to filter, save, add, and remove games.
 */
public class GameList implements IGameList {
    /** Stores current filtered games. */
    private List<BoardGame> filteredGames;

    /**
     * Constructs an empty GameList.
     */
    public GameList() {
        this.filteredGames = new ArrayList<>();
    }

    /**
     * Retrieves a sorted list of game names.
     *
     * @return A list of game names in sorted order.
     */
    @Override
    public List<String> getGameNames() {
        return filteredGames.stream()
                .map(BoardGame::getName)
                .sorted(Comparator.comparing((String name) -> name.toLowerCase()) // Ensure case-insensitive sorting
                        .thenComparing(name -> name)) // Ensures stable sorting
                .collect(Collectors.toList());
    }

    /**
     * Clears the list of filtered games.
     */
    @Override
    public void clear() {
        filteredGames.clear();
    }

    /**
     * Retrieves the count of filtered games.
     *
     * @return The number of games in the list.
     */
    @Override
    public int count() {
        return filteredGames.size();
    }

    /**
     * Saves the game list to a file.
     *
     * @param filename The file path to save the list.
     */
    @Override
    public void saveGame(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            filename = "temp/games.txt";
        }

        File file = new File(filename);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new RuntimeException("Error creating directory for: " + filename);
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String name : getGameNames()) {
                writer.println(name);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error saving game list to file: " + filename, e);
        }
    }

    /**
     * Adds games to the list based on a string filter.
     *
     * @param str      The filter criteria (index, range, or name).
     * @param filtered The stream of board games to select from.
     * @throws IllegalArgumentException If the filter is invalid.
     */
    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> selectedGames = parseGamesFromString(str, filtered.collect(Collectors.toList()));
        for (BoardGame game : selectedGames) {
            if (!filteredGames.contains(game)) {
                filteredGames.add(game);
            }
        }
    }

    /**
     * Removes games from the list based on a string filter.
     *
     * @param str The filter criteria (index, range, or name).
     * @throws IllegalArgumentException If the filter is invalid.
     */
    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        List<BoardGame> toRemove = parseGamesFromString(str, new ArrayList<>(filteredGames));
        filteredGames.removeAll(toRemove);
    }

    /**
     * Updates the filtered games list based on a provided stream.
     *
     * @param filteredStream The stream of filtered games.
     */
    public void applyFilter(Stream<BoardGame> filteredStream) {
        filteredGames = filteredStream.collect(Collectors.toList());
    }

    /**
     * Parses a string filter to extract games based on a name, index, or range.
     *
     * @param str      The filter criteria.
     * @param gameList The list of available games.
     * @return A list of selected board games.
     * @throws IllegalArgumentException If the filter is invalid.
     */
    private List<BoardGame> parseGamesFromString(String str, List<BoardGame> gameList) throws IllegalArgumentException {
        if (str.equalsIgnoreCase(ADD_ALL)) {
            return new ArrayList<>(gameList);
        }

        if (str.matches("\\d+")) { // Single index
            int index = Integer.parseInt(str) - 1;
            if (index < 0 || index >= gameList.size()) {
                throw new IllegalArgumentException("Invalid index");
            }
            return List.of(gameList.get(index));
        }

        if (str.matches("\\d+-\\d+")) { // Range
            String[] parts = str.split("-");
            int start = Integer.parseInt(parts[0]) - 1;
            int end = Integer.parseInt(parts[1]) - 1;
            if (start < 0 || end >= gameList.size() || start > end) {
                throw new IllegalArgumentException("Invalid range");
            }
            return gameList.subList(start, end + 1);
        }

        // Assume it's a name
        Optional<BoardGame> game = gameList.stream()
                .filter(g -> g.getName().equalsIgnoreCase(str))
                .findFirst();
        if (game.isEmpty()) {
            throw new IllegalArgumentException("Game not found in list");
        }

        return List.of(game.get());
    }
}
