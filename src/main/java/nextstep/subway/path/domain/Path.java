package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.station.domain.Station;

public interface Path {

    public List<Station> getStations();

    public int getDistance();

    public int getSurcharge();

}
