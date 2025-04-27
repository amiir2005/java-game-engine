import java.util.*;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinesSweeper implements Game{

    private static class  Cell  {
        char    character;
        boolean visited;
        boolean flag;
    }
    private enum          Level {
        BEGINNER, INTERMEDIATE, EXPERT, CUSTOM
    }
    private static class  GameState{
        int         width, height, mineCount;
        boolean     isSetUp;
        int         flagsCount;
        int         unrevealedCount;
        Level       level;
    }
    private static class  StatusUpdater{
        private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        private ScheduledFuture<?> task;
        private final int statusRow, statusColumn;

        public enum Mode{
            win, lose, processing, normal
        }

        public StatusUpdater(int statusRow, int statusColumn) {
            this.statusRow = statusRow;
            this.statusColumn = statusColumn;
        }

        public void    showStatus(Mode status){
            Utils.saveCursorPosition();
            Utils.moveCursorTo(statusRow, statusColumn);
            switch (status){
                case normal:
                    System.out.print(AppTexts.MinesSweeper.NORMAL_STATUS);
                    break;
                case processing:
                    System.out.print(AppTexts.MinesSweeper.PROCESSING_STATUS);
                    break;
                case win:
                    System.out.print(AppTexts.MinesSweeper.WIN_STATUS);
                    break;
                case lose:
                    System.out.print(AppTexts.MinesSweeper.LOST_STATUS);
            }

            Utils.restoreCursor();
        }

        public void    showStatusWithDelay(Mode status, long delay){
            if (this.task != null){
                if (!this.task.isDone()){
                    task.cancel(false);
                }
            }
            this.task = scheduler.schedule(() -> showStatus(status), delay, TimeUnit.MILLISECONDS);
        }

        public void    cancelDelay(){
            if (this.task != null){
                this.task.cancel(false);
            }
        }

        public void shutdown() {
            if (!scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }
        }
    }

    private TimerManager  timer;
    private GameState     state;
    private StatusUpdater status;
    private Cell[][]      board;






    /**
     * Starts the Minesweeper game, allowing the user to select a difficulty or configure a custom game.
     * It validates input, handles both predefined and custom game setups, and starts the game loop.
     * The user can quit at any time by entering 'q'.
     *
     * @param sc The Scanner object for reading user input.
     */
    public static void  launchGame(Scanner sc){
        int winsCount        = 0;
        int lossesCount      = 0;
        int totalTimePlaying = 0; // in second
        int totalGamesPlayed = 0;
        int gamesQuited      = 0;

        while (true) {
            Utils.setWindowTitle("MinesSweeper Menu");
            // Clear the screen before showing the game title and menu
            Utils.clearScreen(2);

            // Display the game title with animation
            Utils.slowPrintMultiLine(
                    AppTexts.MinesSweeper.GAME_TITLE,
                    AppTexts.MinesSweeper.GAME_TITLE_COLOR,
                    Config.MineSweeper.Coordinates.GAME_TITLE_ROW,
                    Config.MineSweeper.Coordinates.GAME_TITLE_COL,
                    1000  );

            MinesSweeper game;

            // Show welcome message
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.WELCOME_MESSAGE_ROW, Config.MineSweeper.Coordinates.WELCOME_MESSAGE_COL);
            Utils.slowPrint(AppTexts.MinesSweeper.WELCOME_MESSAGE, 1000);

            // Show Player Status
            int seconds = totalTimePlaying%60;
            int minutes = totalTimePlaying/60;

            String formatedStatus = String.format(AppTexts.MinesSweeper.USER_STATUS_FORMAT, totalGamesPlayed, winsCount, lossesCount, gamesQuited, minutes, seconds);
            Utils.slowPrintMultiLine(
                    formatedStatus,
                    AppTexts.MinesSweeper.USER_STATUS_COLOR,
                    Config.MineSweeper.Coordinates.USER_STATUS_ROW,
                    Config.MineSweeper.Coordinates.USER_STATUS_COL,
                    700  );

            // Ask user to select a difficulty level
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.MENU_SELECT_DIFFICULTY_ROW,Config.MineSweeper.Coordinates.MENU_SELECT_DIFFICULTY_COL);
            Utils.slowPrint(AppTexts.MinesSweeper.MENU_SELECT_DIFFICULTY, 300);
            Utils.saveCursorPosition();

            // Show difficulty options
            for (int i = 0; i < AppTexts.MinesSweeper.MENU_OPTIONS.length; i++) {
                String option = AppTexts.MinesSweeper.MENU_OPTIONS[i];
                Utils.moveCursorTo(Config.MineSweeper.Coordinates.MENU_OPTIONS_ROW + i, Config.MineSweeper.Coordinates.MENU_OPTIONS_COL);
                Utils.slowPrint(String.format(AppTexts.MinesSweeper.MENU_OPTION_FORMAT, i+1, option), 200);
                Utils.sleep(100);
            }


            // Get user input for difficulty
            Utils.restoreCursor();
            String user_input = Utils.inputLine(sc).trim();

            // Validate difficulty input: must be "q" or number 1–4
            while (!user_input.equalsIgnoreCase("q") && !Utils.isInRange(user_input, 1, AppTexts.MinesSweeper.MENU_OPTIONS.length)){
                // TODO
                Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_OPTION_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_OPTION_ERROR_COL);
                System.out.print(AppTexts.MinesSweeper.WRONG_OPTION_ERROR);
                Utils.restoreCursor();
                Utils.clearLine(0);
                user_input = Utils.inputLine(sc).trim();
            }


            // Clear any previous error message
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_OPTION_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_OPTION_ERROR_COL);
            Utils.clearLine(2);

            // Handle different user choices
            switch (user_input){
                case "q":
                case "Q":
                    // Quit the game
                    return;

                case "1":
                case "2":
                case "3":
                    // Create a standard game with predefined difficulty
                    game = new MinesSweeper(Integer.parseInt(user_input));
                    break;

                case "4":
                    // ───────────────────────────────
                    // Custom Game Setup
                    // ───────────────────────────────


                    // Clear screen and reprint game title
                    Utils.clearScreen(2);
                    Utils.slowPrintMultiLine(
                            AppTexts.MinesSweeper.GAME_TITLE,
                            AppTexts.MinesSweeper.GAME_TITLE_COLOR,
                            Config.MineSweeper.Coordinates.GAME_TITLE_ROW,
                            Config.MineSweeper.Coordinates.GAME_TITLE_COL,
                            0
                    );

                    // Define input pattern: digits or 'q'
                    Pattern inputPattern = Pattern.compile("^(?i)(\\d+|q)$");  // Accept digits or 'q' (case-insensitive)

                    // ────── Input Row Count ──────
                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.INPUT_HEIGHT_ROW, Config.MineSweeper.Coordinates.INPUT_HEIGHT_COL);
                    System.out.printf(AppTexts.MinesSweeper.EnterRow, Config.MineSweeper.MIN_ROW, Config.MineSweeper.MAX_ROW);
                    Utils.saveCursorPosition();
                    String rowInput = Utils.readValidLine(sc, inputPattern);
                    while (rowInput == null) {
                        Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                        System.out.print(AppTexts.MinesSweeper.WRONG_INPUT_ERROR);
                        Utils.restoreCursor();
                        Utils.clearLine(0);
                        rowInput = Utils.readValidLine(sc, inputPattern);
                    }
                    if (rowInput.equalsIgnoreCase("q")) continue;

                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                    Utils.clearLine(2);

                    int row = Utils.forceInRange(rowInput, Config.MineSweeper.MIN_ROW, Config.MineSweeper.MAX_ROW);

                    // ────── Input Column Count ──────
                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.INPUT_WIDTH_ROW, Config.MineSweeper.Coordinates.INPUT_WIDTH_COL);
                    System.out.printf(AppTexts.MinesSweeper.EnterColumn, Config.MineSweeper.MIN_COL, Config.MineSweeper.MAX_COL);
                    Utils.saveCursorPosition();
                    String colInput = Utils.readValidLine(sc, inputPattern);
                    while (colInput == null) {
                        Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                        System.out.print(AppTexts.MinesSweeper.WRONG_INPUT_ERROR);
                        Utils.restoreCursor();
                        Utils.clearLine(0);
                        colInput = Utils.readValidLine(sc, inputPattern);
                    }
                    if (colInput.equalsIgnoreCase("q")) continue;

                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                    Utils.clearLine(2);

                    int col = Utils.forceInRange(colInput, Config.MineSweeper.MIN_COL, Config.MineSweeper.MAX_COL);

                    // ────── Input Mines Count ──────
                    int maxMines = Config.MineSweeper.MAX_MINES(row, col);
                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.INPUT_MINES_ROW, Config.MineSweeper.Coordinates.INPUT_MINES_COL);
                    System.out.printf(AppTexts.MinesSweeper.EnterMines, Config.MineSweeper.MIN_MINES, maxMines);
                    Utils.saveCursorPosition();
                    String mineInput = Utils.readValidLine(sc, inputPattern);
                    while (mineInput == null) {
                        Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                        System.out.print(AppTexts.MinesSweeper.WRONG_INPUT_ERROR);
                        Utils.restoreCursor();
                        Utils.clearLine(0);
                        mineInput = Utils.readValidLine(sc, inputPattern);
                    }
                    if (mineInput.equalsIgnoreCase("q")) continue;

                    Utils.moveCursorTo(Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_ROW, Config.MineSweeper.Coordinates.WRONG_INPUT_ERROR_COL);
                    Utils.clearLine(2);

                    int mines = Utils.forceInRange(mineInput, Config.MineSweeper.MIN_MINES, maxMines);

                    // Create custom game with user-defined settings
                    game = new MinesSweeper(col, row, mines, Level.CUSTOM);
                    break;


                // Shouldn't be reachable due to earlier validation, but safe fallback
                default:
                    continue;
            }

            // Start the game session
            GameResult result =  game.startPlaying(sc);
            totalGamesPlayed++;
            totalTimePlaying += (int) result.getTimeSpent()/1000;
            if      (result.isWin()) winsCount++;
            else if (result.isLost()) lossesCount++;
            else if (result.isQuited()) gamesQuited++;
        }
    }

    private GameResult  startPlaying(Scanner sc){
        Utils.setWindowTitle("MinesSweeper");
        showGameLayout(1500);

        GameResult result = new GameResult();
        // GAME LOOP
        // run the game until all the empty blocks are gone
        while (state.unrevealedCount != state.mineCount){
            int[] r = getInput(sc);
            while (r == null){
                r = getInput(sc);
            }


            if (r[0] == 'q'){
                double totalTime = timer.stopTimer();
                status.shutdown();
                result.quit(totalTime);
                Utils.clearScreen(2);
                status.cancelDelay();
                return result;
            }
            else if(r[0] == 'v'){
                int row = r[1];
                int col = r[2];
                if (!openCell(row, col)){
                    double totalTime = timer.stopTimer();
                    status.shutdown();
                    result.lost(totalTime);
                    break;
                }
            }
            else if (r[0] == 'f'){
                int row = r[1];
                int col = r[2];
                markFlag(row, col);
            }
            else if (r[0] == 'h'){
                status.cancelDelay();
                timer.pauseTimer();
                Utils.clearScreen(2);
                System.out.print(AppTexts.MinesSweeper.HELPER);
                Utils.moveCursorTo(Config.MineSweeper.Coordinates.WAIT_FOR_ENTER_LABEL_ROW, Config.MineSweeper.Coordinates.WAIT_FOR_ENTER_LABEL_COL);
                Utils.slowPrint(AppTexts.MinesSweeper.WAIT_FOR_ENTER_LABEL, 500);
                Utils.inputLine(sc);
                showGameLayout(0);
            }
        }
        status.cancelDelay();



        Utils.saveCursorPosition();
        Utils.moveCursorTo(Config.MineSweeper.Coordinates.getInputRow, Config.MineSweeper.Coordinates.getInputCol);
        for (int line = 0; line < Utils.getLinesCount(AppTexts.MinesSweeper.INPUT_PROMPT); line++){
            Utils.clearLine(0);
            Utils.moveCursorDown(1);
            Utils.moveCursorToColumn(1);
        }

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.minesInfoRow, Config.MineSweeper.Coordinates.minesInfoCol);
        Utils.clearLine(0);
        System.out.printf(AppTexts.MinesSweeper.MINES_LEFT_FORMAT, 0);

        Utils.restoreCursor();

        int[] bombCoordinates =  displayFinalBoard(2000);
        if (result.isLost()){
            status.showStatus(StatusUpdater.Mode.lose);
            looseAnimation(bombCoordinates);
            displayFinalBoard(500);
        }
        else {
            double totalTime = timer.stopTimer();
            status.shutdown();
            result.win(totalTime);
            status.showStatus(StatusUpdater.Mode.win);
            winAnimation();
        }

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.WAIT_FOR_ENTER_LABEL_ROW, Config.MineSweeper.Coordinates.WAIT_FOR_ENTER_LABEL_COL);
        Utils.slowPrint(AppTexts.MinesSweeper.WAIT_FOR_ENTER_LABEL, 500);
        Utils.inputLine(sc);
        Utils.clearScreen(2);
        return result;
    }



    // Basic functions

    private         MinesSweeper(int width, int height, int mine , Level level) {
        configureGame(width, height, mine , level);
    }

    private         MinesSweeper(int level){
        // Level Beginner
        if (level == 1){
            configureGame(
                    Config.MineSweeper.BEGINNER_WIDTH,
                    Config.MineSweeper.BEGINNER_HEIGHT,
                    Config.MineSweeper.BEGINNER_MINES,
                    Level.BEGINNER  );
        }
        // Level Intermediate
        else if (level == 2){
            configureGame(
                    Config.MineSweeper.INTERMEDIATE_WIDTH,
                    Config.MineSweeper.INTERMEDIATE_HEIGHT,
                    Config.MineSweeper.INTERMEDIATE_MINES,
                    Level.INTERMEDIATE  );
        }
        // Level Expert
        else if (level == 3){
            configureGame(
                    Config.MineSweeper.EXPERT_WIDTH,
                    Config.MineSweeper.EXPERT_HEIGHT,
                    Config.MineSweeper.EXPERT_MINES,
                    Level.EXPERT);
        }
    }

    private void    configureGame(int width, int height, int mine , Level level){
        // setting variables
        state = new GameState();
        state.width = width;
        state.height = height;
        state.mineCount = mine;
        state.level = level;

        timer = new TimerManager(Config.MineSweeper.Coordinates.TIME_ROW, Config.MineSweeper.Coordinates.TIME_COL, AppTexts.MinesSweeper.TIME_FORMAT);
        status = new StatusUpdater(Config.MineSweeper.Coordinates.EMOJI_ROW, Config.MineSweeper.Coordinates.box_topLeftCorner_col + (state.width*Config.MineSweeper.CELL_CHARACTER_SIZE)/2 - 2);

        // creating board matrix
        this.board = new Cell[height][width];
        for (int row = 0; row < height; row++){
            for (int col = 0; col < width; col++){
                this.board[row][col] = new Cell();
            }
        }
        state.unrevealedCount = state.width * state.height;
    }


    // Backend functions
    private boolean isValidRow(int row){
        return Utils.isInRange(row, 0, state.height-1);
    }

    private boolean isValidCol(int col){
        return Utils.isInRange(col, 0, state.width-1);
    }

    private int[]   getInput(Scanner sc){
        Utils.moveCursorTo(Config.MineSweeper.Coordinates.getInputRow, Config.MineSweeper.Coordinates.getInputCol);
        System.out.print(AppTexts.MinesSweeper.INPUT_PROMPT);
        Utils.clearLine(0);



        status.showStatusWithDelay(StatusUpdater.Mode.normal, 150);
        timer.unpauseTimer();
        String line = Utils.inputLine(sc).trim();
        timer.pauseTimer();
        status.showStatus(StatusUpdater.Mode.processing);

        if (line.isEmpty()){
            return null;
        }

        Matcher matcher = Pattern.compile("^(?i)((?:([fv])\\s+)?(\\d+)\\s+(\\d+)|[hq])$").matcher(line);


        // clear the error line (if there is last error
        Utils.moveCursorTo(Config.MineSweeper.Coordinates.errorLineRow, Config.MineSweeper.Coordinates.errorLineCol);
        Utils.clearLine(2);


        char mode;
        if (!matcher.matches()){
            System.out.print(AppTexts.MinesSweeper.INVALID_INPUT_FORMAT);
//            return getInput(sc);
            return null; // To avoid repeated calls to getInput(sc) and recursion
        }

        switch (matcher.group(1)){
            case "Q":
            case "q":
                mode = 'q';
                return new int[]{mode};
            case "H":
            case "h":
                mode = 'h';
                return new int[]{mode};
            default:
                int row = Integer.parseInt(matcher.group(3))-1;
                int col = Integer.parseInt(matcher.group(4))-1;

                // force in range
                if (!isValidRow(row)){
                    System.out.print(AppTexts.MinesSweeper.ROW_OUT_OF_RANGE);
                    return null;
                }
                if (!isValidCol(col)){
                    System.out.print(AppTexts.MinesSweeper.COL_OUT_OF_RANGE);
                    return null;
                }
                mode = (matcher.group(2) != null && matcher.group(2).equalsIgnoreCase("f")) ? 'f' : 'v';
                return new int[]{mode,row, col};
        }
    }

    private void    setupBoard(int firstChoiceRow, int firstChoiceCol){
        Random rand  = new Random();

        List<Integer> cells = new ArrayList<>(state.width * state.height);
        for (int i = 0; i < state.height; i++) {
            for (int j = 0; j < state.width; j++) {
                cells.add(i * state.width + j);  // Convert (i, j) to a single index
            }
        }
        cells.remove(firstChoiceRow* state.width + firstChoiceCol);

        // setting mines in to the board
        for (int bombCount = 0; bombCount < state.mineCount;){
            int index = rand.nextInt(cells.size());
            int row = cells.get(index)/ state.width;
            int col = cells.get(index)% state.width;
            if (Math.abs(row - firstChoiceRow) > 1 || Math.abs(col - firstChoiceCol) > 1 || cells.size() < 9){
                // setting the bomb
                this.board[row][col].character = '*';
                // removing the bomb from list
                cells.remove(index);

                // increasing none-bomb neighborhoods by 1
                for (int i = Math.max(row-1,0); i <= Math.min(row+1, state.height-1); i++){
                    for (int j = Math.max(col-1,0); j <= Math.min(col+1, state.width-1); j++){
                        if (this.board[i][j].character == '\0'){
                            this.board[i][j].character = '1';
                        }else if (this.board[i][j].character != '*'){
                            this.board[i][j].character++;
                        }
                    }
                }

                bombCount++;
            }
        }

        // changing every '\0' to '0'
        for (int i = 0; i < state.height; i++){
            for (int j = 0; j < state.width; j++){
                if (this.board[i][j].character == '\0'){
                    this.board[i][j].character = '0';
                }
            }
        }
    }

    private boolean checkMine(int row, int col){
        return this.board[row][col].character == '*';
    }




    // GUI
    private void    showGameLayout(int totalSleepTimeMillis){
        Utils.clearScreen(2);

        Utils.slowPrintMultiLine(   AppTexts.MinesSweeper.GAME_TITLE, AppTexts.MinesSweeper.GAME_TITLE_COLOR,
                Config.MineSweeper.Coordinates.GAME_TITLE_ROW,
                Config.MineSweeper.Coordinates.GAME_TITLE_COL,
                0  );


        if (totalSleepTimeMillis != 0){
            Utils.loadingAnimation(Config.MineSweeper.Coordinates.LOADING_ANIMATION_ROW, Config.MineSweeper.Coordinates.LOADING_ANIMATION_COL, 40, totalSleepTimeMillis / 2);

            Utils.moveCursorTo(Config.MineSweeper.Coordinates.LOADING_ANIMATION_ROW, Config.MineSweeper.Coordinates.LOADING_ANIMATION_COL);
            Utils.clearScreen(0);
        }



        Utils.moveCursorTo(Config.MineSweeper.Coordinates.LEVEL_INFO_ROW,Config.MineSweeper.Coordinates.LEVEL_INFO_COL);
        System.out.printf(AppTexts.MinesSweeper.LEVEL_INFO_FORMAT_FORMAT, state.level);

        if (timer.isTimerOn()){
            timer.unpauseTimer();
        }else{
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.TIME_ROW,Config.MineSweeper.Coordinates.TIME_COL);
            System.out.printf(AppTexts.MinesSweeper.TIME_FORMAT, 0,0,0);   // display the defaults Info
        }

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.minesInfoRow, Config.MineSweeper.Coordinates.minesInfoCol);
        System.out.printf(AppTexts.MinesSweeper.MINES_LEFT_FORMAT, state.mineCount - state.flagsCount);


        displayBorderNumbers();
        displayBorder(AppTexts.MinesSweeper.NORMAL_BORDER_COLOR, totalSleepTimeMillis/2);
        displayBoard(totalSleepTimeMillis/2);
    }

    private void    displayBorder(String color, int totalSleepTimeMillis){
        Utils.saveCursorPosition();
        

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row, Config.MineSweeper.Coordinates.box_topLeftCorner_col);
        System.out.print(color + AppTexts.MinesSweeper.GRID_TOP_LEFT_CORNER);

        int max = Math.max(state.height, state.width);
        double heightStep = (double) state.height /max;
        double widthStep = (double) state.width * Config.MineSweeper.CELL_CHARACTER_SIZE /max;

        int horizontalPrinted = 0;
        int verticalPrinted = 0;
        for (int i = 1; i <= max; i++){
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row, Config.MineSweeper.Coordinates.box_topLeftCorner_col+1 + horizontalPrinted);
            while (horizontalPrinted < Math.round(i*widthStep) ){
                horizontalPrinted++;
                System.out.print(color + AppTexts.MinesSweeper.GRID_HORIZONTAL_LINE);
            }


            Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row + verticalPrinted + 1, Config.MineSweeper.Coordinates.box_topLeftCorner_col);
            while (verticalPrinted < Math.round(i*heightStep) ){
                verticalPrinted++;
                System.out.print(color + AppTexts.MinesSweeper.GRID_VERTICAL_BORDER);
                Utils.moveCursorDown(1);
                Utils.moveCursorBack(1);
            }

            Utils.sleep(totalSleepTimeMillis/max/2);
        }

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row, Config.MineSweeper.Coordinates.box_topLeftCorner_col + horizontalPrinted + 1);
        System.out.print(color + AppTexts.MinesSweeper.GRID_TOP_RIGHT_CORNER);

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row + verticalPrinted + 1, Config.MineSweeper.Coordinates.box_topLeftCorner_col);
        System.out.print(color + AppTexts.MinesSweeper.GRID_BOTTOM_LEFT_CORNER);

        horizontalPrinted = 0;
        verticalPrinted = 0;
        for (int i = 1; i <= max; i++){
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row + state.height + 1, Config.MineSweeper.Coordinates.box_topLeftCorner_col + horizontalPrinted + 1);
            while (horizontalPrinted < Math.round(i*widthStep) ){
                horizontalPrinted++;
                System.out.print(color + AppTexts.MinesSweeper.GRID_HORIZONTAL_LINE);
            }


            Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row + verticalPrinted + 1, Config.MineSweeper.Coordinates.box_topLeftCorner_col + state.width*Config.MineSweeper.CELL_CHARACTER_SIZE + 1);
            while (verticalPrinted < Math.round(i*heightStep) ){
                verticalPrinted++;
                System.out.print(color + AppTexts.MinesSweeper.GRID_VERTICAL_BORDER);
                Utils.moveCursorDown(1);
                Utils.moveCursorBack(1);
            }

            Utils.sleep(totalSleepTimeMillis/max/2);
        }

        Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row + state.height + 1, Config.MineSweeper.Coordinates.box_topLeftCorner_col + state.width*Config.MineSweeper.CELL_CHARACTER_SIZE + 1);
        System.out.print(color + AppTexts.MinesSweeper.GRID_BOTTOM_RIGHT_CORNER);


        Utils.restoreCursor();
        
    }

    private void    displayBorderNumbers() {
        Utils.saveCursorPosition();
        
        for (int i = 1; i <= state.width; i++){
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.columnNumbers_row,Config.MineSweeper.Coordinates.columnNumbers_col + (i-1)*Config.MineSweeper.CELL_CHARACTER_SIZE);
            System.out.printf(AppTexts.MinesSweeper.COLUMN_INDEX_FORMAT, i);
        }
        for (int i = 1; i <= state.height; i++){
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.rowNumbers_row+ (i-1),Config.MineSweeper.Coordinates.rowNumbers_col);
            System.out.printf(AppTexts.MinesSweeper.ROW_INDEX_FORMAT, i);
            Utils.moveCursorToColumn(Config.MineSweeper.Coordinates.box_topLeftCorner_col + state.width*Config.MineSweeper.CELL_CHARACTER_SIZE + 2);
            System.out.printf(AppTexts.MinesSweeper.ROW_INDEX_FORMAT, i);
        }
        Utils.restoreCursor();
        
    }

    private void    displayBoard(int totalSleepTimeMillis) {
        Utils.saveCursorPosition();
        
        int sleepTime = totalSleepTimeMillis/(state.width* state.height);
        for (int i = 0; i < state.width+ state.height-1; i++){
            int col = Math.min(i, state.width-1);
            for (int row = Math.max(0,i- state.width+1); row < state.height && col >=0; row++){
                Utils.moveCursorTo(Config.MineSweeper.Coordinates.topLeftCell_row + row,Config.MineSweeper.Coordinates.topLeftCell_col + (Config.MineSweeper.CELL_CHARACTER_SIZE*col));
                if (this.board[row][col].visited){
                    System.out.print(AppTexts.MinesSweeper.REVEALED_CELL_BG(row, col) + AppTexts.MinesSweeper.DIGIT_FORMAT(this.board[row][col].character));
                }
                else if (this.board[row][col].flag){
                    System.out.print(AppTexts.MinesSweeper.FLAG);
                }
                else{
                    System.out.print(AppTexts.MinesSweeper.COVERED_CELL(row,col));
                }
                col--;
                Utils.sleep(sleepTime);
            }
        }
        Utils.restoreCursor();
        
    }

    private int[]   displayFinalBoard(int totalSleepTimeMillis){
        Utils.saveCursorPosition();
        
        int bombX = 0, bombY = 0;
        int sleepTime = totalSleepTimeMillis/(state.width* state.height);
        for (int i = 0; i < state.width+ state.height-1; i++){
            int col = Math.min(i, state.width-1);
            for (int row = Math.max(0,i- state.width+1); row < state.height && col >=0; row++){
                Utils.moveCursorTo(Config.MineSweeper.Coordinates.topLeftCell_row + row,Config.MineSweeper.Coordinates.topLeftCell_col + (Config.MineSweeper.CELL_CHARACTER_SIZE*col));
                if (this.board[row][col].character == '*'){
                    if (this.board[row][col].flag){
                        System.out.print(AppTexts.MinesSweeper.CORRECT_FLAG);
                    }
                    else if (this.board[row][col].visited){
                        System.out.print(AppTexts.MinesSweeper.EXPLODED_BOMB);
                        bombY = row;
                        bombX = Config.MineSweeper.CELL_CHARACTER_SIZE*col + (Config.MineSweeper.CELL_CHARACTER_SIZE-1)/2;
                    }
                    else {
                        System.out.print(AppTexts.MinesSweeper.HIDDEN_BOMB);
                    }
                }
                else if (this.board[row][col].flag) {
                    System.out.printf(AppTexts.MinesSweeper.INCORRECT_FLAG);
                }
                else if (this.board[row][col].visited){
                    System.out.print(AppTexts.MinesSweeper.REVEALED_CELL_BG(row, col) + AppTexts.MinesSweeper.DIGIT_FORMAT(this.board[row][col].character));
                    col--;
                    continue;
                }
                else{
                    System.out.print(AppTexts.MinesSweeper.COVERED_CELL(row,col));
                    col--;
                    continue;
                }
                col--;
                Utils.sleep(sleepTime);
            }
        }
        Utils.restoreCursor();
        
        return new int[]{bombX, bombY};
    }

    private boolean openCell(int row, int col){
        if (this.board[row][col].flag){
            return true;
        }
        // set up the board IF it's not setup yet
        if (!state.isSetUp){
            state.isSetUp = true;
            // setup board
            setupBoard(row, col);

            // Start GUI on a separate thread
            timer.startTimer();
        }


        // if it's not visited yet
        if (!this.board[row][col].visited){
            this.board[row][col].visited = true;
            state.unrevealedCount--;
            if (checkMine(row, col)){
                return false;
            }
            Utils.saveCursorPosition();
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.topLeftCell_row + row, Config.MineSweeper.Coordinates.topLeftCell_col + col*Config.MineSweeper.CELL_CHARACTER_SIZE);
            System.out.print(AppTexts.MinesSweeper.REVEALED_CELL_BG(row,col) + AppTexts.MinesSweeper.DIGIT_FORMAT(this.board[row][col].character));
            Utils.restoreCursor();

            // if the pixel is '0', so you have to make its neighborhoods to visited
            if (this.board[row][col].character == '0'){
                for (int i = Math.max(row-1,0); i <= Math.min(row+1, state.height-1); i++){
                    for (int j = Math.max(col-1,0); j <= Math.min(col+1, state.width-1); j++){
                        if ((i!=row || j != col) && !this.board[i][j].visited)  openCell(i,j);
                    }
                }
            }

            return true;
        }
        // when you click on a displayed number
        else if (Character.isDigit(this.board[row][col].character)){
            // count the neighborhood flags
            int flag_count = 0;
            for (int i = Math.max(row-1,0); i <= Math.min(row+1, state.height-1); i++){
                for (int j = Math.max(col-1,0); j <= Math.min(col+1, state.width-1); j++){
                    if (this.board[i][j].flag) flag_count++;
                }
            }


            // do the code only if [neighborhood flags == number on the cell]
            if (flag_count == this.board[row][col].character - '0'){
                boolean returnValue = true;
                for (int i = Math.max(row-1,0); i <= Math.min(row+1, state.height-1); i++){
                    for (int j = Math.max(col-1,0); j <= Math.min(col+1, state.width-1); j++){
                        if (!this.board[i][j].visited){
                            if (!openCell(i, j)){
                                returnValue = false;
                            }
                        }
                    }
                }
                return returnValue;
            }else{
                return true;
            }
        }
        return true;
    }

    private void    markFlag(int row, int col){
        if (!this.board[row][col].visited){
            Utils.saveCursorPosition();
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.topLeftCell_row + row, Config.MineSweeper.Coordinates.topLeftCell_col + col*Config.MineSweeper.CELL_CHARACTER_SIZE);

            if (this.board[row][col].flag){
                System.out.print(AppTexts.MinesSweeper.COVERED_CELL(row,col));
                state.flagsCount--;
            }else{
                System.out.print(AppTexts.MinesSweeper.FLAG);
                state.flagsCount++;
            }

            Utils.moveCursorTo(Config.MineSweeper.Coordinates.minesInfoRow, Config.MineSweeper.Coordinates.minesInfoCol);
            Utils.clearLine(0);
            System.out.printf(AppTexts.MinesSweeper.MINES_LEFT_FORMAT, state.mineCount - state.flagsCount);

            this.board[row][col].flag = !this.board[row][col].flag;

            Utils.restoreCursor();
        }
    }

    private void    winAnimation(){
        Utils.saveCursorPosition();
        

        displayBorder(AppTexts.MinesSweeper.WIN_BORDER_COLOR, 2000);



        for (int i = 0; i < 20; i++){
            int last = i;
            Utils.moveCursorTo(Config.MineSweeper.Coordinates.border_topLeftCorner_row, Config.MineSweeper.Coordinates.box_topLeftCorner_col);
            System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR) + AppTexts.MinesSweeper.GRID_TOP_LEFT_CORNER);
            for (int j = 0; j < state.width*Config.MineSweeper.CELL_CHARACTER_SIZE; j++){
                System.out.print(((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_HORIZONTAL_LINE));
            }
            System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_TOP_RIGHT_CORNER);

            for (int j = 0; j < state.height; j++){
                Utils.moveCursorDown(1);
                Utils.moveCursorBack(1);
                System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_VERTICAL_BORDER);
            }

            Utils.moveCursorDown(1);
            Utils.moveCursorBack(1);
            System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_BOTTOM_RIGHT_CORNER);
            Utils.moveCursorBack(1);

            for (int j = 0; j < Config.MineSweeper.CELL_CHARACTER_SIZE* state.width; j++){
                Utils.moveCursorBack(1);
                System.out.print(((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_HORIZONTAL_LINE));
                Utils.moveCursorBack(1);
            }
            Utils.moveCursorBack(1);
            System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_BOTTOM_LEFT_CORNER);
            Utils.moveCursorBack(1);

            for (int j = 0; j < state.height; j++){
                Utils.moveCursorUp(1);
                System.out.print((((last++)%3==0)?AppTexts.MinesSweeper.WIN_BORDER_COLOR :AppTexts.MinesSweeper.NORMAL_BORDER_COLOR)+AppTexts.MinesSweeper.GRID_VERTICAL_BORDER);
                Utils.moveCursorBack(1);
            }

            Utils.sleep(300);
        }

        Utils.restoreCursor();
        

        displayBorder(AppTexts.MinesSweeper.WIN_BORDER_COLOR, 0);
    }

    private void    looseAnimation(int[] bombCoordinates){
        displayBorder(AppTexts.MinesSweeper.LOOSE_BORDER_COLOR, 1500);
        

        int explosionHeight     = state.height;
        int explosionWidth      = state.width * Config.MineSweeper.CELL_CHARACTER_SIZE;
        int topLeftCorner_row   = Config.MineSweeper.Coordinates.topLeftCell_row;
        int topLeftCorner_col   = Config.MineSweeper.Coordinates.topLeftCell_col;
        Utils.explosion(explosionHeight, explosionWidth, topLeftCorner_row, topLeftCorner_col, bombCoordinates);
        
    }
}
