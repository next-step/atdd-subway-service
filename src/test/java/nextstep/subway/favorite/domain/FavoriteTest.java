package nextstep.subway.favorite.domain;

import static nextstep.subway.DomainFixtureFactory.createLoginMember;
import static nextstep.subway.DomainFixtureFactory.createMember;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.NotOwnerException;
import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    @DisplayName("회원이 Null 일 경우 예외 테스트")
    @Test
    void createFavoriteByNullMember() {
        assertThatThrownBy(
                () -> Favorite.builder(null, createStation(1L, "지하철역"), createStation(2L, "새로운지하철역"))
                        .build()).isInstanceOf(NotFoundException.class).hasMessage("회원 정보가 없습니다.");
    }

    @DisplayName("출발역이 Null 일 경우 예외 테스트")
    @Test
    void createFavoriteByNullSource() {
        assertThatThrownBy(
                () -> Favorite.builder(createMember(1L, "email@email.com", "password", Age.valueOf(20)), null,
                                createStation(1L, "새로운지하철역"))
                        .build()).isInstanceOf(NotFoundException.class).hasMessage("출발역 정보가 없습니다.");
    }

    @DisplayName("도착역이 Null 일 경우 예외 테스트")
    @Test
    void createFavoriteByNullTarget() {
        assertThatThrownBy(
                () -> Favorite.builder(createMember(1L, "email@email.com", "password", Age.valueOf(20)),
                                createStation(1L, "지하철역"),
                                null)
                        .build()).isInstanceOf(NotFoundException.class).hasMessage("도착역 정보가 없습니다.");
    }

    @DisplayName("로그인한 Id와 즐겨찾기를 등록한 회원 Id가 다른 경우")
    @Test
    void validateOwner() {
        LoginMember loginMember = createLoginMember(2L, "mail@email.com", Age.valueOf(20));
        Favorite favorite = Favorite.builder(createMember(1L, "email@email.com", "password", Age.valueOf(20)),
                        createStation(1L, "지하철역"), createStation(2L, "새로운지하철역"))
                .build();
        assertThatThrownBy(
                () -> favorite.validateOwner(loginMember))
                .isInstanceOf(NotOwnerException.class)
                .hasMessage("로그인한 회원의 즐겨찾기가 아닙니다.");
    }
}
