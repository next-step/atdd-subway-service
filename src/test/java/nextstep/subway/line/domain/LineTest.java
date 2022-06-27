package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Line")
class LineTest {

    private Line 신분당선;
    private Station 논현역;
    private Station 정자역;
    private int 최초_구간_거리;

    @BeforeEach
    void setUp() {
        final String 노선명 = "신분당선";
        final String 노선_색상_코드 = "red";
        논현역 = new Station("논현역");
        정자역 = new Station("정자역");
        최초_구간_거리 = 100;
        신분당선 = new Line(노선명, 노선_색상_코드, 논현역, 정자역, 최초_구간_거리);
    }

    private void 구간_추가(final Line 노선, final Station 상행역, final Station 하행역, final int 구간_거리) {
        신분당선.addSection(Section.of(노선, 상행역, 하행역, 구간_거리));
    }

    @Test
    @DisplayName("신규 상행종점역 구간 추가")
    public void addFinalUpSection() {
        // Given
        final Station 신사역 = new Station("신사역");
        final Station 논현역 = new Station("논현역");
        final int 구간_길이 = 5;

        // When
        구간_추가(신분당선, 신사역, 논현역, 구간_길이);

        // Then
        assertAll(
            () -> assertThat(신분당선.getSections()).hasSize(2),
            () -> assertThat(신분당선.getAllStations())
                .hasSize(3)
                .containsExactly(신사역, 논현역, 정자역)
        );
    }

    @Test
    @DisplayName("신규 하행종점역 구간 추가")
    public void addFinalDownSection() {
        // Given
        final Station 정자역 = new Station("정자역");
        final Station 광교역 = new Station("광교역");
        final int 구간_길이 = 5;

        final Section 신규_하행종점역_구간 = Section.of(신분당선, 정자역, 광교역, 구간_길이);

        // When
        신분당선.addSection(신규_하행종점역_구간);

        // Then
        assertAll(
            () -> assertThat(신분당선.getSections()).hasSize(2),
            () -> assertThat(신분당선.getAllStations())
                .hasSize(3)
                .containsExactly(논현역, 정자역, 광교역)
        );
    }

    @Test
    @DisplayName("순서 없는 구간 추가")
    public void getAllSortedStations() {
        // Given
        final Station 신사역 = new Station("신사역");
        구간_추가(신분당선, 신사역, 논현역, 5);

        final Station 미금역 = new Station("미금역");
        구간_추가(신분당선, 정자역, 미금역, 5);

        final Station 강남역 = new Station("강남역");
        구간_추가(신분당선, 논현역, 강남역, 10);

        final Station 신논현역 = new Station("신논현역");
        구간_추가(신분당선, 신논현역, 강남역, 5);

        final Station 판교역 = new Station("판교역");
        구간_추가(신분당선, 판교역, 정자역, 10);

        final Station 양재역 = new Station("양재역");
        구간_추가(신분당선, 강남역, 양재역, 5);

        final Station 양재시민의숲역 = new Station("양재시민의숲역");
        구간_추가(신분당선, 양재시민의숲역, 판교역, 45);

        final Station 청계산입구역 = new Station("청계산입구역");
        구간_추가(신분당선, 양재시민의숲역, 청계산입구역, 20);

        // When
        List<Station> 노선_역목록 = 신분당선.getAllStations();
        신분당선.getSections().forEach(it -> System.out
            .println(it.getUpStation().getName() + " -> " + it.getDownStation().getName() + " = " + it.getDistance()));

        // Then
        // 신사-(5)-논현-(5)-신논현-(5)-강남-(5)-양재-(30)-양재시민의숲역-(20)-청계산입구-(25)-판교-(10)-정자-(5)-미금
        assertAll(
            () -> assertThat(신분당선.getSections()).hasSize(9),
            () -> assertThat(노선_역목록)
                .hasSize(10)
                .containsExactly(신사역, 논현역, 신논현역, 강남역, 양재역, 양재시민의숲역, 청계산입구역, 판교역, 정자역, 미금역)
        );

        // When
        List<Station> station = 신분당선.getAllStations();

        // Then
        assertThat(station.contains(논현역)).isTrue();
    }
}
