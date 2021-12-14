package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteRepositoryTest {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    private Station 강남역;
    private Station 양재역;
    private Member member;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        stationRepository.save(강남역);
        stationRepository.save(양재역);

        member = new Member("email@email.com", "password", 20);
        memberRepository.save(member);
    }

    @DisplayName("멤버 id로 즐겨찾기 전체 조회")
    @Test
    void findAllByMemberId() {
        // given
        Favorite favorite1 = new Favorite(강남역, 양재역, member);
        Favorite favorite2 = new Favorite(양재역, 강남역, member);
        favoriteRepository.save(favorite1);
        favoriteRepository.save(favorite2);

        // when
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        // then
        assertThat(favorites).containsAll(Arrays.asList(favorite1, favorite2));
    }
}