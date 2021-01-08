package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String 이메일 = "jaenyeong.dev@gmail.com";
    private static final String 비밀번호 = "1234";
    private String 사용자_토큰;
    private StationResponse 출발지_강남역;
    private StationResponse 도착지_정자역;
    private StationResponse 출발지_까치산역;
    private StationResponse 도착지_신도림역;

    @BeforeEach
    void setUpForFavorite() {
        회원_생성을_요청(이메일, 비밀번호, 31);
        사용자_토큰 = 로그인_되어_있음(이메일, 비밀번호).as(TokenResponse.class).getAccessToken();

        출발지_강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        도착지_정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        출발지_까치산역 = 지하철역_등록되어_있음("까치산역").as(StationResponse.class);
        도착지_신도림역 = 지하철역_등록되어_있음("신도림역").as(StationResponse.class);
    }

    private static ExtractableResponse<Response> 로그인_되어_있음(final String 이메일, final String 비밀번호) {
        return 로그인_요청(이메일, 비밀번호);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        final ExtractableResponse<Response> 첫번째_즐겨찾기_생성_요청_응답 = 즐겨찾기_생성을_요청(사용자_토큰, 출발지_강남역, 도착지_정자역);
        final Long 첫번째_즐겨찾기_아이디 = 즐겨찾기_아이디_가져오기(첫번째_즐겨찾기_생성_요청_응답);
        즐겨찾기_생성됨(첫번째_즐겨찾기_생성_요청_응답);

        final ExtractableResponse<Response> 두번째_즐겨찾기_생성을_요청_응답 = 즐겨찾기_생성을_요청(사용자_토큰, 출발지_까치산역, 도착지_신도림역);
        final Long 두번째_즐겨찾기_아이디 = 즐겨찾기_아이디_가져오기(두번째_즐겨찾기_생성을_요청_응답);
        즐겨찾기_생성됨(두번째_즐겨찾기_생성을_요청_응답);

        final ExtractableResponse<Response> 즐겨찾기_전체_조회_요청_응답 = 즐겨찾기_전체_조회_요청(사용자_토큰);
        assertThat(즐겨찾기_전체_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<Long> 즐겨찾기_전체_아이디_목록 = 즐겨찾기_아이디_목록으로_변경(즐겨찾기_전체_조회_요청_응답);

        assertThat(즐겨찾기_전체_아이디_목록.get(0)).isEqualTo(첫번째_즐겨찾기_아이디);
        assertThat(즐겨찾기_전체_아이디_목록.get(1)).isEqualTo(두번째_즐겨찾기_아이디);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(final String 사용자_토큰,
                                                            final StationResponse 출발지_강남역, final StationResponse 도착지_정자역) {
        final Map<String, String> 요청_파라미터 = new HashMap<>();
        요청_파라미터.put("source", 출발지_강남역.getId().toString());
        요청_파라미터.put("target", 도착지_정자역.getId().toString());

        return RestAssured
            .given().log().all()
            .auth().oauth2(사용자_토큰)
            .body(요청_파라미터)
            .accept(MediaType.ALL_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/favorites")
            .then()
            .log().all()
            .extract();
    }

    private long 즐겨찾기_아이디_가져오기(final ExtractableResponse<Response> 첫번째_즐겨찾기_생성_요청_응답) {
        return Long.parseLong(첫번째_즐겨찾기_생성_요청_응답.header("Location").split("/")[2]);
    }

    private void 즐겨찾기_생성됨(final ExtractableResponse<Response> 즐겨찾기_생성_요청_응답) {
        assertThat(즐겨찾기_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_전체_조회_요청(final String 사용자_토큰) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(사용자_토큰)
            .accept(MediaType.ALL_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/favorites")
            .then()
            .log().all()
            .extract();
    }

    private List<Long> 즐겨찾기_아이디_목록으로_변경(final ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", FavoriteResponse.class)
            .stream()
            .map(FavoriteResponse::getId)
            .collect(Collectors.toList());
    }
}
