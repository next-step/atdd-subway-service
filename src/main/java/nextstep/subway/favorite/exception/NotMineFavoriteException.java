package nextstep.subway.favorite.exception;

public class NotMineFavoriteException extends RuntimeException {
    public NotMineFavoriteException() {
        super("로그인한 유저의 즐겨찾기만 삭제 가능합니다");
    }

    public NotMineFavoriteException(String s) {
        super(s);
    }
}
