package nextstep.subway.favorite.exception;

public enum FavoriteExceptionCode {
    NULL_MEMBER("회원정보를 입력해야 합니다"),
    NULL_SOURCE_STATION("출발역을 지정해야 합니다"),
    NULL_TARGET_STATION("도착역을 지정해야 합니다"),
    NONE_EXISTS_FAVORITE("존재하지 않는 즐겨찾기 입니다"),
    NONE_EXISTS_MEMBER("존재하지 않는 회원입니다"),
    NONE_EXISTS_SOURCE_STATION("존재하지 않는 출발역입니다"),
    NONE_EXISTS_TARGET_STATION("존재하지 않는 도착역입니다"),
    ALREADY_REGISTER("이미 등록된 역입니다"),
    NOT_OWNER_FAVORITE("등록한사람과 다른사람입니다");

    private final String message;

    FavoriteExceptionCode(final String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}