package nextstep.subway.favorite.domain;

import static java.util.Arrays.asList;
import static nextstep.subway.member.fixture.MemberFixture.사용자1;
import static nextstep.subway.member.fixture.MemberFixture.사용자2;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.서울역;
import static nextstep.subway.station.domain.StationFixture.신촌역;

import java.util.List;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StationRepository stationRepository;

    @Test
    void 회원에_해당한_즐겨찾기_조회() {
        // given
        Member savedMember1 = memberRepository.save(사용자1());
        Member savedMember2 = memberRepository.save(사용자2());
        Station 강남역 = 강남역();
        Station 신촌역 = 신촌역();
        Station 서울역 = 서울역();

        stationRepository.saveAll(asList(강남역, 신촌역, 서울역));
        favoriteRepository.save(Favorite.of(savedMember1.getId(), 강남역, 신촌역));
        favoriteRepository.save(Favorite.of(savedMember1.getId(), 강남역, 서울역));
        favoriteRepository.save(Favorite.of(savedMember2.getId(), 강남역, 서울역));

        // when
        List<Favorite> favorites = favoriteRepository.findDistinctByMemberId(
            new MemberId(savedMember1.getId()));

        // then
        Assertions.assertThat(favorites.size()).isEqualTo(2);
    }
}