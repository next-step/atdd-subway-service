package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.step.AuthStep.로그인_되어_있음;
import static nextstep.subway.auth.step.AuthStep.회원_등록되어_있음;
import static nextstep.subway.favorite.step.FavoriteStep.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String 이메일 = "email@gmail.com";
    private static final String 비밀번호 = "password";
    private static final int 나이 = 20;

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private TokenResponse 토큰;

    @BeforeEach
    void beforeEach() {
        super.setUp();

        회원_등록되어_있음(이메일, 비밀번호, 나이);
        토큰 = 로그인_되어_있음(이메일, 비밀번호);

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
    }

    @Test
    void createFavorite_즐겨찾기를_생성한다() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(토큰, 강남역, 역삼역);

        // then
        즐겨찾기_생성됨(response);
    }

    @Test
    void findFavorites_즐겨찾기_목록을_조회한다() {
        // given
        즐겨찾기_생성되어_있음(토큰, 강남역, 역삼역);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(토큰);

        // then
        즐겨찾기_목록_조회됨(response);
    }
}