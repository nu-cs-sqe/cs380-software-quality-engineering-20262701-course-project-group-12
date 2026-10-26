package domain;

public class Game {
    private boolean started;

    public boolean start(String player1Name, String player2Name) {
        if (player1Name.trim().isEmpty() || player2Name.trim().isEmpty()) {
            return false;
        }

        started = true;
        return true;
    }

    public boolean isStarted() {
        return started;
    }
}
