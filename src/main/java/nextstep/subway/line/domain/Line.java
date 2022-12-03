package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.exception.CannotAddSectionException;
import nextstep.subway.exception.CannotRemoveSectionException;
import nextstep.subway.exception.DuplicatedSectionException;
import nextstep.subway.exception.NotAllowRegisterSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.sections.add(new Section(this, builder.upStation, builder.downStation, builder.distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void deleteSection(Section section) {
        this.sections.remove(section);
    }

    public void addLineStation(Station upStation, Station downStation, int distance) {
        validateDuplicate(upStation, downStation);
        validateNotExist(upStation, downStation);

        if (isStationsEmpty()) {
            this.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (this.sections.isStationExisted(upStation)) {
            this.getSections().stream()
                    .filter(it -> it.getUpStation() == upStation)
                    .findFirst()
                    .ifPresent(it -> it.updateUpStation(downStation, distance));

            this.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        if (this.sections.isStationExisted(downStation)) {
            this.getSections().stream()
                    .filter(it -> it.getDownStation() == downStation)
                    .findFirst()
                    .ifPresent(it -> it.updateDownStation(upStation, distance));

            this.addSection(new Section(this, upStation, downStation, distance));
            return;
        }

        throw new CannotAddSectionException();
    }

    private void validateNotExist(Station upStation, Station downStation) {
        if (isNotExisted(upStation, downStation)) {
            throw new NotAllowRegisterSectionException();
        }
    }

    private void validateDuplicate(Station upStation, Station downStation) {
        if (isDuplicated(upStation, downStation)) {
            throw new DuplicatedSectionException();
        }
    }

    private boolean isDuplicated(Station upStation, Station downStation) {
        return this.sections.isStationExisted(upStation) && this.sections.isStationExisted(downStation);
    }

    private boolean isStationsEmpty() {
        return this.sections.getStations().isEmpty();
    }

    private boolean isNotExisted(Station upStation, Station downStation) {
        return !isStationsEmpty()
                && this.sections.isStationNotExisted(upStation)
                && this.sections.isStationNotExisted(downStation);
    }

    public void removeLineStation(Station station) {
        validateRemoveSection();

        Optional<Section> upLineStation = this.getSections().stream()
                .filter(it -> it.getUpStation() == station)
                .findFirst();
        Optional<Section> downLineStation = this.getSections().stream()
                .filter(it -> it.getDownStation() == station)
                .findFirst();

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            Station newUpStation = downLineStation.get().getUpStation();
            Station newDownStation = upLineStation.get().getDownStation();
            int newDistance = upLineStation.get().plus(downLineStation.get().getDistance());
            this.addSection(new Section(this, newUpStation, newDownStation, newDistance));
        }

        upLineStation.ifPresent(this::deleteSection);
        downLineStation.ifPresent(this::deleteSection);
    }

    private void validateRemoveSection() {
        if (this.getSections().size() <= 1) {
            throw new CannotRemoveSectionException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections.values();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;

        public Builder(){
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder upStation(Station upStation) {
            this.upStation = upStation;
            return this;
        }

        public Builder downStation(Station downStation) {
            this.downStation = downStation;
            return this;
        }

        public Builder distance(int distance) {
            this.distance = distance;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
