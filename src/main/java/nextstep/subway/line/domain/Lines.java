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

    public Path findPath(Station sourceStation, Station targetStation, int age) {
        checkValidateStationsForPath(sourceStation, targetStation);
        PathFinder pathFinder = new PathFinder(this);
        GraphPath path = pathFinder.getGraphPath(sourceStation, targetStation);

        List<Station> stations = path.getVertexList();
        int distance = (int) path.getWeight();
        List<SectionEdge> sectionEdges = path.getEdgeList();
        int additionalFare = getMaxAdditionalLineFare(sectionEdges);

        return Path.of(stations, distance, additionalFare, age);
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
        int maxFare = 0;

        for (SectionEdge sectionEdge : sectionEdges) {
            int lineFare = sectionEdge.getFare();

            if (maxFare < lineFare) {
                maxFare = lineFare;
            }
        }

        return maxFare;
    }
}
