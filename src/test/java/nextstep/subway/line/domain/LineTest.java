package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidAddSectionException;
import nextstep.subway.line.domain.exceptions.InvalidRemoveSectionException;
import nextstep.subway.station.domain.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    @DisplayName("지하철 노선에 이미 등록된 구간을 또 등록할 수 없다.")
    @Test
    void addSectionTwiceTest() {
        String name = "2호선";
        String color = "초록색";
        Station upStation = StationFixtures.강남역;
        Station downStation = StationFixtures.역삼역;

        Line line = new Line(name, color, upStation, downStation, 5);

        assertThatThrownBy(() -> line.addSection(upStation, downStation, 10))
                .isInstanceOf(InvalidAddSectionException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("접점이 아예 없는 구간은 추가할 수 없다.")
    @Test
    void addSectionWithoutAnyConnectionTest() {
        String name = "2호선";
        String color = "초록색";

        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.역삼역, 5);

        assertThatThrownBy(() -> line.addSection(StationFixtures.삼성역, StationFixtures.잠실역, 10))
                .isInstanceOf(InvalidAddSectionException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("지하철 구간이 비어있는 지하철 노선에 새로운 구간을 추가할 수 있다.")
    @Test
    void addSectionToEmptyLine() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color);

        boolean result = line.addSection(StationFixtures.삼성역, StationFixtures.잠실역, 5);

        assertThat(result).isTrue();
    }

    @DisplayName("기존 지하철 구간의 상행역이나 하행역과 일치하는 구간을 추가할 수 있다.")
    @ParameterizedTest
    @MethodSource("addSectionTestResource")
    void addSectionTest(Station upStation, Station downStation, int distance) {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.역삼역, 6);

        boolean result = line.addSection(upStation, downStation, distance);

        assertThat(result).isTrue();
    }
    public static Stream<Arguments> addSectionTestResource() {
        return Stream.of(
                Arguments.of(StationFixtures.역삼역, StationFixtures.삼성역, 100),
                Arguments.of(StationFixtures.잠실역, StationFixtures.역삼역, 5),
                Arguments.of(StationFixtures.강남역, StationFixtures.삼성역, 5),
                Arguments.of(StationFixtures.삼성역, StationFixtures.강남역, 100)
        );
    }

    @DisplayName("구간이 하나밖에 없는 지하철 노선의 구간을 제거할 수 없다.")
    @Test
    void removeSectionFailWhenLineHasJustOneSectionTest() {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.역삼역, 5);

        assertThatThrownBy(() -> line.removeSection(StationFixtures.강남역))
                .isInstanceOf(InvalidRemoveSectionException.class)
                .hasMessage("구간이 하나밖에 없는 지하철 노선의 구간을 제거할 수 없습니다.");
    }

    @DisplayName("지하철 노선의 구간을 제거할 수 있다.")
    @ParameterizedTest
    @MethodSource("removeSectionTestResource")
    void removeSectionTest(Station removeTarget) {
        String name = "2호선";
        String color = "초록색";
        Line line = new Line(name, color, StationFixtures.강남역, StationFixtures.역삼역, 5);
        line.addSection(StationFixtures.역삼역, StationFixtures.삼성역, 5);

        boolean result = line.removeSection(removeTarget);

        assertThat(result).isTrue();
    }
    public static Stream<Arguments> removeSectionTestResource() {
        return Stream.of(
                Arguments.of(StationFixtures.강남역),
                Arguments.of(StationFixtures.역삼역),
                Arguments.of(StationFixtures.삼성역)
        );
    }
}