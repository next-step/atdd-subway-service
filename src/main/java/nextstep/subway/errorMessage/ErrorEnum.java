package nextstep.subway.errorMessage;

public enum ErrorEnum {
    NOT_FOUND_STATION("역을 찾을 수 없습니다."),
    FAVORITE_ALREADY_ADDED("이미 생성 된 즐겨찾기 구간입니다."),
    NOT_FOUND_FAVORITE("즐겨 찾기로 설정 된 구간이 없습니다."),
    INVALID_DISTANCE("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
    SECTION_IS_ALREADY_ADD("이미 등록된 구간 입니다."),
    CANT_ADD_THIS_SECTION("등록할 수 없는 구간 입니다."),
    NOT_FOUND_SECTION("구간을 찾을 수 없습니다."),
    SECTIONS_HAVE_ONLY_ONE("구간이 1개밖에 없습니다."),
    SAME_STATION("같은 역입니다."),
    INVALID_FARE_AMOUNT("요금은 음수가 될 수 없습니다."),
    ;


    ErrorEnum(String message) {
        this.message = message;
    }

    private final String message;

    public String message() {
        return message;
    }

}
