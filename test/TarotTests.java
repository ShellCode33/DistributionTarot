import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TarotTests {

    @Test
    public void isCardCreated(){
        Card card = new Card();

        assertNotEquals("Card not created !", null, card);
    }

}