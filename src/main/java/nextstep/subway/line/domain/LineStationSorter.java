package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LineStationSorter {

    private static final int FIRST_INDEX = 0;

    public static List<Station> sort(List<Section> sections) {
        List<Station> stations = new ArrayList<>();
        Station currentStation = findFirstStation(sections);

        while(currentStation != null) {
            stations.add(currentStation);
            currentStation = findNextStation(currentStation, sections);
        }

        return stations;
    }

    private static Station findNextStation(Station currentStation, List<Section> sections) {
        Station beforeDownStation = currentStation;

        Optional<Section> nextSection = sections.stream()
                .filter(section -> section.getUpStation().equals(beforeDownStation))
                .findFirst();

        return nextSection.isPresent() ? nextSection.get().getDownStation() : null;
    }

    private static Station findFirstStation(List<Section> sections) {
        Station currentStation = sections.get(FIRST_INDEX).getUpStation();
        Station firstStation = currentStation;

        while (currentStation != null) {
            firstStation = currentStation;
            currentStation = findPrevStation(firstStation, sections);
        }

        return firstStation;
    }

    private static Station findPrevStation(Station beforeUpStation, List<Section> sections) {
        Optional<Section> prevSection = sections.stream()
                .filter(section -> section.getDownStation().equals(beforeUpStation))
                .findFirst();

        return prevSection.isPresent() ? prevSection.get().getUpStation() : null;
    }
}
