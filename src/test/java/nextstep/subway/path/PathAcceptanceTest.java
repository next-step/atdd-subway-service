package nextstep.subway.path;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.apiHelper.LineApiHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.utils.apiHelper.LineSectionApiHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.utils.apiHelper.MemberApiHelper.회원_생성을_요청;
import static nextstep.subway.utils.apiHelper.PathApiHelper.지하철_경로_조회_요청;
import static nextstep.subway.utils.assertionHelper.PathAssertionHelper.최단경로_결과_확인;
import static nextstep.subway.utils.assertionHelper.PathAssertionHelper.최단경로_금액_확인;
import static nextstep.subway.utils.assertionHelper.PathAssertionHelper.최단경로_조회불가;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "testPassword";
    private static final int AGE = 30;
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
            new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 7)).as(
            LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 9)).as(
            LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
            new LineRequest("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 8)).as(
            LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     * background
         * given : 사용자를 생성하고
         * given : 위와같은 지하철 도면일때
     * given : 생성한 사용자에 대한 토큰을 획득하고
     * when 교대~양재역 까지의 거리 및 경로를 조회했을때
     * then 교대~남부터미널~양재 경로로 안내된다.
     * then 지나온 정거장 만큼 금액이 안내된다
     */
    @Test
    public void 지하철_경로_조회하기() {
        //given
        String 토큰 = null;

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(교대역.getId(),
            양재역.getId(), 토큰);

        //then
        최단경로_결과_확인(지하철_경로_조회_요청_response, Arrays.asList("교대역", "남부터미널역", "양재역"), 11);
        최단경로_금액_확인(지하철_경로_조회_요청_response, 1350);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * background
         * given : 사용자를 생성하고
         * given : 위와같은 지하철 도면일때
     * given : 생성한 사용자에 대한 토큰을 획득하고
     * when 교대~교대 까지의 거리 및 경로를 조회했을때
     * then 동일 역에대한 경로 조회가 불가함을 반환한다(status code 400)
    */
    @Test
    public void 동일_지하철_경로_조회_에러(){
        //given
        String 토큰 = null;

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(교대역.getId(),
            교대역.getId(), 토큰);

        //then
        최단경로_조회불가(지하철_경로_조회_요청_response);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * background
         * given : 사용자를 생성하고
         * given : 위와같은 지하철 도면일때
     * given : 생성한 사용자에 대한 토큰을 획득하고
     * given 노선에 포함되지않는 지하철 역을 추가하고
     * when 교대~해당 지하철역 까지의 거리 및 경로를 조회했을때
     * then 경로 조회가 불가함을 반환한다(status code 400)
     */
    @Test
    public void 없는_지하철_경로_조회_에러(){
        //given
        String 토큰 = null;
        StationResponse 동두천역= 지하철역_등록되어_있음("동두천역").as(StationResponse.class);

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(교대역.getId(),
            동두천역.getId(), 토큰);

        //then
        최단경로_조회불가(지하철_경로_조회_요청_response);
    }

    /**
     * background
         * given : 사용자를 생성하고
         * given : 위와같은 지하철 도면일때
     * given : 생성한 사용자에 대한 토큰을 획득하고
     * given 역들을 추가하고
     * given 해당 역들을 포함하는, 격리된 노선을 추가하였을때
     * when 신분당선의 역 ~ 격리된 노선의 역 간에  경로를 조회했을때
     * then 경로 조회가 불가함을 반환한다(status code 400)
     */
    @Test
    public void 없는_경로_조회_에러(){
        //given
        String 토큰 = null;
        StationResponse 송도달빛축제공원 = 지하철역_등록되어_있음("송도달빛축제공원").as(StationResponse.class);
        StationResponse 국제업무지구 = 지하철역_등록되어_있음("국제업무지구").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("인천1호선", "짙은푸른색", 송도달빛축제공원.getId(), 국제업무지구.getId(), 10));

        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_response = 지하철_경로_조회_요청(교대역.getId(),
            송도달빛축제공원.getId(), 토큰);

        //then
        최단경로_조회불가(지하철_경로_조회_요청_response);
    }


}
