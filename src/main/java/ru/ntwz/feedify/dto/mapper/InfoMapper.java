package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.InfoHealthDTO;

public class InfoMapper {
    public static InfoHealthDTO toInfoHealthDTO(String status, String version) {
        InfoHealthDTO infoHealthDTO = new InfoHealthDTO();
        infoHealthDTO.setStatus(status);
        infoHealthDTO.setVersion(version);
        return infoHealthDTO;
    }
}
