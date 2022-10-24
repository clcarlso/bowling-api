package SB.IT.FP.bowlingapi.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;
import SB.IT.FP.bowlingapi.Game.Game;

@RestController
public class GameController {
   
    Game game = new Game();

    @GetMapping("/score")
    String getScore(){
        return "The current score of the game is: " + game.score();
    }

    @GetMapping("/roll/{pins}")
    String rollPin(@PathVariable int pins){
        try{
            game.roll(pins);
            return "You have rolled a pin worth " + pins + " points.";
        }
        catch(PinsBelowBoundException e){
            return e.getMessage();
        }
        catch(PinsAboveBoundException e){
            return e.getMessage();
        }
    }

}
