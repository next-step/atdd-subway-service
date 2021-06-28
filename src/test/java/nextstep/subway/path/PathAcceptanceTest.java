package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공함;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.성인_회원;
import static nextstep.subway.member.MemberAcceptanceTest.아기_회원;
import static nextstep.subway.member.MemberAcceptanceTest.어린이_회원;
import static nextstep.subway.member.MemberAcceptanceTest.청소년_회원;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptancePerClassTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


@DisplayName("지하철 경로 조회 인수테스트")
public class PathAcceptanceTest extends AcceptancePerClassTest {

    private StationResponse 서울역;
    private StationResponse 시청역;
    private StationResponse 종각역;
    private StationResponse 종로3가역;
    private StationResponse 아현역;
    private StationResponse 충정로역;
    private StationResponse 을지로입구역;
    private StationResponse 독립문역;
    private StationResponse 경복궁역;
    private StationResponse 애오개역;
    private StationResponse 서대문역;
    private StationResponse 광화문역;
    private StationResponse 회현역;
    private StationResponse 명동역;

    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private LineResponse 오호선;

    private int 기본요금 = 1_250;
    private int 일호선_요금 = 500;
    private int 사호선_요금 = 900;
    private int 거리_29KM = 29;
    private int 거리_59KM = 59;
    private int 거리_29KM_초과요금 = 400;
    private int 거리_59KM_초과요금 = 1000;
    private int 청소년_기본할인_요금 = -350;
    private int 청소년_추가_할인율 = 20;
    private int 어린이_기본할인_요금 = -350;
    private int 어린이_추가_할인율 = 50;
    private int 무료 = 0;

    @BeforeAll
    void setup() {
        //1호선
        서울역 = StationAcceptanceTest.지하철역_등록되어_있음("서울역").as(StationResponse.class);
        시청역 = StationAcceptanceTest.지하철역_등록되어_있음("시청역").as(StationResponse.class);
        종각역 = StationAcceptanceTest.지하철역_등록되어_있음("종각역").as(StationResponse.class);
        종로3가역 = StationAcceptanceTest.지하철역_등록되어_있음("종로3가역").as(StationResponse.class);
        일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("일호선", "blue", 서울역, 시청역, 10, 일호선_요금)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 시청역, 종각역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 종각역, 종로3가역, 9);

        //2호선
        아현역 = StationAcceptanceTest.지하철역_등록되어_있음("아현역").as(StationResponse.class);
        충정로역 = StationAcceptanceTest.지하철역_등록되어_있음("충정로역").as(StationResponse.class);
        을지로입구역 = StationAcceptanceTest.지하철역_등록되어_있음("을지로입구역").as(StationResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "green", 아현역, 충정로역, 10)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 충정로역, 시청역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 시청역, 을지로입구역, 10);

        //3호선
        독립문역 = StationAcceptanceTest.지하철역_등록되어_있음("독립문역").as(StationResponse.class);
        경복궁역 = StationAcceptanceTest.지하철역_등록되어_있음("경복궁역").as(StationResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("삼호선", "orange", 독립문역, 경복궁역, 10)).as(LineResponse.class);

        //4호선
        회현역 = StationAcceptanceTest.지하철역_등록되어_있음("회현역").as(StationResponse.class);
        명동역 = StationAcceptanceTest.지하철역_등록되어_있음("명동역").as(StationResponse.class);
        사호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("사호선", "skyblue", 서울역, 회현역, 10, 사호선_요금)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(사호선, 회현역, 명동역, 10);

        //5호선
        애오개역 = StationAcceptanceTest.지하철역_등록되어_있음("애오개역").as(StationResponse.class);
        서대문역 = StationAcceptanceTest.지하철역_등록되어_있음("서대문역").as(StationResponse.class);
        광화문역 = StationAcceptanceTest.지하철역_등록되어_있음("광화문역").as(StationResponse.class);
        오호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("오호선", "purple", 애오개역, 충정로역, 10)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(오호선, 충정로역, 서대문역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(오호선, 서대문역, 광화문역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(오호선, 광화문역, 종로3가역, 10);
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (비회원, 노선요금, 59km 초과 추가요금 적용)")
    @Test
    void 최단경로조회_비회원_59KM초과() {
        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역)
            , 거리_59KM
            , 기본요금 + 사호선_요금 + 거리_59KM_초과요금
        );
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (성인, 노선요금, 59km 초과 추가요금 적용)")
    @Test
    void 최단경로조회_성인_59KM초과() {
        // Given
        String 로그인_토큰 = 로그인_되어있음(성인_회원);

        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(로그인_토큰, 광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역)
            , 거리_59KM
            , 기본요금 + 사호선_요금 + 거리_59KM_초과요금
        );
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (청소년 할인, 노선요금, 59km 초과 추가요금 적용)")
    @Test
    void 최단경로조회_청소년_59KM초과() {
        // Given
        String 로그인_토큰 = 로그인_되어있음(청소년_회원);

        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(로그인_토큰, 광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역)
            , 거리_59KM
            , 기본요금 + 사호선_요금 + 거리_59KM_초과요금 + 청소년_기본할인_요금
            , 청소년_추가_할인율
        );
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (어린이 할인, 노선요금, 59km 초과 추가요금 적용)")
    @Test
    void 최단경로조회_어린이_59KM초과() {
        // Given
        String 로그인_토큰 = 로그인_되어있음(어린이_회원);

        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(로그인_토큰, 광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역)
            , 거리_59KM
            , 기본요금 + 사호선_요금 + 거리_59KM_초과요금 + 어린이_기본할인_요금
            , 어린이_추가_할인율
        );
    }

    @DisplayName("지하철 구간의 최단경로, 거리, 금액을 조회 (아기, 노선요금, 59km 초과 추가요금 적용)")
    @Test
    void 최단경로조회_아기_59KM초과() {
        // Given
        String 로그인_토큰 = 로그인_되어있음(아기_회원);

        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(로그인_토큰, 광화문역, 명동역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역, 서울역, 회현역, 명동역)
            , 거리_59KM
            , 무료
        );
    }

    @DisplayName("최단 경로 찾기 - 동일한 경로가 여럿인경우(거쳐가는 지하철역 개수가 동일) 짧은 거리를 기준")
    @Test
    void findPathByPointsWithDistance() {
        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(광화문역, 시청역);

        // Then
        지하철_최단_경로_목록_응답됨(조회결과);
        지하철_최단_경로_조회됨(조회결과
            , Arrays.asList(광화문역, 종로3가역, 종각역, 시청역)
            , 거리_29KM
            , 기본요금 + 일호선_요금 + 거리_29KM_초과요금
        );
    }

    @DisplayName("출발역과 도착역이 같을 경우 예외 발생")
    @Test
    void originAndDestinationSame() {
        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(광화문역, 광화문역);

        // Then
        지하철_최단_경로_조회_실패함(조회결과);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    @Test
    void notConnectedOriginAndDestination() {
        // When
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(광화문역, 독립문역);

        // Then
        지하철_최단_경로_조회_실패함(조회결과);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외 발생")
    @Test
    void notExistOriginAndDestination() {
        // When
        StationResponse 알수없는역 = new StationResponse(999L, "알수없는역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> 조회결과 = 지하철_최단_경로_조회_요청(광화문역, 알수없는역);

        // Then
        지하철_최단_경로_조회_실패함(조회결과);
    }

    private void 지하철_최단_경로_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_최단_경로_조회_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStationResponses, int distance, int fare) {
        지하철_최단_경로_조회됨(response, expectStationResponses, distance, fare, 0);
    }

    private void 지하철_최단_경로_조회됨(ExtractableResponse<Response> response, List<StationResponse> expectStationResponses, int distance, int fare, int discountRate) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations()).containsExactly(expectStationResponses.toArray(new StationResponse[0]));
        assertThat(pathResponse.getDistance()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isNotNull();
        assertThat(pathResponse.getFare()).isEqualTo(할인율_적용(fare, discountRate));
    }

    private int 할인율_적용(int fare, int discountRate) {
        return (int) (fare - ((discountRate * 0.01) * fare));
    }

    private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(StationResponse startStation, StationResponse endStation) {
        return get(String.format("/paths?source=%d&target=%d", startStation.getId(), endStation.getId()));
    }

    private ExtractableResponse<Response> 지하철_최단_경로_조회_요청(String accessToken, StationResponse startStation, StationResponse endStation) {
        return get(String.format("/paths?source=%d&target=%d", startStation.getId(), endStation.getId()), accessToken);
    }

    private String 로그인_되어있음(MemberRequest 회원) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(회원);
        회원_생성됨(createResponse);
        ExtractableResponse<Response> loginResponse = 로그인_요청(회원);
        로그인_성공함(loginResponse);
        return loginResponse.as(TokenResponse.class).getAccessToken();
    }

}
