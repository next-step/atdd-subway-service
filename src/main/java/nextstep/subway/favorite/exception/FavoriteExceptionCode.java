package nextstep.subway.favorite.exception;

public enum FavoriteExceptionCode {
    NONE_EXISTS_MEMBER("회원정보를 입력해야 합니다"),
    NONE_EXISTS_SOURCE_STATION("출발역을 지정해야 합니다"),
    NONE_EXISTS_TARGET_STATION("도착역을 지정해야 합니다");
    private final String message;

    FavoriteExceptionCode(final String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
