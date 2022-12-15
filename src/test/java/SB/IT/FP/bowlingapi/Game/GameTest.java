package SB.IT.FP.bowlingapi.Game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;


@SpringBootTest
public class GameTest  {
    private Game game;

    @BeforeEach
    protected void setUp() throws Exception{
        game = new Game();
    }

    private void placeMany(int n, int pins) throws PinsAboveBoundException, PinsBelowBoundException  {
        for(int i =0; i<n; i++){
            this.game.place(pins);
        }
    }

    private void placeSpare() throws PinsAboveBoundException, PinsBelowBoundException {
        game.place(5);
        game.place(5);
    }

    private void placeStrike() throws PinsAboveBoundException, PinsBelowBoundException {
            game.place(10);
    }

    @Test
    public void testGutterGame() throws PinsAboveBoundException, PinsBelowBoundException  {
        placeMany(20, 0);
        //assertEquals(0,game.score());
    }

    @Test
    public void testAllOnes() throws PinsAboveBoundException, PinsBelowBoundException {
        placeMany(20, 1);
        //assertEquals(20,game.score());
    }

    @Test 
    public void testOneSpare() throws PinsAboveBoundException, PinsBelowBoundException{
        placeSpare();
        game.place(3);
        placeMany(17, 0);
        //assertEquals(16, game.score());
    }

    @Test 
    public void testOneStrike() throws PinsAboveBoundException, PinsBelowBoundException{
        placeStrike();
        game.place(3);
        game.place(4);
        placeMany(17, 0);
        //assertEquals(24, game.score());
    }

    @Test 
    public void testPerfectGame() throws PinsAboveBoundException, PinsBelowBoundException{
        placeMany(12, 10);
        //assertEquals(300, game.score());
    }

    @Test 
    public void testAbovePointBoundary() throws PinsBelowBoundException{
        String PinsExceptionError = "Number of pins placed are above the bounds. Try again.";
        try {
            game.place(999);
        } catch (PinsAboveBoundException e) {
            assertEquals(e.getMessage(),PinsExceptionError);
            //assertThrows
        }
        //assertEquals(0, game.score());
    }

    @Test 
    public void testBelowPointBoundary() throws PinsAboveBoundException{
        String PinsExceptionError = "Number of pins placed are below the bounds. Try again.";
        try {
            game.place(-1);
        } catch (PinsBelowBoundException e) {
            assertEquals(e.getMessage(),PinsExceptionError);
        }
        //assertEquals(0, game.score());
    }
}