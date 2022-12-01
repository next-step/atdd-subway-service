package nextstep.subway.favorite.exception;

public class FavoriteException extends RuntimeException{
    public FavoriteException(FavoriteExceptionCode code){
        super(code.getMessage());
    }
}
