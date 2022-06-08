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

    public void initSection(Section section) {
        this.sections.add(section);
    }

    public void addSection(Section section) {
        validateSection(section);
        this.sections.forEach(it -> it.update(section));
        this.sections.add(section);
    }

    public void deleteSection(Station station) {
        validateSize();
        Optional<Section> upLineStation = findUpSection(station);
        Optional<Section> downLineStation = findDownSection(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Section downLineSection = downLineStation.get();
            Section upLineSection = upLineStation.get();
            Distance newDistance = downLineSection.getDistance().add(upLineSection.getDistance());
            this.sections.add(new Section(upLineSection.getLine(), downLineSection.upStation(),
                    upLineSection.downStation(), newDistance));
        }
        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    public List<Station> getStations() {
        if (isEmpty()) {
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
        return isStationExisted(upStation) && isStationExisted(downStation);
    }

    private boolean hasNothingStations(Station upStation, Station downStation) {
        return !isStationExisted(upStation) && !isStationExisted(downStation);
    }

    private boolean isStationExisted(Station station) {
        return getStations().stream()
                .anyMatch(it -> it == station);
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

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    private void validateSize() {
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException("노선의 구간이 존재하지 않거나 유일한 구간입니다.");
        }
    }
}
