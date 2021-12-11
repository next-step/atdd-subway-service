package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ApiRequest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 광교역;
    private LineRequest lineRequest;
    private LineResponse 이호선;
    private String email;
    private String password;
    private TokenResponse token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        lineRequest = new LineRequest("이호선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 5);

        email = "test@emila.com";
        password = "pass";
        MemberAcceptanceTest.회원_생성되어있음(email, password, 1);
        token = AuthAcceptanceTest.로그인을_요청한다(email, password).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청한다(token.getAccessToken(), 강남역.getId(), 역삼역.getId());

        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> searchResponse = 즐겨찾기_목록_조회_요청(token.getAccessToken());

        // then
        즐겨찾기_목록_조회됨(searchResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token.getAccessToken());

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response>  즐겨찾기_생성을_요청한다(String token, Long sourceId, Long targetId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        return ApiRequest.postWithAuth("/favorites/me", token, favoriteRequest);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> searchResponse, FavoriteResponse... expectResponses) {
        List<Long> resultLineIds = searchResponse.jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = Arrays.stream(expectResponses)
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token) {
        return ApiRequest.deleteWithAuth("/favorites/me", token);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return ApiRequest.getWithAuth("/favorites/me", token);
    }
}