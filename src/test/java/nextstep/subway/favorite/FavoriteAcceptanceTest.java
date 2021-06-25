package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.AuthHelper.로그인_요청;
import static nextstep.subway.favorite.FavoriteHelper.*;
import static nextstep.subway.line.acceptance.LineHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberHelper.회원_생성을_요청;
import static nextstep.subway.station.StationHelper.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;

    private String accessToken;

    private ExtractableResponse createResponse;

    @BeforeEach
    void 미리_생성() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 강남역.getId(), 역삼역.getId(), 5, 400)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, 5);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(tokenRequest);
        accessToken = loginResponse.jsonPath().getObject(".", TokenResponse.class).getAccessToken();

        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 선릉역.getId());
        createResponse = 즐겨찾기_생성_요청(favoriteRequest, accessToken);
    }


    @DisplayName("즐겨찾기_관리")
    @Test
    void 즐겨찾기_관리() {
        //given
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(강남역.getId(), 역삼역.getId());

        //when
        즐겨찾기_생성_요청(favoriteRequest2, accessToken);

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        ExtractableResponse findResponse = 즐겨찾기_목록_조회_요청(accessToken);

        //then
        assertThat(findResponse.jsonPath().getList(".", FavoriteResponse.class)).hasSize(2);

        //given
        String uri = createResponse.header("Location");

        //when
        ExtractableResponse deleteResponse = 즐겨찾기_삭제(uri, accessToken);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse findResponseAfterDelete = 즐겨찾기_목록_조회_요청(accessToken);
        assertThat(findResponseAfterDelete.jsonPath().getList(".", FavoriteResponse.class)).hasSize(1);
    }

    @DisplayName("다른 사용자가 내 즐겨찾기 삭제 시도시")
    @Test
    void 다른_사용자_내_즐겨찾기_삭제() {
        //given
        회원_생성을_요청("abc@abc.com", "pass", 30);
        TokenRequest tokenRequest = new TokenRequest("abc@abc.com", "pass");
        ExtractableResponse<Response> loginResponse = 로그인_요청(tokenRequest);
        String accessToken = loginResponse.jsonPath().getObject(".", TokenResponse.class).getAccessToken();
        String uri = createResponse.header("Location");

        //when
        ExtractableResponse deleteResponse = 즐겨찾기_삭제(uri, accessToken);

        //then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}