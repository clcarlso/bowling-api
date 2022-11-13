package SB.IT.FP.bowlingapi.Game;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;
import org.apache.logging.log4j.*;
public class Game {
    
    private int rolls[] = new int[21];
    private int currentRoll = 0;

    private static Logger logger = LogManager.getLogger(Game.class.getName());

    private static final int maxPointValue = 10;
    private static final int minPointValue = 0;

    public int roll(int pins) throws PinsAboveBoundException, PinsBelowBoundException{
        logger.info("Attempting to roll a pin: " + pins);
        if(pins < minPointValue){
            logger.error("Pins rolled was less than the current games minPointValue. roll: " + pins + " vs minPointValue: " + minPointValue);
            throw new PinsBelowBoundException("Number of pins rolled are below the bounds. Try again.");
        }
        else if (pins > maxPointValue){
            logger.error("Pins rolled was more than the current games maxPointValue. roll: " + pins + " vs minPointValue: " + maxPointValue);
            throw new PinsAboveBoundException("Number of pins rolled are above the bounds. Try again.");
        }
        else{
            logger.info("pins added to array of rolls");
            rolls[currentRoll++] = pins;
        }
        return pins;
    }

    public int score(){
        int score = 0;
        int frameIndex = 0;
        for (int frame = 0; frame< 10; frame++){
            logger.info("for frame: " + frame);
            if (isStrike(frameIndex)){
                score += 10 + strikeBonus(frameIndex);
                frameIndex++;
            }
            else if (isSpare(frameIndex)){
                score += 10 + spareBonus(frameIndex);
                frameIndex = frameIndex + 2;
            }
            else{
                score += sumOfBallsInFrame(frameIndex);
                frameIndex = frameIndex + 2;
            }
            logger.info("current score: " + score);
        }
        return score;
    }

    private boolean isSpare(int frameIndex){
        return rolls[frameIndex] + rolls[frameIndex+1] == 10;
    }

    private boolean isStrike(int frameIndex){
        return rolls[frameIndex] == 10;
    }

    private int sumOfBallsInFrame(int frameIndex){
        return rolls[frameIndex] + rolls[frameIndex + 1];
    } 

    private int spareBonus(int frameIndex){
        logger.info("spare bonus!");
        return rolls[frameIndex + 2];
    }

    private int strikeBonus(int frameIndex){
        logger.info("strike bonus!");
        return rolls[frameIndex + 1] + rolls[frameIndex + 2];
    }

}
