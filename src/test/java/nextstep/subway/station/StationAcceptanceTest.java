package nextstep.subway.station;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = StationFixture.지하철역_생성_요청(StationFixture.강남역);

        // then
        StationFixture.지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        //given
        StationFixture.지하철역_등록되어_있음(StationFixture.강남역);

        // when
        ExtractableResponse<Response> response = StationFixture.지하철역_생성_요청(StationFixture.강남역);

        // then
        StationFixture.지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        ExtractableResponse<Response> createResponse1 = StationFixture
            .지하철역_등록되어_있음(StationFixture.강남역);
        ExtractableResponse<Response> createResponse2 = StationFixture
            .지하철역_등록되어_있음(StationFixture.역삼역);

        // when
        ExtractableResponse<Response> response = StationFixture.지하철역_목록_조회_요청();

        // then
        StationFixture.지하철역_목록_응답됨(response);
        StationFixture.지하철역_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = StationFixture
            .지하철역_등록되어_있음(StationFixture.강남역);

        // when
        ExtractableResponse<Response> response = StationFixture.지하철역_제거_요청(createResponse);

        // then
        StationFixture.지하철역_삭제됨(response);
    }
}
