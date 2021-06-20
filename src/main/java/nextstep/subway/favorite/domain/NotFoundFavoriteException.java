package nextstep.subway.favorite.domain;

public class NotFoundFavoriteException extends RuntimeException {

    private static final long serialVersionUID = -5383622906077593794L;

    public NotFoundFavoriteException() {
        super("요청한 즐겨찾기를 찾을 수 없습니다.");
    }
}
