package SB.IT.FP.bowlingapi.model;

import java.util.UUID;


public class GameOptions {
    private UUID uuid;
    private Integer pins;
    private Integer frames;
    private Integer rollsPerFrame;
    private Integer endFrames;
    private Integer rollsPerEndFrame;
    private String mode;
    
  
    public GameOptions(GameOptions gameOptions) {}
  
    public GameOptions(Integer pins, Integer frames, Integer rollsPerFrame, Integer endFrames, Integer rollsPerEndFrame, String mode) {
      this.uuid = UUID.randomUUID();
      
      this.pins = pins;
      this.frames = frames;
      this.rollsPerFrame = rollsPerFrame;
      this.endFrames = endFrames;
      this.rollsPerEndFrame = rollsPerEndFrame;
      this.mode = mode;
    }
  
    public UUID getUUID() {
      return uuid;
    }
  
    public void setId(UUID id) {
      this.uuid = id;
    }
  
    public Integer getPins() {
        return pins;
    }

    public void setPins(Integer pins) {
        this.pins = pins;
    }

    public Integer getFrames() {
        return frames;
    }

    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    public Integer getRollsPerFrame() {
        return rollsPerFrame;
    }

    public void setRollsPerFrame(Integer rollsPerFrame) {
        this.rollsPerFrame = rollsPerFrame;
    }

    public Integer getEndFrames() {
        return endFrames;
    }

    public void setEndFrames(Integer endFrames) {
        this.endFrames = endFrames;
    }

    public Integer getRollsPerEndFrame() {
        return rollsPerEndFrame;
    }

    public void setRollsPerEndFrame(Integer rollsPerEndFrame) {
        this.rollsPerEndFrame = rollsPerEndFrame;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
