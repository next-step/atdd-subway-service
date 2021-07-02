package nextstep.subway.favorite.exception;

public class FavoriteAlreadyExistsException extends RuntimeException {
    public FavoriteAlreadyExistsException() {
        super("이미 등록된 즐겨찾기 구간입니다.");
    }

    public FavoriteAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
