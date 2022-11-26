package nextstep.subway.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password157#";
    private static final String NEW_EMAIL = "newemail@email.com";
    private static final String NEW_PASSWORD = "newpassword157#";
    private static final int AGE = 20;
    private static final int NEW_AGE = 21;

    /**
     * When 회원을 생성하면
     * Then 회원 정보 조회 시 회원 정보를 찾을 수 있다.
     */
    @DisplayName("회원을 생성한다.")
    @Test
    void createMember() {
        // when
        ExtractableResponse<Response> createResponse = MemberAcceptance.create_member(EMAIL, PASSWORD, AGE);

        // then
        MemberResponse memberResponse = MemberAcceptance.member_was_queried(createResponse).as(MemberResponse.class);
        assertAll(
                () -> assertThat(memberResponse.getId()).isNotNull(),
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getAge()).isEqualTo(AGE)
        );
    }

    /**
     * Given 회원이 생성되어 있고
     * When 회원을 수정하면
     * Then 회원 정보를 수정할 수 있다.
     */
    @DisplayName("회원 정보를 수정한다.")
    @Test
    void updateMember() {
        // given
        ExtractableResponse<Response> createResponse = MemberAcceptance.create_member(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> updateResponse = MemberAcceptance.update_member(createResponse,
                NEW_EMAIL, NEW_PASSWORD, NEW_AGE);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 회원이 생성되어 있고
     * When 회원을 삭제하면
     * Then 회원을 삭제할 수 있다.
     */
    @DisplayName("회원을 삭제한다.")
    @Test
    void deleteMember() {
        // given
        ExtractableResponse<Response> createResponse = MemberAcceptance.create_member(EMAIL, PASSWORD, AGE);

        // when
        ExtractableResponse<Response> deleteResponse = MemberAcceptance.delete_member(createResponse);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {

    }
}
