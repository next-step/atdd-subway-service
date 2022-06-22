package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;

public class Lines {

    private final List<Line> items;

    public Lines(List<Line> items) {
        this.items = items;
    }

    public void foreach(Consumer<Line> consumer) {
        items.forEach(consumer);
    }

    public Sections findSections(List<Station> stations) {
        return new Sections(stationsMapToSection(stations).stream()
                .map(this::findSection)
                .collect(Collectors.toList()));
    }

    private List<Section> stationsMapToSection(List<Station> stations) {
        List<Section> sectionList = new ArrayList<>();
        for (int i = 0; i < stations.size() - 1; i++) {
            sectionList.add(new Section(stations.get(i), stations.get(i + 1)));
        }
        return sectionList;
    }

    private Section findSection(Section section) {
        return items.stream()
                .filter(line -> line.hasSection(section))
                .findFirst()
                .map(line -> line.bindDistance(section))
                .orElseThrow(() -> new NoSuchElementException("구간을 찾을 수 없습니다."));
    }

    public boolean hasStation(Station station) {
        return items.stream()
                .anyMatch(line -> line.getStations().contains(station));
    }
}
