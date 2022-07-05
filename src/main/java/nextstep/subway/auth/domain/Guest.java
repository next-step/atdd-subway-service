package nextstep.subway.auth.domain;


public class Guest implements User {

    @Override
    public boolean isGuest() {
        return true;
    }
}
