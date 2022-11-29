package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private final List<Line> lines = new ArrayList();

    public PathFinder(List<Line> lines) {
        this.lines.addAll(lines);
    }

    public PathFinder(Line line) {
        lines.add(line);
    }

    public PathFinder() {

    }

    public List<StationResponse> getShortestPath(){

        return null;
    }



}
