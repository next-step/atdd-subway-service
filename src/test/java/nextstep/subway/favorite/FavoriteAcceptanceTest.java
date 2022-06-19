package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.factory.AuthAcceptanceFactory.로그인_되어_있음;
import static nextstep.subway.member.factory.MemberAcceptanceFactory.회원_생성을_요청;
import static nextstep.subway.path.factory.PathAcceptanceFactory.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 이호선;
    private StationResponse 역삼역;
    private StationResponse 강남역;
    private StationResponse 교대역;

    private String 이메일 = "14km@github.com";
    private String 패스워드 = "a1s2d3f4";
    private int 나이 = 20;

    private String 사용자_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        역삼역 = 지하철역_등록되어_있음("역삼역");
        강남역 = 지하철역_등록되어_있음("강남역");
        교대역 = 지하철역_등록되어_있음("교대역");

        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-600", 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 3);

        회원_생성을_요청(이메일, 패스워드, 나이);
        사용자_토큰 = 로그인_되어_있음(이메일, 패스워드);
    }

    @Test
    @DisplayName("즐겨찾기 신규 추가")
    void createFavorites() {
        Long 역삼역_ID = 역삼역.getId();
        Long 교대역_ID = 교대역.getId();

        즐겨찾기_추가(사용자_토큰, 역삼역_ID, 교대역_ID);
    }

    public ExtractableResponse<Response> 즐겨찾기_추가(String accessToken, Long source, Long target) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(source, target);

        return RestAssured
                .given().log().all()
                .body(favoriteRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}
