package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.라인_생성;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    public static final Line twoLine = 라인_생성("2호선", "yellow");
    public static final Station gangNam = 지하철역_생성("강남역");
    public static final Station yangJae = 지하철역_생성("양재역");
    public static final Station panGyo = 지하철역_생성("판교역");
    public static final Distance distance = Distance.of(10);

    @Test
    @DisplayName("Section 객체가 같은지 검증")
    void verifySameSection() {
        Section section = new Section(twoLine, gangNam, yangJae, distance);

        assertThat(section).isEqualTo(new Section(twoLine, gangNam, yangJae, distance));
    }

    @Test
    @DisplayName("구간 변경이 변경되는지 확인")
    void updateSection() {
        Section section = new Section(twoLine, gangNam, yangJae, distance);
        Section interSection = new Section(twoLine, gangNam, panGyo, Distance.of(3));
        section.update(interSection);

        assertAll(
                () -> assertThat(section.upStation()).isEqualTo(panGyo),
                () -> assertThat(section.downStation()).isEqualTo(yangJae),
                () -> assertThat(section.getDistance()).isEqualTo(Distance.of(7))
        );
    }
}
