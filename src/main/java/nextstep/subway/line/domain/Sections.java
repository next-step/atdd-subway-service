package nextstep.subway.line.domain;

import nextstep.subway.line.exception.SectionNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }

        boolean isUpStationExisted = isExistStation(section.getUpStation());
        boolean isDownStationExisted = isExistStation(section.getDownStation());

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }

        if (isUpStationExisted) {
            sections.stream()
                    .filter(it -> it.getUpStation() == section.getUpStation())
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));

            sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else if (isDownStationExisted) {
            sections.stream()
                    .filter(it -> it.getDownStation() == section.getDownStation())
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

            sections.add(new Section(section.getLine(), section.getUpStation(), section.getDownStation(), section.getDistance()));
        } else {
            throw new RuntimeException();
        }
    }

    public void removeSection(Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = sections.stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = sections.stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Line line = upLineStation.get().getLine();
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Section firstSection = findFirstSection();
        stations.add(firstSection.getUpStation());

        Section nextSection = firstSection;
        while (isNotNull(nextSection)) {
            stations.add(nextSection.getDownStation());
            nextSection = findNextSection(nextSection.getDownStation()).orElse(null);
        }
        return stations;
    }

    public List<StationResponse> getStationResponses() {
        return getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<Section> getSections() {
        return sections;
    }

    private boolean isExistStation(Station station) {
        return getStations().contains(station);
    }

    private Section findFirstSection() {
        List<Station> downStations = getDownStations();
        return sections.stream()
                .filter(section -> !downStations.contains(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException("첫번째 Section을 찾을 수가 없습니다."));
    }

    private boolean isNotNull(Section section) {
        return section != null;
    }

    private Optional<Section> findNextSection(Station downStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(downStation))
                .findFirst();
    }

    private List<Station> getDownStations() {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }
}
