package nextstep.subway.favorite.application.exception;

public class FavoriteNotFoundException extends RuntimeException {
    private static final String DEFAULT_EXCEPTION_MESSAGE = "요청한 정보에 해당하는 즐겨 찾기를 찾을 수 없습니다.";

    public FavoriteNotFoundException(){
        super(DEFAULT_EXCEPTION_MESSAGE);
    }

    public FavoriteNotFoundException(String message) {
        super(message);
    }
}
