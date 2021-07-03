package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.PrivateRestAssuredTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static nextstep.subway.PageController.URIMapping.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAfterLoginAcceptanceTest extends AcceptanceTest {
    private PrivateRestAssuredTemplate restAssuredTemplate;

    private Long 강남역ID;
    private Long 정자역ID;

    @BeforeEach
    public void setup() {
        지하철_노선_등록됨();
        회원_로그인함();
    }

    @Test
    @DisplayName("로그인 상태에서 지하철 최단경로를 조회한다.")
    public void requestPath() {
        // given
        Map<String, Long> pathRequestParamMap = PathAcceptanceTest.getPathRequestParamMap(강남역ID, 정자역ID);

        // when
        ExtractableResponse<Response> response = requestPath(pathRequestParamMap);

        // then
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(response.as(PathResponse.class).getStationList()).isNotNull(),
            () -> assertThat(response.as(PathResponse.class).getDistance()).isNotNull()
        );
    }

    private void 지하철_노선_등록됨() {
        //지하철역 등록되어 있음
        강남역ID = StationAcceptanceTest.requestCreateStation("강남역").as(StationResponse.class).getId();
        정자역ID = StationAcceptanceTest.requestCreateStation("정자역").as(StationResponse.class).getId();
        LineRequest lineRequest = LineRequest.builder()
                .name("신분당선").upStationId(강남역ID).downStationId(정자역ID).distance(6)
                .build();

        //지하철 노선 등록되어 있음
        LineAcceptanceTest.requestCreatedLine(lineRequest);
    }

    private void 회원_로그인함() {
        //회원 등록되어 있음
        MemberAcceptanceTest.회원_생성됨(MemberAcceptanceTest.requestCreateMember(EMAIL, PASSWORD, AGE));

        //로그인 되어있음
        String token = AuthAcceptanceTest.login(new TokenRequest(EMAIL, PASSWORD))
                .as(TokenResponse.class)
                .getAccessToken();

        restAssuredTemplate = new PrivateRestAssuredTemplate(token, PATH);
    }

    /**
     * @see nextstep.subway.path.ui.PathController#findLineById(LoginMember loginMember, PathRequest request)
     */
    private ExtractableResponse<Response> requestPath(Map<String, Long> query) {
        return restAssuredTemplate.get(query);
    }
}
