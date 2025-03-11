package student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameListTest {
    static Set<BoardGame> games;
    GameList gameList;

    @BeforeAll
    public static void setup() {
        games = new HashSet<>();
        games.add(new BoardGame("17 days", 6, 1, 8, 70, 70, 9.0, 600, 9.0, 2005));
        games.add(new BoardGame("Chess", 7, 2, 2, 10, 20, 10.0, 700, 10.0, 2006));
        games.add(new BoardGame("Go", 1, 2, 5, 30, 30, 8.0, 100, 7.5, 2000));
        games.add(new BoardGame("Go Fish", 2, 2, 10, 20, 120, 3.0, 200, 6.5, 2001));
        games.add(new BoardGame("golang", 4, 2, 7, 50, 55, 7.0, 400, 9.5, 2003));
        games.add(new BoardGame("GoRami", 3, 6, 6, 40, 42, 5.0, 300, 8.5, 2002));
        games.add(new BoardGame("Monopoly", 8, 6, 10, 20, 1000, 1.0, 800, 5.0, 2007));
        games.add(new BoardGame("Tucano", 5, 10, 20, 60, 90, 6.0, 500, 8.0, 2004));
    }

    @BeforeEach
    void setUp() {
        gameList = new GameList();
    }

    @Test
    void getGameNames() {
        gameList.addToList("all", games.stream());
        List<String> gameNames = gameList.getGameNames();
        assertEquals(8, gameNames.size());
        assertEquals("17 days", gameNames.get(0)); // Ensure sorted order
    }

    @Test
    void clear() {
        gameList.addToList("all", games.stream());
        gameList.clear();
        assertEquals(0, gameList.count());
    }

    @Test
    void count() {
        gameList.addToList("all", games.stream());
        assertEquals(8, gameList.count());
    }

    @Test
    void saveGame() throws IOException {
        gameList.addToList("all", games.stream());
        String filename = "test_games.txt";
        gameList.saveGame(filename);

        File file = new File(filename);
        assertTrue(file.exists());

        List<String> lines = Files.readAllLines(Paths.get(filename));
        assertEquals(8, lines.size()); // Ensure all names are written
        assertEquals("17 days", lines.get(0)); // Ensure sorted order

        file.delete(); // Clean up
    }

    @Test
    void addToList() {
        gameList.addToList("1", games.stream());
        assertEquals(1, gameList.count());
    }

    @Test
    void addToListRange() {
        gameList.addToList("1-3", games.stream());
        assertEquals(3, gameList.count());
    }

    @Test
    void addToListInvalid() {
        assertThrows(IllegalArgumentException.class, () -> gameList.addToList("10", games.stream()));
    }

    @Test
    void removeFromList() {
        gameList.addToList("all", games.stream());
        gameList.removeFromList("1");
        assertEquals(7, gameList.count());
    }

    @Test
    void removeFromListRange() {
        gameList.addToList("all", games.stream());
        gameList.removeFromList("1-3");
        assertEquals(5, gameList.count());
    }

    @Test
    void removeFromListInvalid() {
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("10"));
    }

    @Test
    void addToListByName() {
        gameList.addToList("Chess", games.stream());
        assertEquals(1, gameList.count()); // Only one game should be added
        assertEquals("Chess", gameList.getGameNames().get(0)); // The added game should be "Chess"
    }

    @Test
    void removeNonExistentGame() {
        gameList.addToList("all", games.stream()); // Add all games first
        assertThrows(IllegalArgumentException.class, () -> gameList.removeFromList("NonExistentGame"));
    }
}
