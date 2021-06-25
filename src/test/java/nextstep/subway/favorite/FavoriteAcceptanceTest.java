package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptancePerClassTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptancePerClassTest {

    private StationResponse 서대문역;
    private StationResponse 광화문역;
    private LineResponse 오호선;
    private String loginAccessToken;

    @BeforeAll
    void setup() {
        서대문역 = StationAcceptanceTest.지하철역_등록되어_있음("서대문역").as(StationResponse.class);
        광화문역 = StationAcceptanceTest.지하철역_등록되어_있음("광화문역").as(StationResponse.class);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("오호선", "purple", 서대문역, 광화문역, 10)).as(LineResponse.class);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(회원);
        회원_생성됨(createResponse);
        ExtractableResponse<Response> loginResponse = 로그인_요청(회원);
        로그인_성공함(loginResponse);
        loginAccessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("나의 즐겨찾기 관리")
    @Test
    void manageMyFavorite() {
        // When
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(서대문역, 광화문역);

        // Then
        즐겨찾기_생성됨(createResponse);

        // When
        ExtractableResponse<Response> listResponse = 즐겨찾기_목록_조회_요청();

        // Then
        List<FavoriteResponse> favoriteResponses = 즐겨찾기_목록_조회됨(listResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(favoriteResponses.get(0));

        // Then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(StationResponse 서대문역, StationResponse 광화문역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(서대문역.getId(), 광화문역.getId());
        return post(favoriteRequest, "/favorites", loginAccessToken);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return get("/favorites", loginAccessToken);
    }

    private List<FavoriteResponse> 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = new ArrayList<>(response.jsonPath().getList(".", FavoriteResponse.class));
        assertThat(favoriteResponses).hasSize(1);
        return favoriteResponses;
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(FavoriteResponse createdFavorite) {
        return delete("/favorites/" + createdFavorite.getId(), loginAccessToken);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}