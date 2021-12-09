package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import nextstep.subway.common.exception.SubwayErrorCode;
import nextstep.subway.common.exception.SubwayException;
import nextstep.subway.station.domain.Station;

public class Stations {
    private List<Station> stations;

    public Stations() {
        this(new ArrayList<>());
    }

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public boolean containsStation(Station station) {
        return stations.stream().anyMatch(station::equals);
    }

    public boolean isNotEmpty() {
        return !stations.isEmpty();
    }

    public void checkCanAddStation(Section section) {
        boolean containsUpStation = containsStation(section.getUpStation());
        boolean containsDownStation = containsStation(section.getDownStation());

        checkAllStationExists(containsUpStation, containsDownStation);
        checkContainsAnyStation(containsUpStation, containsDownStation);
    }

    private void checkAllStationExists(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new SubwayException(SubwayErrorCode.ALREADY_REGISTERED_SECTION);
        }
    }

    private void checkContainsAnyStation(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (!isUpStationExisted && !isDownStationExisted) {
            throw new SubwayException(SubwayErrorCode.INVALID_LINE_SECTION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stations)) {
            return false;
        }
        Stations stations1 = (Stations)o;
        return Objects.equals(stations, stations1.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }
}
