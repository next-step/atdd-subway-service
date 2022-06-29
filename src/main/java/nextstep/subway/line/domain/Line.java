package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LineName name;

    private LineColor color;

    private ExtraFare extraFare;

    @Embedded
    private Sections sections = Sections.create();

    protected Line() {
    }

    public Line(String name, String color, long extraFare) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
        this.extraFare = new ExtraFare(extraFare);
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance, long extraFare) {
        this.name = new LineName(name);
        this.color = new LineColor(color);
        this.extraFare = new ExtraFare(extraFare);

        sections.addSection(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
        this.extraFare = line.getExtraFare();
    }

    public void addLineSection(Station upStation, Station downStation, Distance distance) {
        boolean isUpStationExisted = sections.isExisted(upStation);
        boolean isDownStationExisted = sections.isExisted(downStation);

        sections.valid(isUpStationExisted, isDownStationExisted);


        if (isUpStationExisted) {
            sections.updateUpStation(upStation, downStation, distance);
        }

        if (isDownStationExisted) {
            sections.updateDownStation(upStation, downStation, distance);
        }

        sections.addSection(new Section(this, upStation, downStation, distance.getValue()));
    }

    public void removeStation(Long stationId) {
        Optional<Section> upLineStation = sections.findUpStation(stationId);
        Optional<Section> downLineStation = sections.findDownStation(stationId);

        if (!upLineStation.isPresent() && !downLineStation.isPresent()) {
            throw new IllegalArgumentException("노선에 포햠되지 않은 지하철역입니다.");
        }

        if (upLineStation.isPresent() && downLineStation.isPresent()) {
            createSection(upLineStation.get(), downLineStation.get());
        }

        upLineStation.ifPresent(it -> sections.remove(it));
        downLineStation.ifPresent(it -> sections.remove(it));
    }

    private void createSection(Section upLineStation, Section downLineStation) {
        Station newUpStation = downLineStation.getUpStation();
        Station newDownStation = upLineStation.getDownStation();
        int newDistance = upLineStation.getDistance() + downLineStation.getDistance();
        sections.addSection(new Section(this, newUpStation, newDownStation, newDistance));
    }

    public Long getId() {
        return id;
    }

    public LineName getName() {
        return name;
    }

    public LineColor getColor() {
        return color;
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        return sections.orderBySection();
    }

    public Sections getSections() {
        return sections;
    }

    public int getDistance() {
        return sections.getDistance();
    }

    public ExtraFare getExtraFare() {
        return extraFare;
    }
}
