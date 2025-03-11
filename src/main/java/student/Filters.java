package student;

/**
 * Utility class for filtering board games based on different criteria.
 * This class provides methods to filter games by name, numerical values, or double values.
 */
public final class Filters {

    /** Private constructor to prevent instantiation. */
    private Filters() {
    }

    /**
     * Filters a BoardGame based on the specified column, operation, and value.
     *
     * @param game  The BoardGame object to filter.
     * @param column The column of the game to filter on.
     * @param op     The operation to apply (e.g., EQUALS, GREATER_THAN).
     * @param value  The value to compare against.
     * @return true if the game matches the filter condition, false otherwise.
     */
    public static boolean filter(BoardGame game, GameData column, Operations op, String value) {
        switch (column) {
            case NAME:
                return filterString(game.getName(), op, value);
            case MIN_PLAYERS:
                return filterNum(game.getMinPlayers(), op, value);
            case MAX_PLAYERS:
                return filterNum(game.getMaxPlayers(), op, value);
            case MIN_TIME:
                return filterNum(game.getMinPlayTime(), op, value);
            case MAX_TIME:
                return filterNum(game.getMaxPlayTime(), op, value);
            case YEAR:
                return filterNum(game.getYearPublished(), op, value);
            case RANK:
                return filterNum(game.getRank(), op, value);
            case RATING:
                return filterDouble(game.getRating(), op, value);
            case DIFFICULTY:
                return filterDouble(game.getDifficulty(), op, value);
            case ID:
                return filterNum(game.getId(), op, value);
            default:
                return false;
        }
    }

    /**
     * Filters a string-based game attribute based on the specified operation and value.
     *
     * @param gameData The game attribute as a string.
     * @param op       The comparison operation.
     * @param value    The value to compare against.
     * @return true if the game attribute matches the filter condition, false otherwise.
     */
    public static boolean filterString(String gameData, Operations op, String value) {
        if (gameData == null || value == null) {
            return false;
        }

        switch (op) {
            case EQUALS:
                return gameData.equalsIgnoreCase(value);
            case NOT_EQUALS:
                return !gameData.equalsIgnoreCase(value);
            case CONTAINS:
                return gameData.toLowerCase().contains(value.toLowerCase());
            case GREATER_THAN:
                return gameData.compareToIgnoreCase(value) > 0;
            case LESS_THAN:
                return gameData.compareToIgnoreCase(value) < 0;
            case GREATER_THAN_EQUALS:
                return gameData.compareToIgnoreCase(value) >= 0;
            case LESS_THAN_EQUALS:
                return gameData.compareToIgnoreCase(value) <= 0;
            default:
                return false;
        }
    }

    /**
     * Filters an integer-based game attribute based on the specified operation and value.
     *
     * @param gameData The game attribute as an integer.
     * @param op       The comparison operation.
     * @param valueStr The string representation of the value to compare against.
     * @return true if the game attribute matches the filter condition, false otherwise.
     */
    private static boolean filterNum(int gameData, Operations op, String valueStr) {
        int value;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            return false;
        }

        switch (op) {
            case EQUALS:
                return gameData == value;
            case NOT_EQUALS:
                return gameData != value;
            case GREATER_THAN:
                return gameData > value;
            case LESS_THAN:
                return gameData < value;
            case GREATER_THAN_EQUALS:
                return gameData >= value;
            case LESS_THAN_EQUALS:
                return gameData <= value;
            default:
                return false;
        }
    }

    /**
     * Filters a double-based game attribute based on the specified operation and value.
     *
     * @param gameData The game attribute as a double.
     * @param op       The comparison operation.
     * @param valueStr The string representation of the value to compare against.
     * @return true if the game attribute matches the filter condition, false otherwise.
     */
    private static boolean filterDouble(double gameData, Operations op, String valueStr) {
        double value;
        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            return false;
        }

        switch (op) {
            case EQUALS:
                return gameData == value;
            case NOT_EQUALS:
                return gameData != value;
            case GREATER_THAN:
                return gameData > value;
            case LESS_THAN:
                return gameData < value;
            case GREATER_THAN_EQUALS:
                return gameData >= value;
            case LESS_THAN_EQUALS:
                return gameData <= value;
            default:
                return false;
        }
    }

    /**
     * Retrieves the value of a specified column from a BoardGame object.
     *
     * @param game   The BoardGame object.
     * @param column The column whose value should be retrieved.
     * @return The value of the specified column.
     * @throws IllegalArgumentException if the column is unsupported.
     */
    public static Comparable<?> getColumnValue(BoardGame game, GameData column) {
        switch (column) {
            case NAME:
                return game.getName();
            case MIN_PLAYERS:
                return game.getMinPlayers();
            case MAX_PLAYERS:
                return game.getMaxPlayers();
            case MIN_TIME:
                return game.getMinPlayTime();
            case MAX_TIME:
                return game.getMaxPlayTime();
            case YEAR:
                return game.getYearPublished();
            case RANK:
                return game.getRank();
            case RATING:
                return game.getRating();
            case DIFFICULTY:
                return game.getDifficulty();
            case ID:
                return game.getId();
            default:
                throw new IllegalArgumentException("Unsupported column: " + column);
        }
    }
}
