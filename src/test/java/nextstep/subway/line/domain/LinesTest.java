package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.StationResponses;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LinesTest {
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 광교역 = new Station("광교역");
    private Station 정자역 = new Station("정자역");

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        광교역 = new Station("광교역");
        정자역 = new Station("정자역");
    }

    @Test
    @DisplayName("없는 역이면 IllegalArgumentException이 발생한다")
    void 없는_역이면_IllegalArgumentException이_발생한다() {
        Line 신분당선 = new Line("신분당", "RED", 양재역, 정자역, 3);
        Line 분당선 = new Line("분당", "YELLO", 광교역, 양재역, 3);

        신분당선.addSection(new Section(광교역, 정자역, new Distance(1)));
        분당선.addSection(new Section(양재역, 정자역, new Distance(1)));

        Lines lines = new Lines(Arrays.asList(신분당선, 분당선));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> lines.findShortDistance(강남역, 양재역));
    }

    @Test
    @DisplayName("최단거리를_가진_라인을_가져올_수_있다")
    void 최단거리를_가진_라인을_가져올_수_있다() {
        Line 신분당선 = new Line("신분당", "RED", 강남역, 양재역, 3);
        Line 이호선 = new Line("이호선", "YELLO", 강남역, 정자역, 7);
        Line 삼호선 = new Line("삼호선", "ORANGE", 강남역, 광교역, 2);

        신분당선.addSection(new Section(신분당선, 양재역, 정자역, 2));
        이호선.addSection(new Section(이호선, 양재역, 정자역, 5));
        삼호선.addSection(new Section(삼호선, 광교역, 정자역, 1));

        Lines lines = new Lines(Arrays.asList(신분당선, 이호선, 삼호선));

        Line shortDistance = lines.findShortDistance(강남역, 광교역);
        
        assertThat(shortDistance)
                .isEqualTo(삼호선);
    }
}