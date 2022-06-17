package nextstep.subway.favorite.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

public class Favorite {
    private Long id;
    private LoginMember loginMember;
    private Station source;
    private Station destination;

    protected Favorite() {
    }

    public Favorite(LoginMember loginMember, Station source, Station destination) {
        this.loginMember = loginMember;
        this.source = source;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }

    public Station getSource() {
        return source;
    }

    public Station getDestination() {
        return destination;
    }
}
