package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.enums.ErrorMessage;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    private static final int MIN_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    @Embedded
    private Sections sections = new Sections();
    @Embedded
    private Fare addedFare;

    public Line() {}

    public Line(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.sections.add(new Section(this, builder.upStation, builder.downStation, builder.distance));
        this.addedFare = Fare.from(builder.addedFare);
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
        this.validateDuplicate(upStation, downStation);
        this.validateNotExist(upStation, downStation);
        this.checkExistedAndUpdateStation(upStation, downStation, distance);
        this.addSection(new Section(this, upStation, downStation, distance));
    }

    private void checkExistedAndUpdateStation(Station upStation, Station downStation, int distance) {
        if (this.sections.isStationExisted(upStation)) {
            this.sections.updateUpStation(upStation, downStation, distance);
        }
        if (this.sections.isStationExisted(downStation)) {
            this.sections.updateDownStation(upStation, downStation, distance);
        }
    }

    private void validateNotExist(Station upStation, Station downStation) {
        if (isNotExisted(upStation, downStation)) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST_SECTION.getMessage());
        }
    }

    private void validateDuplicate(Station upStation, Station downStation) {
        if (isDuplicated(upStation, downStation)) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATED_SECTION.getMessage());
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
                && !this.sections.isStationExisted(upStation)
                && !this.sections.isStationExisted(downStation);
    }

    public void removeLineStation(Station station) {
        validateRemoveSection();

        Optional<Section> upLineStation = this.sections.getUpStation(station);
        Optional<Section> downLineStation = this.sections.getDownStation(station);

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
        if (this.getSections().size() <= MIN_SIZE) {
            throw new IllegalArgumentException(ErrorMessage.CANNOT_REMOVE_SECTION.getMessage());
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

    public Fare getAddedFare() {
        return addedFare;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private Station upStation;
        private Station downStation;
        private int distance;
        private int addedFare;

        public Builder(){}

        public Builder id(Long id) {
            this.id = id;
            return this;
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

        public Builder addedFare(int addedFare) {
            this.addedFare = addedFare;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", addedFare=" + addedFare +
                '}';
    }
}
