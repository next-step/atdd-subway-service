package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberShipTest {

    @Test
    @DisplayName("6세 미만 65세 이상은 무료 요금")
    void findMemberShip() {
        assertAll(
                () -> assertThat(MemberShip.findMemberShip(5)).isEqualTo(MemberShip.PREFERENTIAL),
                () -> assertThat(MemberShip.findMemberShip(65)).isEqualTo(MemberShip.PREFERENTIAL)
        );
    }

    @Test
    @DisplayName("6세 이상 13세 미만은 어린이 요금")
    void findMemberShipForChild() {
        assertAll(
                () -> assertThat(MemberShip.findMemberShip(6)).isEqualTo(MemberShip.CHILD),
                () -> assertThat(MemberShip.findMemberShip(12)).isEqualTo(MemberShip.CHILD)
        );
    }

    @Test
    @DisplayName("13세 이상 19세 미만은 청소년 요금")
    void findMemberShipForTeenager() {
        assertAll(
                () -> assertThat(MemberShip.findMemberShip(13)).isEqualTo(MemberShip.TEENAGER),
                () -> assertThat(MemberShip.findMemberShip(18)).isEqualTo(MemberShip.TEENAGER)
        );
    }

    @Test
    @DisplayName("19세 이상 65세 미만은 성인 요금")
    void findMemberShipForGeneral() {
        assertAll(
                () -> assertThat(MemberShip.findMemberShip(19)).isEqualTo(MemberShip.GENERAL),
                () -> assertThat(MemberShip.findMemberShip(64)).isEqualTo(MemberShip.GENERAL)
        );
    }
}
