package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 1급 컬렉션 모델 단위 테스트")
class SectionsTest {

    private static final Station head = new Station("강남역");
    private static final Station middle = new Station("판교역");
    private static final Station tail = new Station("광교역");
    private static final Station newStation = new Station("새로운 역");

    private Section headToMiddle;
    private Section middleToTail;

    @BeforeEach
    void setup() {
        headToMiddle = new Section(head, middle, Distance.from(10));
        middleToTail = new Section(middle, tail, Distance.from(10));
    }

    @DisplayName("노선이 비어 있을 경우, 새로운 노선 추가후, 역 목록 조회시, 추가된 노선만의 역 목록을 반환할 수 있다")
    @Test
    void addSection_empty() {
        assertThat(SectionsBuilder.aSections()
            .addSection(headToMiddle)
            .build()
            .getStations()
        ).containsExactly(head, middle);
    }

    @DisplayName("노선 추가시, 노선의 상행역과 하행역이 이미 등록되어 있다면 추가할 수 없다")
    @Test
    void addSection_duplicatedSection() {
        assertThatThrownBy(() ->
            SectionsBuilder.aSections()
                .addSection(headToMiddle)
                .addSection(headToMiddle)
                .build()
                .getStations())
            .isInstanceOf(DuplicatedSectionException.class)
            .hasMessageContaining("이미 등록된 구간 입니다");
    }

    @DisplayName("노선 추가시, 노선의 상행역과 하행역 중 어느 하나도, 기존 노선에 포함되어있지 않으면 추가할 수 없다")
    @Test
    void addSection_notFoundConnectedStation() {
        assertThatThrownBy(() ->
            SectionsBuilder.aSections()
                .addSection(headToMiddle)
                .addSection(new Section(new Station("동탄역"), new Station("수원역"), Distance.from(10)))
                .build()
                .getStations())
            .isInstanceOf(ConnectedStationNotPresentException.class)
            .hasMessageContaining("연결 할 수 없는 구간 입니다");
    }

    @DisplayName("노선 추가시, 노선의 하행역이, 기존 노선에 포함되어 있다면 추가할 수 있다")
    @Test
    void addSection_upStationConnected() {
        assertThat(SectionsBuilder.aSections()
            .addSection(middleToTail)
            .addSection(headToMiddle)
            .build()
            .getStations()
        ).containsExactly(head, middle, tail);
    }

    @DisplayName("노선 추가시, 노선의 상행역이, 기존 노선에 포함되어 있다면 추가할 수 있다")
    @Test
    void addSection_downStationConnected() {
        assertThat(SectionsBuilder.aSections()
            .addSection(headToMiddle)
            .addSection(middleToTail)
            .build()
            .getStations()
        ).containsExactly(head, middle, tail);
    }

    @DisplayName("기존 거리보다 큰 구간을 추가할 수 없다")
    @Test
    void addSection_nextToHeadStation_longerDistance() {
        assertThatThrownBy(() ->
            SectionsBuilder.aSections()
                .addSection(headToMiddle)
                .addSection(new Section(head, newStation, headToMiddle.getDistance().plus(Distance.from(1))))
                .build()
                .getStations())
            .isInstanceOf(InvalidSectionDistanceException.class)
            .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("기존 거리와 같은 구간을 추가할 수 없다")
    @Test
    void addSection_nextToHeadStation_sameDistance() {
        assertThatThrownBy(() ->
            SectionsBuilder.aSections()
                .addSection(headToMiddle)
                .addSection(new Section(head, newStation, headToMiddle.getDistance()))
                .build()
                .getStations())
            .isInstanceOf(InvalidSectionDistanceException.class)
            .hasMessageContaining("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    }

    @DisplayName("상행역 종점 구간 사이에 구간을 추가할 수 있다")
    @Test
    void addSection_nextToHeadStation_shorterDistance() {
        assertThat(SectionsBuilder.aSections()
            .addSection(headToMiddle)
            .addSection(middleToTail)
            .addSection(new Section(head, newStation, Distance.from(5)))
            .build()
            .getStations()
        ).containsExactly(head, newStation, middle, tail);
    }

    @DisplayName("하행역 종점 구간 사이에 구간을 추가할 수 있다")
    @Test
    void addSection_beforeTailStation() {
        assertThat(SectionsBuilder.aSections()
            .addSection(headToMiddle)
            .addSection(middleToTail)
            .addSection(new Section(newStation, tail, Distance.from(5)))
            .build()
            .getStations()
        ).containsExactly(head, middle, newStation, tail);
    }

}
