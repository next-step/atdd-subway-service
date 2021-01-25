package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Line 일호선;
    private Station 소요산역;
    private Station 서울역;
    private Station 창동역;
    private Station 인천역;

    @BeforeEach
    void setUp() {
        소요산역 = new Station("소요산역");
        서울역 = new Station("서울역");
        창동역 = new Station("창동역");
        인천역 = new Station("인천역");
        일호선 = Line.builder()
                .name("1호선")
                .color("파란색")
                .upStation(소요산역)
                .downStation(인천역)
                .distance(20)
                .build();

        일호선.addSection(Section.builder()
                .line(일호선)
                .upStation(소요산역)
                .downStation(서울역)
                .distance(10)
                .build());
    }

    @DisplayName("노선에 구간 등록")
    @Test
    void addSection() {
        일호선.addSection(Section.builder()
                .line(일호선)
                .upStation(소요산역)
                .downStation(창동역)
                .distance(5)
                .build());

        노선에_포함된_지하철역_검증(일호선 ,소요산역, 창동역, 서울역, 인천역);
    }

    @DisplayName("노선에 지하철역 제거")
    @Test
    void deleteSection() {
        일호선.remove(서울역);

        노선에_포함된_지하철역_검증(일호선 ,소요산역, 인천역);
    }

    private void 노선에_포함된_지하철역_검증(Line 일호선, Station... stations) {
        assertThat(일호선.getStations()).containsExactly(stations);
    }
}