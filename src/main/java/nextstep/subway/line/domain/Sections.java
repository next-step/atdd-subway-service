package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() { }

    public Sections(List<Section> sections) {
        for (Section section : sections) {
            add(section);
        }
    }

    protected void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        if (containsByUpStation(section) && containsByDownStation(section)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!containsByUpStation(section) && !containsByUpStation(section)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        sections.add(section);
//
//        if (isUpStationExisted) {
//            getSections().stream()
//                    .filter(it -> it.getUpStation() == upStation)
//                    .findFirst()
//                    .ifPresent(it -> it.updateUpStation(downStation, distance));
//
//            getSections().add(section);
//        } else if (isDownStationExisted) {
//            getSections().stream()
//                    .filter(it -> it.getDownStation() == downStation)
//                    .findFirst()
//                    .ifPresent(it -> it.updateDownStation(upStation, distance));
//
//            getSections().add(section);
//        } else {
//            throw new RuntimeException();
//        }
    }

    protected NewSection removeStation(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findByUpStationEquals(station);
        Optional<Section> downLineStation = findByDownStationEquals(station);

        removeSection(upLineStation, downLineStation);

        return createNewSection(upLineStation, downLineStation);
    }

    protected SortedStations toSortedStations() {
        return new SortedStations(sections);
    }

    protected List<Section> toCollection() {
        return sections;
    }

    private NewSection createNewSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();

            return new NewSection(newUpStation, newDownStation, newDistance);
        }
        return null;
    }

    private Optional<Section> findByUpStationEquals(Station station) {
        return sections.stream()
                .filter(item -> item.isUpStationEquals(station))
                .findFirst();
    }

    private Optional<Section> findByDownStationEquals(Station station) {
        return sections.stream()
                .filter(item -> item.isDownStationEquals(station))
                .findFirst();
    }

    private boolean containsByUpStation(Section section) {
        return sections.stream()
                .anyMatch(item -> item.containsByUpStation(section));
    }

    private boolean containsByDownStation(Section section) {
        return sections.stream()
                .anyMatch(item -> item.containsByDownStation(section));
    }

    private boolean containsSameUpStation(Section section) {
        return sections.stream()
                .anyMatch(item -> item.containsSameUpStation(section));
    }

    private boolean containsSameDownStation(Section section) {
        return sections.stream()
                .anyMatch(item -> item.containsSameDownStation(section));
    }

    private void removeSection(Optional<Section> upLineStation, Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> remove(it));
        downLineStation.ifPresent(it -> remove(it));
    }

    private void remove(Section section) {
        sections.remove(section);
    }
}
