public class AppTexts {
    /**
     * Class for handling ANSI color codes using RGB values.
     * Supports foreground and background modes, with common color presets.
     */
    public static final class Colors{
        /**
         * Enum representing the two modes for text color manipulation.
         * - `FG` refers to the foreground color (text color).
         * - `BG` refers to the background color.
         */
        public enum Mode{
            FG, BG
        }
        
        /**
         * Generates an ANSI escape code for setting text or background color using RGB values.
         *
         * @param r     the red component (0–255)
         * @param g     the green component (0–255)
         * @param b     the blue component (0–255)
         * @param mode  the mode specifying foreground (FG) or background (BG) color
         * @return      the ANSI escape code string for the specified color and mode
         */
        public static String rgb(int r, int g, int b, Mode mode){
            return String.format("\033[%d;2;%d;%d;%dm", (mode == Colors.Mode.BG ? 48 : 38), r, g, b);
        }

        public static final String REVERSE_COLOR = "\033[7m";

        public static final String BLACK_BG  = Colors.rgb(0  , 0  , 0  , Mode.BG);
        public static final String YELLOW_BG = Colors.rgb(255, 255, 0  , Mode.BG);
        public static final String RED_BG    = Colors.rgb(255, 0  , 0  , Mode.BG);
        public static final String GREEN_BG  = Colors.rgb(0  , 255, 0  , Mode.BG);
        public static final String ORANGE_BG = Colors.rgb(252, 127, 0  , Mode.BG);

        public static final String BLACK_FG  = Colors.rgb(0  , 0  , 0  , Mode.FG);
        public static final String WHITE_FG  = Colors.rgb(255, 255, 255, Mode.FG);
        public static final String BLUE_FG   = Colors.rgb(0  , 0  , 255, Mode.FG);
        public static final String RED_FG    = Colors.rgb(255, 0  , 0  , Mode.FG);

        public static final String RESET_COLOR = "\033[0m";
    }

    /**
     * Stores constant strings for the Minesweeper game.
     */
    public static final class MinesSweeper {
        public static final String   WAIT_FOR_ENTER_LABEL   =  Colors.REVERSE_COLOR + ">> Press Enter to continue..." + Colors.RESET_COLOR;

        // Game Title and Intro Message
        public static final String   GAME_TITLE_COLOR       = Colors.rgb(22, 161, 119, Colors.Mode.FG);
        public static final String   GAME_TITLE             = "   __  ____              ____                           \n" +
                                                              "  /  |/  (_)__  ___ ___ / __/    _____ ___ ___  ___ ____\n" +
                                                              " / /|_/ / / _ \\/ -_|_-<_\\ \\| |/|/ / -_) -_) _ \\/ -_) __/\n" +
                                                              "/_/  /_/_/_//_/\\__/___/___/|__,__/\\__/\\__/ .__/\\__/_/   \n" +
                                                              "                                        /_/             ";
        public static final String   WELCOME_MESSAGE        = Colors.rgb(255, 0  , 0  , Colors.Mode.FG) + "Welcome to MineSweeper game!" + Colors.RESET_COLOR;
        public static final String   MENU_SELECT_DIFFICULTY = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Select an option to start the game. (Enter 'q' ro quit): " + Colors.RESET_COLOR;
        public static final String[] MENU_OPTIONS           = {"Beginner", "Intermediate", "Expert", "Custom"};
        public static final String   MENU_OPTION_FORMAT     = Colors.rgb(255, 69, 0, Colors.Mode.FG) + "%d. " + Colors.rgb(120, 81, 169, Colors.Mode.FG) + "%s" + Colors.RESET_COLOR;

        // User Stats Format
        public static final String   USER_STATUS_COLOR      = Colors.rgb(255, 191, 0, Colors.Mode.FG);
        public static final String   USER_STATUS_FORMAT     =
                "╔═══════════════════════════════╗\n" +
                "║         PLAYER STATS          ║\n" +
                "╠═══════════════════════════════╣\n" +
                "║ Games Played   :%7d       ║\n" +
                "║ Games Won      :%7d       ║\n" +
                "║ Games Lost     :%7d       ║\n" +
                "║ Games Quit     :%7d       ║\n" +
                "║                               ║\n" +
                "║ Total Time Played:%4dm %2ds   ║\n" +
                "╚═══════════════════════════════╝";

        // Input Prompts
        public static final String   EnterRow                 = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Enter number of rows (Minimum %d, Maximum %d) or 'q' to quit: "    + Colors.RESET_COLOR;
        public static final String   EnterColumn              = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Enter number of columns (Minimum %d, Maximum %d) or 'q' to quit: " + Colors.RESET_COLOR;
        public static final String   EnterMines               = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Enter number of mines (Minimum %d, Maximum %d) or 'q' to quit: "   + Colors.RESET_COLOR;
        public static final String   WRONG_INPUT_ERROR        = Colors.WHITE_FG  +  Colors.RED_BG +                    "That doesn’t look like a number. Try again or type 'q' to quit."   + Colors.RESET_COLOR;

        // Game Time and Mines Left Format
        public static final String   LEVEL_INFO_FORMAT_FORMAT = Colors.rgb(80 , 255, 200, Colors.Mode.FG) + "Level: "       + Colors.rgb(255, 100, 100, Colors.Mode.FG) + "%s"           + Colors.RESET_COLOR;
        public static final String   TIME_FORMAT              = Colors.rgb(80 , 255, 200, Colors.Mode.FG) + "Time: "        + Colors.rgb(255, 100, 100, Colors.Mode.FG) + "%d:%02d:%02d" + Colors.RESET_COLOR;
        public static final String   MINES_LEFT_FORMAT        = Colors.rgb(80 , 255, 200, Colors.Mode.FG) + "Mines left: "  + Colors.rgb(255, 100, 100, Colors.Mode.FG) + "%d"           + Colors.RESET_COLOR;

        // Input Prompt
        public static final String   INPUT_PROMPT             = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Press 'q' to quit or 'h' for help.\nEnter your move (row col): " + Colors.RESET_COLOR;

        // Error Messages
        public static final String   WRONG_OPTION_ERROR       = Colors.WHITE_FG + Colors.RED_BG + "Oops! Invalid option." + Colors.RESET_COLOR;
        public static final String   INVALID_INPUT_FORMAT     = Colors.WHITE_FG + Colors.RED_BG + "Invalid input format!" + Colors.RESET_COLOR;
        public static final String   ROW_OUT_OF_RANGE         = Colors.WHITE_FG + Colors.RED_BG + "Row out of range!"     + Colors.RESET_COLOR;
        public static final String   COL_OUT_OF_RANGE         = Colors.WHITE_FG + Colors.RED_BG + "Column out of range!"  + Colors.RESET_COLOR;


        // Helper Information
        private static final String  command_color            = Colors.rgb(66,66,66,Colors.Mode.BG);
        private static final String  title_color              = Colors.rgb(188,44,104,Colors.Mode.FG);
        private static final String  subtitle_color           = Colors.rgb(207,133,13,Colors.Mode.FG);
        public  static final String  HELPER                   =
                Colors.rgb(22, 161, 119, Colors.Mode.FG) + "Minesweeper Guide\n\n" + Colors.RESET_COLOR +
                title_color + "Game Mechanics:\n" + Colors.RESET_COLOR +
                "\t" + subtitle_color + "Bombs" + Colors.RESET_COLOR + ": The grid has hidden bombs. Opening a cell with a bomb ends the game.\n\n" +
                "\t" + subtitle_color + "Numbers" + Colors.RESET_COLOR + ": Each opened cell shows a number (0-8),\n\t\tindicating how many bombs are in adjacent cells (including diagonals).\n\t\tAn empty cell represents 0.\n\n" +
                "\t" + subtitle_color + "Flagging" + Colors.RESET_COLOR + ": If you suspect a bomb in a cell, flag it to avoid opening it by mistake.\n\n" +
                title_color + "Commands:\n" + Colors.RESET_COLOR +
                "\t" + subtitle_color + "Flagging/Unflagging a Cell" + Colors.RESET_COLOR + ":\n" +
                "\t\t" + command_color + "f row col" + Colors.RESET_COLOR + "\n\t\tExample: f 2 3 flags the cell at row 2, column 3.\n\t\tUse the same command to unflagging a cell.\n\n" +
                "\t" + subtitle_color + "Opening a Cell" + Colors.RESET_COLOR + ":\n" +
                "\t\t" + command_color + "v row col" + Colors.RESET_COLOR + " or " + command_color + "row col" + Colors.RESET_COLOR + "\n\t\tExample: " + command_color + "2 3" + Colors.RESET_COLOR + " opens the cell at row 2, column 3.\n\n" +
                "\t" + subtitle_color + "Quit the Game" + Colors.RESET_COLOR + ":\n" +
                "\t\tTo quit the game, enter the command:\n\t\t" + command_color + "q"  + Colors.RESET_COLOR +  "\n\n" +
                title_color + "Special Rules:\n" + Colors.RESET_COLOR +
                "\t" + subtitle_color + "Recursive Opening" + Colors.RESET_COLOR + ":\n\t\tOpening a \"0\" cell automatically opens adjacent cells, continuing for other \"0\" cells.\n\n" +
                "\t" + subtitle_color + "Winning" + Colors.RESET_COLOR + ": You win the game when you have opened all non-bomb cells.\n\n" +
                "\t" + subtitle_color + "Losing" + Colors.RESET_COLOR + ": The game ends if you open a cell with a bomb.\n\n" +
                title_color + "Bonus Command:\n" + Colors.RESET_COLOR +
                "\t" + subtitle_color + "Auto-Open Adjacent Cells" + Colors.RESET_COLOR + ": If all surrounding mines are flagged,\n\t\topening a numbered cell automatically reveals adjacent safe cells.\n\t\tThis helps clear safe zones quickly!";

        // Border and Grid Characters
        public static final String COLUMN_INDEX_FORMAT      = "%2d";
        public static final String ROW_INDEX_FORMAT         = "%2d";
        public static final String GRID_VERTICAL_BORDER     = '║' + Colors.RESET_COLOR;
        public static final String GRID_HORIZONTAL_LINE     = '═' + Colors.RESET_COLOR;
        public static final String GRID_TOP_LEFT_CORNER     = '╔' + Colors.RESET_COLOR;
        public static final String GRID_TOP_RIGHT_CORNER    = '╗' + Colors.RESET_COLOR;
        public static final String GRID_BOTTOM_LEFT_CORNER  = '╚' + Colors.RESET_COLOR;
        public static final String GRID_BOTTOM_RIGHT_CORNER = '╝' + Colors.RESET_COLOR;
        public static final String NORMAL_BORDER_COLOR      = Colors.rgb(24,80,200, Colors.Mode.FG);
        public static final String WIN_BORDER_COLOR         = Colors.rgb(80,226,90, Colors.Mode.FG);
        public static final String LOOSE_BORDER_COLOR       = Colors.rgb(250,27,27, Colors.Mode.FG);
        public static final String CORRECT_FLAG             = Colors.BLACK_FG + Colors.GREEN_BG + " F " + Colors.RESET_COLOR;
        public static final String INCORRECT_FLAG           = Colors.BLACK_FG + Colors.RED_BG + " F " + Colors.RESET_COLOR;


        // Inside the Grid
        public static final String FLAG                     = Colors.RED_FG + Colors.ORANGE_BG + " F " + Colors.RESET_COLOR;
        /**
         * Returns the ANSI-colored string representation of a covered cell based on its position.
         * Applies a checkerboard-like pattern by varying background colors depending on the row and column parity.
         *
         * @param row the row index of the cell
         * @param col the column index of the cell
         * @return a string representing the cell with a specific background color
         */
        public static       String COVERED_CELL             (int row, int col){
            if (row%2 == 1 && col%2 == 1){
                return Colors.rgb(90,90,120, Colors.Mode.BG) + " - " + Colors.RESET_COLOR;
            }else if (col%2 == 0 && row%2 == 0) {
                return Colors.rgb(30,30,50,Colors.Mode.BG) + " - " + Colors.RESET_COLOR;
            }else{
                return Colors.rgb(60,60,90,Colors.Mode.BG) + " - " + Colors.RESET_COLOR;
            }

        }
        /**
         * Returns the ANSI background color code for a revealed cell based on its position.
         * Creates a subtle checkerboard pattern using different shades of dark gray and black.
         *
         * @param row the row index of the cell
         * @param col the column index of the cell
         * @return the ANSI escape code string for the background color
         */
        public static       String REVEALED_CELL_BG         (int row, int col){
            if (row%2 == 1 && col%2 == 1){
                return Colors.rgb(30,30,30,Colors.Mode.BG);
            }else if (col%2 == 0 && row%2 == 0) {
                return Colors.BLACK_BG;
            }else{
                return Colors.rgb(17,17,17,Colors.Mode.BG);
            }
        }
        /**
         * Formats a digit character with its corresponding ANSI foreground color code.
         * This is used to visually distinguish numbers in a terminal-based Minesweeper-style grid.
         *
         * @param c the digit character (e.g., '1' to '8', or '0' for empty)
         * @return a formatted string with color applied, or empty string for invalid characters
         */
        public static       String DIGIT_FORMAT             (char c) {
            return switch (c) {
                case '0' -> "   ";
                case '1' -> Colors.BLUE_FG + " 1 " + Colors.RESET_COLOR;
                case '2' -> Colors.rgb(0, 123, 0, Colors.Mode.FG) + " 2 " + Colors.RESET_COLOR;
                case '3' -> Colors.RED_FG + " 3 " + Colors.RESET_COLOR;
                case '4' -> Colors.rgb(0, 0, 123, Colors.Mode.FG) + " 4 " + Colors.RESET_COLOR;
                case '5' -> Colors.rgb(123, 0, 0, Colors.Mode.FG) + " 5 " + Colors.RESET_COLOR;
                case '6' -> Colors.rgb(0, 123, 123, Colors.Mode.FG) + " 6 " + Colors.RESET_COLOR;
                case '7' -> Colors.BLACK_FG + " 7 " + Colors.RESET_COLOR;
                case '8' -> Colors.rgb(123, 123, 123, Colors.Mode.FG) + " 8 " + Colors.RESET_COLOR;
                default -> "";
            };
        }
        public static final String EXPLODED_BOMB            = Colors.BLACK_FG + Colors.RED_BG + " X " + Colors.RESET_COLOR;
        public static final String HIDDEN_BOMB              = Colors.RED_FG + Colors.rgb(20,10,10,Colors.Mode.BG) + " * " + Colors.RESET_COLOR;


        // Status Emojis
        public static final String NORMAL_STATUS            =  Colors.YELLOW_BG + Colors.BLACK_FG + "(^w^)"   + Colors.RESET_COLOR;
        public static final String PROCESSING_STATUS        =  Colors.YELLOW_BG + Colors.BLACK_FG + "(\"O\")" + Colors.RESET_COLOR;
        public static final String WIN_STATUS               =  Colors.YELLOW_BG + Colors.BLACK_FG + "(⌐■_■)"  + Colors.RESET_COLOR;
        public static final String LOST_STATUS              =  /*  "(x_x)"  */ Colors.YELLOW_BG + Colors.BLACK_FG + "(" + Colors.RED_FG + "x" + Colors.BLACK_FG + "_" + Colors.RED_FG + "x" + Colors.BLACK_FG + ")"   + Colors.RESET_COLOR;
    }

    /**
     * Stores constant UI strings used for the player-related messages.
     */
    public static final class Player {
        public static final String SELECT_GAME_MENU   = Colors.rgb(100, 149, 237, Colors.Mode.FG) + "Choose a game to play: " + Colors.RESET_COLOR;
        public static final String MENU_OPTION_FORMAT = Colors.rgb(255, 69, 0, Colors.Mode.FG) + "%d. " + Colors.rgb(255, 165, 0, Colors.Mode.FG)+ "%s" + Colors.RESET_COLOR;
        public static final String INVALID_INPUT      = Colors.WHITE_FG + Colors.RED_BG + "Oops! That input isn't valid. Please try again." + Colors.RESET_COLOR;
    }

    /**
     * Stores constant strings used across utility functions.
     */
    public static final class Utils{
        /**
         * Stores constant strings used by the loadingAnimation function in Utils.
         */
        public static final class loadingAnimation{
            private static final String BORDER_COLOR        = Colors.rgb(0,255,255,Colors.Mode.FG);
            public  static final String TOP_LEFT_CORNER     = BORDER_COLOR + "┌" + Colors.RESET_COLOR;
            public  static final String TOP_RIGHT_CORNER    = BORDER_COLOR + "┐" + Colors.RESET_COLOR;
            public  static final String BOTTOM_LEFT_CORNER  = BORDER_COLOR + "└" + Colors.RESET_COLOR;
            public  static final String BOTTOM_RIGHT_CORNER = BORDER_COLOR + "┘" + Colors.RESET_COLOR;
            public  static final String HORIZONTAL_BORDER   = BORDER_COLOR + "─" + Colors.RESET_COLOR;
            public  static final String VERTICAL_BORDER     = BORDER_COLOR + "│" + Colors.RESET_COLOR;
            public  static final String PROGRESS            = Colors.rgb(200, 0, 200, Colors.Mode.FG) + "█" + Colors.RESET_COLOR;
            public  static final String PERCENT             = Colors.rgb(0, 150, 150, Colors.Mode.FG) + "%" + Colors.RESET_COLOR;
        }

        /**
         * Stores constant strings used by the explosion function in Utils.
         */
        public static final class explosion{
            public  static final String   CURSOR_FORWARD       = "\033[1C";
            public  static final String[] EXPLOSION_CORE_CHARS = {
                    Colors.rgb(255, 255, 255, Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 1 - white
                    Colors.rgb(255, 245, 200, Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 2 - pale yellow
                    Colors.rgb(255, 230, 160, Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 3 - creamy yellow
                    Colors.rgb(255, 207, 14 , Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 4 - bright core orange
                    Colors.rgb(255, 177, 102, Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 5 - soft orange
                    Colors.rgb(255, 140, 40 , Colors.Mode.FG) + "@" + Colors.RESET_COLOR, // 6 - deeper orange
                    Colors.rgb(255, 100, 20 , Colors.Mode.FG) + "H" + Colors.RESET_COLOR, // 7 - orange-red heat
                    Colors.rgb(230, 60 , 0  , Colors.Mode.FG) + "H" + Colors.RESET_COLOR, // 8 - intense red-orange
                    Colors.rgb(200, 20 , 0  , Colors.Mode.FG) + "H" + Colors.RESET_COLOR, // 9 - red
                    Colors.rgb(140, 0  , 0  , Colors.Mode.FG) + "!" + Colors.RESET_COLOR, //10 - dark red
                    Colors.rgb(90 , 0  , 0  , Colors.Mode.FG) + "!" + Colors.RESET_COLOR, //11 - ember red
                    Colors.rgb(40 , 0  , 0  , Colors.Mode.FG) + "." + Colors.RESET_COLOR, //12 - fading edge
                    CURSOR_FORWARD};
            public  static final String[] EXPLOSION_WAVE_CHARS = {
                    // Outer Wave
                    Colors.rgb(0  , 0  , 0  , Colors.Mode.FG) + " " + Colors.RESET_COLOR,
                    Colors.rgb(60 , 0  , 20 , Colors.Mode.FG) + "." + Colors.RESET_COLOR,
                    Colors.rgb(90 , 0  , 40 , Colors.Mode.FG) + ":" + Colors.RESET_COLOR,
                    Colors.rgb(120, 0  , 60 , Colors.Mode.FG) + "!" + Colors.RESET_COLOR,
                    Colors.rgb(160, 0  , 80 , Colors.Mode.FG) + "H" + Colors.RESET_COLOR,
                    Colors.rgb(190, 0  , 110, Colors.Mode.FG) + "I" + Colors.RESET_COLOR,
                    Colors.rgb(180, 0  , 170, Colors.Mode.FG) + "M" + Colors.RESET_COLOR,
                    Colors.rgb(140, 0  , 190, Colors.Mode.FG) + "W" + Colors.RESET_COLOR,
                    Colors.rgb(120, 0  , 200, Colors.Mode.FG) + "#" + Colors.RESET_COLOR,
                    Colors.rgb(100, 0  , 200, Colors.Mode.FG) + "O" + Colors.RESET_COLOR,

                    // Middle part
                    Colors.rgb(80 , 0  , 180, Colors.Mode.FG) + "+" + Colors.RESET_COLOR,
                    Colors.rgb(60 , 0  , 160, Colors.Mode.FG) + "." + Colors.RESET_COLOR,
                    Colors.rgb(80 , 0  , 180, Colors.Mode.FG) + "+" + Colors.RESET_COLOR,

                    // Inner wave
                    Colors.rgb(0  , 0  , 200, Colors.Mode.FG) + "%" + Colors.RESET_COLOR,
                    Colors.rgb(0  , 80 , 200, Colors.Mode.FG) + "$" + Colors.RESET_COLOR,
                    Colors.rgb(0  , 140, 220, Colors.Mode.FG) + "&" + Colors.RESET_COLOR,
                    Colors.rgb(0  , 200, 255, Colors.Mode.FG) + "@" + Colors.RESET_COLOR,
                    Colors.rgb(80 , 220, 255, Colors.Mode.FG) + "0" + Colors.RESET_COLOR,
                    Colors.rgb(140, 240, 255, Colors.Mode.FG) + "8" + Colors.RESET_COLOR,
                    Colors.rgb(180, 255, 255, Colors.Mode.FG) + "O" + Colors.RESET_COLOR,
                    Colors.rgb(200, 255, 255, Colors.Mode.FG) + "=" + Colors.RESET_COLOR,
                    Colors.rgb(220, 255, 255, Colors.Mode.FG) + "+" + Colors.RESET_COLOR,
                    Colors.rgb(240, 255, 255, Colors.Mode.FG) + "-" + Colors.RESET_COLOR
            };

            public static final String[] SPACE_BLOB_CHARS = {
                    Colors.rgb(255, 170, 170, Colors.Mode.FG) + "." + Colors.RESET_COLOR,
                    Colors.rgb(100, 120, 255, Colors.Mode.FG) + "o" + Colors.RESET_COLOR,
                    Colors.rgb(100,  50, 255, Colors.Mode.FG) + "@" + Colors.RESET_COLOR
            };
        }
    }
}
