package nextstep.subway.auth.domain;

public class NonLoginMember implements Member{

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public AgeGroup getAgeGroup() {
        return AgeGroup.NORMAL;
    }

    @Override
    public boolean isLoginMember() {
        return false;
    }
}
