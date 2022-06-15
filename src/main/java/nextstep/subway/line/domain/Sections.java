package nextstep.subway.line.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @JsonManagedReference
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.size() == 0;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station station = findUpStation();
        stations.add(station);

        while (station != null) {
            Station finalStation = station;
            Optional<Section> section = sections.stream()
                                                .filter(sec -> sec.containsUpStation(finalStation))
                                                .findFirst();
            if (!section.isPresent()) {
                break;
            }
            station = section.get().getDownStation();
            stations.add(station);
        }

        return stations;
    }

    public Station findUpStation() {
        Station station = sections.get(0).getUpStation();

        while (station != null) {
            Station finalStation = station;
            Optional<Section> section = sections.stream()
                                                .filter(sec -> sec.containsDownStation(finalStation))
                                                .findFirst();
            if (!section.isPresent()) {
                break;
            }
            station = section.get().getUpStation();
        }

        return station;
    }

    public void addSection(Section section) {
        if (isEmpty()) {
            sections.add(section);
            return;
        }

        List<Station> stations = getStations();
        boolean isUpStationExisted = isUpStationExisted(stations, section);
        boolean isDownStationExisted = isDownStationExisted(stations, section);
        confirmStationIsOnLine(isUpStationExisted, isDownStationExisted);
        confirmPossibleLinkToLine(stations, section);

        if (isUpStationExisted) {
            updateUpStationReducedDistance(section);
        } else if (isDownStationExisted) {
            updateDownStationReducedDistance(section);
        }
        sections.add(section);
    }

    public boolean isUpStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(section::containsUpStation);
    }

    public boolean isDownStationExisted(List<Station> stations, Section section) {
        return stations.stream().anyMatch(section::containsDownStation);
    }

    public void confirmStationIsOnLine(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    public void confirmPossibleLinkToLine(List<Station> stations, Section section) {
        if (!stations.isEmpty()
                && stations.stream().noneMatch(section::containsUpStation)
                && stations.stream().noneMatch(section::containsDownStation)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    public void updateUpStationReducedDistance(Section section) {
        sections.stream()
                .filter(sec -> section.containsUpStation(sec.getUpStation()))
                .findFirst()
                .ifPresent(sec -> sec.updateUpStationReducedDistance(section));
    }

    public void updateDownStationReducedDistance(Section section) {
        sections.stream()
                .filter(sec -> section.containsDownStation(sec.getDownStation()))
                .findFirst()
                .ifPresent(sec -> sec.updateDownStationReducedDistance(section));
    }

    public void removeSection(Station station) {
        if (sections.size() == 1) {
            throw new RuntimeException("마지막 구간은 삭제할 수 없습니다.");
        }
        if (getStations().stream().noneMatch(st -> st.equals(station))) {
            throw new RuntimeException(String.format("%s 역은 노선에 존재하지 않습니다.", station.getName()));
        }

        if (isUpStationInSection(station)) {
            updateDownStationIncreasedDistance(station);
            return;
        }
        updateUpStationIncreasedDistance(station);
    }

    public boolean isUpStationInSection(Station station) {
        return sections.stream().anyMatch(section -> section.getUpStation().equals(station));
    }

    public void updateDownStationIncreasedDistance(Station station) {
        Section section = sections.stream()
                                  .filter(sec -> sec.containsUpStation(station))
                                  .findFirst().orElseThrow(RuntimeException::new);
        sections.stream()
                .filter(sec -> sec.containsDownStation(station))
                .findFirst()
                .ifPresent(sec -> sec.updateDownStationIncreasedDistance(section));
        sections.remove(section);
    }

    public void updateUpStationIncreasedDistance(Station station) {
        Section section = sections.stream()
                                  .filter(sec -> sec.containsDownStation(station))
                                  .findFirst().orElseThrow(RuntimeException::new);
        sections.stream()
                .filter(sec -> sec.containsUpStation(station))
                .findFirst()
                .ifPresent(sec -> sec.updateUpStationIncreasedDistance(sec));
        sections.remove(section);
    }
}
