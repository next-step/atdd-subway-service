package nextstep.subway.line.domain;

import nextstep.subway.exception.ErrorMessage;
import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.List;
import java.util.Optional;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean hasStation(Station station) {
        return lines.stream().anyMatch(line -> line.hasStation(station));
    }

    public List<Line> getLines() {
        return lines;
    }

    public Path findPath(Station sourceStation, Station targetStation) {
        checkValidateStationsForPath(sourceStation, targetStation);
        PathFinder pathFinder = new PathFinder(this);
        GraphPath path = pathFinder.getGraphPath(sourceStation, targetStation);

        List<Station> stations = path.getVertexList();
        int distance = (int) path.getWeight();

        return Path.of(stations, distance);
    }

    public int getAdditionalFare(Station sourceStation, Station targetStation) {
        checkValidateStationsForPath(sourceStation, targetStation);
        PathFinder pathFinder = new PathFinder(this);
        GraphPath path = pathFinder.getGraphPath(sourceStation, targetStation);

        return  getMaxAdditionalLineFare(path.getEdgeList());
    }

    private void checkValidateStationsForPath(Station sourceStation, Station targetStation) {
        checkSameStation(sourceStation, targetStation);
        checkContainStation(hasStation(sourceStation), hasStation(targetStation));
    }

    private void checkSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException(ErrorMessage.SOURCE_EQUALS_TARGET);
        }
    }

    private void checkContainStation(boolean isSourceStationExisted, boolean isTargetStationExisted) {
        if (!isSourceStationExisted | !isTargetStationExisted) {
            throw new NoSuchElementFoundException(ErrorMessage.NOT_FOUND_STATION_FOR_FIND_PATH);
        }
    }

    private int getMaxAdditionalLineFare(List<SectionEdge> sectionEdges) {
        Optional<SectionEdge> maxSectionEdge = sectionEdges.stream().max((e1, e2) -> Integer.compare(e1.getFare(), e2.getFare()));

        return maxSectionEdge.map(SectionEdge::getFare).orElse(0);

    }
}
