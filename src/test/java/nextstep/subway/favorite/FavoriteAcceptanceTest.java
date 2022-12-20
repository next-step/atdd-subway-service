package nextstep.subway.favorite;

import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.auth.acceptance.AuthRestAssured.로그인_요청;
import static nextstep.subway.favorite.FavoriteRestAssured.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.FavoriteRestAssured.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteRestAssured.즐겨찾기_생성_요청;
import static nextstep.subway.line.acceptance.LineRestAssured.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.line.acceptance.LineSectionRestAssured.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * Feature: 즐겨찾기를 관리한다.
 *
 *   Background
 *     Given 지하철역 등록되어 있음
 *     And 지하철 노선 등록되어 있음
 *     And 지하철 노선에 지하철역 등록되어 있음
 *     And 회원 등록되어 있음
 *     And 로그인 되어있음
 *
 *   Scenario: 즐겨찾기를 관리
 *     When 즐겨찾기 생성을 요청
 *     Then 즐겨찾기 생성됨
 *     When 즐겨찾기 목록 조회 요청
 *     Then 즐겨찾기 목록 조회됨
 *     When 즐겨찾기 삭제 요청
 *     Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 경의중앙선;
    private StationResponse 홍대입구역;
    private StationResponse 용산역;
    private StationResponse 왕십리역;
    private String accessToken;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        홍대입구역 = 지하철역_등록되어_있음("홍대입구역").as(StationResponse.class);
        용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("경의중앙선", "sky-blue", 홍대입구역.getId(), 왕십리역.getId(), 10);
        경의중앙선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(경의중앙선, 홍대입구역, 용산역, 5);
        지하철_노선에_지하철역_등록됨(response);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_됨(loginResponse);

        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기 관리")
    @TestFactory
    Stream<DynamicTest> manageFavorites() {
        final ExtractableResponse<Response>[] readResponse = new ExtractableResponse[]{new RestAssuredResponseImpl()};
        return Stream.of(
                dynamicTest("즐겨찾기 생성", () -> {
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 홍대입구역.getId(), 왕십리역.getId());
                    즐겨찾기_생성됨(createResponse);
                }),
                dynamicTest("즐겨찾기 생성", () -> {
                    ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 홍대입구역.getId(), 용산역.getId());
                    즐겨찾기_생성됨(createResponse);
                }),
                dynamicTest("즐겨찾기 목록 조회", () -> {
                    readResponse[0] = 즐겨찾기_목록_조회_요청(accessToken);
                    즐겨찾기_목록_조회됨(readResponse[0], 홍대입구역.getId(), 왕십리역.getId(), 0, 2);
                }),
                dynamicTest("즐겨찾기 목록 조회", () -> {
                    readResponse[0] = 즐겨찾기_목록_조회_요청(accessToken);
                    즐겨찾기_목록_조회됨(readResponse[0], 홍대입구역.getId(), 용산역.getId(), 1, 2);
                }),
                dynamicTest("즐겨찾기 삭제 요청", () -> {
                    ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, readResponse[0].as(FavoriteResponse[].class)[0].getId());
                    즐겨찾기_삭제됨(deleteResponse);
                }),
                dynamicTest("즐겨찾기 목록 조회", () -> {
                    readResponse[0] = 즐겨찾기_목록_조회_요청(accessToken);
                    즐겨찾기_목록_조회됨(readResponse[0], 홍대입구역.getId(), 용산역.getId(), 0, 1);
                })
        );
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, Long source, Long target, int index, int size) {
        List<FavoriteResponse> favorites = Arrays.asList(response.as(FavoriteResponse[].class));
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favorites).hasSize(size),
                () -> assertThat(favorites.get(index).getSource().getId()).isEqualTo(source),
                () -> assertThat(favorites.get(index).getTarget().getId()).isEqualTo(target)
        );
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
