package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class FarePolicyElements {

    private int distance;
    private LoginMember loginMember;
    private List<Station> stations;
    private List<Line> persistLines;

    public FarePolicyElements() {
    }

    public FarePolicyElements(int distance, LoginMember loginMember, List<Station> stations, List<Line> persistLines) {
        this.distance = distance;
        this.loginMember = loginMember;
        this.stations = stations;
        this.persistLines = persistLines;
    }

    public int getDistance() {
        return distance;
    }

    public LoginMember getLoginMember() {
        return loginMember;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getPersistLines() {
        return persistLines;
    }
}
