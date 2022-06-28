package nextstep.subway.member.domain;

public enum UserType {
    NORMAL("일반 사용자"), GUEST("게스트 사용자");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}
