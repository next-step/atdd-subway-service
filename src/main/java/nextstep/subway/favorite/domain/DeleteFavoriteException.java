package nextstep.subway.favorite.domain;

public class DeleteFavoriteException extends RuntimeException {

    private static final long serialVersionUID = -4656993888223000292L;

    public DeleteFavoriteException(String message) {
        super(message);
    }
}
