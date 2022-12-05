package nextstep.subway.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;

@Component
@Profile("!test")
public class DataLoaderConfig implements CommandLineRunner {
	private LineRepository lineRepository;
	private MemberRepository memberRepository;

	public DataLoaderConfig(LineRepository lineRepository, MemberRepository memberRepository) {
		this.lineRepository = lineRepository;
		this.memberRepository = memberRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		Station 강남역 = Station.from(Name.from("강남역"));
		Station 교대역 = Station.from(Name.from("교대역"));
		Station 양재역 = Station.from(Name.from("양재역"));
		Station 남부터미널역 = Station.from(Name.from("남부터미널역"));

		Line 신분당선 = Line.of(Name.from("신분당선"), Color.from("red lighten-1"),
			Sections.from(Section.of(강남역, 양재역, Distance.from(10))));
		Line 이호선 = Line.of(Name.from("2호선"), Color.from("green lighten-1"),
			Sections.from(Section.of(교대역, 강남역, Distance.from(10))));
		Line 삼호선 = Line.of(Name.from("3호선"), Color.from("orange darken-1"),
			Sections.from(Section.of(교대역, 양재역, Distance.from(10))));

		lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

		memberRepository.save(new Member("probitanima11@gmail.com", "11", 10));
	}
}