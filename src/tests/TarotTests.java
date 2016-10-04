package tests;

import fr.iut.etu.Card;
import fr.iut.etu.Player;
import fr.iut.etu.Trump;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class TarotTests {

    public ExpectedException exception = ExpectedException.none();


    @Test
    public void isCardCreated() throws Exception {
        Card card = new Card(Card.Type.CLOVER, 2);
        assertNotEquals("Card not created !", null, card);
        assertNotEquals("Wrong card type !", Card.Type.CLOVER, card.getType());
        assertNotEquals("Wrong card value !", 2, card.getValue());
    }

    @Test
    public void isTrumpCreated() throws Exception {
        Trump trump = new Trump(21);

        assertNotEquals("Trump not created !", null, trump);
        assertNotEquals("Wrong trump type !", Card.Type.TRUMP, trump.getType());
        assertNotEquals("Wrong trump value !", 2, trump.getValue());
    }

    @Test
    public void exceptionCardCreation() throws Exception {

        exception.expect(IllegalStateException.class);
        Card card = new Card(Card.Type.TILE, 15);

        exception.expect(IllegalStateException.class);
        Card card1 = new Card(Card.Type.HEART, 0);
    }

    @Test
    public void exceptionTrumpCreation() throws Exception {
        exception.expect(IllegalStateException.class);
        Trump trump = new Trump(22);

        exception.expect(IllegalStateException.class);
        Trump trump1 = new Trump(0);
    }


    @Test
    public void isPlayerCreated() throws Exception {
        Player player = new Player();

        assertNotEquals("Player not created !", null, player);
    }
}
