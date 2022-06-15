package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorCode;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DiscountType;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(LoginMember loginMember, PathRequest request) {
        request.checkValid();
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());

        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        DiscountType discountType = loginMember.isLogin() ? loginMember.getDiscountType() : DiscountType.NONE;
        Path path = pathFinder.getPath(sourceStation, targetStation, discountType);
        return new PathResponse(path);
    }

    private Station getStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.STATION_NOT_FOUND));
    }

}
