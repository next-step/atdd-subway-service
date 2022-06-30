package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Path {
    private final Stations stations;
    private final int distance;
    private final Charge charge;

    public Path(List<Station> stations, int distance, Charge charge) {
        this.stations = new Stations(stations);
        this.distance = distance;
        this.charge = charge;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStationResponses() {
        return stations.toStationResponses();
    }

    public Integer getFare() {
        return charge.getFare();
    }

    public void updateByLoginMember(LoginMember loginMember) {
        if(!loginMember.isGuest()) {
            charge.updateAgeType(loginMember.getAge());
        }
    }
}
