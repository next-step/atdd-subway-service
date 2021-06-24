package nextstep.subway.exception;

public class CannotAddException extends RuntimeException{

    public CannotAddException(Message message){
        super(message.showText());
    }
}
