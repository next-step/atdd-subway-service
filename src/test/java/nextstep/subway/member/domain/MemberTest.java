package nextstep.subway.member.domain;

import static nextstep.subway.member.domain.MemberTestFixture.createMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("회원 관련 도메인 테스트")
public class MemberTest {

    @DisplayName("회원 생성 시, 나이가 0보다 작거나 같으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, 0})
    void createMemberThrowErrorWhenAgeLessThenOrEqualToZero(int age) {
        // when & then
        assertThatThrownBy(() -> createMember("email@email.com", "password", age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.나이는_0보다_작거나_같을_수_없음.getErrorMessage());
    }
}
