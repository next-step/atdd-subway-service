package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void saveTest(){
        // favorite 가 입력되면 db에 저장된다.
        // 그리고 저장된 favorite 을 반환한다.
        Member member = memberRepository.save(new Member("byunsw4@naver.com", "password", 30));

        // when
        Favorite favorite = favoriteRepository.save(new Favorite(new Station("강남역"), new Station("잠실역"), member));

        // then
        assertThat(favorite).isNotNull();
        assertThat(favorite.getId()).isNotNull();
    }

}