package nextstep.subway.favorite.exception;

public class FavoriteNotFoundException extends RuntimeException {

    public FavoriteNotFoundException() {
        super("즐겨찾기를 찾을 수 없습니다.");
    }
}
