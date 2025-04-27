public class GameResult {
    private boolean win;
    private boolean lost;
    private boolean quited;
    private int score;
    private double timeSpent;


    public GameResult win(double timeSpent){
        this.timeSpent = timeSpent;
        win = true;
        return this;
    }
    public GameResult lost(double timeSpent){
        this.timeSpent = timeSpent;
        lost = true;
        return this;
    }
    public GameResult quit(double timeSpent){
        this.timeSpent = timeSpent;
        quited = true;
        return this;
    }
    public GameResult score(int score){
        this.score = score;
        return this;
    }


    public boolean isWin() {
        return win;
    }

    public boolean isLost() {
        return lost;
    }

    public boolean isQuited() {
        return quited;
    }

    public double getTimeSpent() {
        return timeSpent;
    }

    public int getScore() {
        return score;
    }
}
