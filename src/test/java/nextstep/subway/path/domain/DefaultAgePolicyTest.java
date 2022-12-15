package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.MemberType;
import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultAgePolicyTest {

    @Test
    @DisplayName("로그인 한 10살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginChildrenFee() {
        int result = new DefaultAgePolicy(Age.from(10), MemberType.LOGIN, 1000).discount();

        assertThat(result).isEqualTo(325);
    }

    @Test
    @DisplayName("로그인 한 16살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginTeenagerFee() {
        int result = new DefaultAgePolicy(Age.from(16), MemberType.LOGIN, 1000).discount();

        assertThat(result).isEqualTo(520);
    }

    @Test
    @DisplayName("로그인 한 50살 유저의 할인 정책을 반영한 요금을 구한다")
    void loginAdultFee() {
        int result = new DefaultAgePolicy(Age.from(50), MemberType.LOGIN, 1000).discount();

        assertThat(result).isEqualTo(1000);
    }

    @Test
    @DisplayName("로그인 하지않은 10살 유저의 할인 정책을 반영한 요금을 구한다")
    void notLoginChildrenFee() {
        int result = new DefaultAgePolicy(Age.from(10), MemberType.NOT_LOGIN, 1000).discount();

        assertThat(result).isEqualTo(1000);
    }

    @Test
    @DisplayName("로그인 하지않은 16살 유저의 할인 정책을 반영한 요금을 구한다")
    void notLoginTeenagerFee() {
        int result = new DefaultAgePolicy(Age.from(16), MemberType.NOT_LOGIN, 1000).discount();

        assertThat(result).isEqualTo(1000);
    }

    @Test
    @DisplayName("로그인 하지않은 50살 유저의 할인 정책을 반영한 요금을 구한다")
    void notLoginAdultFee() {
        int result = new DefaultAgePolicy(Age.from(50), MemberType.NOT_LOGIN, 1000).discount();

        assertThat(result).isEqualTo(1000);
    }
}
