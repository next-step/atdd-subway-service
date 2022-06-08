package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.AlreadyExistException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections;

    public Sections() {
        sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        validateSection(section);
        this.sections.forEach(it -> it.update(section));
        this.sections.add(section);
    }

    private void validateSection(Section section) {
        Station upStation = section.upStation();
        Station downStation = section.downStation();

        if (hasAlreadyStations(upStation, downStation)) {
            throw new AlreadyExistException("이미 등록된 구간 입니다.");
        }

        if (hasNothingStations(upStation, downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간 입니다.");
        }
    }

    private boolean hasAlreadyStations(Station upStation, Station downStation) {
        return isUpStationExisted(upStation) && isDownStationExisted(downStation);
    }

    private boolean hasNothingStations(Station upStation, Station downStation) {
        return !isUpStationExisted(upStation) && !isDownStationExisted(downStation);
    }

    private boolean isUpStationExisted(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
    }

    private boolean isDownStationExisted(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findFirstUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findUpSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().downStation();
            stations.add(downStation);
        }
        return stations;
    }

    private Station findFirstUpStation() {
        Station downStation = this.sections.get(0).upStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findDownSection(finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().upStation();
        }
        return downStation;
    }

    public Optional<Section> findUpSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.upStation() == station)
                .findFirst();
    }

    public Optional<Section> findDownSection(Station station) {
        return this.sections.stream()
                .filter(it -> it.downStation() == station)
                .findFirst();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public int size() {
        return this.sections.size();
    }

    public void remove(Section section) {
        this.sections.remove(section);
    }

    public void init(Section section) {
        this.sections.add(section);
    }

    public void deleteSection(Station station) {
        validateSize();
        Optional<Section> upLineStation = findUpSection(station);
        Optional<Section> downLineStation = findDownSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().upStation();
            Station newDownStation = upLineStation.get().downStation();
            Distance newDistance = upLineStation.get().getDistance().add(downLineStation.get().getDistance());
            this.sections.add(new Section(upLineStation.get().getLine(), newUpStation, newDownStation, newDistance));
        }
        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private void validateSize() {
        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
