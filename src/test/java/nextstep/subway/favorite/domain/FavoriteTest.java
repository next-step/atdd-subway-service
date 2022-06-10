package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 관련 단위테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteTest {

    private Station 강남 = new Station("강남");
    private Station 교대 = new Station("교대");
    private Station 신도림 = new Station("신도림");
    private Station 잠실 = new Station("잠실");
    private Member 사용자 = new Member("test@unit.com","1234",15);

    @Mock
    private FavoriteRepository favoriteRepository;

    @DisplayName("즐겨찾기을 생성한다.")
    @Test
    void create(){
        //given
        Favorite favorite = new Favorite(1L, 강남, 교대, 사용자);

        //when
        lenient().when(favoriteRepository.save(favorite)).thenReturn(favorite);

        //then
        assertAll(
                ()-> assertThat(favorite.getId()).isNotNull(),
                ()-> assertThat(favorite.getSource()).isEqualTo(강남),
                ()-> assertThat(favorite.getTarget()).isEqualTo(교대),
                ()-> assertThat(favorite.getMember()).isEqualTo(사용자)
        );
    }

    @DisplayName("사용자 즐겨찾기 목록을 조회한다.")
    @Test
    void findAllByMember(){
        //given
        when(favoriteRepository.findAllByMember(사용자))
                .thenReturn(Arrays.asList(new Favorite(강남, 교대, 사용자), new Favorite(신도림, 잠실, 사용자)));

        //when
        List<Favorite> favorites = favoriteRepository.findAllByMember(사용자);

        //then
        assertAll(
                ()-> assertThat(favorites.get(0).getSource()).isEqualTo(강남),
                ()-> assertThat(favorites.get(0).getTarget()).isEqualTo(교대),
                ()-> assertThat(favorites.get(0).getMember()).isEqualTo(사용자),

                ()-> assertThat(favorites.get(1).getSource()).isEqualTo(신도림),
                ()-> assertThat(favorites.get(1).getTarget()).isEqualTo(잠실),
                ()-> assertThat(favorites.get(1).getMember()).isEqualTo(사용자)
        );
    }
}
