package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.utils.fixture.AcceptanceTestMemberFixture;
import nextstep.subway.utils.fixture.AcceptanceTestSubwayFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private AcceptanceTestSubwayFixture 지하철;
    private AcceptanceTestMemberFixture 회원;

    @BeforeEach
    public void setUp() {
        super.setUp();
        지하철 = new AcceptanceTestSubwayFixture();
        회원 = new AcceptanceTestMemberFixture();
    }

    @DisplayName("비회원이 경로를 조회한다")
    @Test
    void getPathByGuestUser() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.남부터미널역.getId(), null);

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 13);
        요금이_일치됨(path, 1350);
    }

    @DisplayName("출발역과 도착역이 같을 때 경로를 조회한다")
    @Test
    void getPathWithSameStations() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.강남역.getId(), null);

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("존재하지 않는 경로를 조회한다")
    @Test
    void getNotExistedPath() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 지하철.여의도역.getId(), null);

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("없는 역에 대해 경로를 조회한다")
    @Test
    void getPathWithNotExistedStation() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.강남역.getId(), 0L, null);

        // then
        경로_조회_실패됨(response);
    }

    @DisplayName("비회원이 추가요금이 있는 노선이 포함된 경로를 조회한다")
    @Test
    void getPathWithExtraFeeByGuestUser() {
        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.여의도역.getId(), 지하철.샛강역.getId(), null);

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 55);
        요금이_일치됨(path, 3150);
    }

    @DisplayName("성인 회원이 추가요금이 있는 노선이 포함된 경로를 조회한다")
    @Test
    void getPathWithExtraFeeByAdultUser() {
        // given
        final String token = AuthAcceptanceTest
                .로그인_요청(회원.성인.getEmail(), 회원.공통_비밀번호)
                .as(TokenResponse.class)
                .getAccessToken();

        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.여의도역.getId(), 지하철.샛강역.getId(), token);

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 55);
        요금이_일치됨(path, 3150);
    }

    @DisplayName("청소년 회원이 추가요금이 있는 노선이 포함된 경로를 조회한다")
    @Test
    void getPathWithExtraFeeByTeenagerUser() {
        // given
        final String token = AuthAcceptanceTest
                .로그인_요청(회원.청소년.getEmail(), 회원.공통_비밀번호)
                .as(TokenResponse.class)
                .getAccessToken();

        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.여의도역.getId(), 지하철.샛강역.getId(), token);

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 55);
        요금이_일치됨(path, 2240);
    }

    @DisplayName("어린이 회원이 추가요금이 있는 노선이 포함된 경로를 조회한다")
    @Test
    void getPathWithExtraFeeByKidUser() {
        // given
        final String token = AuthAcceptanceTest
                .로그인_요청(회원.어린이.getEmail(), 회원.공통_비밀번호)
                .as(TokenResponse.class)
                .getAccessToken();

        // when
        final ExtractableResponse<Response> response = 경로_조회_요청(지하철.여의도역.getId(), 지하철.샛강역.getId(), token);

        // then
        final PathResponse path = 경로가_응답됨(response);

        // then
        경로의_길이가_일치됨(path, 55);
        요금이_일치됨(path, 1400);
    }

    private ExtractableResponse<Response> 경로_조회_요청(final long sourceId, final long targetId, final String token) {
        RequestSpecification given = RestAssured.given().log().all().accept(MediaType.APPLICATION_JSON_VALUE);
        if (null != token) {
            given = given.auth().oauth2(token);
        }
        return given
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

    private void 요금이_일치됨(final PathResponse path, final int fare) {
        assertThat(path.getFare()).isEqualTo(fare);
    }

    private void 경로_조회_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
