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

    public Sections() {
    }

    public void add(Section section){
        sections.add(section);
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        validateAleadyExist(upStation, downStation);
        validateRegister(upStation, downStation);

        if (sections.isEmpty()) {
            add(new Section(line, upStation, downStation, distance));
            return;
        }

        updateUpStation(upStation, downStation, distance);
        updateDownStation(upStation, downStation, distance);
        add(new Section(line, upStation, downStation, distance));
    }

    private void updateUpStation(Station upStation, Station downStation, int distance) {
        findUpStation(upStation).ifPresent(it -> it.updateUpStation(downStation, distance));
    }

    private void updateDownStation(Station upStation, Station downStation, int distance){
        findDownStation(downStation).ifPresent(it -> it.updateDownStation(upStation, distance));
    }

    private boolean isDownStationExisted(Station downStation) {
        return getStations().stream().anyMatch(it -> it == downStation);
    }

    private boolean isUpStationExisted(Station upStation) {
        return getStations().stream().anyMatch(it -> it == upStation);
    }

    public void removeSection(Section section) {
        sections.remove(section);
    }

    public void removeStation(Line line, Station station) {
        validateSize();

        Optional<Section> upSection = findUpStation(station);
        Optional<Section> downSection = findDownStation(station);

        if (upSection.isPresent() && downSection.isPresent()) {
            Station newUpStation = downSection.get().getUpStation();
            Station newDownStation = upSection.get().getDownStation();
            int newDistance = plusDistance(upSection.get(), downSection.get());
            add(new Section(line, newUpStation, newDownStation, newDistance));
        }

        upSection.ifPresent(this::removeSection);
        downSection.ifPresent(this::removeSection);
    }

    private int plusDistance(Section upSection, Section downSection){
        return upSection.getDistance() + downSection.getDistance();
    }

    private Optional<Section> findDownStation(Station downStation){
        return this.sections.stream()
                .filter(it -> it.getDownStation() == downStation)
                .findFirst();
    }

    private Optional<Section> findUpStation(Station upStation){
        return this.sections.stream()
                .filter(it -> it.getUpStation() == upStation)
                .findFirst();
    }

    private Station findTerminalUpStation() {
        Station downStation = this.sections.get(0).getUpStation();
        while (downStation != null) {
            Optional<Section> nextSection = findDownStation(downStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getUpStation();
        }

        return downStation;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findTerminalUpStation();
        stations.add(downStation);

        while (downStation != null) {
            Optional<Section> nextSection = findUpStation(downStation);
            if (!nextSection.isPresent()) {
                break;
            }
            downStation = nextSection.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    public int getSize(){
        return this.sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    private void validateAleadyExist(Station upStation, Station downStation){
        if (isUpStationExisted(upStation) && isDownStationExisted(downStation)) {
            throw new RuntimeException("이미 등록된 구간 입니다.");
        }
    }

    private void validateRegister(Station upStation, Station downStation) {
        List<Station> stations = getStations();
        if (
            !stations.isEmpty()
            && stations.stream().noneMatch(it -> it == upStation)
            && stations.stream().noneMatch(it -> it == downStation)
        ) {
            throw new RuntimeException("등록할 수 없는 구간 입니다.");
        }
    }

    private void validateSize() {
        if (sections.size() <= 1) {
            throw new RuntimeException();
        }
    }
}
