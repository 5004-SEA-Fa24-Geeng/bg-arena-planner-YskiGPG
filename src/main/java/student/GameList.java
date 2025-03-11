package student;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameList implements IGameList {
    //    private final Set<BoardGame> games;
    private List<BoardGame> filteredGames;  // Stores current filtered games

    public GameList() {
//        this.games = new HashSet<>();
        this.filteredGames = new ArrayList<>(); // Initially empty
    }

    @Override
    public List<String> getGameNames() {
        return filteredGames.stream()
                .map(BoardGame::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        filteredGames.clear();
    }

    @Override
    public int count() {
        return filteredGames.size();
    }

    @Override
    public void saveGame(String filename) {
        System.out.println("Before processing filename: '" + filename + "'");

        // If no filename is provided, use the default "games_list.txt"
        if (filename == null || filename.trim().isEmpty()) {
            filename = "games_list.txt";
        }
        System.out.println("Saving to: " + new File(filename).getAbsolutePath());

        try (PrintWriter writer = new PrintWriter(filename)) {
            for (String name : getGameNames()) {
                writer.println(name);
            }
            System.out.println("Game list saved successfully to: " + filename); // Debug output
        } catch (Exception e) {
            System.err.println("Failed to save game list: " + e.getMessage()); // Print actual error
            e.printStackTrace(); // Debugging
            throw new RuntimeException("Error saving game list to file: " + filename, e);
        }
    }



    @Override
    public void addToList(String str, Stream<BoardGame> filtered) throws IllegalArgumentException {
        List<BoardGame> selectedGames = parseGamesFromString(str, filtered.collect(Collectors.toList()));

        for (BoardGame game : selectedGames) {
            if (!filteredGames.contains(game)) {  // Only add if not already in the list
                filteredGames.add(game);
            }
        }
    }


    @Override
    public void removeFromList(String str) throws IllegalArgumentException {
        if (str.equalsIgnoreCase(ADD_ALL)) {
            clear();
            return;
        }

        // Use the current filtered list
        List<BoardGame> toRemove = parseGamesFromString(str, new ArrayList<>(filteredGames));
        filteredGames.removeAll(toRemove);
    }

    /**
     * Updates the filtered games list based on a provided filter.
     * This method should be called whenever filtering is applied.
     */
    public void applyFilter(Stream<BoardGame> filteredStream) {
        filteredGames = filteredStream.collect(Collectors.toList());
    }

    /**
     * Parses a string to extract a list of games based on a name, index, or range.
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
