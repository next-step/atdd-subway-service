package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FavoriteRepository 테스트")
@DataJpaTest
class FavoriteRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    private Member member;
    private Station 강남역;
    private Station 잠실역;
    private Station 판교역;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("testuser@test.com", "password157#", 20));
        강남역 = stationRepository.save(new Station("강남역"));
        잠실역 = stationRepository.save(new Station("잠실역"));
        판교역 = stationRepository.save(new Station("판교역"));
    }

    @Test
    @DisplayName("사용자 ID로 즐겨찾기 목록을 조회")
    void findAllByMemberId() {
        favoriteRepository.save(new Favorite(member, 강남역, 잠실역));
        favoriteRepository.save(new Favorite(member, 강남역, 판교역));

        assertThat(favoriteRepository.findAllByMemberId(member.getId())).hasSize(2);
    }
}
