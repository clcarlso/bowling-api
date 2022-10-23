package SB.IT.FP.bowlingapi.Exceptions;

public class PinsAboveBoundException extends Exception{
    
    public PinsAboveBoundException(String errorMessage){
        super(errorMessage);
    }

    public PinsAboveBoundException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
