package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(it -> it.getUpStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = this.sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        final boolean isUpStationExisted = existStation(upStation);
        boolean isDownStationExisted = existStation(downStation);

        notExistSectionValidator(isUpStationExisted, isDownStationExisted);
        existUpStationAndDownStationValidator(upStation, downStation);

        if (isUpStationExisted) {
            getUpStation(upStation)
                    .ifPresent(section -> section.updateUpStation(downStation, distance));
        }

        if (isDownStationExisted) {
            getDownStation(downStation)
                    .ifPresent(section -> section.updateDownStation(upStation, distance));
        }

        addSection(new Section(line, upStation, downStation, distance));
    }

    private void existUpStationAndDownStationValidator(final Station upStation, final Station downStation) {
        final boolean stationsIsEmpty = getStations().isEmpty();
        final boolean existUpStation = existStation(upStation);
        final boolean existDownStation = existStation(downStation);

        if (!stationsIsEmpty && !existUpStation && !existDownStation) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void notExistSectionValidator(final boolean isUpStationExisted, final boolean isDownStationExisted) {
        if (isUpStationExisted && isDownStationExisted) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private Optional<Section> getUpStation(Station upStation) {
        return sections.stream()
                .filter(section -> section.getUpStation().equals(upStation))
                .findFirst();
    }

    private Optional<Section> getDownStation(Station downStation) {
        return sections.stream()
                .filter(section -> section.getDownStation().equals(downStation))
                .findFirst();
    }

    private boolean existStation(Station station) {
        return getStations().stream()
                .anyMatch(st -> st.equals(station));
    }

    public void removeSection(Line line, Station station) {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }

        Optional<Section> upLineStation = getUpStation(station);
        Optional<Section> downLineStation = getDownStation(station);

        if (isBetweenStationRemove(upLineStation, downLineStation)) {
            sectionRelocation(line, upLineStation.get(), downLineStation.get());
        }

        isNotBetweenStationRemove(upLineStation, downLineStation);
    }

    private void isNotBetweenStationRemove(final Optional<Section> upLineStation, final Optional<Section> downLineStation) {
        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));
    }

    private boolean isBetweenStationRemove(final Optional<Section> upLineStation, final Optional<Section> downLineStation) {
        return upLineStation.isPresent() && downLineStation.isPresent();
    }

    private void sectionRelocation(final Line line, final Section upLineStation, final Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        addSection(new Section(line, newUpStation, newDownStation, newDistance));
    }
}
