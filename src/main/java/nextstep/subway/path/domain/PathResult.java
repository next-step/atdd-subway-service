package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public class PathResult {

    private final Sections sections;
    private final Stations stations;

    public PathResult(Sections sections, Stations stations) {
        this.sections = sections;
        this.stations =stations;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public List<Section> getSections() {
        return sections.getSections();
    }

    public int getTotalDistance() {
        return 0;
    }
}
