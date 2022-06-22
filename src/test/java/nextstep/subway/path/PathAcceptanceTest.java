package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.GivenSubway;
import nextstep.subway.path.dto.PathResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private GivenSubway 지하철;

    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철 = new GivenSubway();
    }

    @DisplayName("경로를 조회한다")
    @Test
    void getPath() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.남부터미널역.getId());

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 13);
    }

    @DisplayName("출발역과 도착역이 같을 때 경로를 조회한다")
    @Test
    void getPathWithSameStations() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.강남역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("존재하지 않는 경로를 조회한다")
    @Test
    void getNotExistedPath() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.여의도역.getId());

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("없는 역에 대해 경로를 조회한다")
    @Test
    void getPathWithNotExistedStation() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 0L);

        // then
        경로_조회_실패됨(response);
    }

    private ExtractableResponse<Response> 경로_조회_요청(final long sourceId, final long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", sourceId)
                .queryParam("target", targetId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private PathResponse 경로가_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final PathResponse path = response.as(PathResponse.class);
        assertThat(path).isNotNull();

        return path;
    }

    private void 경로의_길이가_일치됨(final PathResponse path, final int distance) {
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    private void 경로_조회_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
