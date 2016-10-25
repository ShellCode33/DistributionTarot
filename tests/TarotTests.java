import fr.iut.etu.model.*;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by sdupouy002 on 04/10/16.
 */
public class TarotTests {

    public ExpectedException exception = ExpectedException.none();

    @Test
    public void canCreateCard() throws Exception {
        Card card = new Card(Card.Type.CLUB, 2);
        assertNotEquals("Card not created !", null, card);
        assertEquals("Wrong card type !", Card.Type.CLUB, card.getType());
        assertEquals("Wrong card value !", 2, card.getValue());
    }

    @Test(expected =  IllegalArgumentException.class)
    public void exceptionCardCreation() throws Exception {
        new Card(Card.Type.DIAMOND, 15);
        new Card(Card.Type.HEART, 0);
    }

    @Test
    public void canCreatetrump() throws Exception {
        Trump trump = new Trump(19);

        assertNotEquals("Trump not created !", null, trump);
        assertEquals("Wrong trump type !", Card.Type.TRUMP, trump.getType());
        assertEquals("Wrong trump value !", 19, trump.getValue());
    }

    @Test(expected =  IllegalArgumentException.class)
    public void exceptionTrumpCreation() throws Exception {
        new Trump(22);
        new Trump(0);
    }


    @Test
    public void canCreatePlayer() throws Exception {
        Player player = new Player();
        assertNotEquals("Player not created !", null, player);

        Player player2 = new Player("Patrick");
        assertNotEquals("Player not created !", null, player2);
    }

    @Test
    public void canCreateDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", null, deck);
        assertEquals("Wrong deck size !", 0, deck.getSize());
    }

    @Test
    public void canRefillDeck() throws Exception {
        Deck deck = new Deck();

        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);
    }

    @Test
    public void doesDeckContains() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));
    }

    @Test
    public void canShuffleDeck() throws Exception {

        Deck notshuffled = new Deck();
        assertNotEquals("Deck not created !", notshuffled, null);
        assertEquals("Wrong deck size !", notshuffled.getSize(), 0);

        notshuffled.refill();
        assertEquals("Wrong deck size !", notshuffled.getSize(), 78);

        Deck shuffled = new Deck();
        assertNotEquals("Deck not created !", shuffled, null);
        assertEquals("Wrong deck size !", shuffled.getSize(), 0);

        shuffled.refill();
        assertEquals("Wrong deck size !", shuffled.getSize(), 78);

        shuffled.shuffle();

        boolean isShuffled = false;
        int i = 0;

        while(i < shuffled.getCards().size()
                && i < notshuffled.getCards().size()
                && !isShuffled) {

            if(!shuffled.getCard(i).equals(notshuffled.getCard(i)))
                isShuffled = true;

            i++;
        }

        assertTrue("Deck not shuffled !", isShuffled);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void exceptionGetACardFromDeck() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        deck.getCard(-1);
        deck.getCard(78);
    }

    @Test
    public void canCutDeck() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        Card cardAtCut = deck.getCard(24);
        Card cardAtBottom = deck.getCard(0);

        deck.cut(24);

        assertTrue("Deck not cuted !", deck.getCard(78 - 24).equals(cardAtBottom));
        assertTrue("Deck not cuted !", deck.getCard(0).equals(cardAtCut));
    }

    @Test
    public void canDealACard() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);

        assertTrue("Deck.contains not working !", deck.contains(new Card(Card.Type.CLUB, 9)));

        assertTrue("Deck didn't deal !", deck.deal().equals(new Fool()));
        assertEquals("Deck size not reduced !", deck.getSize(), 77);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionDealIndex() throws Exception {
        Deck deck = new Deck();
        assertNotEquals("Deck not created !", deck, null);
        assertEquals("Wrong deck size !", deck.getSize(), 0);

        deck.cut(24);
        deck.refill();
        assertEquals("Wrong deck size !", deck.getSize(), 78);
        deck.cut(2);
        deck.cut(76);
    }
}
