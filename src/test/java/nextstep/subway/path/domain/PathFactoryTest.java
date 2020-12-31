package nextstep.subway.path.domain;

import nextstep.subway.path.domain.exceptions.PathCreationException;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFactoryTest {
    @DisplayName("역ID 컬렉션, SafeSectionInfo 컬렉션을 인자로 받아서 객체를 생성할 수 있다.")
    @Test
    void createTest() {
        List<Long> stationIds = Arrays.asList(1L, 2L, 3L);
        List<SafeSectionInfo> safeSectionInfos = Arrays.asList(
                new SafeSectionInfo(1L, 2L, 3),
                new SafeSectionInfo(2L, 3L, 4)
        );

        WeightedMultigraph path = PathFactory.of(stationIds, safeSectionInfos);

        assertThat(path.vertexSet()).hasSize(3);
        assertThat(path.edgeSet()).hasSize(2);
    }

    @DisplayName("잘못된 인자로 객체를 생성할 수 없다.")
    @ParameterizedTest
    @MethodSource("createFailTestResource")
    void createFailTest(List<Long> stationIds, List<SafeSectionInfo> safeSectionInfos) {
        assertThatThrownBy(() -> PathFactory.of(stationIds, safeSectionInfos))
                .isInstanceOf(PathCreationException.class);
    }
    public static Stream<Arguments> createFailTestResource() {
        return Stream.of(
                Arguments.of(
                        null,
                        Arrays.asList(
                                new SafeSectionInfo(1L, 2L, 3),
                                new SafeSectionInfo(2L, 3L, 4)
                        )
                ),
                Arguments.of(
                        Arrays.asList(1L, 2L, 3L),
                        null
                ),
                Arguments.of(
                        Arrays.asList(1L, 2L, 3L),
                        new ArrayList<>()
                ),
                Arguments.of(
                        new ArrayList<>(),
                        Arrays.asList(
                                new SafeSectionInfo(1L, 2L, 3),
                                new SafeSectionInfo(2L, 3L, 4)
                        )
                )
        );
    }
}