package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Embedded
    private Distance distance;

    @Builder
    public Section(Line line, Station upStation, Station downStation, Distance distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Section section) {
        return Section.builder()
                .line(section.getLine())
                .upStation(section.getUpStation())
                .downStation(section.getDownStation())
                .distance(section.getDistance())
                .build();
    }

    public void updateUpStation(Station station, Distance newDistance) {
        this.distance = distance.minusDistance(newDistance);
        this.upStation = station;
    }

    public void updateDownStation(Station station, Distance newDistance) {
        this.distance = distance.minusDistance(newDistance);
        this.downStation = station;
    }
}
