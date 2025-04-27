public class TimerManager{
    private       long    startTime;
    private       boolean timerOn;
    private       boolean timerPause;
    private final int     timeRow;
    private final int     timeColumn;
    private final String  timeFormat;

    public TimerManager(int timeRow, int timeColumn, String timeFormat) {
        this.timeColumn = timeColumn;
        this.timeRow    = timeRow;
        this.timeFormat = timeFormat;
    }

    public boolean isTimerOn() {
        return timerOn;
    }

    public void    startTimer(){
        this.timerOn = true;
        this.startTime = System.currentTimeMillis();
        new Thread(this::timeUpdater).start();
    }

    public double  stopTimer() {
        if (!timerOn) return 0.0;
        this.timerOn = false;
        return System.currentTimeMillis() - this.startTime;
    }

    public void    pauseTimer(){
        this.timerPause = true;
    }

    public void    unpauseTimer(){
        this.timerPause = false;
    }

    public void    timeUpdater() {
        while (this.timerOn) {
            if (this.timerPause) {
                Utils.sleep(500);
                continue;
            }
            Utils.saveCursorPosition();

            Utils.moveCursorTo(timeRow, timeColumn);
            int timeSpent = (int) ((System.currentTimeMillis() - this.startTime)/1000);
            int seconds = timeSpent%60;
            int minutes = timeSpent/60%60;
            int hours = timeSpent/3600;
            System.out.printf(timeFormat, hours, minutes, seconds);
            Utils.restoreCursor();


            Utils.sleep(1000);
        }
    }
}