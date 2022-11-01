package neonaduri.api.controller;

import lombok.RequiredArgsConstructor;
import neonaduri.api.service.RegionService;
import neonaduri.dto.response.SidoResponseDto;
import neonaduri.dto.response.SigunguResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/region")
public class RegionController {
    private final RegionService regionService;

    /** A08 지역에서 시도를 기준으로 검색하고 결과를 출력 */
    @GetMapping("/sido")
    public ResponseEntity<?> sidoController() {
            return new ResponseEntity<>(regionService.getSidoList(), HttpStatus.OK);
    }

    /** A09: 지역에서 시군구를 기준으로 검색하고 결과를 출력 */
    @GetMapping("/sigungu/{sido}")
    public ResponseEntity<?> sigunguController(@PathVariable("sido") String sido) {
            return new ResponseEntity<>(regionService.getSigunguList(sido),HttpStatus.OK);
    }

    /** A10: 지역에서 면,동 를 기준으로 검색하고 결과 */
    @GetMapping("/myeon/{sido}/{sigungu}")
    public ResponseEntity<?> myeonController(@PathVariable("sido") String sido,@PathVariable("sigungu") String sigungu) {
        return new ResponseEntity<>(regionService.getMyeonList(sido,sigungu), HttpStatus.OK);
    }
}
