package nextstep.subway.line.domain;

import static nextstep.subway.line.enums.LineExceptionType.ALREADY_ADDED_SECTION;
import static nextstep.subway.line.enums.LineExceptionType.CANNOT_ADDED_SECTION;
import static nextstep.subway.line.enums.LineExceptionType.CANNOT_REMOVE_STATION_IS_NOT_EXIST;
import static nextstep.subway.line.enums.LineExceptionType.CANNOT_REMOVE_STATION_WHEN_ONLY_ONE_SECTIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LineTest {

    private Line 신분당선;
    private Station 강남역;
    private Station 판교역;
    private Section 강남역_판교역_구간;

    @BeforeEach
    void setUp() {
        신분당선 = Line.of("신분당선", "RED");
        강남역 = Station.of(1L, "강남역");
        판교역 = Station.of(2L, "판교역");
        강남역_판교역_구간 = Section.of(강남역, 판교역, 10);
    }

    @DisplayName("지하철 노선을 이름과 노선 색상으로 생성할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"신분당선,RED", "분당선,YELLOW"})
    void generate01(String name, String color) {
        // given & when
        Line line = Line.of(name, color);

        // then
        assertAll(
            () -> assertEquals(line.getName(), LineName.from(name)),
            () -> assertEquals(line.getColor(), LineColor.from(color))
        );
    }

    @DisplayName("지하철 노선에 Section을 추가할 수 있다.")
    @Test
    void generate02() {
        // when & then
        assertThatNoException().isThrownBy(() -> 신분당선.addSection(강남역_판교역_구간));
    }

    @DisplayName("지하철 노선에 동일한 Section을 추가할 수 없다.")
    @Test
    void generate03() {
        // when
        신분당선.addSection(강남역_판교역_구간);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> 신분당선.addSection(강남역_판교역_구간))
            .withMessageContaining(ALREADY_ADDED_SECTION.getMessage());
    }
    @DisplayName("지하철 노선에 다른 Section 이지만 이미 존재하는 상/하행역을 추가할 수 없다.")
    @Test
    void generate04() {
        // given
        Section 중복된_강남역_판교역_구간 = Section.of(2L, 강남역, 판교역, Distance.from(5));

        // when
        신분당선.addSection(강남역_판교역_구간);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> 신분당선.addSection(중복된_강남역_판교역_구간))
            .withMessageContaining(ALREADY_ADDED_SECTION.getMessage());
    }

    @DisplayName("지하철 노선에 존재하지 않는 상/하행 역의 구간은 추가할 수 없다.")
    @Test
    void generate05() {
        // given
        신분당선.addSection(강남역_판교역_구간);
        Station 양재역 = Station.of(3L, "양재역");
        Station 양재시민의숲역 = Station.of(4L, "양재시민의숲역");
        Section 양재역_양재시민의숲역_구간 = Section.of(2L, 양재역, 양재시민의숲역, Distance.from(1));

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> 신분당선.addSection(양재역_양재시민의숲역_구간))
            .withMessageContaining(CANNOT_ADDED_SECTION.getMessage());
    }

    @DisplayName("지하철 노선에 중간역을 제거할 수 있다.")
    @Test
    void remove01() {
        // given
        신분당선.addSection(강남역_판교역_구간);

        Station 양재역 = Station.of(3L, "양재역");
        Section 강남역_양재역_구간 = Section.of(2L, 강남역, 양재역, Distance.from(1));
        신분당선.addSection(강남역_양재역_구간);

        // when
        신분당선.removeSection(양재역);

        // then
        assertThat(신분당선.getStations()).isEqualTo(Arrays.asList(강남역, 판교역));
    }

    @DisplayName("지하철 노선에 포함되지 않는 역은 제거할 수 없다.")
    @Test
    void removeException01() {
        // given
        신분당선.addSection(강남역_판교역_구간);

        Station 양재역 = Station.of(3L, "양재역");
        Section 강남역_양재역_구간 = Section.of(2L, 강남역, 양재역, Distance.from(1));
        신분당선.addSection(강남역_양재역_구간);

        Station 정자역 = Station.of(4L, "정자역");

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> 신분당선.removeSection(정자역))
            .withMessageContaining(CANNOT_REMOVE_STATION_IS_NOT_EXIST.getMessage());
    }

    @DisplayName("노선의 마지막 구간에 해당하는 역을 제거하는 경우 제거될 수 없다.")
    @Test
    void removeException02() {
        // given
        신분당선.addSection(강남역_판교역_구간);

        // when & then
        assertThatIllegalStateException().isThrownBy(() -> 신분당선.removeSection(강남역))
            .withMessageContaining(CANNOT_REMOVE_STATION_WHEN_ONLY_ONE_SECTIONS.getMessage());
    }
}