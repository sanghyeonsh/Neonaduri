package neonaduri.api.controller;

import lombok.RequiredArgsConstructor;
import neonaduri.api.service.StatisticsService;
import neonaduri.domain.statistics.Visited;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;
    
    /** A05: 만족도에 따른 통계 자료 가져온다. */
    @GetMapping("/sat")
    public ResponseEntity<?> getSatisfaction() {
        return new ResponseEntity<>(statisticsService.findSatisfaction(),HttpStatus.OK);
    }


    /** A06: 여행횟수에 따른 통계 자료를 가져온다. */
    @GetMapping("/visited")
    public ResponseEntity<List<Visited>> getVisited() {
        return new ResponseEntity<>(statisticsService.findVisited(), HttpStatus.OK);

    }

    /** A07: 빙믄지 선택횟수에 따른 통계 자료를 가져온다. */
    @GetMapping("/sel")
    public ResponseEntity<?> getSelected() {
        return new ResponseEntity<>(statisticsService.findSelected(), HttpStatus.OK);
    }
}
