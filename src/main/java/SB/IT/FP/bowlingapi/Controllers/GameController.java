package SB.IT.FP.bowlingapi.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;
import SB.IT.FP.bowlingapi.Game.Game;

import java.util.ArrayList;

import org.apache.logging.log4j.*;

@RestController
public class GameController {
   
    private Game game = new Game();
    

    @GetMapping("/score")
    @CrossOrigin(origins = {"http://localhost:4200"})
    ArrayList<Integer> getScore(){
        return game.returnScore();
    }

    @GetMapping("/roll/{pins}")
    @CrossOrigin(origins = {"http://localhost:4200"})
    String rollPin(@PathVariable int pins){
        try{
            game.roll(pins);
            return String.valueOf(pins);
        }
        catch(PinsBelowBoundException e){
            return e.getMessage();
        }
        catch(PinsAboveBoundException e){
            return e.getMessage();
        }
    }

}
