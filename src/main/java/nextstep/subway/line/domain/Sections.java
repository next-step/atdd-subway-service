package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.NOT_FOUND_SECTION;
import static nextstep.subway.line.enums.LineExceptionType.NOT_FOUND_UP_STATION_BY_SECTION;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.StreamUtils;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    private Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections from(List<Section> sections) {
        return new Sections(sections);
    }

    public static Sections createEmpty() {
        return new Sections(Lists.newArrayList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public List<Section> getSections() {
        return this.sections;
    }

    public List<Station> findSortedStations() {
        List<Station> sortedStations = Lists.newArrayList();
        sortedStations.add(findFirstSection().getUpStation());

        for (int i = 0; i < this.sections.size(); i++) {
            Station station = this.findSectionByUpStation(sortedStations.get(i))
                .map(Section::getDownStation)
                .orElseThrow(() -> new IllegalStateException(NOT_FOUND_UP_STATION_BY_SECTION.getMessage()));

            sortedStations.add(station);
        }
        return sortedStations;
    }

    public Station findUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;

            Optional<Section> nextLineStation = StreamUtils.filterAndFindFirst(this.sections,
                it -> it.getDownStation() == finalDownStation);

            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = Lists.newArrayList();
        Station downStation = this.findUpStation();
        stations.add(this.findUpStation());

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = StreamUtils.filterAndFindFirst(this.sections, it -> it.getUpStation() == finalDownStation);
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Optional<Section> findSectionByUpStation(Station station) {
        return StreamUtils.filterAndFindFirst(this.sections,
            section -> section.isEqualsUpStation(station));
    }

    private Section findFirstSection() {
        List<Station> downStations = this.findDownStations();
        return StreamUtils.filterAndFindFirst(this.sections, section -> !downStations.contains(section.getUpStation()))
            .orElseThrow(() -> new IllegalStateException(NOT_FOUND_SECTION.getMessage()));
    }

    private List<Station> findDownStations() {
        return StreamUtils.mapToList(this.sections, Section::getDownStation);
    }

}
