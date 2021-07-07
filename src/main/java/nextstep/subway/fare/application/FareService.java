package nextstep.subway.fare.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.fare.domain.DiscountPolicy;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@Service
public class FareService {
    private final SectionRepository sectionRepository;

    public FareService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public long calculateFare(final List<Station> stationList, final int distance,
        final DiscountPolicy discountPolicy) {
        final Sections sections = new Sections(sectionRepository.findAll());
        final Stations stations = new Stations(stationList);
        final int extraFare = sections.getMaxExtraFare(stations.toStationPairs());
        final Fare fare = new Fare(distance, extraFare);

        return discountPolicy.discount(fare.calculate());
    }
}
