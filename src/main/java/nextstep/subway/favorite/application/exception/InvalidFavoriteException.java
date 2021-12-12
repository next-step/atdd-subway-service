package nextstep.subway.favorite.application.exception;

public class InvalidFavoriteException extends RuntimeException {

    public InvalidFavoriteException() {
        super("출발역과 도착역이 같습니다.");
    }

    public InvalidFavoriteException(String message) {
        super(message);
    }
}
