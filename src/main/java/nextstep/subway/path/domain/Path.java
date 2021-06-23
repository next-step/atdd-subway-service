package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public interface Path {

    public int getMinDistance();

    public List<Station> getStations();

}
