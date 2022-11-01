package neonaduri.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neonaduri.api.repository.SatisfactionRepository;
import neonaduri.api.repository.SelectedRepository;
import neonaduri.api.repository.VisitedRepository;
import neonaduri.domain.statistics.Satisfaction;
import neonaduri.domain.statistics.Selected;
import neonaduri.domain.statistics.Visited;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final SatisfactionRepository satisfactionRepository;

    private final SelectedRepository selectedRepository;

    private final VisitedRepository visitedRepository;

    //만족도 조회
    @Transactional
    public List<Satisfaction> findSatisfaction() {
        return satisfactionRepository.findAll();
    }

    @Transactional
    /** 방문지 선택이유*/
    public List<Selected> findSelected() {

        try {
            return selectedRepository.findAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Transactional
    /** 관광지여행횟수 */
    public List<Visited> findVisited() {
        try {
            return visitedRepository.findAll();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

}
