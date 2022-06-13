package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteRepositoryTest {
    private Station 강남역;
    private Station 잠실역;
    private Member 회원;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    void save() {
        // given, when
        Favorite actual = favoriteRepository.save(createTestEntity());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void findByMember() {
        // given
        Favorite actual = favoriteRepository.save(createTestEntity());

        // when
        List<Favorite> list = favoriteRepository.findAllByMember(actual.getMember());

        // then
        assertThat(list).hasSize(1);
    }

    @Test
    void findAll() {
        // given
        Favorite saved = favoriteRepository.save(createTestEntity());

        // when
        List<Favorite> list = favoriteRepository.findAll();

        // then
        assertThat(list).hasSize(1);
    }

    @Test
    void delete() {
        // given
        Favorite saved = favoriteRepository.save(createTestEntity());

        // when
        favoriteRepository.delete(saved);

        // then
        List<Favorite> list = favoriteRepository.findAll();
        assertThat(list).hasSize(0);
    }

    private Favorite createTestEntity() {
        강남역 = stationRepository.save(new Station("강남역"));
        잠실역 = stationRepository.save(new Station("잠실역"));
        회원 = memberRepository.save(new Member("handh0413@gmail.com", "password", 99));
        return new Favorite(회원, 강남역, 잠실역);
    }
}
