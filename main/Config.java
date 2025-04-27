/**
 * Central configuration class containing constants and settings
 * shared across different parts of the application.
 */
public class Config {
    /**
     * Configuration settings specific to the MineSweeper game.
     */
    public static class MineSweeper {
        public static final int MIN_ROW   =  1;
        public static final int MAX_ROW   = 20;
        public static final int MIN_COL   =  8;
        public static final int MAX_COL   = 35;
        public static final int MIN_MINES =  0;

        // level configs
        public static final int BEGINNER_WIDTH      =  9;
        public static final int BEGINNER_HEIGHT     =  9;
        public static final int BEGINNER_MINES      = 10;

        public static final int INTERMEDIATE_WIDTH  = 16;
        public static final int INTERMEDIATE_HEIGHT = 16;
        public static final int INTERMEDIATE_MINES  = 40;

        public static final int EXPERT_WIDTH        = 30;
        public static final int EXPERT_HEIGHT       = 16;
        public static final int EXPERT_MINES        = 99;

        /**
         * Calculates the maximum number of mines that can be placed on the board
         * while leaving at least one cell free.
         *
         * @param height the number of rows in the board
         * @param width the number of columns in the board
         * @return the maximum number of mines (total cells - 1)
         */
        public static int MAX_MINES(int height, int width) { return height*width-1;}

        public static int CELL_CHARACTER_SIZE = 3;
        public static class Coordinates {
            // TEXTS COORDINATES
            public static final int WAIT_FOR_ENTER_LABEL_ROW = 9999;
            public static final int WAIT_FOR_ENTER_LABEL_COL = 1;

            // Menu texts coordinates (launchGame function)
            public static final int GAME_TITLE_ROW = 1;
            public static final int WELCOME_MESSAGE_ROW =  GAME_TITLE_ROW + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.GAME_TITLE);
            public static final int USER_STATUS_ROW = WELCOME_MESSAGE_ROW + Utils.getLinesCount(AppTexts.MinesSweeper.WELCOME_MESSAGE);
            public static final int MENU_SELECT_DIFFICULTY_ROW = USER_STATUS_ROW + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.USER_STATUS_FORMAT);
            public static final int WRONG_OPTION_ERROR_ROW = MENU_SELECT_DIFFICULTY_ROW + Utils.getLinesCount(AppTexts.MinesSweeper.MENU_SELECT_DIFFICULTY);
            public static final int MENU_OPTIONS_ROW = WRONG_OPTION_ERROR_ROW + Utils.getLinesCount(AppTexts.MinesSweeper.MENU_SELECT_DIFFICULTY);

            public static final int GAME_TITLE_COL = 1;
            public static final int WELCOME_MESSAGE_COL = 1;
            public static final int USER_STATUS_COL = 1;
            public static final int MENU_SELECT_DIFFICULTY_COL = 1;
            public static final int WRONG_OPTION_ERROR_COL = 1;
            public static final int MENU_OPTIONS_COL = 1;

            // custom game menu text coordinates
            public static final int WRONG_INPUT_ERROR_ROW = GAME_TITLE_ROW + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.GAME_TITLE);
            public static final int INPUT_HEIGHT_ROW = WRONG_INPUT_ERROR_ROW +  1 + Utils.getLinesCount(AppTexts.MinesSweeper.WRONG_INPUT_ERROR);
            public static final int INPUT_WIDTH_ROW =  INPUT_HEIGHT_ROW +  1 + Utils.getLinesCount(AppTexts.MinesSweeper.EnterRow);
            public static final int INPUT_MINES_ROW =  INPUT_WIDTH_ROW +  1 + Utils.getLinesCount(AppTexts.MinesSweeper.EnterColumn);

            public static final int WRONG_INPUT_ERROR_COL = 1;
            public static final int INPUT_HEIGHT_COL = 1;
            public static final int INPUT_WIDTH_COL =  1;
            public static final int INPUT_MINES_COL =  1;

            // gameplay texts coordinates
            public static final int LOADING_ANIMATION_ROW = GAME_TITLE_ROW + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.GAME_TITLE);
            public static final int LOADING_ANIMATION_COL = 1;

            public static final int LEVEL_INFO_ROW = GAME_TITLE_ROW + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.GAME_TITLE);
            public static final int TIME_ROW = LEVEL_INFO_ROW     + Utils.getLinesCount(AppTexts.MinesSweeper.LEVEL_INFO_FORMAT_FORMAT);
            public static final int minesInfoRow   = TIME_ROW + Utils.getLinesCount(AppTexts.MinesSweeper.TIME_FORMAT);
            public static final int getInputRow    = minesInfoRow   + 1 + Utils.getLinesCount(AppTexts.MinesSweeper.MINES_LEFT_FORMAT);
            public static final int errorLineRow   = getInputRow        + Utils.getLinesCount(AppTexts.MinesSweeper.INPUT_PROMPT);

            public static final int LEVEL_INFO_COL = 1;
            public static final int TIME_COL = 1;
            public static final int minesInfoCol   = 1;
            public static final int getInputCol    = 1;
            public static final int errorLineCol   = 1;




            // BOARD COORDINATES
            public static final int EMOJI_ROW                 = errorLineRow + 1;
            public static final int columnNumbers_row         = EMOJI_ROW + 1;
            public static final int border_topLeftCorner_row = columnNumbers_row + 1;
            public static final int rowNumbers_row            = border_topLeftCorner_row + 1;
            public static final int topLeftCell_row           = border_topLeftCorner_row + 1;

            //public static final int EMOJI_COL  -> Measured during Runtime.
            public static final int rowNumbers_col            = 1;
            public static final int box_topLeftCorner_col     = rowNumbers_col + 3;
            public static final int columnNumbers_col         = box_topLeftCorner_col + 1;
            public static final int topLeftCell_col           = box_topLeftCorner_col + 1;
        }
    }

    /**
     * Configuration settings specific to the Player.
     * */
    public static class Player {
        public static GameEntry[] gamesList = {
                new GameEntry("MinesSweeper", MinesSweeper::launchGame),
//                new GameEntry("AnotherGame",  AnotherGame::launchGame),
        };


        public static class Coordinates {
            public static final int SELECT_GAME_MENU_ROW = 1;
            public static final int INVALID_INPUT_ROW = SELECT_GAME_MENU_ROW + Utils.getLinesCount(AppTexts.Player.SELECT_GAME_MENU);
            public static final int MENU_OPTIONS_ROW = INVALID_INPUT_ROW + Utils.getLinesCount(AppTexts.Player.INVALID_INPUT) + 1;



            public static final int SELECT_GAME_MENU_COL = 1;
            public static final int INVALID_INPUT_COL = 1;
            public static final int MENU_OPTIONS_COL = 1;
        }
    }
}