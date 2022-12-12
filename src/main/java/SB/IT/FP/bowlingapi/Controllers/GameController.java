package SB.IT.FP.bowlingapi.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;
import SB.IT.FP.bowlingapi.Game.Game;
import SB.IT.FP.bowlingapi.model.GameOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.*;

@RestController
public class GameController {
   
    private Game game = new Game();
    

    @GetMapping("/score")
    @CrossOrigin(origins = {"http://localhost:4200"})
    ArrayList<Integer> getScore(){
        return game.returnScore();
    }

    
    @GetMapping("/place/{pins}")
    @CrossOrigin(origins = {"http://localhost:4200"})
    String placePin(@PathVariable int pins){
        try{
            game.place(pins);
            return String.valueOf(pins);
        }
        catch(PinsBelowBoundException e){
            return e.getMessage();
        }
        catch(PinsAboveBoundException e){
            return e.getMessage();
        }
    }

    @GetMapping("/roll")
    @CrossOrigin(origins = {"http://localhost:4200"})
    String rollBall(){
            return String.valueOf(game.roll());
    }

    @GetMapping("/rollMap")
    @CrossOrigin(origins = {"http://localhost:4200"})
    HashMap<Integer, List<Integer>> returnRollMap(){
        return game.returnRollMap();
    }

    @GetMapping("/clear")
    @CrossOrigin(origins = {"http://localhost:4200"})
    boolean clearScore(){
        return game.clearScore();
    }
    @PostMapping("/createGame")
    @CrossOrigin(origins = {"http://localhost:4200"})
    GameOptions createGame(@RequestBody GameOptions gameOptions){
        return game.initializeGameOptions(gameOptions);
    }
}
