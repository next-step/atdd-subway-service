package nextstep.subway.line.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.StationAcceptance;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = StationAcceptance.create_station("교대역").as(StationResponse.class);
        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        역삼역 = StationAcceptance.create_station("역삼역").as(StationResponse.class);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 역 사이에 새로운 구간을 생성하면
     * Then 지하철 구간이 분리된다
     */
    @DisplayName("역 사이에 새로운 구간을 생성한다.")
    @Test
    void updateMiddleSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(),
                역삼역.getId(), 10).as(LineResponse.class);

        // when
        SectionAcceptance.update_section(이호선.getId(), 교대역.getId(), 강남역.getId(), 7);

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(교대역, 강남역, 역삼역));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 맨 앞에 새로운 구간을 생성하면
     * Then 지하철 구간이 생성된다
     */
    @DisplayName("맨 앞에 새로운 구간을 생성한다.")
    @Test
    void updateFirstSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("이호선", "bg-green-600", 강남역.getId(),
                역삼역.getId(), 10).as(LineResponse.class);

        // when
        SectionAcceptance.update_section(이호선.getId(), 교대역.getId(), 강남역.getId(), 7);

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(교대역, 강남역, 역삼역));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 맨 뒤에 새로운 구간을 생성하면
     * Then 지하철 구간이 생성된다
     */
    @DisplayName("맨 뒤에 새로운 구간을 생성한다.")
    @Test
    void updateLastSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("이호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);

        // when
        SectionAcceptance.update_section(이호선.getId(), 강남역.getId(), 역삼역.getId(), 7);

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(교대역, 강남역, 역삼역));
    }

    /**
     * Given 지하철 노선과 지하철 구간을 생성하고
     * When 역 사이의 구간을 삭제하면
     * Then 기존 구간이 연장된다
     */
    @DisplayName("역 사이 구간을 삭제한다.")
    @Test
    void deleteMiddleSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);
        SectionAcceptance.update_section(이호선.getId(), 강남역.getId(), 역삼역.getId(), 10);

        // when
        SectionAcceptance.delete_section(이호선.getId(), 강남역.getId());

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(교대역, 역삼역));
    }

    /**
     * Given 지하철 노선과 지하철 구간을 생성하고
     * When 맨 앞의 구간을 삭제하면
     * Then 구간이 삭제된다
     */
    @DisplayName("맨 앞의 구간을 삭제한다.")
    @Test
    void deleteFirstSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);
        SectionAcceptance.update_section(이호선.getId(), 강남역.getId(), 역삼역.getId(), 10);

        // when
        SectionAcceptance.delete_section(이호선.getId(), 교대역.getId());

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(강남역, 역삼역));
    }

    /**
     * Given 지하철 노선과 지하철 구간을 생성하고
     * When 맨 뒤의 구간을 삭제하면
     * Then 구간이 삭제된다
     */
    @DisplayName("맨 뒤의 구간을 삭제한다.")
    @Test
    void deleteLastSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);
        SectionAcceptance.update_section(이호선.getId(), 강남역.getId(), 역삼역.getId(), 10);

        // when
        SectionAcceptance.delete_section(이호선.getId(), 역삼역.getId());

        // then
        LineResponse lineResponse = LineAcceptance.line_was_queried(이호선.getId());
        SectionAcceptance.stations_are_sorted_on_the_subway_line(lineResponse, Arrays.asList(교대역, 강남역));
    }
}
