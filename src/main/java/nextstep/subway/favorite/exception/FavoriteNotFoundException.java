package nextstep.subway.favorite.exception;

public class FavoriteNotFoundException extends RuntimeException {

    private static final String FAVORITE_CAN_NOT_FIND = "favorite can not find.";

    public FavoriteNotFoundException() {
        super(FAVORITE_CAN_NOT_FIND);
    }
}
