package nextstep.subway.line.domain;

import java.util.List;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathGraph;
import nextstep.subway.path.domain.PathGraphCreator;
import nextstep.subway.station.domain.Station;

public class Lines implements PathGraphCreator {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public int getFinalSurcharge(Path path) {
        List<Station> pathStations = path.getPathVertexes(Station.class);
        return lines.stream().filter(line -> line.inUsedBy(pathStations))
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }

    @Override
    public PathGraph createPathGraph() {
        PathGraph pathGraph = new PathGraph();
        lines.forEach(line -> line.addPathInfoTo(pathGraph));
        return pathGraph;
    }
}
