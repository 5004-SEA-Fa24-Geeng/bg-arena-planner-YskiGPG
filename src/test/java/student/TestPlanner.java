package student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * JUnit test for the Planner class.
 * 
 * Just a sample test to get you started, also using
 * setup to help out. 
 */
public class TestPlanner {
    static Set<BoardGame> games;

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

     @Test
    public void testFilterName() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("name == Go").toList();
        assertEquals(1, filtered.size());
        assertEquals("Go", filtered.get(0).getName());
    }

    // Test for Numeric Filters
    @Test
    public void testFilterMinPlayers() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("minPlayers > 2").toList();

        // Verify at least one game meets the criteria
        assertEquals(3, filtered.size());  // Adjust based on expected results
//        assertEquals("Chess", filtered.get(0).getName());  // Ensure sorting is correct
    }

    @Test
    public void testFilterMaxPlayers() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("maxPlayers <= 5").toList();
        assertEquals(2, filtered.size()); // Adjust expected count based on data
    }

    @Test
    public void testFilterPlayingTime() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("maxplayTime <= 50").toList();
        assertEquals(3, filtered.size()); // Adjust expected count based on data
    }

    @Test
    public void testFilterYearPublished() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("yearPublished == 2002").toList();
        assertEquals(1, filtered.size());
        assertEquals(2002, filtered.get(0).getYearPublished());
    }

    @Test
    public void testSortByYearPublishedDescending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedGames = planner.filter("", GameData.YEAR, false).toList();
        assertEquals(2007, sortedGames.get(0).getYearPublished()); // Ensure first is the most recent year
        assertEquals(2000, sortedGames.get(sortedGames.size() - 1).getYearPublished()); // Ensure last is the oldest year
    }

    @Test
    public void testSortByNameAscending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedGames = planner.filter("", GameData.NAME, true).toList();
        assertEquals("17 days", sortedGames.get(0).getName()); // Ensure first game alphabetically
        assertEquals("Tucano", sortedGames.get(sortedGames.size() - 1).getName()); // Ensure last game alphabetically
    }

    @Test
    public void testSortByMaxPlayersAscending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedGames = planner.filter("maxPlayers <= 15", GameData.MAX_PLAYERS, true).toList();
        assertEquals(2, sortedGames.get(0).getMaxPlayers()); // Smallest maxPlayers first
        assertEquals(10, sortedGames.get(sortedGames.size() - 1).getMaxPlayers()); // Largest maxPlayers last
    }

    @Test
    public void testSortByRatingDescending() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedGames = planner.filter("", GameData.RATING, false).toList();
        assertEquals(10.0, sortedGames.get(0).getRating()); // Highest rating first
        assertEquals(5.0, sortedGames.get(sortedGames.size() - 1).getRating()); // Lowest rating last
    }

    @Test
    public void testResetAfterFiltering() {
        IPlanner planner = new Planner(games);
        List<BoardGame> filtered = planner.filter("maxPlayers < 6").toList();
        assertNotEquals(games.size(), filtered.size()); // Ensure filtering reduces the set

        planner.reset();
        List<BoardGame> resetGames = planner.filter("", GameData.NAME, true).toList();
        assertEquals(games.size(), resetGames.size()); // Ensure reset restores the original data
    }

    @Test
    public void testResetAfterSorting() {
        IPlanner planner = new Planner(games);
        List<BoardGame> sortedGames = planner.filter("", GameData.YEAR, false).toList();
        assertEquals(2007, sortedGames.get(0).getYearPublished()); // Sorted order

        planner.reset();
        List<BoardGame> resetGames = planner.filter("", GameData.NAME, true).toList();
        assertEquals(games.size(), resetGames.size()); // Ensure reset restores the original set size
    }
}
