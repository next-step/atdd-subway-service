package nextstep.subway.line.domain;

import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineTest {

    private SectionRequest request;
    private Station 강남역;
    private Station 잠실역;

    @BeforeEach
    void setUp() {
        request = new SectionRequest(3L, 4L, 10);
        강남역 = new Station(1L, "강남역");
        잠실역 = new Station(2L,"잠실역");
    }

    @Test
    @DisplayName("Line에 존재하는 역들을 추출한다.")
    void getStations_test() {
        Station 강남역 = new Station(1L, "강남역");
        Station 왕십리역 = new Station(2L, "왕십리역");
        Line 첫번째_라인 = new Line("2호선", "red", 강남역, 왕십리역, 10);

        Station 청량리역 = new Station(2L, "청량리역");
        첫번째_라인.addSection(new Section(첫번째_라인, 왕십리역, 청량리역, 15));

        List<Station> 지하철역들 = 첫번째_라인.extractStations();

        assertThat(지하철역들).containsExactly(
                강남역,
                왕십리역,
                청량리역
        );
    }

    @Test
    @DisplayName("구간 추가 시 상행,하행 모두 일치하는 역이 없는 경우 예외처리한다.")
    void validateNonMatchStations_test() {
        //given
        Line 첫번째_라인 = new Line("2호선", "green", 강남역, 잠실역, 25);

        Station 영등포구청 = new Station(3L, "영등포구청");
        Station 대림역 = new Station(4L, "대림역");

        //when
        assertThrows(RuntimeException.class,
                () -> 첫번째_라인.validateAndAddSection(request, 영등포구청, 대림역)
        );
    }

    @Test
    @DisplayName("구간 추가 시 상행,하행 모두 일치하는 경우 예외처리한다.")
    void validateDuplicateStations_test() {
        //given
        Line 첫번째_라인 = new Line("2호선", "green", 강남역, 잠실역, 25);

        //when
        assertThrows(RuntimeException.class,
                () -> 첫번째_라인.validateAndAddSection(request, 강남역, 잠실역)
        );
    }

    @Test
    @DisplayName("하차역이 존재할 때 하차역을 승차역으로 변경한다.")
    void updatedUpToDownStation_test() {
        //given
        Line 첫번째_라인 = new Line("2호선", "green", 강남역, 잠실역, 25);

        Station 삼성역 = new Station(1L, "삼성역");

        //when
        첫번째_라인.validateAndAddSection(request, 강남역, 삼성역);

        Section section = 첫번째_라인.getSections().get(0);

        //then
        assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(15)
        );
    }

    @Test
    @DisplayName("승차역이 존재할 때 승차역을 하차역으로 변경한다.")
    void updatedDownToUpStation_test() {
        //given
        Line 첫번째_라인 = new Line("2호선", "green", 강남역, 잠실역, 25);

        Station 삼성역 = new Station(1L, "삼성역");

        //when
        첫번째_라인.validateAndAddSection(request, 삼성역, 잠실역);

        Section section = 첫번째_라인.getSections().get(0);

        //then
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(삼성역),
                () -> assertThat(section.getDistance()).isEqualTo(15)
        );
    }
}
