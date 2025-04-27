import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class Utils {

    // ======== GENERAL UTILITIES ========
    /**
     * Puts the current thread to sleep for the specified duration.
     *
     * @param sleepTimeMillis the time in milliseconds to sleep
     */
    public static void sleep(long sleepTimeMillis){
        try {
            Thread.sleep(sleepTimeMillis);
        } catch (InterruptedException _) {}
    }

    private static final Random rand = new Random(); // Random instance to generate random numbers

    /**
     * Generates a random double value between the given range [a, b).
     *
     * @param a the lower bound (inclusive)
     * @param b the upper bound (exclusive)
     * @return a random double value between a and b
     */
    public static double getRandomDouble(double a, double b) {
        return rand.nextDouble() * (b-a) + a;
    }

    /**
     * Returns the number of lines in a given string, where lines are separated by newline characters ("\n").
     *
     * @param str the input string to count lines in
     * @return the number of lines in the string
     */
    public static int getLinesCount(String str){
        return str.split("\n").length;
    }

    /**
     * Checks if the given integer is within the specified range.
     *
     * @param num the number to check
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @return true if the number is within the range, false otherwise
     */
    public static boolean isInRange(int num, int min, int max){
        return num >= min && num <= max;
    }

    /**
     * Checks if the given string represents a valid integer within the specified range.
     *
     * @param str the string to check
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @return true if the string represents an integer within the range, false otherwise
     */
    public static boolean isInRange(String str, int min, int max){
        if (!str.matches("^\\d+$")) return false;

        return isInRange(Integer.parseInt(str), min, max);
    }

    /**
     * Forces the given integer to be within the specified range.
     * If the number is less than the minimum, it returns the minimum.
     * If the number is greater than the maximum, it returns the maximum.
     * Otherwise, it returns the number itself.
     *
     * @param num the number to check
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the number adjusted to the range [min, max]
     */
    public static int     forceInRange(int num, int min, int max){
        return Math.max(Math.min(num, max), min);
    }

    /**
     * Forces the integer value represented by the given string to be within the specified range.
     * If the string represents a number that is less than the minimum, it returns the minimum.
     * If the string represents a number that is greater than the maximum, it returns the maximum.
     * Otherwise, it returns the parsed number itself.
     *
     * @param num the string representing a number to check
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @return the parsed number adjusted to the range [min, max]
     */
    public static int     forceInRange(String num, int min, int max){
        return forceInRange(Integer.parseInt(num), min, max);
    }


    // ======== INPUT UTILITIES ========
    /**
     * Reads a line of input from the user, showing and hiding the cursor while doing so.
     * This method ensures the cursor is visible when the input is being entered,
     * and hides it again immediately after the input is read.
     *
     * @param sc the scanner object used to read input from the user
     * @return the line of input entered by the user
     */
    public static String inputLine(Scanner sc){
        setCursorColor("#00FF00");
        changeCursorShape(1);
        showCursor();
        String line = sc.nextLine();  // getting the line
        hideCursor();
        changeCursorShape(0);
        resetCursorColor();
        return line;
    }

    /**
     * Reads a line of input from the user and validates it against a given regular expression pattern.
     * If the input matches the pattern, it is returned; otherwise, null is returned.
     *
     * @param sc the scanner object used to read input from the user
     * @param inputPattern the regular expression pattern that the input must match
     * @return the input line if it matches the pattern; otherwise, null
     */
    public static String readValidLine(Scanner sc, Pattern inputPattern){
        String line = inputLine(sc).trim();
        if (inputPattern.matcher(line).matches()) return line;
        return null;
    }


    // ======== PRINTING & DISPLAY UTILITIES ========
    /**
     * Prints a string to the terminal one character at a time, with a delay between each character,
     * creating a "typing" effect.
     *
     * @param s the string to be printed
     * @param sleepTimeMillis the total time in milliseconds to pause between each character print
     */
    public static void slowPrint(String s, int sleepTimeMillis){
        for (char c : s.toCharArray()) {
            System.out.print(c);

            sleep(sleepTimeMillis/s.length());
        }
    }

    /**
     * Prints a multi-line string to the terminal with a slow printing effect, one character at a time
     * per line. The printing process occurs at a specific screen position and color.
     *
     * @param s the string to be printed, with multiple lines separated by '\n'
     * @param color the color code to use for the text (e.g., ANSI color code)
     * @param topLeftCorner_row the row where the top-left corner of the text block starts
     * @param topLeftCorner_col the column where the top-left corner of the text block starts
     * @param sleepTimeMillis the total time in milliseconds to pause between printing each character
     */
    public static void slowPrintMultiLine(String s, String color, int topLeftCorner_row, int topLeftCorner_col, int sleepTimeMillis){
        String[] lines = s.split("\n");
        int maxLength = Integer.MIN_VALUE;
        for (String line : lines) {
            maxLength = Math.max(maxLength, line.length());
        }

        System.out.print(color);
        for (int i = 0; i < maxLength; i++) {
            for (int line_index = 0; line_index < lines.length; line_index++) {
                String line = lines[line_index];
                if (i < line.length()) {
                    moveCursorTo(topLeftCorner_row + line_index, topLeftCorner_col + i);
                    System.out.print(line.charAt(i));
                }
            }
            sleep(sleepTimeMillis/maxLength);

        }
        System.out.print(AppTexts.Colors.RESET_COLOR);
    }


    // ======== ANIMATION UTILITIES ========
    /**
     * Renders a terminal explosion animation using ASCII characters and perspective projection.
     *
     * @param rows            Number of rows in the display area
     * @param cols            Number of columns in the display area
     * @param topLeft_row     Row coordinate for the animation's top-left position
     * @param topLeft_col     Column coordinate for the animation's top-left position
     * @param bombCoordinates Coordinates of the explosion origin (relative to the display area)
     */
    public static void explosion(int rows,int cols, int topLeft_row, int topLeft_col, int[]bombCoordinates) {
        class SpaceBlob {
            final double x, y, z;

            SpaceBlob(double x, double y, double z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }
        }

        final int NUM_FRAMES = 150;
        final int NUM_BLOBS = 800;
        final int PERSPECTIVE = 50;

        int minX = -bombCoordinates[0];
        int maxX = cols + minX - 1;
        int minY = -bombCoordinates[1];
        int maxY = rows + minY - 1;

        // Generating space blobs
        List<SpaceBlob> blobs = new ArrayList<>();
        for (int i = 0; i < NUM_BLOBS; i++) {
            // Generate random positions for each blob in 3D space
            double bx = getRandomDouble(-1, 1);
            double by = getRandomDouble(-1, 1);
            double bz = getRandomDouble(-1, 1);
            double br = Math.sqrt(bx * bx + by * by + bz * bz); // Normalize the blob's position
            blobs.add(new SpaceBlob(
                    (bx / br) * (1.3 + 0.2 * getRandomDouble(-1, 1)),
                    (0.5 * by / br) * (1.3 + 0.2 * getRandomDouble(-1, 1)),
                    (bz / br) * (2 + 0.2 * getRandomDouble(-1, 1))
            ));
        }


        // Generating frames
        List<List<String>> frames = new ArrayList<>();
        for (int i = 0; i < NUM_FRAMES; i++) {
            String[][] frame = new String[rows][cols];

            // Initialize the frame with empty spaces
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    frame[r][c] = " ";
                }
            }

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    int scrX = x - minX;
                    int scrY = y - minY;

                    double nx = x / (double)Math.max(cols, rows) * 100;
                    double ny = y / (double)Math.max(cols, rows) * 100;
                    String str;

                    if (i < 8) {
                        double r = Math.sqrt(nx * nx + 4 * ny * ny);
                        int length = AppTexts.Utils.explosion.EXPLOSION_CORE_CHARS.length;
                        str = AppTexts.Utils.explosion.EXPLOSION_CORE_CHARS[Math.min((int)(r / (i * 2) * 11), length - 1)];
                    } else {
                        double r = Math.sqrt(nx * nx + 4 * ny * ny) * (0.5 + (getRandomDouble(-1, 1) / 3.0) * Math.cos(16 * Math.atan2(2 * ny + 0.01, nx + 0.01)) * 0.3);
                        double v = i - r + 1;
                        if (v >= 0 && v < 25) {
                            int length = AppTexts.Utils.explosion.EXPLOSION_WAVE_CHARS.length;
                            str = AppTexts.Utils.explosion.EXPLOSION_WAVE_CHARS[Math.min((int)v, length - 1)];
                        } else if (v < 0) {
                            str = AppTexts.Utils.explosion.CURSOR_FORWARD;
                        }else continue;
                    }
                    frame[scrY][scrX] = str;
                }
            }

            if (i > 6) {
                int i0 = i - 6;
                for (SpaceBlob blob : blobs) {
                    double bx = blob.x * i0 / Math.max(cols, rows) * 100;
                    double by = blob.y * i0 / Math.max(cols, rows) * 100;
                    double bz = blob.z * i0 / Math.max(cols, rows) * 100;

                    if (bz < 5 - PERSPECTIVE || bz > PERSPECTIVE) continue;

                    int x = (int)(-minX + bx * PERSPECTIVE / (bz + PERSPECTIVE));
                    int y = (int)(-minY + by * PERSPECTIVE / (bz + PERSPECTIVE));

                    if (x >= 0 && x < cols && y >= 0 && y < rows) {
                        if (bz > 40){
                            frame[y][x] = AppTexts.Utils.explosion.SPACE_BLOB_CHARS[0];
                        } else if (bz > -20) {
                            frame[y][x] = AppTexts.Utils.explosion.SPACE_BLOB_CHARS[1];
                        }else{
                            frame[y][x] = AppTexts.Utils.explosion.SPACE_BLOB_CHARS[2];
                        }
                    }
                }
            }

            List<String> frameList = new ArrayList<>();
            for (String[] row : frame) {
                frameList.add(String.join("", row));
            }
            frames.add(frameList);
            boolean allEmpty = frameList.stream().allMatch(s -> s.replace("\033[1C", " ").trim().isEmpty());
            if (allEmpty) {
                break;
            }

        }

        double delay = 0.5;
        // printing the animation
        for (List<String> strings : frames) {
            for (int line = 0; line < strings.size(); line++) {
                System.out.printf("\033[%d;%dH", topLeft_row + line, topLeft_col);
                System.out.print(strings.get(line));
            }
            sleep((long) (delay * 1000L));
            delay = 0.02;
        }
        sleep(500);

    }

    /**
     * Displays a loading bar animation at a given screen location with a specified width and duration.
     *
     * @param topLeftCorner_row row coordinate for the top-left of the loading box
     * @param topLeftCorner_col column coordinate for the top-left of the loading box
     * @param width width of the loading bar in characters
     * @param totalSleepTimeMiles total duration of the animation in milliseconds
     */
    public static void loadingAnimation(int topLeftCorner_row, int topLeftCorner_col,int width, long totalSleepTimeMiles) {
        saveCursorPosition();
        int totalSteps = 100;  // Total steps to completion

        moveCursorTo(topLeftCorner_row, topLeftCorner_col);
        System.out.print(AppTexts.Utils.loadingAnimation.TOP_LEFT_CORNER);
        System.out.print(AppTexts.Utils.loadingAnimation.HORIZONTAL_BORDER.repeat(width));
        System.out.print(AppTexts.Utils.loadingAnimation.TOP_RIGHT_CORNER);


        moveCursorTo(topLeftCorner_row+2, topLeftCorner_col);
        System.out.print(AppTexts.Utils.loadingAnimation.BOTTOM_LEFT_CORNER);
        System.out.print(AppTexts.Utils.loadingAnimation.HORIZONTAL_BORDER.repeat(width));
        System.out.print(AppTexts.Utils.loadingAnimation.BOTTOM_RIGHT_CORNER);


        moveCursorTo(topLeftCorner_row+1, topLeftCorner_col);
        System.out.print(AppTexts.Utils.loadingAnimation.VERTICAL_BORDER);  // Left border
        moveCursorTo(topLeftCorner_row+1, topLeftCorner_col+width+1);
        System.out.print(AppTexts.Utils.loadingAnimation.VERTICAL_BORDER);  // Right border

        int lastProgress = 0;
        for (int i = 0; i <= totalSteps; i++) {
            // Calculate progress
            int progress = i * width / totalSteps;

            // Print the loading bar
            moveCursorTo(topLeftCorner_row+1, topLeftCorner_col+1+lastProgress);
            System.out.print(AppTexts.Utils.loadingAnimation.PROGRESS.repeat(progress-lastProgress));  // Filled portion

            moveCursorTo(topLeftCorner_row+1, topLeftCorner_col+width+3);
            System.out.print((int)((double) i/totalSteps*100) + AppTexts.Utils.loadingAnimation.PERCENT);  // Right border and percentage

            sleep(totalSleepTimeMiles/totalSteps);  // Pause for visual effect
        }
        restoreCursor();
    }


    // ======== ANSI CODE UTILITIES SECTION ========
    /**
     * Erases part or all of the current line in the terminal using ANSI escape codes.
     * Modes:
     *   0 - Clear from cursor to end of the line.
     *   1 - Clear from cursor to beginning of the line.
     *   2 - Clear the entire line.
     * The cursor position remains unchanged.
     *
     * @param mode the erase mode (0–2)
     */
    public static void clearLine(int mode){ System.out.printf("\033[%dK", mode);}

    /**
     * Clears a portion or all of the terminal screen using ANSI escape codes.
     * Modes:
     *   0 - Clear from cursor to end of screen.
     *   1 - Clear from cursor to beginning of screen.
     *   2 - Clear entire screen and move cursor to top-left.
     *   3 - Clear entire screen and delete scrollback buffer (xterm+ only).
     *
     * @param mode The clear mode (0–3).
     */
    public static void clearScreen(int mode) {
        // Ensure cursor moves to (1,1) only for full screen clear
        if (mode == 2){
            moveCursorTo(1,1);
        }
        System.out.printf("\033[%dJ", mode);
        System.out.flush();
    }

    /**
     * Moves the cursor up by the specified number of lines.
     * If already at the top, nothing happens.
     */
    public static void moveCursorUp(int n){
        System.out.printf("\033[%dA", n);
    }

    /**
     * Moves the cursor down by the specified number of lines.
     * If already at the bottom, nothing happens.
     */
    public static void moveCursorDown(int n){
        System.out.printf("\033[%dB", n);
    }

    /**
     * Moves the cursor forward (right) by the specified number of cells.
     */
    public static void moveCursorForward(int n){
        System.out.printf("\033[%dC", n);
    }

    /**
     * Moves the cursor backward (left) by the specified number of cells.
     */
    public static void moveCursorBack(int n){
        System.out.printf("\033[%dD", n);
    }

    /**
     * Moves the cursor to the specified column in the current row.
     *
     * @param col The column to move to (1-based)
     */
    public static void moveCursorToColumn(int col){
        System.out.printf("\033[%dG", col);
    }

    /**
     * Moves the terminal cursor to the specified position (1-based row and column).
     *
     * @param row the row number (starting from 1)
     * @param col the column number (starting from 1)
     */
    public static void moveCursorTo(int row, int col){
        System.out.printf("\033[%d;%dH", row, col);
    }

    /**
     * Hides the terminal cursor.
     */
    public static void hideCursor(){
        System.out.print("\033[?25l");
    }

    /**
     * Shows the terminal cursor.
     */
    public static void showCursor(){
        System.out.print("\033[?25h");
    }

    /**
     * Switches to the alternate screen buffer.
     */
    public static void openBuffer(){
        System.out.print("\033[?1049h");
        moveCursorTo(1,1);
    }

    /**
     * Returns to the main screen buffer.
     */
    public static void closeBuffer(){
        System.out.print("\033[?1049l");
    }

    /**
     * Saves the current cursor position using ANSI escape codes.
     * Can be restored later with {@link #restoreCursor()}.
     */
    public static void saveCursorPosition(){
        System.out.print("\033[s");
    }

    /**
     * Restores the most recently saved cursor position.
     * Only works if {@link #saveCursorPosition()} was called before.
     */
    public static void restoreCursor(){
        System.out.print("\033[u");
    }

    /**
     * Changes the terminal window title.
     */
    public static void setWindowTitle(String title){
        System.out.printf("\033]0;%s\07", title);
        System.out.flush();
    }

    /**
     * Changes the terminal cursor shape using ANSI escape codes (DECSCUSR).
     * Supported shapes:
     *  0 - Default  (blinking block)
     *  1 - Blinking block
     *  2 - Steady   block
     *  3 - Blinking underline
     *  4 - Steady   underline
     *  5 - Blinking bar (vertical line)
     *  6 - Steady   bar
     *
     * @param shapeCode An integer from 0 to 6 representing the desired cursor shape.
     */
    public static void changeCursorShape(int shapeCode){
        System.out.printf("\033[%d q", shapeCode);
        System.out.flush();
    }

    /**
     * Sets the cursor color in supported terminals (e.g. iTerm2, xterm, kitty).
     *
     * @param hexColor A hex color string like "#FF0000" (for red).
     */
    public static void setCursorColor(String hexColor) {
        System.out.printf("\033]12;%s\007", hexColor);
        System.out.flush();
    }

    /**
     * Resets the cursor color to the terminal default.
     */
    public static void resetCursorColor() {
        System.out.print("\033]112\007");
        System.out.flush();
    }

    /**
     * Triggers the terminal bell (beep).
     * Works in most terminals that support the BEL character.
     */
    public static void bell() {
        System.out.print("\007");
        System.out.flush();
    }
}
