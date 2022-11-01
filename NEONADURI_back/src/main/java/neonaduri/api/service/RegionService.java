package neonaduri.api.service;

import lombok.RequiredArgsConstructor;
import neonaduri.api.repository.RegionRepository;
import neonaduri.dto.response.MyeonResponseDto;
import neonaduri.dto.response.SidoResponseDto;
import neonaduri.dto.response.SigunguResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Transactional
    public List<SidoResponseDto> getSidoList() {

        try {
            return regionRepository.findAllSido();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<SigunguResponseDto> getSigunguList(String sido) {
        try {
            return regionRepository.findSigunguBySido(sido);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    @Transactional
    public List<MyeonResponseDto> getMyeonList(String sido,String sigungu) {
        try {
            return regionRepository.findMyeonBySigungu(sido,sigungu);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }
}
