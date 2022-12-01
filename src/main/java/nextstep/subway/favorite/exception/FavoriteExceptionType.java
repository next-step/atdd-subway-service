package nextstep.subway.favorite.exception;

public enum FavoriteExceptionType {
    NOT_FOUND("해당 즐겨찾기는 존재 하지 않습니다."),
    SAME_STATION("출발역과 도착역이 같습니다.");

    private final String message;

    FavoriteExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
