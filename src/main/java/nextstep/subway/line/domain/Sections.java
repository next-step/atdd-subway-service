package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.exeption.CanNotDeleteStateException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.exeption.CanNotAddStationException;
import nextstep.subway.station.exeption.RegisteredStationException;

@Embeddable
public class Sections {

    public static final int FIRST_INDEX = 0;
    public static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public List<Station> getStations() {
        List<Section> copyList = copySections();

        Set<Station> stations = new LinkedHashSet<>();
        for (Section section : copyList) {
            copyList.stream().filter(s -> isConnectUpStation(s, section)).findFirst()
                             .ifPresent(s -> stations.addAll(Arrays.asList(s.getUpStation(), s.getDownStation())));
            stations.addAll(Arrays.asList(section.getUpStation(), section.getDownStation()));
        }

        return new ArrayList<>(stations);
    }

    private List<Section> copySections() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        List<Section> copyList = new ArrayList<>(this.sections);
        Section section = findFirstSection(sections.get(FIRST_INDEX));
        copyList.remove(section);
        copyList.add(FIRST_INDEX, section);

        return copyList;
    }

    private Section findFirstSection(Section section) {
        return sections.stream().filter(s -> isConnectUpStation(s, section))
                                .findFirst()
                                .map(this::findFirstSection)
                                .orElse(section);
    }

    private boolean isConnectUpStation(Section target, Section compare) {
        return !target.equals(compare) && target.getDownStation().equals(compare.getUpStation());
    }

    public void addStation(Line line, Station upStation, Station downStation, int distance) {
        validateStation(upStation, downStation);

        sections.stream()
                .filter(s -> s.getUpStation().equals(upStation) || s.getDownStation().equals(downStation))
                .findFirst()
                .ifPresent(s -> s.updateStation(upStation, downStation, distance));
        sections.add(new Section(line, upStation, downStation, distance));
    }

    private void validateStation(Station upStation, Station downStation) {
        if (!sections.isEmpty() && containsStation(upStation) && containsStation(downStation)) {
            throw new RegisteredStationException();
        }
        if (!sections.isEmpty() &&!containsStation(upStation) && !containsStation(downStation)) {
            throw new CanNotAddStationException();
        }
    }

    public boolean containsStation(Station station) {
        return sections.stream().anyMatch(s -> s.containStation(station));
    }

    public void removeStation(Line line, Station station) {
        if (canNotDelete()) {
            throw new CanNotDeleteStateException();
        }

        Optional<Section> upLineSection = sections.stream().filter(s -> s.getDownStation().equals(station))
                                                           .findFirst().map(this::remove);
        Optional<Section> downLineSection = sections.stream().filter(s -> s.getUpStation().equals(station))
                                                             .findFirst().map(this::remove);

        if (upLineSection.isPresent() && downLineSection.isPresent()) {
            int newDistance = upLineSection.get().getDistance() + downLineSection.get().getDistance();
            Station upStation = upLineSection.get().getUpStation();
            Station downStation = downLineSection.get().getDownStation();

            sections.add(new Section(line, upStation, downStation, newDistance));
        }
    }

    private boolean canNotDelete() {
        return sections.size() == MIN_SIZE;
    }

    private Section remove(Section section) {
        sections.remove(section);
        return section;
    }
}
