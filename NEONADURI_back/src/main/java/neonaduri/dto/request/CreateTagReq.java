package neonaduri.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTagReq {
    private final String tagContent;
}
