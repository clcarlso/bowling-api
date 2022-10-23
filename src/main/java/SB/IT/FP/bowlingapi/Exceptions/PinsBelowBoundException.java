package SB.IT.FP.bowlingapi.Exceptions;

public class PinsBelowBoundException extends Exception{
    
    public PinsBelowBoundException(String errorMessage){
        super(errorMessage);
    }

    public PinsBelowBoundException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }
}
