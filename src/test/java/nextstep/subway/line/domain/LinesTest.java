package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DomainFixtureFactory.createLine;
import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {
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
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, Distance.valueOf(10));
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, Distance.valueOf(10));
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, Distance.valueOf(5));
        삼호선.addSection(createSection(삼호선, 교대역, 남부터미널역, Distance.valueOf(3)));
    }

    @DisplayName("구간들 길이(합) 테스트")
    @Test
    void valueOf() {
        Lines lines = Lines.valueOf(Lists.newArrayList(신분당선, 이호선, 삼호선));
        assertThat(lines.lines()).containsExactlyElementsOf(Lists.newArrayList(신분당선, 이호선, 삼호선));
    }
}
