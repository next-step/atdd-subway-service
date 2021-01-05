package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST,
            CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        if (sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        boolean isUpStationExisted = contains(section.getUpStation());
        boolean isDownStationExisted = contains(section.getDownStation());

        validate(isUpStationExisted, isDownStationExisted);

        if (isUpStationExisted) {
            findFirstSection(it -> it.getUpStation() == section.getUpStation())
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(),
                            section.getDistance()));
            this.sections.add(section);
            return;
        }

        if (isDownStationExisted) {
            findFirstSection(it -> it.getDownStation() == section.getDownStation())
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(),
                            section.getDistance()));
            this.sections.add(section);
            return;
        }

        throw new RuntimeException();
    }

    public void remove(Line line, Station station) {

        if (this.sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = findFirstSection(it -> it.getUpStation() == station);
        Optional<Section> downLineStation = findFirstSection(it -> it.getDownStation() == station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().getDistance() + downLineStation.get().getDistance();
            this.sections.add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = findFirstSection(it -> it.getUpStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            Optional<Section> nextLineStation = findFirstSection(it -> it.getDownStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    private Optional<Section> findFirstSection(Predicate<Section> predicate) {
        return this.sections.stream().filter(predicate).findFirst();
    }

    private boolean contains(Station station) {
        return this.sections.stream()
                .anyMatch(it -> it.getUpStation() == station || it.getDownStation() == station);
    }

    private void validate(boolean isUpStationExisted, boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }

        if (!isUpStationExisted && !isDownStationExisted) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }
}
