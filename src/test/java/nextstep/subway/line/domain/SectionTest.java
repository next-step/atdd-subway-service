package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import nextstep.subway.line.domain.exception.CannotAddSectionException;
import nextstep.subway.line.domain.exception.CannotDeleteSectionException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class SectionTest {
    Line 이호선;
    Station 강변역 = new Station("강변역");
    Station 당산역 = new Station("당산역");
    Station 신림역 = new Station("신림역");
    Station 역삼역 = new Station("역삼역");
    Sections 이호선_구간;

    @Nested
    class 구간추가{
        @BeforeEach
        void setUp(){
            이호선 = new Line("이호선","초록색",강변역,당산역,100);
            이호선_구간 = 이호선.getSections();
        }

        @Test
        void 구간추가성공_구간_시작역_일치(){
            이호선_구간.addSection(new Section(이호선,강변역,신림역,10));
            List<Station> stationList = 이호선_구간.getStations();
            assertThat(stationList).hasSize(3).containsExactly(강변역,신림역,당산역);
        }

        @Test
        void 구간추가성공_구간_종착역_일치(){
            이호선_구간.addSection(new Section(이호선,신림역,당산역,10));
            List<Station> stationList = 이호선_구간.getStations();
            assertThat(stationList).hasSize(3).containsExactly(강변역,신림역,당산역);
        }

        @Test
        void 구간추가실패_이미_존재하는_구간(){
            Section newSection = new Section(이호선, 강변역, 당산역, 10);

            assertThatThrownBy(()-> 이호선_구간.addSection(newSection)).isInstanceOf(CannotAddSectionException.class);
        }

        @Test
        void 구간추가실패_거리_초과(){
            Section newSection = new Section(이호선,강변역,신림역,100);

            assertThatThrownBy(()->이호선_구간.addSection(newSection)).isInstanceOf(CannotAddSectionException.class);
        }

        @Test
        void 구간추가실패_추가위치_알수없음(){
            Section newSection = new Section(이호선,역삼역,신림역,10);

            assertThatThrownBy(()->이호선_구간.addSection(newSection)).isInstanceOf(CannotAddSectionException.class);
        }
    }

    @Nested
    class 구간삭제{
        @BeforeEach
        void setUp(){
            이호선 = new Line("이호선","초록색",강변역,당산역,100);
            이호선.addSection(new Section(이호선,강변역,신림역,10));
            이호선_구간 = 이호선.getSections();
        }

        @Test
        void 구간삭제성공_노선_시작역(){
            이호선_구간.deleteStation(이호선,강변역);
            List<Station> stationList = 이호선_구간.getStations();
            assertThat(stationList).hasSize(2).containsExactly(신림역,당산역);
        }
        @Test
        void 구간삭제성공_노선_중간역(){
            이호선_구간.deleteStation(이호선,신림역);
            List<Station> stationList = 이호선_구간.getStations();
            assertThat(stationList).hasSize(2).containsExactly(강변역,당산역);
        }
        @Test
        void 구간삭제성공_노선_마지막역(){
            이호선_구간.deleteStation(이호선,당산역);
            List<Station> stationList = 이호선_구간.getStations();
            assertThat(stationList).hasSize(2).containsExactly(강변역,신림역);
        }

        void 구간삭제실패_구간제거시_노선유지불가(){
            이호선_구간.deleteStation(이호선,강변역);
            assertThatThrownBy(()->이호선_구간.deleteStation(이호선,당산역)).isInstanceOf(CannotDeleteSectionException.class);
        }
    }




}
