package nextstep.subway.domain.line.domain;

import nextstep.subway.domain.station.domain.Station;
import nextstep.subway.global.exception.NoRegisterSectionException;
import nextstep.subway.global.exception.SectionAlreadyRegisterException;
import nextstep.subway.global.exception.SectionNoRegistrationException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        while (!stations.contains(downStation)) {
            stations.add(downStation);
            downStation = findDownStation(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        final List<Station> upStations = this.sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
        final List<Station> downStations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());

        upStations.removeAll(downStations);

        return upStations.get(0);
    }

    private Station findDownStation(Station upStation) {
        final Optional<Section> station = this.sections.stream()
                .filter(section -> section.getUpStation().equals(upStation))
                .findFirst();

        if (station.isPresent()) {
            return station.get().getDownStation();
        }
        return upStation;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        notExistSectionValidator(upStation, downStation);
        existUpStationAndDownStationValidator(upStation, downStation);

        getUpStation(upStation)
                .ifPresent(section -> section.updateUpStation(downStation, distance));

        getDownStation(downStation)
                .ifPresent(section -> section.updateDownStation(upStation, distance));

        addSection(new Section(line, upStation, downStation, distance));
    }

    private void existUpStationAndDownStationValidator(final Station upStation, final Station downStation) {
        final boolean stationsIsEmpty = getStations().isEmpty();
        final boolean existUpStation = existStation(upStation);
        final boolean existDownStation = existStation(downStation);

        if (!stationsIsEmpty && !existUpStation && !existDownStation) {
            throw new SectionNoRegistrationException();
        }
    }

    private void notExistSectionValidator(Station upStation, Station downStation) {
        final boolean isUpStationExisted = existStation(upStation);
        boolean isDownStationExisted = existStation(downStation);

        if (isUpStationExisted && isDownStationExisted) {
            throw new SectionAlreadyRegisterException();
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
            throw new NoRegisterSectionException();
        }

        Optional<Section> upLineStation = getUpStation(station);
        Optional<Section> downLineStation = getDownStation(station);

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            sectionRelocation(line, upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> this.sections.remove(it));
        downLineStation.ifPresent(it -> this.sections.remove(it));

    }

    private void sectionRelocation(final Line line, final Section upLineStation, final Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        final Distance newDistance = upLineStation.plusDistance(downLineStation);
        addSection(new Section(line, newUpStation, newDownStation, newDistance));
    }

    public List<Section> getSections() {
        return sections;
    }
}
