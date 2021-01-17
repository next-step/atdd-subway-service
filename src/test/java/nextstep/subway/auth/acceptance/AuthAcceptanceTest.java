package nextstep.subway.auth.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("로그인 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @Autowired
    private MemberService memberService;

    private String 이메일주소;
    private String 패스워드;
    private int 나이;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        이메일주소 = "email@email.com";
        패스워드 = "password";
        나이 = 32;
    }

    @DisplayName("로그인을 시도한다")
    @Test
    void login() {
        회원_등록됨(이메일주소, 패스워드, 나이);

        ExtractableResponse<Response> loginResponse = 로그인_요청(이메일주소, 패스워드);

        로그인_됨(loginResponse);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
    }

    private void 유효하지_않은_토큰으로_멤버가_조회되지_않음(Long memberId) {
        MemberResponse member = memberService.findMember(memberId);
        System.out.println(member.getId());
        System.out.println(member.getEmail());
    }

    private void 유효한_토큰으로_멤버가_조회됨(Long memberId) {
        MemberResponse member = memberService.findMember(memberId);
        System.out.println(member.getId());
        System.out.println(member.getEmail());

    }


    public static void 회원_등록됨(String email, String password, int age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);

        RestAssured.given().log().all()
                .body(memberRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/members")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);

        return RestAssured.given().log().all()
                .body(tokenRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login/token")
                .then().log().all().extract();
    }


    public static void 로그인_됨(ExtractableResponse<Response> response) {
        TokenResponse tokenResponse = response.as(TokenResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(tokenResponse.getAccessToken()).isNotEmpty();

    }




}
