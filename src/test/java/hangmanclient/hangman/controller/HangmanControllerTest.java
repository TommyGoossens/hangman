package hangmanclient.hangman.controller;

import hangmanshared.models.Hangman;
import hangmanshared.models.Player;
import hangmanshared.models.enums.Difficulty;
import hangmanshared.models.enums.GameMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HangmanControllerTest {

    private MockHangman controller;
    private  Player tommy = new Player("Tommy", 1, 1, 0);
    private Player senna = new Player("Senna", 1, 1, 0);
    private Player maikel = new Player("Senna", 1, 1, 0);

    @Before
    public void setUpGame() {
        controller = new MockHangman();
        Hangman game = new Hangman(tommy);
        game.setGameMode(GameMode.MULTI_PLAYER);
        game.setDifficulty(Difficulty.EASY);
        controller.createGame(game);
    }

    @Test
    public void wordIsTooShort() {
        controller.setSecretWord("ja");
        Assert.assertFalse(controller.getGamedetails().getSecretWordHasBeenSet());
    }


    @Test
    public void tooManyUniqueLetters(){
        controller.setSecretWord("Dampkringsluchtje");
        Assert.assertFalse(controller.getGamedetails().getSecretWordHasBeenSet());
    }

    @Test
    public void wordMeetsRequirements(){
        controller.setSecretWord("Paard");
        Assert.assertTrue(controller.getGamedetails().getSecretWordHasBeenSet());
    }

    @Test
    public void createGamePlayerIsCreator() {

        Assert.assertEquals(tommy, controller.getGamedetails().getCreator());
        Assert.assertEquals(1, controller.getGamedetails().getPlayers().size());
    }


    @Test
    public void gameJoinNotFull() {
        Assert.assertEquals(tommy, controller.getGamedetails().getCreator());
        Assert.assertEquals(1, controller.getGamedetails().getPlayers().size());

        controller.joinGame(senna, controller.getGamedetails().getGameid());
        Assert.assertNotEquals(senna, controller.getGamedetails().getCreator());
        Assert.assertEquals(2, controller.getGamedetails().getPlayers().size());
    }

    @Test
    public void gameJoinFull() {
        Assert.assertEquals(tommy, controller.getGamedetails().getCreator());
        Assert.assertEquals(1, controller.getGamedetails().getPlayers().size());

        controller.joinGame(senna, controller.getGamedetails().getGameid());
        Assert.assertNotEquals(senna, controller.getGamedetails().getCreator());
        Assert.assertEquals(2, controller.getGamedetails().getPlayers().size());

        controller.joinGame(maikel,controller.getGamedetails().getGameid());
        Assert.assertEquals(2, controller.getGamedetails().getPlayers().size());
    }

    @Test
    public void leaveGame(){
        Assert.assertEquals(tommy, controller.getGamedetails().getCreator());
        Assert.assertEquals(1, controller.getGamedetails().getPlayers().size());

        controller.joinGame(senna, controller.getGamedetails().getGameid());
        Assert.assertNotEquals(senna, controller.getGamedetails().getCreator());
        Assert.assertEquals(2, controller.getGamedetails().getPlayers().size());

        controller.leaveGame(tommy,controller.getGamedetails().getGameid());
        Assert.assertFalse(controller.getGamedetails().getPlayers().contains(tommy));
        Assert.assertEquals(senna, controller.getGamedetails().getCreator());
    }

    @Test
    public void correctletter(){
        controller.setSecretWord("PAARD");
        Assert.assertTrue(controller.getGamedetails().getSecretWord().equals("PAARD"));

        controller.guessLetter("a");
        Assert.assertEquals("A",controller.getGamedetails().getGuessedCorrectLetters().get(1));
        Assert.assertEquals("A",controller.getGamedetails().getGuessedCorrectLetters().get(2));
    }

    @Test
    public void incorrectLetter(){
        controller.setSecretWord("PAARD");
        Assert.assertTrue(controller.getGamedetails().getSecretWord().equals("PAARD"));

        controller.guessLetter("B");
        Assert.assertEquals(1,controller.getGamedetails().getGuessedWrongLetters().size());
        Assert.assertFalse(controller.getGamedetails().getSecretWord().contains("B"));
    }

}