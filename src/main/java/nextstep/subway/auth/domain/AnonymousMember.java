package nextstep.subway.auth.domain;

public class AnonymousMember implements AuthMember {
    @Override
    public Integer getAge() {
        return 0;
    }
}
