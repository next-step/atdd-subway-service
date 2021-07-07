package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;

/**
 * Lines 클래스 기능 검증 테스트
 */
@DisplayName("Lines 클래스 기능 검증 테스트")
class LinesTest {

	private Station gangnam;
	private Station gyodae;
	private Station seocho;
	private Station bangbae;
	private Station sadang;
	private Station nakseongdae;
	private Station goter;
	private Station chongshin;
	private Station naebang;
	private Line line2;
	private Line line3;
	private Line line4;
	private Line line7;

	@BeforeEach
	void setUp() {
		gangnam = new Station("강남역");
		gyodae = new Station("교대역");
		seocho = new Station("서초역");
		bangbae = new Station("방배역");
		sadang = new Station("사당역");
		nakseongdae = new Station("낙성대역");
		goter = new Station("고속터미널역");
		chongshin = new Station("총신대입구역");
		naebang = new Station("내방역");

		line2 = new Line("2호선", "green", gangnam, gyodae, 3);
		line2.addSection(new Section(line2, gyodae, seocho, 2));
		line2.addSection(new Section(line2, seocho, bangbae, 2));
		line2.addSection(new Section(line2, bangbae, sadang, 2));
		line2.addSection(new Section(line2, sadang, nakseongdae, 2));

		line3 = new Line("3호선", "orange", gyodae, goter, 2, 900);
		line4 = new Line("4호선", "blue", sadang, chongshin, 3, 800);
		line7 = new Line("7호선", "dark_green", chongshin, naebang, 5, 1100);
		line7.addSection(new Section(line7, naebang, goter, 3));
	}

	@TestFactory
	@DisplayName("최종 추가요금 확인")
	List<DynamicTest> maxSurcharge() {
		// given
		Lines lines = new Lines(Arrays.asList(line2, line3, line4, line7));
		PathFinder finder = PathFinder.of(lines);
		return Arrays.asList(
				dynamicTest("추가요금 조회-1", () -> {
					Path path = finder.findPath(gangnam, chongshin);

					// when
					int finalSurcharge = lines.getFinalSurcharge(path);

					// then
					assertThat(finalSurcharge).isEqualTo(800);
				}),
				dynamicTest("추가요금 조회-2", () -> {
					Path path = finder.findPath(gangnam, naebang);

					// when
					int finalSurcharge = lines.getFinalSurcharge(path);

					// then
					assertThat(finalSurcharge).isEqualTo(1100);
				})
		);
	}
}
