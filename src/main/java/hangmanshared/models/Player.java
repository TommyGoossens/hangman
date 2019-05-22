package hangmanshared.models;

public class Player {
    private String name;
    private int gamesPlayed;
    private int gameswon;
    private int gameslost;
    private double winratio;


    //Updates the number of wins, when the player wins a game
    public void updateWin() {
        this.gameswon++;
        updateWinRatio();
    }

    //Updates the number of losses, when the player loses a game
    public void updateLoss() {
        this.gameslost++;
        updateWinRatio();
    }

    //Updates the win / lose ratio
    private void updateWinRatio() {
        this.winratio = (double) gameswon / gameslost;
    }

    //Returns the name of the player
    public String getName() {
        return name;
    }

    //Returns the amount of games the player has won
    public int getGameswon() {
        return gameswon;
    }

    //Returns the amount of games the player has lost
    public int getGameslost() {
        return gameslost;
    }

    //Returns the win / lose ratio
    public double getWinratio() {
        return winratio;
    }

    public Player(String name, int gamesPlayed, int gameswon, int gameslost) {
        this.name = name;
        this.gamesPlayed = gamesPlayed;
        this.gameswon = gameswon;
        this.gameslost = gameslost;
        this.winratio = calculateWinRatio();
    }

    private double calculateWinRatio() {
        if(gameslost == 0 || gameswon == 0) return 0;
        return gameswon /(gameswon + gameslost);
    }

    @Override
    public String toString() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }
}
