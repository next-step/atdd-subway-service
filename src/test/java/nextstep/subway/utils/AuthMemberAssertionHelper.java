package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.member.domain.Member;
import org.springframework.http.HttpStatus;

public class AuthMemberAssertionHelper {
    public static void 가져온_내정보_확인하기(Member 내정보, ExtractableResponse<Response> 조회결과){
        assertAll(
            () -> assertThat(조회결과.jsonPath().get("email").toString()).isEqualTo(
                내정보.getEmail()),
            () -> assertThat(조회결과.jsonPath().get("age").toString()).isEqualTo(
                String.valueOf(내정보.getAge()))
        );
    }

    public static void 인증실패(ExtractableResponse<Response> 인증결과){
        assertThat(인증결과.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
