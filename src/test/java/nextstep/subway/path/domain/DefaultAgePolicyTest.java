package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.MemberType;
import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultAgePolicyTest {

    @Test
    @DisplayName("10살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginChildrenFee() {
        int result = new DefaultAgePolicy().discount(1000, Age.from(10));

        assertThat(result).isEqualTo(325);
    }

    @Test
    @DisplayName("16살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginTeenagerFee() {
        int result = new DefaultAgePolicy().discount(1000, Age.from(16));

        assertThat(result).isEqualTo(520);
    }

    @Test
    @DisplayName("로그인 한 50살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginAdultFee() {
        int result = new DefaultAgePolicy().discount(1000, Age.from(50));

        assertThat(result).isEqualTo(1000);
    }
}