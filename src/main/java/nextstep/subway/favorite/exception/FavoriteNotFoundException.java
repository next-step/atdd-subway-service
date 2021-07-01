package nextstep.subway.favorite.exception;

public class FavoriteNotFoundException extends RuntimeException{
    public FavoriteNotFoundException() {
        super("즐겨찾기 항목을 찾을 수 없습니다.");
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
