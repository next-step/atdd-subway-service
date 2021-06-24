package nextstep.subway.line.domain;

import java.util.Optional;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;

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

    private int distance;

    public Section() {
    }

    public Section(Line line, Station upStation, Station downStation, int distance) {
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Section upSection, Section downSection) {
        final Section validUpSection = validUpSection(upSection);
        final Section validDownSection = validDownSection(downSection);
        final Station newUpStation = validUpSection.getDownStation();
        final Station newDownStation = validDownSection.getUpStation();
        final int distance = validUpSection.getDistance() + validDownSection.getDistance();

        return new Section(validUpSection.getLine(), newUpStation, newDownStation, distance);
    }

    private static Section validUpSection(Section upSection) {
        return Optional.ofNullable(upSection)
            .filter(it -> it.getDownStation() != null && it.getDistance() > 0)
            .orElseThrow(RuntimeException::new);
    }

    private static Section validDownSection(Section downSection) {
        return Optional.ofNullable(downSection)
            .filter(it -> it.getUpStation() != null && it.getDistance() > 0)
            .orElseThrow(RuntimeException::new);
    }

    public Long getId() {
        return id;
    }

    public Line getLine() {
        return line;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public void updateUpStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.upStation = station;
        this.distance -= newDistance;
    }

    public void updateDownStation(Station station, int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.downStation = station;
        this.distance -= newDistance;
    }
}
