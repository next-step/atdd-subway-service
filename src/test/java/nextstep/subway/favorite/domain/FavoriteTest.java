package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.Distance;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.favorite.domain
 * fileName : FavoriteTest
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
@DataJpaTest
public class FavoriteTest {
    private Station 강남역;
    private Station 노원역;
    private Member 회원;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        노원역 = stationRepository.save(new Station("노원역"));
        회원 = memberRepository.save(new Member("haedoang@gmail.com", "11", 33));
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다.")
    public void create() throws Exception {
        //given
        Favorite favorite = new Favorite(강남역, 노원역, Distance.of(100)).by(회원);

        //when
        favoriteRepository.save(favorite);

        //then
        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getSourceStation()).isEqualTo(강남역);
        assertThat(favorite.getTargetStation()).isEqualTo(노원역);
        assertThat(favorite.getDistance()).isEqualTo(Distance.of(100));
    }

    @Test
    @DisplayName("유효성 검증_ 출발지_목적지_동일")
    public void validate() throws Exception {
        //then
        assertThatThrownBy(() -> new Favorite(new Station("강남"), new Station("강남"), Distance.of(100)));
    }
}
