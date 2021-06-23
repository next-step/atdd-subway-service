package nextstep.subway.exception;

public class DataAlreadyExistsException extends RuntimeException{

    public DataAlreadyExistsException(Message message){
        super(message.showText());
    }
}
