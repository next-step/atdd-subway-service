package nextstep.subway.line.domain;

import static nextstep.subway.station.domain.StationFixtures.공덕역;
import static nextstep.subway.station.domain.StationFixtures.광화문역;
import static nextstep.subway.station.domain.StationFixtures.서대문역;
import static nextstep.subway.station.domain.StationFixtures.애오개역;
import static nextstep.subway.station.domain.StationFixtures.을지로4가역;
import static nextstep.subway.station.domain.StationFixtures.종로3가역;
import static nextstep.subway.station.domain.StationFixtures.충정로역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 테스트")
public class SectionsTest {

    //노선
    private static final Line 오호선 = new Line("5호선", "purple");

    //구간
    private static final Section firstSection = new Section(오호선, 공덕역, 애오개역, 10);
    private static final Section secondSection = new Section(오호선, 애오개역, 충정로역, 10);
    private static final Section thirdSection = new Section(오호선, 충정로역, 서대문역, 10);
    private static final Section forthSection = new Section(오호선, 서대문역, 광화문역, 10);
    private static final Section fifthSection = new Section(오호선, 광화문역, 종로3가역, 10);
    private static final Section sixthSection = new Section(오호선, 종로3가역, 을지로4가역, 10);

    @DisplayName("구간추가시 연결이 불가능한 구간일 경우 예외 발생")
    @Test
    void addFail() {
        // given
        Sections sections = new Sections();
        sections.add(firstSection);

        // when & then
        assertThatThrownBy(() -> sections.add(thirdSection))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자를 이용하여 구간을 생성시 지하철역 정렬 확인")
    @Test
    void findStationsInOrder() {
        // given
        List<Section> sectionList = new ArrayList<>(Arrays.asList(firstSection, secondSection, thirdSection, forthSection, fifthSection, sixthSection));
        Collections.shuffle(sectionList); //섞기

        // when
        Sections sections = new Sections(sectionList);
        List<Station> stations = sections.findStationsInOrder();

        // then
        assertThat(stations.get(0)).isEqualTo(firstSection.getUpStation());
        assertThat(stations.get(1)).isEqualTo(secondSection.getUpStation());
        assertThat(stations.get(2)).isEqualTo(thirdSection.getUpStation());
    }

    @DisplayName("구간추가 메서드를 이용했을때 지하철역 정렬 확인")
    @Test
    void add() {
        // given
        Sections sections = new Sections();
        sections.add(forthSection);
        sections.add(thirdSection);
        sections.add(fifthSection);
        sections.add(secondSection);
        sections.add(sixthSection);
        sections.add(firstSection);

        // when
        List<Station> stations = sections.findStationsInOrder();

        // then
        assertThat(stations.get(0)).isEqualTo(firstSection.getUpStation());
        assertThat(stations.get(1)).isEqualTo(secondSection.getUpStation());
        assertThat(stations.get(2)).isEqualTo(thirdSection.getUpStation());
        assertThat(stations.get(3)).isEqualTo(forthSection.getUpStation());
        assertThat(stations.get(4)).isEqualTo(fifthSection.getUpStation());
        assertThat(stations.get(5)).isEqualTo(sixthSection.getUpStation());
        assertThat(stations.get(6)).isEqualTo(sixthSection.getDownStation());
    }
}
