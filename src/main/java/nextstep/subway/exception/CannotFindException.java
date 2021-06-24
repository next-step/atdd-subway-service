package nextstep.subway.exception;

public class CannotFindException extends RuntimeException{

    public CannotFindException(Message message){
        super(message.showText());
    }
}
