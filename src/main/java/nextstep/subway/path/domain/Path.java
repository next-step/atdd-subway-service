package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class Path {
    private List<Station> stations;
    private int distance;
    private int surcharge;

    private Path() {
    }

    private Path(List<Station> stations, int distance, int surcharge) {
        this.stations = stations;
        this.distance = distance;
        this.surcharge = surcharge;
    }

    public static Path of(List<Station> stations, List<SectionEdge> sectionEdges, int distance) {
        return new Path(stations, distance, calculatorMaxSurcharge(sectionEdges));
    }
    
    private static int calculatorMaxSurcharge(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .map(SectionEdge::getSection)
                .map(Section::getLine)
                .mapToInt(Line::getSurcharge)
                .max()
                .orElse(0);
    }
    
    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getSurcharge() {
        return surcharge;
    }
    
}
