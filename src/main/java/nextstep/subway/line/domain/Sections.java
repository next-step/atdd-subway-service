package nextstep.subway.line.domain;

import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void add(Section section) {
        sections.add(section);
    }

    public void updateUpToDownStationWhenExist(SectionRequest request, Station upStation, Station downStation) {
        sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst()
                .ifPresent(it -> it.updateUpStation(downStation, request.getDistance()));
    }

    public void updateDownToUpStationWhenExist(SectionRequest request, Station upStation, Station downStation) {
        sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst()
                .ifPresent(it -> it.updateDownStation(upStation, request.getDistance()));
    }


    public Optional<Section> findNextSectionByUpStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getUpStation().equals(finalDownStation))
                .findFirst();
    }

    public Optional<Section> findNextSectionByDownStation(Station finalDownStation) {
        return sections.stream()
                .filter(it -> it.getDownStation().equals(finalDownStation))
                .findFirst();
    }

    public Station getFirstUpStation() {
        return sections.get(0).getUpStation();
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
