package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public Sections findSections() {
        return lines.stream()
                .flatMap(Line::getSectionsStream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Sections::new));
    }

    public Station findStation(Long stationId) {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(station -> station.isEqualToId(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 출발역이나 도착역입니다."));
    }

    public Fee calculatedFee(LoginMember loginMember, Distance distance) {
        Fee maxFee = findMaxFee();
        Fee fee = maxFee.calculateBasicFee(distance);
        if (loginMember.isEmpty()) {
            return fee;
        }
        return fee.calculateAgeFee(loginMember);
    }

    private Fee findMaxFee() {
        Optional<Fee> max = lines.stream().map(Line::getFee)
                .max(Comparator.comparingInt(Fee::get));
        return max.orElseGet(() -> Fee.ofWithOverFare(0));
    }
}
