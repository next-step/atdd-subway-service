package nextstep.subway.auth.domain;

public class AnonymousMember implements LoginMember {
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Integer getAge() {
        return null;
    }
}
