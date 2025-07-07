package ru.ntwz.feedify.dto.mapper;

import ru.ntwz.feedify.dto.response.InfoHealthDto;

public class InfoMapper {
    public static InfoHealthDto toInfoHealthDTO(String status, String version) {
        InfoHealthDto infoHealthDTO = new InfoHealthDto();
        infoHealthDTO.setStatus(status);
        infoHealthDTO.setVersion(version);
        return infoHealthDTO;
    }
}
