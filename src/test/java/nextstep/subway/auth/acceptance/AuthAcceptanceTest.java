package nextstep.subway.auth.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Base64;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthAcceptanceTest extends AcceptanceTest {
    @Autowired
    JwtTokenProvider jwtProvider;

    private MemberRequest 테스트계정;
    private MemberRequest 암호오입력_테스트계정;
    private MemberResponse 정상생성된_테스트계정정보;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테스트계정 = new MemberRequest("probitanima11@gmail.com", "11", 10);
        암호오입력_테스트계정 = new MemberRequest("probitanima11@gmail.com", "22", 10);

        MemberAcceptanceTest.회원_생성을_요청(테스트계정.getEmail(), 테스트계정.getPassword(), 테스트계정.getAge());

        정상생성된_테스트계정정보 = new MemberResponse(1L, "probitanima11@gmail.com", 10);
    }

    @DisplayName("통합 권한테스트")
    @Test
    void AcceptanceTest() {
        // when
        ExtractableResponse<Response> mismatchPasswordResponse = JWT_요청(암호오입력_테스트계정);
        // then
        인증_못받음(mismatchPasswordResponse);

        // when
        ExtractableResponse<Response> correctAccountResponse = JWT_요청(테스트계정);
        // then
        String accessJwt = JWT_받음(correctAccountResponse);

        // when
        ExtractableResponse<Response> memberInfoResponse = MemberAcceptanceTest.나의_회원정보_조회_요청(accessJwt);
        // then
        MemberAcceptanceTest.나의_회원정보_조회됨(memberInfoResponse, 정상생성된_테스트계정정보);

        // when
        String changedJwt = 인증받은JWT에_해킹이시도됨(accessJwt);
        ExtractableResponse<Response> responseForhackingJwtRequest = MemberAcceptanceTest.나의_회원정보_조회_요청(changedJwt);
        // then
        인증_못받음(responseForhackingJwtRequest);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        // when
        ExtractableResponse<Response> response = JWT_요청(테스트계정);
        // then
        String accessJwt = JWT_받음(response);

        // when
        ExtractableResponse<Response> memberInfoResponse = MemberAcceptanceTest.나의_회원정보_조회_요청(accessJwt);
        // then
        MemberAcceptanceTest.나의_회원정보_조회됨(memberInfoResponse, 정상생성된_테스트계정정보);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        // when
        ExtractableResponse<Response> response = JWT_요청(암호오입력_테스트계정);

        // then
        인증_못받음(response);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        ExtractableResponse<Response> response = JWT_요청(테스트계정);
        // then
        String accessJwt = JWT_받음(response);

        // given
        String chagnedAccessJwt = JWT_변조됨(accessJwt);
        // when
        ExtractableResponse<Response> memberInfoResponse = MemberAcceptanceTest.나의_회원정보_조회_요청(chagnedAccessJwt);
        // then
        인증_못받음(memberInfoResponse);
    }

    public static void 인증_못받음(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static String 인증받은JWT에_해킹이시도됨(String accessJwt) {
        return JWT_변조됨(accessJwt);
    }

    public static String JWT_변조됨(String accessJwt) {
        String changedBody = "{\"sub\": \"probitanima22@gmail.com\", \"iat\": 1638169161, \"exp\": 1638172761}";
        
        String header = accessJwt.split("\\.")[0];
        String changedPayload = new String(Base64.getEncoder().encode(changedBody.getBytes()));
        String signature = accessJwt.split("\\.")[2];

        return String.join(".", List.of(header, changedPayload, signature));
    }

    public static ExtractableResponse<Response> JWT_요청(MemberRequest requestMember) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new TokenRequest(requestMember.getEmail(), requestMember.getPassword()))
                .when().post("/login/token")
                .then().log().all()
                .extract();
    }

    public String JWT_받음(ExtractableResponse<Response> response) {
        String accessJwt = response.as(TokenResponse.class).getAccessToken();

        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> Assertions.assertThat(jwtProvider.validateToken(accessJwt)).isTrue(),
            () -> Assertions.assertThat(jwtProvider.getPayload(accessJwt)).isEqualTo(테스트계정.getEmail())
        );

        return accessJwt;
    }

}
