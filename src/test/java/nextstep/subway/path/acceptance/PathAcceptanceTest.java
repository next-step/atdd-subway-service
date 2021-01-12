package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.domain.PathSelector;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int LINE_CHARGE_500 = 500;
    private static final int LINE_CHARGE_900 = 900;

    private static final String EMAIL = "example@example.com";
    private static final String PASSWORD = "example";
    private static final int AGE = 15;

    private static StationResponse 신도림;
    private static StationResponse 문래;
    private static StationResponse 영등포구청;
    private static StationResponse 당산;

    private static StationResponse 국회의사당;
    private static StationResponse 여의도;
    private static StationResponse 샛강;
    private static StationResponse 노량진;

    private static StationResponse 영등포;
    private static StationResponse 신길;
    private static StationResponse 대방;

    private static StationResponse 서울;

    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();
        PathSelector.clear();

        신도림 = StationAcceptanceTest.지하철역_등록되어_있음("신도림").as(StationResponse.class);
        문래 = StationAcceptanceTest.지하철역_등록되어_있음("문래").as(StationResponse.class);
        영등포구청 = StationAcceptanceTest.지하철역_등록되어_있음("영등포구청").as(StationResponse.class);
        당산 = StationAcceptanceTest.지하철역_등록되어_있음("당산").as(StationResponse.class);

        LineResponse 이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("2호선", "bg-green-600", 신도림.getId(), 문래.getId(), DEFAULT_DISTANCE))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 문래, 영등포구청, DEFAULT_DISTANCE);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 영등포구청, 당산, DEFAULT_DISTANCE);

        국회의사당 = StationAcceptanceTest.지하철역_등록되어_있음("국회의사당").as(StationResponse.class);
        여의도 = StationAcceptanceTest.지하철역_등록되어_있음("여의도").as(StationResponse.class);
        샛강 = StationAcceptanceTest.지하철역_등록되어_있음("샛강").as(StationResponse.class);
        노량진 = StationAcceptanceTest.지하철역_등록되어_있음("노량진").as(StationResponse.class);

        LineResponse 구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("9호선", "bg-brown-600", 당산.getId(), 국회의사당.getId(), DEFAULT_DISTANCE, LINE_CHARGE_900))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 국회의사당, 여의도, DEFAULT_DISTANCE);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 여의도, 샛강, DEFAULT_DISTANCE);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(구호선, 샛강, 노량진, DEFAULT_DISTANCE);

        영등포 = StationAcceptanceTest.지하철역_등록되어_있음("영등포").as(StationResponse.class);
        신길 = StationAcceptanceTest.지하철역_등록되어_있음("신길").as(StationResponse.class);
        대방 = StationAcceptanceTest.지하철역_등록되어_있음("대방").as(StationResponse.class);

        LineResponse 일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("1호선", "bg-blue-600", 신도림.getId(), 영등포.getId(), DEFAULT_DISTANCE, LINE_CHARGE_500))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 영등포, 신길, DEFAULT_DISTANCE);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 신길, 대방, DEFAULT_DISTANCE);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 대방, 노량진, DEFAULT_DISTANCE);

        서울 = StationAcceptanceTest.지하철역_등록되어_있음("서울").as(StationResponse.class);


        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("최단거리 역 목록 조회")
    @ParameterizedTest
    @MethodSource
    void selectPath(ArgumentSupplier<StationResponse> sourceSupplier, ArgumentSupplier<StationResponse> targetSupplier, ArgumentSupplier<StationResponse[]> expectedSupplier, int expectedPayment) {
        StationResponse source = sourceSupplier.get();
        StationResponse target = targetSupplier.get();
        StationResponse[] expected = expectedSupplier.get();

        ExtractableResponse<Response> response = 최단거리_역_목록_조회_요청(source.getId(), target.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);

        assertThat(pathResponse.getStations()).containsExactly(expected);
        assertThat(pathResponse.getDistance()).isEqualTo((pathResponse.getStations().size() - 1) * DEFAULT_DISTANCE);
        assertThat(pathResponse.getPayment()).isEqualTo(expectedPayment);
    }

    private static Stream<Arguments> selectPath() {
        return Stream.of(
                Arguments.of(supply(()->신도림), supply(() ->당산), supply(() -> new StationResponse[]{신도림, 문래, 영등포구청, 당산}), (int)((1_250 + 400)*0.8)),
                Arguments.of(supply(()->영등포구청), supply(() ->노량진), supply(() -> new StationResponse[]{영등포구청, 당산, 국회의사당, 여의도, 샛강, 노량진}), (int)((1_250 + LINE_CHARGE_900 + 800)*0.8)),
                Arguments.of(supply(()->문래), supply(() ->노량진), supply(() -> new StationResponse[]{문래, 신도림, 영등포, 신길, 대방, 노량진}), (int)((1_250 + LINE_CHARGE_500 + 800)*0.8))
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void selectSameStation() {
        ExtractableResponse<Response> response = 최단거리_역_목록_조회_요청(신도림.getId(), 신도림.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void selectNotRelatedStation() {
        ExtractableResponse<Response> response = 최단거리_역_목록_조회_요청(신도림.getId(), 서울.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("출발역이나 도착역이 존재하지 않을 경우")
    @Test
    void selectNotExistsStation() {
        ExtractableResponse<Response> response = 최단거리_역_목록_조회_요청(신도림.getId(), 99L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        ExtractableResponse<Response> response2 = 최단거리_역_목록_조회_요청(99L, 신도림.getId());
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 최단거리_역_목록_조회_요청(Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("source", sourceStationId)
                .param("target", targetStationId)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static <T> ArgumentSupplier<T> supply(Supplier<T> supplier) {
        return new ArgumentSupplier<>(supplier);
    }

    private static class ArgumentSupplier<T> {
        private Supplier<T> supplier;

        public ArgumentSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public T get() {
            return supplier.get();
        }
    }
}
