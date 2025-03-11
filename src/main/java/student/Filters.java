package student;

public class Filters {
    private Filters() {
    }

    public static boolean filter(BoardGame game, GameData column,
                                 Operations op, String value) {
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

    public static boolean filterString(String gameData, Operations op, String value) {
        if (gameData == null || value == null) {
            return false;
        }
        System.out.println("Comparing: '" + gameData + "' with '" + value + "'");
        switch (op) {
            case EQUALS:
                return gameData.equalsIgnoreCase(value); // Case-insensitive equality
            case NOT_EQUALS:
                return !gameData.equalsIgnoreCase(value);
            case CONTAINS:
                return gameData.toLowerCase().contains(value.toLowerCase()); // Ensure proper substring match
            default:
                return false;
        }
    }

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
