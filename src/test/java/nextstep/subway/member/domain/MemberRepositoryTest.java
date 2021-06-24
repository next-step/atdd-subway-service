package nextstep.subway.member.domain;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static nextstep.subway.member.domain.MemberTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("회원 삭제 - 회원 삭제시 즐겨찾기 삭제")
    @Test
    public void 회원_삭제_즐겨찾기삭제확인() throws Exception {
        //given
        Station station1 = new Station("station1");
        Station station2 = new Station("station2");
        stationRepository.save(station1);
        stationRepository.save(station2);

        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member saveMember = memberRepository.save(member);
        Favorite favorite1 = new Favorite(member, station1, station2);
        Favorite favorite2 = new Favorite(member, station2, station1);
        flushAndClear();

        //when
        memberRepository.delete(member);
        flushAndClear();

        //then
        assertThat(favoriteRepository.findById(favorite1.getId())).isEqualTo(Optional.empty());
        assertThat(favoriteRepository.findById(favorite2.getId())).isEqualTo(Optional.empty());
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
