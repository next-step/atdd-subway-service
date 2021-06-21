package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.station.domain.Station;

public interface StationPath {
	List<Station> getStations();
	int getTotalDistance();
}
