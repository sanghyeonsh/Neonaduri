package neonaduri.api.controller;

import lombok.RequiredArgsConstructor;
import neonaduri.api.service.RecommendService;
import neonaduri.dto.response.RecommendSpotRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendController {
    
    private final RecommendService recommendService;

    /** A04: 고객들이 가장 선호하고 있는 장소를 가지고 온다. */
    @GetMapping
    public ResponseEntity<RecommendSpotRes> showHotSpotForUser(){
        return ResponseEntity.ok(recommendService.getHotSpot());
    }
}
