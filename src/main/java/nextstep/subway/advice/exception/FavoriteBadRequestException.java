package nextstep.subway.advice.exception;

public class FavoriteBadRequestException extends RuntimeException {

    public FavoriteBadRequestException(String message, long id) {
        super(String.format(message + "id : %d", id));
    }
}
