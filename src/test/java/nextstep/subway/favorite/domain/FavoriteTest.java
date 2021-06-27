package nextstep.subway.favorite.domain;

import static nextstep.subway.station.domain.StationTest.강남역;
import static nextstep.subway.station.domain.StationTest.선릉역;
import static nextstep.subway.station.domain.StationTest.양재역;
import static nextstep.subway.station.domain.StationTest.잠실역;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.favorite.NotFoundAnyThingException;
import nextstep.subway.exception.favorite.SameStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;

public class FavoriteTest {

    private Line 신분당선;
    private Member 멤버;

    @BeforeEach
    void setUP() {
        //강남 - 양재 - 선릉 - 잠실
        신분당선 = new Line(1L, "신분당선", "그린", 강남역, 양재역, 10);
        신분당선.addSection(new Section(신분당선, 양재역, 선릉역, 10));
        신분당선.addSection(new Section(신분당선, 선릉역, 잠실역, 10));

        //member
        멤버 = new Member(1L, "7271kim@naver.com", "pw", 20);
    }

    @Test
    @DisplayName("line, source, target이 없을 시 에러를 뱉어낸다.")
    void 빈값으로_생성한다() {
        assertThrows(NotFoundAnyThingException.class, () -> {
            Favorite.of(null, 강남역, 양재역);
        });

        assertThrows(NotFoundAnyThingException.class, () -> {
            Favorite.of(멤버.getId(), null, 양재역);
        });

        assertThrows(NotFoundAnyThingException.class, () -> {
            Favorite.of(멤버.getId(), 강남역, null);
        });
    }

    @Test
    @DisplayName("동일한 역으로는 등록 불가능하다.")
    void 동일_역으로_생성한다() {
        assertThrows(SameStationException.class, () -> {
            Favorite.of(멤버.getId(), 강남역, 강남역);
        });
    }

}
