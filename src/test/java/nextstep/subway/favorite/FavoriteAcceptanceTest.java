package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.auth.acceptance.AuthSteps.로그인_요청;
import static nextstep.subway.auth.application.AuthServiceTest.*;
import static nextstep.subway.favorite.FavoriteSteps.*;
import static nextstep.subway.line.acceptance.LineSteps.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberSteps.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

// Feature
@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 판교역;
    private StationResponse 정자역;
    private TokenResponse 로그인후_발급된_토큰;

    // Background
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given: 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        // and: 지하철 노선 등록되어 있음
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId(), 20));
        // and: 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // and: 로그인 되어있음
        로그인후_발급된_토큰 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    // Scenario
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void scenario1() {
        // when: 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 생성된_즐겨찾기 = 즐겨찾기_생성_요청(로그인후_발급된_토큰, 강남역, 정자역);
        // then: 즐겨찾기 생성됨
        즐겨찾기_생성됨(생성된_즐겨찾기);

        // when: 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> 조회된_즐겨찾기_목록 = 즐겨찾기_목록_조회_요청(로그인후_발급된_토큰);
        // then: 즐겨찾기 목록 조회됨
        즐겨찾기_목록_조회됨(조회된_즐겨찾기_목록);

        // when: 즐겨찾기 삭제 요청
        ExtractableResponse<Response> 삭제된_즐겨찾기 = 즐겨찾기_삭제_요청(로그인후_발급된_토큰, 생성된_즐겨찾기);
        // then: 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(삭제된_즐겨찾기);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        List<Long> expected = favoriteResponses.stream()
                .map(FavoriteResponse::getId)
                .collect(toList());
        List<Long> actual = response.jsonPath().getList("id", Long.class);
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
