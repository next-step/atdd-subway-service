package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineException;
import nextstep.subway.line.exception.LineExceptionType;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> list;

    protected Sections() {
        this.list = new ArrayList<>();
    }

    public static Sections instance() {
        return new Sections();
    }

    public void add(final Section section) {
        additionalValidation(section);

        if (list.isEmpty()) {
            list.add(section);
            return;
        }
        updateUpStation(section);
        updateDownStation(section);

        list.add(section);
    }

    private void additionalValidation(final Section section) {
        bothStationExist(section);
        canNotRegisterSection(section);
    }

    private void bothStationExist(final Section section) {
        if (isUpStationExisted(section.getUpStation()) && isDownStationExisted(section.getDownStation())) {
            throw new LineException(LineExceptionType.EXIST_SECTION);
        }
    }

    private boolean isUpStationExisted(final Station upStation) {
        return getAllStations().stream()
                .anyMatch(it -> it.equals(upStation));
    }

    private boolean isDownStationExisted(final Station downStation) {
        return getAllStations().stream()
                .anyMatch(it -> it.equals(downStation));
    }

    private void canNotRegisterSection(final Section section) {
        if (!list.isEmpty() && isUpStationNotExisted(section.getUpStation()) &&
               isDownStationNotExisted(section.getDownStation())) {
            throw new LineException(LineExceptionType.CAN_NOT_REGISTER_SECTION);
        }
    }

    private boolean isUpStationNotExisted(final Station upStation) {
        return list.stream()
                .noneMatch(it -> it.equalsUpStation(upStation));
    }

    private boolean isDownStationNotExisted(final Station downStation) {
        return list.stream()
                .noneMatch(it -> it.equalsDownStation(downStation));
    }

    private void updateUpStation(final Section section) {
        if (isUpStationExisted(section.getUpStation())) {
            list.stream()
                    .filter(it -> it.equalsUpStation(section.getUpStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(section.getDownStation(), section.getDistance()));
        }
    }

    private void updateDownStation(final Section section) {
        if (isDownStationExisted(section.getDownStation())) {
            list.stream()
                    .filter(it -> it.equalsDownStation(section.getDownStation()))
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(section.getUpStation(), section.getDistance()));

        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(list);
    }

    public List<Station> getAllStations() {
        final Set<Station> stations = new HashSet<>();
        for (Section section : list) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }

        return Collections.unmodifiableList(new ArrayList<>(stations));
    }

}
