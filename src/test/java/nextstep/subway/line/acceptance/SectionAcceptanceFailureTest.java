package nextstep.subway.line.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.acceptance.StationAcceptance;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("지하철 구간 관련 실패 테스트")
class SectionAcceptanceFailureTest extends AcceptanceTest {

    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 잠실역;
    private StationResponse 잠실나루역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = StationAcceptance.create_station("교대역").as(StationResponse.class);
        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        역삼역 = StationAcceptance.create_station("역삼역").as(StationResponse.class);
        잠실역 = StationAcceptance.create_station("잠실역").as(StationResponse.class);
        잠실나루역 = StationAcceptance.create_station("잠실나루역").as(StationResponse.class);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 이미 등록되어 있는 구간을 생성하면
     * Then 지하철 구간을 생성할 수 없다
     */
    @DisplayName("이미 등록되어 있는 구간을 생성한다.")
    @Test
    void updateAlreadyAddedSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response =
                SectionAcceptance.update_section(이호선.getId(), 교대역.getId(), 강남역.getId(), 10);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 거리가 추가하려는 구간의 거리보다 크거나 같으면
     * Then 지하철 구간을 생성할 수 없다
     */
    @DisplayName("잘못된 거리 정보로 지하철 구간을 생성한다.")
    @Test
    void updateWithInvalidDistance() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                역삼역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response =
                SectionAcceptance.update_section(이호선.getId(), 교대역.getId(), 강남역.getId(), 20);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 상행역과 하행역이 모두 기존 구간에 포함되어 있지 않으면
     * Then 지하철 구간을 생성할 수 없다
     */
    @DisplayName("상행역과 하행역이 모두 기존 구간에 포함되어 있지 않을 때 지하철 구간을 생성한다.")
    @Test
    void updateWhenPositionNotFound() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response =
                SectionAcceptance.update_section(이호선.getId(), 잠실역.getId(), 잠실나루역.getId(), 20);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 지하철 구간을 생성하고
     * When 구간에 포함되어 있지 않은 역을 삭제하면
     * Then 구간을 삭제할 수 없다
     */
    @DisplayName("지하철 구간에 포함되어 있지 않은 역을 삭제한다.")
    @Test
    void deleteWhenSectionNotContainLine() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response =
                SectionAcceptance.delete_section(이호선.getId(), 잠실역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철 노선과 구간을 생성하고
     * When 구간이 하나인 노선에서 마지막 구간을 삭제하면
     * Then 구간을 삭제할 수 없다
     */
    @DisplayName("지하철 구간이 하나인 노선에서 마지막 구간을 삭제한다.")
    @Test
    void deleteWhenLastOneSection() {
        // given
        LineResponse 이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 교대역.getId(),
                강남역.getId(), 10).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response =
                SectionAcceptance.delete_section(이호선.getId(), 교대역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }
}
