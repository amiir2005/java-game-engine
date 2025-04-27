import java.util.Scanner;

public class Player {
    public void launchMenu(){
        Utils.openBuffer();
        Utils.hideCursor();
        Scanner sc = new Scanner(System.in);
        while(true){
            Utils.setWindowTitle("Player Menu");
            Utils.clearScreen(2);
            GameEntry[] gamesList = Config.Player.gamesList;

            Utils.moveCursorTo(Config.Player.Coordinates.SELECT_GAME_MENU_ROW, Config.Player.Coordinates.SELECT_GAME_MENU_COL);
            System.out.print(AppTexts.Player.SELECT_GAME_MENU);
            Utils.saveCursorPosition();

            for (int i = 0; i < gamesList.length; i++){
                Utils.moveCursorTo(Config.Player.Coordinates.MENU_OPTIONS_ROW + (i), Config.Player.Coordinates.MENU_OPTIONS_COL);
                System.out.printf(AppTexts.Player.MENU_OPTION_FORMAT, i+1, gamesList[i].getName());
            }
            Utils.restoreCursor();

            String input = Utils.inputLine(sc).trim();
            while(!isValidInput(input, gamesList.length)){
                Utils.moveCursorTo(Config.Player.Coordinates.INVALID_INPUT_ROW, Config.Player.Coordinates.INVALID_INPUT_COL);
                System.out.print(AppTexts.Player.INVALID_INPUT);
                Utils.restoreCursor();
                Utils.clearLine(0);
                input = Utils.inputLine(sc);

            }
            Utils.moveCursorTo(Config.Player.Coordinates.INVALID_INPUT_ROW, Config.Player.Coordinates.INVALID_INPUT_COL);
            Utils.clearLine(0);


            if (input.trim().equalsIgnoreCase("q")){
                break;
            }

            GameEntry chosenGame = gamesList[Integer.parseInt(input) - 1];
            chosenGame.launch(sc);

        }
        Utils.closeBuffer();
        Utils.showCursor();
        Utils.setWindowTitle("");
    }

    /**
     * Checks if the input is valid.
     * A valid input is either "q" (case-insensitive) or a number within the range [1, max].
     *
     * @param input The user input to validate
     * @param max   The maximum acceptable value (inclusive)
     * @return true if input is "q" or a number between 1 and max, false otherwise
     */
    private static boolean isValidInput(String input, int max) {
        return input.trim().equalsIgnoreCase("q") ||
                (input.trim().matches("^(?i)(\\d+)$") && Utils.isInRange(input.trim(), 1, max));
    }
}
