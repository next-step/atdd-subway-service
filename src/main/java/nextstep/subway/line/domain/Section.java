package nextstep.subway.line.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;


@Getter
@NoArgsConstructor
@Entity
public class Section extends BaseEntity {

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

    @Builder
    public Section(final Long id, final Station upStation, final Station downStation, final int distance) {
        this.id = id;
        this.registerStation(upStation, downStation, distance);
    }

    public void registerStation(final Station upStation, final Station downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void registerLine(Line line) {
        Optional.ofNullable(line).ifPresent(it -> {
            this.line = line;
            it.addSection(this);
        });
    }

    protected boolean isBefore(Section section) {
        return Objects.equals(downStation, section.getUpStation());
    }

    protected boolean isAfter(Section section) {
        return Objects.equals(upStation, section.getDownStation());
    }

    protected boolean contains(Station station) {
        return Objects.equals(upStation, station) || Objects.equals(downStation, station);
    }

    protected boolean hasSameUpStation(Section section) {
        return Objects.equals(upStation, section.getUpStation());
    }

    protected boolean hasSameDownStation(Section section) {
        return Objects.equals(downStation, section.getDownStation());
    }

    protected void updateUpStation(final Section section) {
        updateDistance(section);

        this.upStation = section.downStation;
    }

    protected void updateDownStation(final Section section) {
        updateDistance(section);

        this.downStation = section.upStation;
    }

    private void updateDistance(final Section section) {
        int distance = this.distance - section.getDistance();
        if (distance < 1) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }

        this.distance = distance;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", upStation, downStation, distance);
    }
}
