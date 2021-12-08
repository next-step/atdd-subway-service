package nextstep.subway.member.application;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.utils.DatabaseCleanup;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
    }

    @DisplayName("중복 이메일 생성 요청")
    @Test
    void 중복_이메일_생성_요청() {
        // given
        MemberRequest 사용자_생성_요청 = new MemberRequest("email", "password", 10);
        MemberRequest 동일한_이메일_사용자_생성_요청 = new MemberRequest("email", "password2", 20);
        memberService.createMember(사용자_생성_요청);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> memberService.createMember(동일한_이메일_사용자_생성_요청);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("중복 이메일 변경 요청")
    @Test
    void 중복_이메일_변경_요청() {
        // given
        MemberRequest 사용자_생성_요청 = new MemberRequest("email", "password", 10);
        MemberRequest 다른_사용자_생성_요청 = new MemberRequest("email2", "password2", 20);
        MemberResponse 사용자_생성_응답 = memberService.createMember(사용자_생성_요청);
        memberService.createMember(다른_사용자_생성_요청);

        MemberRequest 사용자_업데이트_요청 = new MemberRequest("email2", "password", 10);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> memberService.updateMember(사용자_생성_응답.getId(), 사용자_업데이트_요청);

        // then
        assertThatThrownBy(throwingCallable)
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}