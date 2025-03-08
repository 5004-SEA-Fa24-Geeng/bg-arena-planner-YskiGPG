package student;

public class Filters {
    private Filters() {
    }

    public static boolean filter(BoardGame game, GameData column,
                                 Operations op, String value) {
        switch (column) {
            case NAME:
                // filter the name
                return filterString(game.getName(), op, value);
            case MAX_PLAYERS:

            default:
                return false;
        }
    }

    public static boolean filterString(String gameData, Operations op, String value) {
        if (gameData == null || value == null) {
            return false;
        }
        switch (op) {
            case EQUALS:
                // case insensitive
                return gameData.equalsIgnoreCase(value);
            case NOT_EQUALS:
                return !gameData.equalsIgnoreCase(value);
            case CONTAINS:
                return gameData.toLowerCase().contains(value.toLowerCase());
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
}
