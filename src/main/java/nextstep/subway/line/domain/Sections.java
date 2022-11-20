package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void addSection(Section section) {
        // TODO validation 추가 필요
        sections.add(section);
    }

    public List<Station> findStations() {
        return sections.stream()
                .map(Section::stations)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Station> findInOrderStations() {
        Map<Station, Station> stations = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        return sortStations(findLineUpStation(stations), stations);
    }

    private Station findLineUpStation(Map<Station, Station> stations) {
        return stations.keySet()
                .stream()
                .filter(upStation -> !stations.containsValue(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.상행종착역은_비어있을_수_없음.getErrorMessage()));
    }

    private List<Station> sortStations(Station lineUpStation, Map<Station, Station> stations) {
        List<Station> sortStations = new ArrayList<>();
        sortStations.add(lineUpStation);
        Station currentStation = lineUpStation;
        while(stations.get(currentStation) != null) {
            currentStation = stations.get(currentStation);
            sortStations.add(currentStation);
        }
        return sortStations;
    }

    public List<Section> getSections() { // TODO 추후 리팩토링 작업에 의해 제거 예정
        return sections;
    }
}
