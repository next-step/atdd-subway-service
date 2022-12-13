package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineTest {

    private Station 강남역;
    private Station 양재역;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
    }

    @DisplayName("지정위치에 따라 다음역 찾기")
    @Test
    void 다음역_찾기() {
        assertAll(
            () -> assertThat(신분당선.nextStationOf(강남역, StationPosition.DOWN_STATION)).isEqualTo(양재역),
            () -> assertThat(신분당선.nextStationOf(양재역, StationPosition.UP_STATION)).isEqualTo(강남역)
        );
    }

    @DisplayName("상행종점 찾기")
    @Test
    void 상행종점_찾기() {
        Station 신논현역 = new Station("신논현역");
        Station 정자역 = new Station("정자역");
        Section 구간1 = new Section(신분당선, 신논현역, 강남역, 10);
        Section 구간2 = new Section(신분당선, 양재역, 정자역, 10);
        신분당선.getSections().add(구간1);
        신분당선.getSections().add(구간2);

        assertThat(신분당선.findFinalUpStation()).isEqualTo(신논현역);
    }

    @DisplayName("노선에 등록된 두 지하철역 사이의 역 제거 ")
    @Test
    void 중간역_제거() {
        Station 정자역 = new Station("정자역");
        Station 광교역 = new Station("광교역");
        Section 구간1 = new Section(신분당선, 양재역, 정자역, 10);
        Section 구간2 = new Section(신분당선, 정자역, 광교역, 10);
        신분당선.getSections().add(구간1);
        신분당선.getSections().add(구간2);

        신분당선.removeLineStation(정자역);

        Section 예상_새_구간 = new Section(신분당선, 양재역, 광교역, 20);
        assertAll(
                () -> assertThat(신분당선.getSections()).doesNotContain(구간1, 구간2),
                () -> assertThat(신분당선.getSections()).contains(예상_새_구간)
        );
    }

    @DisplayName("노선에 등록된 상행종점 제거 ")
    @Test
    void 상행종점_제거() {
        Station 신논현역 = new Station("신논현역");
        Section 구간1 = new Section(신분당선, 신논현역, 강남역, 10);
        신분당선.getSections().add(구간1);

        신분당선.removeLineStation(신논현역);

        Section 예상_잔여_구간 = new Section(신분당선, 강남역, 양재역, 10);
        assertAll(
                () -> assertThat(신분당선.getSections()).doesNotContain(구간1),
                () -> assertThat(신분당선.getSections()).contains(예상_잔여_구간)
        );
    }

    @DisplayName("노선에 등록된 하행종점 제거 ")
    @Test
    void 하행종점_제거() {
        Station 정자역 = new Station("정자역");
        Section 구간1 = new Section(신분당선, 양재역, 정자역, 10);
        신분당선.getSections().add(구간1);

        신분당선.removeLineStation(정자역);

        Section 예상_잔여_구간 = new Section(신분당선, 강남역, 양재역, 10);
        assertAll(
                () -> assertThat(신분당선.getSections()).doesNotContain(구간1),
                () -> assertThat(신분당선.getSections()).contains(예상_잔여_구간)
        );
    }
}
