package SB.IT.FP.bowlingapi.Game;

import SB.IT.FP.bowlingapi.Exceptions.PinsAboveBoundException;
import SB.IT.FP.bowlingapi.Exceptions.PinsBelowBoundException;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

import org.apache.logging.log4j.*;
public class Game {
    
    //private  ArrayList<Integer>rolls = new ArrayList<>();

    private static Logger logger = LogManager.getLogger(Game.class.getName());

    private static final int maxPointValue = 10;
    private static final int minPointValue = 0;
    private static final int numberOfFrames = 10;
    private static final int rollsPerFrame = 2;
    private static final int lastFrameBonusThrow = 1;
    private static final int strikeBonusRolls = 2;

    private int currentFrame=0;
    
    private boolean spareFlag;
    private HashMap<Integer, Integer> strikeFlag = new HashMap<Integer,Integer>();
    private HashMap<Integer, List<Integer>> rollMap = new HashMap<Integer, List<Integer>>();
    private HashMap<Integer, Integer> scoreMap = new HashMap<>();

    public int place(int score) throws PinsAboveBoundException, PinsBelowBoundException{
        logger.info("Attempting to place a score: " + score);
        
        if(score < minPointValue){
            logger.error("Score placed was less than the current games minPointValue. score: " + score + " vs minPointValue: " + minPointValue);
            throw new PinsBelowBoundException("Score trying to place is below the bounds. Try again.");
        }
        else if (score > maxPointValue){
            logger.error("Score placed was more than the current games maxPointValue. score: " + score + " vs minPointValue: " + maxPointValue);
            throw new PinsAboveBoundException("Score trying to place is above the bounds. Try again.");
        }
        else{
            logger.info("Score added to array of rolls");
            //rolls.add(pins);
            addToRollMap(score);
        }
        return score;
    }

    public int roll(){
        int currentScoreOfFrame = 0;
        if (rollMap.containsKey(currentFrame)){
            List<Integer>rollList = rollMap.get(this.currentFrame);
            for(int i =0; i< rollList.size(); i++){
                currentScoreOfFrame = rollList.get(i);
            }
        }
        int maxRollValue = (Game.maxPointValue+1) - currentScoreOfFrame;
        Random randomGenerator = new Random();
        int pins = randomGenerator.nextInt(maxRollValue);
        addToRollMap(pins);
        return pins;
    }

    
    private void addPinsToFrameTotal(int frameIndex , int pins){
        int frameScore = this.scoreMap.get(frameIndex);
        int newTotal = frameScore + pins;
        //update the score of the tracked frame
        this.scoreMap.put(frameIndex, newTotal);
       
    }


    private boolean decrementFromStrikeFlag(int frameIndex){
        int strikeBonusRemaining = this.strikeFlag.get(frameIndex);
        if(strikeBonusRemaining <= 1){
            return true;
        }
        else{
            this.strikeFlag.put(frameIndex, strikeBonusRemaining-1);
            return false;
        }
       
    }

    private void strikeBackTrack(int pins){
        //if there is anything in the strike hashmap then add this value to that frame's total and decrement the count 
        if(!this.strikeFlag.isEmpty()){
            //get an iterator
            Iterator<HashMap.Entry<Integer,Integer>> strikeFlagIterator = this.strikeFlag.entrySet().iterator();
            //iterate through our list of strikeFlags
            while(strikeFlagIterator.hasNext()){
                HashMap.Entry<Integer,Integer> entry = strikeFlagIterator.next();
                int frameIndex = entry.getKey();
                
                Set strikeFrameKeysSet =  this.strikeFlag.keySet();
                Iterator<Integer> strikeFrameKeysIterator = strikeFrameKeysSet.iterator();

                //iterate through all the frames that have a strike bonus waiting and add to their frame.
                while(strikeFrameKeysIterator.hasNext()) {
                    int strikeFrameKey = strikeFrameKeysIterator.next();
                    addPinsToFrameTotal(strikeFrameKey, pins);
                 }
                 //but only decrement from the first frame in the set
                if(decrementFromStrikeFlag(frameIndex)){
                    strikeFlagIterator.remove();
                }
            }
        }
    }

    private List<Integer> strikeAction(List<Integer> rollsForCurrentFrame, Integer pins){
        //set strike flag
        this.strikeFlag.put(currentFrame, strikeBonusRolls);

        //add our roll to the current roll frame
        rollsForCurrentFrame.add(pins);

        //fill the remaining frame with empty
        for( int i =1;i<rollsPerFrame; i++){
            rollsForCurrentFrame.add(0);
        }
        return rollsForCurrentFrame;
    } 

    private boolean checkIfSpare(List<Integer> rollsForCurrentFrame, int maxPointValue){
        int currentFrameSum = 0;
        //if we add up our throws this frame does it equal a strike?
        for( int i =0;i<rollsForCurrentFrame.size(); i++){
            currentFrameSum = currentFrameSum + rollsForCurrentFrame.get(i);
        }
        if(currentFrameSum >= maxPointValue){
            return true;
        }
        else{
            return false;
        }
    }
    
    private void addToRollMap(int pins){
        List<Integer> rollsForCurrentFrame = new ArrayList<>();
        //prev = strike
        
        strikeBackTrack(pins);
        //first roll
        if(!rollMap.containsKey(currentFrame)){
            //first roll + strike
            if(pins == maxPointValue){
                rollsForCurrentFrame = strikeAction(rollsForCurrentFrame,pins);
            }
            //first roll + not strike
            else{
                rollsForCurrentFrame.add(pins);
            }
            //prev = spare
            if (spareFlag == true){
                addPinsToFrameTotal(currentFrame-1, pins);
                this.spareFlag = false;
            }
        }
        //not first roll
        else{
            //get our previous throws for the current frame
            rollsForCurrentFrame = this.rollMap.get(this.currentFrame);
            //add our current throw to the rolls
            rollsForCurrentFrame.add(pins);
            //not first roll + spare
            if(checkIfSpare(rollsForCurrentFrame, maxPointValue)){
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
    public HashMap<Integer, List<Integer>> returnRollMap(){
        return this.rollMap;
    }
    
    public boolean clearScore(){
        
        try{
            this.rollMap.clear();
            this.scoreMap.clear();
            this.strikeFlag.clear();
            this.currentFrame = 0;
            this.spareFlag = false;
            return true;
        }
        catch(Exception e){
            logger.error("Unable to clear roll map");
            return false;
        }
    }

    /*private boolean isSpare(int frameIndex){
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
    }*/

}
