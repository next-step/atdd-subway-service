package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.LineFixture.이호선;
import static nextstep.subway.line.domain.SectionFixture.당산_합정_구간;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionTest {

    Line line;

    @BeforeEach
    void setup() {
        Line line = 이호선();
    }

    @DisplayName("구간의 상행역이 같은지를 비교한다.")
    @Test
    void 상행역_비교_테스트() {
        Section section = 당산_합정_구간(line);

        assertAll(
                () -> assertThat(section.hasEqualUpStation(당산역())).isTrue(),
                () -> assertThat(section.hasEqualUpStation(영등포구청역())).isFalse()
        );
    }

    @DisplayName("구간의 하행역이 같은지를 비교한다.")
    @Test
    void 하행역_비교_테스트() {
        Section section = 당산_합정_구간(line);

        assertAll(
                () -> assertThat(section.hasEqualDownStation(합정역())).isTrue(),
                () -> assertThat(section.hasEqualDownStation(영등포구청역())).isFalse()
        );
    }

    @DisplayName("상행역과 거리를 갱신한다.")
    @Test
    void 상행역_및_거리_갱신_테스트() {
        Section section = 당산_합정_구간(line);
        section.updateUpStation(영등포구청역(), new Distance(5));

        assertAll(
                () -> assertThat(section.hasEqualUpStation(영등포구청역())).isTrue(),
                () -> assertThat(section.getDistance()).isEqualTo(new Distance(2))
        );
    }

    @DisplayName("하행역과 거리를 갱신한다.")
    @Test
    void 하행역_및_거리_갱신_테스트() {
        Section section = 당산_합정_구간(line);
        section.updateDownStation(홍대입구역(), new Distance(5));

        assertAll(
                () -> assertThat(section.hasEqualDownStation(홍대입구역())).isTrue(),
                () -> assertThat(section.getDistance()).isEqualTo(new Distance(2))
        );
    }

    @DisplayName("기존 거리보다 큰 거리의 하행역으로 갱신한다.")
    @Test
    void 기존_거리보다_큰_거리의_하행역_갱신_테스트() {
        Section section = 당산_합정_구간(line);

        assertThatThrownBy(
                () -> section.updateDownStation(홍대입구역(), new Distance(15))
        ).isInstanceOf(InvalidDataException.class);
    }

}
