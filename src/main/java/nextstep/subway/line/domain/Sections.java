package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            addValidate(section);
            updateSection(section);
        }
        sections.add(section);
    }

    public int getTotalDistance() {
        return sections.stream().mapToInt(section -> section.getDistance()).sum();
    }

    public List<Station> getStations() {
        Station departStation = findUpStation();
        return sortedStations(departStation);
    }

    public void remove(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException("구간을 삭제 할수 없습니다.");
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private Station findUpStation() {
        Section notOrderFirstSection = sections.stream().findFirst().orElseThrow(RuntimeException::new);
        Station departStation = notOrderFirstSection.getUpStation();

        while (isExistUpSection(departStation)) {
            final Station finalDepartStation = departStation;
            Section nextUpSection = sections.stream()
                    .filter(section -> section.getDownStation().equals(finalDepartStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            departStation = nextUpSection.getUpStation();
        }

        return departStation;
    }

    private List<Station> sortedStations(Station upStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);

        while (isExistDownStation(upStation)) {
            final Station finalUpStation = upStation;
            Section nextDownSection = sections.stream()
                    .filter(section -> section.getUpStation().equals(finalUpStation))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            upStation = nextDownSection.getDownStation();
            stations.add(upStation);
        }

        return stations;
    }

    private boolean isExistUpSection(Station departStation) {
        return sections.stream().anyMatch(section -> section.getDownStation().equals(departStation));
    }

    private boolean isExistDownStation(Station upSection) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(upSection));
    }

    private void addValidate(Section section) {
        Set<Station> stations = sections.stream()
                .flatMap(o -> Stream.of(o.getUpStation(), o.getDownStation()))
                .collect(Collectors.toSet());
        if (stations.contains(section.getUpStation()) && stations.contains(section.getDownStation())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
        if (!stations.contains(section.getUpStation()) && !stations.contains(section.getDownStation())) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateSection(Section newSection) {
        sections.stream()
                .filter(section -> section.getUpStation().equals(newSection.getUpStation()))
                .findFirst()
                .ifPresent(section -> section.updateUpStation(newSection));

        sections.stream()
                .filter(it -> it.getDownStation().equals(newSection.getDownStation()))
                .findFirst()
                .ifPresent(section -> section.updateDownStation(newSection));
    }
}


