package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FavoriteTest {
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager entityManager;

    @DisplayName("즐겨찾기를 저장한다.")
    @Test
    void save() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Member member = memberRepository.save(new Member("email@email.com", "password", 12));
        // when
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, firstStation, secondStation));
        // then
        assertThat(favorite.getId()).isNotNull();
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void findById() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Member member = memberRepository.save(new Member("email@email.com", "password", 12));
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, firstStation, secondStation));
        entityManager.clear();
        // when
        final Optional<Favorite> optionalFavorite = favoriteRepository.findById(favorite.getId());
        // then
        assertTrue(optionalFavorite.isPresent());
        assertThat(optionalFavorite.get().getId()).isEqualTo(favorite.getId());
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findAllByMemberId() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Station thirdStation = stationRepository.save(Station.of("3번"));
        final Member member = memberRepository.save(new Member("email@email.com", "password", 12));
        final Favorite favorite1 = favoriteRepository.saveAndFlush(Favorite.of(member, firstStation, secondStation));
        final Favorite favorite2 = favoriteRepository.saveAndFlush(Favorite.of(member, secondStation, thirdStation));
        final List<Favorite> expectedList = Arrays.asList(favorite1, favorite2);
        entityManager.clear();
        // when
        final List<Favorite> favoriteList = favoriteRepository.findAllByMemberId(member.getId());
        // then
        assertThat(favoriteList).containsExactlyInAnyOrderElementsOf(expectedList);
    }

    @DisplayName("아이디와 유저로 부터 즐겨찾기 존재를 확인한다.")
    @Test
    void existsByIdAndMember() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Member member = memberRepository.save(new Member("email@email.com", "password", 12));
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, firstStation, secondStation));
        // when
        final boolean exists = favoriteRepository.existsByIdAndMember(favorite.getId(), member);
        // then
        assertThat(exists).isTrue();
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void delete() {
        // given
        final Station firstStation = stationRepository.save(Station.of("1번"));
        final Station secondStation = stationRepository.save(Station.of("2번"));
        final Member member = memberRepository.save(new Member("email@email.com", "password", 12));
        final Favorite favorite = favoriteRepository.save(Favorite.of(member, firstStation, secondStation));
        // when
        favoriteRepository.deleteById(favorite.getId());
        entityManager.flush();
        entityManager.clear();
        // then
        final Optional<Favorite> optionalFavorite = favoriteRepository.findById(favorite.getId());
        assertFalse(optionalFavorite.isPresent());
    }
}