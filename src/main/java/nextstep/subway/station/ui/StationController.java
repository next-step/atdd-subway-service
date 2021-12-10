package nextstep.subway.station.ui;

import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import nextstep.subway.station.application.*;
import nextstep.subway.station.dto.*;

@RestController
public class StationController {
    private final StationService stationService;
    private final StationReadService stationReadService;

    public StationController(StationService stationService, StationReadService stationReadService) {
        this.stationService = stationService;
        this.stationReadService = stationReadService;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationRequest stationRequest) {
        StationResponse station = stationService.saveStation(stationRequest);
        return ResponseEntity.created(URI.create("/stations/" + station.getId())).body(station);
    }

    @GetMapping(value = "/stations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationReadService.findAllStations());
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<StationResponse> deleteStation(@PathVariable Long id) {
        stationService.deleteStationById(id);
        return ResponseEntity.noContent().build();
    }
}
