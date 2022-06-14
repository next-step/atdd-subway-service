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
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired FavoriteRepository favoriteRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired StationRepository stationRepository;

    @Test
    void findAllByMemberId() {
        // given
        Member member1 = memberRepository.save(new Member("email@email.com", "password", 20));
        Member member2 = memberRepository.save(new Member("email@email.com", "password", 20));
        Station 강남역 = stationRepository.save(new Station("강남역"));
        Station 역삼역 = stationRepository.save(new Station("역삼역"));
        favoriteRepository.save(new Favorite(member1, 강남역, 역삼역));
        favoriteRepository.save(new Favorite(member2, 강남역, 역삼역));

        // when
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member1.getId());

        // then
        assertThat(favorites).hasSize(1);
    }
}
