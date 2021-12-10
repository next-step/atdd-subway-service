package nextstep.subway.line.domain;

import nextstep.subway.common.exception.CannotAddException;
import nextstep.subway.common.exception.CannotRemoveException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("ALL")
@Embeddable
public class Sections {
    private static final String SECTION = "구간";
    private static final int SECTION_SIZE_MIN = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {

    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public void addSection(Line line, Station upStation, Station downStation, Distance distance) {
        validate(upStation, downStation);

        List<Station> stations = stations();
        if (!stations.isEmpty()) {
            addIfExistUpStation(line, upStation, downStation, distance, stations);
            addIfExistDownStation(line, upStation, downStation, distance, stations);
            return;
        }
        sections.add(Section.of(line, upStation, downStation, distance));
    }

    private void addIfExistUpStation(Line line, Station upStation, Station downStation, Distance distance,
                                     List<Station> stations) {
        if (stations.contains(upStation)) {
            addLineBaseOfUpStation(distance, line, upStation, downStation);
        }
    }

    private void addIfExistDownStation(Line line, Station upStation, Station downStation, Distance distance,
                                       List<Station> stations) {
        if (stations.contains(downStation)) {
            addLineBaseOfDownStation(distance, line, upStation, downStation);
        }
    }

    public void removeLineStation(Line line, Station station) {
        if (sections.size() <= SECTION_SIZE_MIN) {
            throw new CannotRemoveException(SECTION);
        }

        Optional<Section> upLineStation = findSectionBasedOnUpStation(station);
        Optional<Section> downLineStation = findSectionBasedOnDownStation(station);

        addSectionIfExistBothStations(line, upLineStation, downLineStation);

        upLineStation.ifPresent(sections::remove);
        downLineStation.ifPresent(sections::remove);
    }

    public List<Station> stations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        Optional<Section> nextLineStation = findSectionBasedOnUpStation(downStation);
        while (downStation != null && nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
            nextLineStation = findSectionBasedOnUpStation(downStation);
        }

        return stations;
    }

    private Station findUpStation() {
        Station downStation = sections.get(0).getUpStation();
        Optional<Section> nextLineStation = findSectionBasedOnDownStation(downStation);
        while (downStation != null && nextLineStation.isPresent()) {
            downStation = nextLineStation.get().getUpStation();
            nextLineStation = findSectionBasedOnDownStation(downStation);
        }
        return downStation;
    }

    private void addSectionIfExistBothStations(Line line, final Optional<Section> upLineStation,
                                               final Optional<Section> downLineStation) {
        downLineStation.ifPresent(downLineSection -> upLineStation.ifPresent(upLineSection -> {
            Section section = Section.of(line,
                downLineSection.getUpStation(),
                upLineSection.getDownStation(),
                upLineSection.getDistance().add(downLineSection.getDistance())
            );
            sections.add(section);
        }));
    }

    private Optional<Section> findSectionBasedOnUpStation(Station station) {
        return sections.stream()
            .filter(section -> section.getUpStation() == station)
            .findFirst();
    }

    private Optional<Section> findSectionBasedOnDownStation(Station station) {
        return sections.stream()
            .filter(section -> section.getDownStation() == station)
            .findFirst();
    }

    private void addLineBaseOfUpStation(final Distance distance, final Line line, final Station upStation,
                                        final Station downStation) {
        sections.stream()
            .filter(section -> section.getUpStation() == upStation)
            .findFirst()
            .ifPresent(section -> section.updateUpStation(downStation, distance));

        sections
            .add(Section.of(line, upStation, downStation, distance));
    }

    private void addLineBaseOfDownStation(final Distance distance, final Line line, final Station upStation,
                                          final Station downStation) {
        sections.stream()
            .filter(section -> section.getDownStation() == downStation)
            .findFirst()
            .ifPresent(section -> section.updateDownStation(upStation, distance));

        sections.add(Section.of(line, upStation, downStation, distance));
    }

    private void validate(final Station upStation, final Station downStation) {
        List<Station> stations = sections.stream()
            .flatMap(e -> Stream.of(e.getUpStation(), e.getDownStation()))
            .distinct()
            .collect(Collectors.toList());

        if (isExistBothStation(upStation, downStation, stations)) {
            throw new CannotAddException(SECTION);
        }

        if (!stations.isEmpty() && isExistNeitherStation(upStation, downStation, stations)) {
            throw new CannotAddException(SECTION);
        }
    }

    private boolean isExistBothStation(Station upStation, Station downStation, List<Station> stations) {
        return stations.contains(upStation) && stations.contains(downStation);
    }

    private boolean isExistNeitherStation(Station upStation, Station downStation, List<Station> stations) {
        return !stations.contains(upStation) && !stations.contains(downStation);
    }

    public List<Section> sections() {
        return sections;
    }
}
