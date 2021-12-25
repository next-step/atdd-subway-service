package nextstep.subway.auth.domain;

public interface Member {
    Long getId();
    AgeGroup getAgeGroup();
    boolean isLoginMember();
}
