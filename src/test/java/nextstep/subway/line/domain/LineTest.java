package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.distance;
import static nextstep.subway.line.domain.SectionTest.gangNam;
import static nextstep.subway.line.domain.SectionTest.panGyo;
import static nextstep.subway.line.domain.SectionTest.yangJae;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @Test
    @DisplayName("해당 객체가 같은지 검증")
    void verifySameLine() {
        final Line twoLine = 라인_생성("2호선", "yellow");
        assertThat(twoLine).isEqualTo(라인_생성("2호선", "yellow"));
    }

    @Test
    @DisplayName("노선 업데이트 확인")
    void updateLine() {
        final Line sinBunDang = 라인_생성("신분당선", "red");
        Line line = 라인_생성("2호선", "yellow");
        line.update(sinBunDang);

        assertThat(line).isEqualTo(sinBunDang);
    }

    @Test
    @DisplayName("노선 구간 추가 확인")
    void addSection() {
        final Line line = 라인_생성("2호선", "yellow");
        line.addSection(new Section(line, gangNam, yangJae, distance));

        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(1),
                () -> assertThat(line.getSections().getStations()).containsExactly(gangNam, yangJae)
        );

    }

    @Test
    @DisplayName("노선 구간 삭제 확인")
    void deleteSection() {
        final Line line = 라인_생성("2호선", "yellow");
        line.addSection(new Section(line, gangNam, yangJae, distance));
        line.addSection(new Section(line, yangJae, panGyo, distance));
        line.deleteSection(yangJae);

        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(1),
                () -> assertThat(line.getSections().getStations()).containsExactly(gangNam, panGyo)
        );
    }

    public static Line 라인_생성(String name, String color) {
        return new Line(name, color);
    }

    public static Line 라인_생성(String name, String color, int charge) {
        return new Line(name, color, charge);
    }

    public static Line 라인_생성(String name, String color, Station upStation, Station downStation, Distance distance) {
        return new Line(name, color, upStation, downStation, distance);
    }

    public static Line 라인_생성(String name, String color, int charge, Station upStation, Station downStation,
                       Distance distance) {
        return new Line(name, color, charge, upStation, downStation, distance);
    }
}
