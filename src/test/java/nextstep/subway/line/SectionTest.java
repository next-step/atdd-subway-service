package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("구간 단위테스트")
class SectionTest {
    Line line;
    Station upStation;
    Station downStation;
    Section section;

    @BeforeEach
    void setUp() {
        line = new Line("2호선", "green");
        upStation = new Station("상행선");
        downStation = new Station("하행선");
        section = new Section(line, upStation, downStation, 10);
    }

    @DisplayName("상행선을 수정한다")
    @Test
    void updateUpStation() {
        Station newStation = new Station("신규역");
        section.updateUpStation(newStation, 5);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.getUpStation()).isEqualTo(newStation);
            softAssertions.assertThat(section.getDistance()).isEqualTo(5);
        });
    }

    @DisplayName("하행선을 수정한다")
    @Test
    void updateDownStation() {
        Station newStation = new Station("신규역");
        section.updateDownStation(newStation, 5);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(section.getDownStation()).isEqualTo(newStation);
            softAssertions.assertThat(section.getDistance()).isEqualTo(5);
        });
    }

    @DisplayName("수정할 길이는 원래 길이보다 같거나 크면 안된다")
    @Test
    void distanceTooLong() {
        Station newStation = new Station("신규역");
        assertSoftly(softAssertions -> {
            softAssertions.assertThatThrownBy(() -> section.updateUpStation(newStation, 10))
                    .isInstanceOf(RuntimeException.class);
            softAssertions.assertThatThrownBy(() -> section.updateDownStation(newStation, 10))
                    .isInstanceOf(RuntimeException.class);
        });
    }

    @DisplayName("상행선과 비교한다")
    @Test
    void matchUpStation() {
        assertThat(section.matchUpStation(upStation)).isTrue();
    }

    @DisplayName("하행선과 비교한다")
    @Test
    void matchDownStation() {
        assertThat(section.matchDownStation(downStation)).isTrue();
    }
}
