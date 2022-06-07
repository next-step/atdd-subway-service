package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.DomainFixtureFactory.createSection;
import static nextstep.subway.line.domain.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionsTest {
    @DisplayName("구간들 길이(합) 테스트")
    @Test
    void getDistance() {
        List<Section> sections = new ArrayList<>();
        sections.add(createSection(Line.createEmpty(), createStation(1L, "강남역"), createStation(2L, "양재역"),
                Distance.valueOf(10)));
        sections.add(createSection(Line.createEmpty(), createStation(2L, "양재역"), createStation(4L, "양재시민의숲역"),
                Distance.valueOf(5)));
        assertThat(Sections.valueOf(sections).distance()).isEqualTo(Distance.valueOf(15));
    }

    @DisplayName("구간들 사이에 새로운 구간 등록 테스트 (상행역 일치)")
    @Test
    void addSectionIfEqualUpStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(2L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "판교역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들 사이에 새로운 구간 등록 테스트 (하행역 일치)")
    @Test
    void addSectionIfEqualDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(2L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(3L, "판교역"), createStation(2L, "양재시민의숲역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 하행 종점에 구간 추가")
    @Test
    void addSectionAtDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "판교역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(3L, "판교역"), createStation(2L, "양재시민의숲역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 상행 종점에 구간 추가")
    @Test
    void addSectionAtUpStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(2L, "판교역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(2L, "판교역"),
                Distance.valueOf(5));
        sections.addSection(newSection);
        List<String> stationNames = sections.orderedStations().stream()
                .map(Station::name)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("양재역", "판교역", "양재시민의숲역");
    }

    @DisplayName("구간들에 있는 구간 사이에 구간 길이가 같거나 큰 구간 추가하면 예외 발생")
    @ParameterizedTest(name = "구간들에 있는 하행역이 일치하는 구간 사이에 {0}의 구간길이가 같거나 큰 구간 추가하면 예외 발생")
    @ValueSource(ints = {10, 12})
    void addSectionInsideByEqualOrLongerDistance(int input) {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(2L, "판교역"),
                Distance.valueOf(input));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("구간들에 구간 등록시 상행역과 하행역이 이미 구간들에 모두 등록되어 있는 경우 예외")
    @Test
    void addSectionContainsUpStationAndDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                Distance.valueOf(5));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("구간들에 구간 등록시 상행역과 하행역 둘 중 하나도 구간들에 포함되어있지 않으면 예외 발생")
    @Test
    void addSectionContainsNoneOfUpStationAndDownStation() {
        Sections sections = Sections.valueOf(Lists.newArrayList(
                createSection(Line.createEmpty(), createStation(1L, "양재역"), createStation(3L, "양재시민의숲역"),
                        Distance.valueOf(10))));
        Section newSection = createSection(Line.createEmpty(), createStation(2L, "판교"), createStation(4L, "미정"),
                Distance.valueOf(5));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.addSection(newSection))
                .withMessage("등록할 수 없는 구간 입니다.");
    }
}
