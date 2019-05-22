package hangmanshared.models.DTO;

import hangmanclient.HangmanClientApplication;

public class MessageDTO {
    private String sender;
    private String message;
    private int gameId;


    public MessageDTO() {
    }

    public MessageDTO(String message) {
        this.sender = HangmanClientApplication.getAuthenticatedUser().getUsername();
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return sender + ":" + message;
    }
}
