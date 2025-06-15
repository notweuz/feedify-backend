package ru.ntwz.makemyfeed.dto.mapper;

import ru.ntwz.makemyfeed.dto.response.InfoHealthDTO;

public class InfoMapper {
    public static InfoHealthDTO toInfoHealthDTO(String status, String version) {
        InfoHealthDTO infoHealthDTO = new InfoHealthDTO();
        infoHealthDTO.setStatus(status);
        infoHealthDTO.setVersion(version);
        return infoHealthDTO;
    }
}
