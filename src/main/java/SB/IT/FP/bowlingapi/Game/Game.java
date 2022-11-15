package SB.IT.FP.bowlingapi.Game;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.*;
public class Game {
    
    private  ArrayList<Integer>rolls = new ArrayList<>();

    private static Logger logger = LogManager.getLogger(Game.class.getName());

    private static final int maxPointValue = 10;
    private static final int minPointValue = 0;
    private static final int numberOfFrames = 10;
    private static final int rollsPerFrame = 2;
    private static final int lastFrameBonusThrow = 1;

    private int currentFrame=0;
    
    private boolean spareFlag;
    private boolean strikeFlag;
    private HashMap<Integer, List<Integer>> rollMap = new HashMap<Integer, List<Integer>>();
    private HashMap<Integer, Integer> scoreMap = new HashMap<>();

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
            rolls.add(pins);
            addToRollMap(pins);
        }
        return pins;
    }

    private void addToRollMap( int pins){
        List<Integer> rollsForCurrentFrame = new ArrayList<>();
        

        //if this is the first roll
        if(!rollMap.containsKey(currentFrame)){
            //if strike
            if(pins == maxPointValue){
                //set strike flag
                this.strikeFlag = true;
                //add our roll to the current roll frame
                rollsForCurrentFrame.add(pins);
                //fill the remaining frame with empty
                for( int i =1;i<rollsPerFrame; i++){
                    rollsForCurrentFrame.add(0);
                }
            }
            else{
                rollsForCurrentFrame.add(pins);
            }
        }
        //if this is not our first throw we wont be able to get a strike anymore. but we can still get a spare
        else{
            //get our previous throws for the current frame
            rollsForCurrentFrame = this.rollMap.get(this.currentFrame);
            //add our current throw to the rolls
            rollsForCurrentFrame.add(pins);
            int frameSum =0;
            //if we add up our throws this frame does it equal a strike?
            for( int i =0;i<rollsForCurrentFrame.size(); i++){
                frameSum = frameSum + rollsForCurrentFrame.get(i);
            }
            if(frameSum >= maxPointValue){
                this.spareFlag = true;
                //TODO: set up to fill the remainder if there is still room.
            }
        }

        rollMap.put(currentFrame, rollsForCurrentFrame);
        //if this is the final roll in the frame
        if (rollsForCurrentFrame.size() >=rollsPerFrame){
            //add up the rolls in the frame and store in the score map
            addToScoreMap();
            //move to the next frame
            this.currentFrame++;
        }
    }

    private void addToScoreMap(){
        List<Integer> rollsForCurrentFrame = new ArrayList<>();
        int prevFrameTotal;
        //get the last frames total score to add onto.
        if(scoreMap.containsKey(currentFrame-1)){
            prevFrameTotal = scoreMap.get(currentFrame-1);
        }
        else{
            prevFrameTotal = 0;
        }
        
        int currentFrameTotal = 0;
        if(rollMap.containsKey(currentFrame)){
            rollsForCurrentFrame = rollMap.get(currentFrame);
        }
        else{
            return;
        }

        for(int i =0; i< rollsForCurrentFrame.size();i++){
            currentFrameTotal = currentFrameTotal + rollsForCurrentFrame.get(i);
        }
        
        

        this.scoreMap.put(this.currentFrame, prevFrameTotal + currentFrameTotal);

    }

    /*public ArrayList<Integer> score(){
        int score = 0;
        ArrayList<Integer> scoresByFrame = new ArrayList<Integer>(); 
        int frameIndex = 0;
        if (rolls.size() == 0){
            return scoresByFrame;
        }
        for (int frame = 0; frame< rollMap.size(); frame++){
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
            
            scoresByFrame.add(score);
            logger.info("scores by frame: " + scoresByFrame.get(frame).toString());
        }
        return scoresByFrame;
    }*/

    public ArrayList<Integer> returnScore(){
        ArrayList<Integer> scoresByFrame = new ArrayList<Integer>(this.scoreMap.values());

        return scoresByFrame;
    }

    private boolean isSpare(int frameIndex){
        return rolls.get(frameIndex) + rolls.get(frameIndex+1) == 10;
    }

    private boolean isStrike(int frameIndex){
        return rolls.get(frameIndex) == 10;
    }

    private int sumOfBallsInFrame(int frameIndex){
        return rolls.get(frameIndex) + rolls.get(frameIndex+1);
    } 

    private int spareBonus(int frameIndex){
        logger.info("spare bonus!");
        return rolls.get(frameIndex+2);
    }

    private int strikeBonus(int frameIndex){
        logger.info("strike bonus!");
        return rolls.get(frameIndex+1) + rolls.get(frameIndex+2);
    }

}
