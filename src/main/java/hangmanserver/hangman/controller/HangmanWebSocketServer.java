package hangmanserver.hangman.controller;

public interface HangmanWebSocketServer {
    void leaveGame(String content);

    void updatePlayerList(int gameId);

    void guessSecretWord(String content);

    void guessLetter(String content);

    void setSecretWord(String content);
}
