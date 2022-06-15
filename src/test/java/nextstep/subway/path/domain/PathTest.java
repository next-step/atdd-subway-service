package nextstep.subway.path.domain;

import static nextstep.subway.DomainFixtureFactory.createLine;
import static nextstep.subway.DomainFixtureFactory.createPath;
import static nextstep.subway.DomainFixtureFactory.createSection;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.google.common.collect.Lists;
import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;

    @BeforeEach
    public void setUp() {
        강남역 = createStation(1L, "강남역");
        양재역 = createStation(2L, "양재역");
        교대역 = createStation(3L, "교대역");
        남부터미널역 = createStation(4L, "남부터미널역");

        신분당선 = createLine(1L, "신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10));
        이호선 = createLine(2L, "이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine(3L, "삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("경로 초기화 테스트")
    @Test
    void path() {
        Path path = createPath(Lists.newArrayList(양재역, 남부터미널역, 교대역), Distance.valueOf(5));
        assertAll(
                () -> assertThat(path.distanceValue()).isEqualTo(5),
                () -> assertThat(path.stations()).containsExactlyElementsOf(Lists.newArrayList(양재역, 남부터미널역, 교대역))
        );
    }

    @DisplayName("경로에 포함된 노선들 반환 테스트")
    @Test
    void findPathLinesFrom() {
        List<Section> sections = Lists.newArrayList(createSection(신분당선, 강남역, 양재역, Distance.valueOf(10)),
                createSection(이호선, 교대역, 강남역, Distance.valueOf(10)),
                createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)),
                createSection(삼호선, 남부터미널역, 양재역, Distance.valueOf(2)));
        Path path = createPath(Lists.newArrayList(남부터미널역, 양재역, 강남역), Distance.valueOf(5));
        assertThat(path.findPathLinesFrom(sections)).containsAll(Lists.newArrayList(삼호선, 신분당선));
    }
}
