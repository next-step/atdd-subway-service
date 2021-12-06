package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    List<Station> getStationsInOrder() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station station = findFirstUpStation();
        stations.add(station);

        while (isExistNextSection(station)) {
            Section nextSection = findNextSection(station);
            station = nextSection.getDownStation();
            stations.add(station);
        }
        return stations;
    }

    private Station findFirstUpStation() {
        Station upStation = sections.get(0).getUpStation();
        while (isExistPreSection(upStation)) {
            Section preSection = findPreSection(upStation);
            upStation = preSection.getUpStation();
        }
        return upStation;
    }

    private boolean isExistPreSection(Station upStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsDownStation(upStation));
    }

    private Section findPreSection(Station upStation) {
        return sections.stream()
                .filter(section -> section.equalsDownStation(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이전 구간 없습니다."));
    }

    private boolean isExistNextSection(Station downStation) {
        return sections.stream()
                .anyMatch(section -> section.equalsUpStation(downStation));
    }

    private Section findNextSection(Station downStation) {
        return sections.stream()
                .filter(section -> section.equalsUpStation(downStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("다음 구간 없습니다."));
    }
}
