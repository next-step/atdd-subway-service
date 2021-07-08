package nextstep.subway.auth.domain;

public class EmptyMember implements AuthMember {

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