package ru.ntwz.makemyfeed.dto.mapper;

import ru.ntwz.makemyfeed.dto.response.InfoStatusDTO;

public class InfoMapper {
    public static InfoStatusDTO toInfoStatusDTO(String status, String version) {
        InfoStatusDTO infoStatusDTO = new InfoStatusDTO();
        infoStatusDTO.setStatus(status);
        infoStatusDTO.setVersion(version);
        return infoStatusDTO;
    }
}
