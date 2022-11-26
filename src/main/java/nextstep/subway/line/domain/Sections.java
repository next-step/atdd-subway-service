package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {}

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public void add(Section section) {
        if (sections.isEmpty()) {
            sections.add(section);
            return;
        }
        validateAddExisted(section);
        validateAddNothingExisted(section);
        updateSection(section);
        sections.add(section);
    }

    private void validateAddExisted(Section section) {
        if (getStations().containsAll(section.getStations())) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateAddNothingExisted(Section section) {
        List<Station> stations = getStations();
        if (section.getStations().stream().noneMatch(stations::contains)) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void updateSection(Section section) {
        sections.stream()
                .forEach(it -> it.update(section.getUpStation(), section.getDownStation(), section.getDistance()));
    }

    public List<Section> get() {
        return sections;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
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
        Station downStation = sections.get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = sections.stream()
                    .filter(it -> it.getDownStation() == finalDownStation)
                    .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }
        return downStation;
    }
}
