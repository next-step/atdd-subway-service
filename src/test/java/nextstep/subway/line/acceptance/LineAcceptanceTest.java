package nextstep.subway.line.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
    }

	@DisplayName("지하철 노선 등록, 수정 및 삭제 시나리오")
	@Test
	void lineAddAndUpdateAndDeleteScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음

		// Scenario : 지하철 노선 등록, 수정 및 삭제 시나리오
		// When : 지하철 노선 생성 요청
		ExtractableResponse<Response> createResponse1 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1);
		ExtractableResponse<Response> createResponse2 = LineTestMethod.지하철_노선_등록되어_있음(lineRequest2);
		// Then : 지하철 노성 생성됨
		LineTestMethod.지하철_노선_생성됨(createResponse1);
		LineTestMethod.지하철_노선_생성됨(createResponse2);
		// When : 지하철 노선 목록을 조회 요청
		ExtractableResponse<Response> response = LineTestMethod.지하철_노선_목록_조회_요청();
		// Then : 지하철 노선 목록 조회됨
		LineTestMethod.지하철_노선_목록_응답됨(response);
		LineTestMethod.지하철_노선_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
		// When : 지하철 노선을 조회 요청
		ExtractableResponse<Response> findResponse = LineTestMethod.지하철_노선_목록_조회_요청(createResponse1);
		// then : 지하철 노선 응답됨
		LineTestMethod.지하철_노선_응답됨(findResponse, createResponse1);
		// When : 지하철 노선을 수정 요청
		LineRequest lineRequest3 = new LineRequest("2호선", "bg-green-600", 강남역.getId(), 광교역.getId(), 9);
		ExtractableResponse<Response> updateResponse = LineTestMethod.지하철_노선_수정_요청(createResponse1, lineRequest3);
		// Then : 지하철 노선 수정됨
		LineTestMethod.지하철_노선_수정됨(updateResponse);
		// when : 지하철 노선 제거 요청
		ExtractableResponse<Response> deleteResponse = LineTestMethod.지하철_노선_제거_요청(createResponse1);
		// Then : 지하철 노선 삭제됨
		LineTestMethod.지하철_노선_삭제됨(deleteResponse);
	}

	@DisplayName("지하철 노선 등록 에러 시나리오")
	@Test
	void lineAddErrorScenario() {
		// Backgroud
		// Given : 지하철역 등록되어 있음

		// Scenario : 지하철 노선 등록, 수정 및 삭제 시나리오
		// When : 지하철 노선 생성 요청
		ExtractableResponse<Response> createResponse = LineTestMethod.지하철_노선_등록되어_있음(lineRequest1);
		// Then : 지하철 노성 생성됨
		LineTestMethod.지하철_노선_생성됨(createResponse);
		// When : 기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성 요청
		ExtractableResponse<Response> response = LineTestMethod.지하철_노선_생성_요청(lineRequest1);
		// Then : 지하철_노선_생성_실패
		LineTestMethod.지하철_노선_생성_실패됨(response);
	}
}
