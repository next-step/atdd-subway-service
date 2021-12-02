package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_section"))
    private List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections of(List<Section> sections) {
        return new Sections(sections);
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Section downStation = findUpStation();
        stations.add(downStation.getUpStation());

        while (downStation != null) {
            stations.add(downStation.getDownStation());
            Station nextUpStation = downStation.getDownStation();

            downStation = findSectionByUpStation(nextUpStation).orElse(null);
        }

        return stations;
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        if (sections.isEmpty()) {
            sections.add(new Section(line, upStation, downStation, distance));
            return;
        }

        List<Station> stations = getOrderedStations();
        validateAbleToAdd(stations, upStation, downStation);

        sections.stream()
                .filter(section -> section.isEqualToUpStation(upStation) || section.isEqualToDownStation(downStation))
                .findFirst()
                .ifPresent(section -> updateSection(section, upStation, downStation, distance));

        sections.add(new Section(line, upStation, downStation, distance));
    }

    public void removeStation(Line line, Station targetStation) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findSectionByUpStation(targetStation);
        Optional<Section> downLineStation = findSectionByDownStation(targetStation);

        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new RuntimeException("존재하지 않는 역 입니다.");
        }

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(section -> sections.remove(section));
        downLineStation.ifPresent(section -> sections.remove(section));
    }

    private void updateSection(Section section, Station upStation, Station downStation, int distance) {
        if (section.isEqualToUpStation(upStation)) {
            section.updateUpStation(downStation, distance);
            return;
        }

        if (section.isEqualToDownStation(downStation)) {
            section.updateDownStation(upStation, distance);
            return;
        }

        throw new RuntimeException();
    }

    private void validateAbleToAdd(List<Station> stations, Station upStation, Station downStation) {
        boolean isUpStationExisted = stations.contains(upStation);
        boolean isDownStationExisted = stations.contains(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted
                && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private Section findUpStation() {
        Section downSection = sections.get(0);
        assert (downSection != null);
        Section resultUpSection = null;

        while (downSection != null) {
            resultUpSection = downSection;
            Station nextDownStation = downSection.getUpStation();

            downSection = findSectionByDownStation(nextDownStation).orElse(null);
        }

        return resultUpSection;
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualToUpStation(station))
                .findFirst();
    }

    private Optional<Section> findSectionByDownStation(Station station) {
        return sections.stream()
                .filter(section -> section.isEqualToDownStation(station))
                .findFirst();
    }
}
