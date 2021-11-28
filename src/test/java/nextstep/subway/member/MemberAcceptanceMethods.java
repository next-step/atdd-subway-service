package nextstep.subway.member;

import static nextstep.subway.AcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

public class MemberAcceptanceMethods {
    public static final String LOCATION_HEADER_NAME = "Location";
    private static final String MEMBER_URL_PATH = "/members";
    private static final String MEMBER_ME_URL_PATH = "/me";

    private MemberAcceptanceMethods() {}

    public static ExtractableResponse<Response> 회원_생성을_요청(MemberRequest memberRequest) {
        return post(MEMBER_URL_PATH, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_등록되어_있음(MemberRequest memberRequest) {
        return 회원_생성을_요청(memberRequest);
    }

    public static ExtractableResponse<Response> 회원_정보_조회_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_NAME);
        return get(uri);
    }
    public static ExtractableResponse<Response> 회원_정보_수정_요청(ExtractableResponse<Response> response,
                                                                 MemberRequest memberRequest) {
        String uri = response.header(LOCATION_HEADER_NAME);
        return put(uri, memberRequest);
    }

    public static ExtractableResponse<Response> 회원_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header(LOCATION_HEADER_NAME);
        return delete(uri);
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return getByAuth(MEMBER_URL_PATH + MEMBER_ME_URL_PATH, tokenResponse);
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_수정_요청(TokenResponse token,
                                                                        MemberRequest memberUpdateRequest) {
        return putByAuth(MEMBER_URL_PATH + MEMBER_ME_URL_PATH, memberUpdateRequest, token);
    }

    public static ExtractableResponse<Response> 로그인한_회원_정보_삭제_요청(TokenResponse token) {
        return deleteByAuth(MEMBER_URL_PATH + MEMBER_ME_URL_PATH, token);
    }

    public static void 회원_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 회원_정보_조회됨(ExtractableResponse<Response> response, String email, int age) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(email);
        assertThat(memberResponse.getAge()).isEqualTo(age);
    }

    public static void 회원_정보_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 회원_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
