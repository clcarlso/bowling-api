package SB.IT.FP.bowlingapi.Game;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;

public class Game {
    
    private int rolls[] = new int[21];
    private int currentRoll = 0;

    private int maxPointValue = 10;
    private int minPointValue = 0;

    public void roll(int pins) throws PinsAboveBoundException, PinsBelowBoundException{
        if(pins < minPointValue){
            throw new PinsBelowBoundException("Number of pins rolled are below the bounds. Try again.");
        }
        else if (pins > maxPointValue){
            throw new PinsAboveBoundException("Number of pins rolled are above the bounds. Try again.");
        }
        else{
            rolls[currentRoll++] = pins;
        }
    }

    public int score(){
        int score = 0;
        int frameIndex = 0;
        for (int frame = 0; frame< 10; frame++){
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
        return rolls[frameIndex + 2];
    }

    private int strikeBonus(int frameIndex){
        return rolls[frameIndex + 1] + rolls[frameIndex + 2];
    }

}
