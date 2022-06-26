package nextstep.subway.favorite.acceptance;

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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.favorite.acceptance.FavoriteRequests.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberRequests.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 정자역;
    private StationResponse 판교역;
    private TokenResponse 인증_토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 정자역.getId(), 20000));
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        인증_토큰 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다.")
    void manageFavorite() {
        // when
        ExtractableResponse<Response> 생성된_즐겨찾기 = 즐겨찾기_생성_요청(인증_토큰, 강남역, 정자역);
        // then
        즐겨찾기_생성됨(생성된_즐겨찾기);

        // given
        즐겨찾기_생성_요청(인증_토큰, 정자역, 판교역);
        // when
        ExtractableResponse<Response> 조회된_즐겨찾기_목록 = 즐겨찾기_목록_조회_요청(인증_토큰);
        // then
        List<Long> 즐겨찾기_모든_역IDs = Arrays.asList(강남역.getId(), 정자역.getId(), 판교역.getId());
        즐겨찾기_목록_조회됨(조회된_즐겨찾기_목록, 즐겨찾기_모든_역IDs);

        // when
        ExtractableResponse<Response> 삭제된_즐겨찾기 = 즐겨찾기_삭제_요청(인증_토큰, 생성된_즐겨찾기);
        // then
        즐겨찾기_삭제됨(삭제된_즐겨찾기);
    }


    private static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, List<Long> expectedStationIds) {
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        List<Long> actualStationIds = favoriteResponses.stream()
                .flatMap(favorite -> Stream.of(favorite.getSource().getId(), favorite.getTarget().getId()))
                .distinct()
                .collect(Collectors.toList());

        assertThat(actualStationIds).containsExactlyElementsOf(expectedStationIds);
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
