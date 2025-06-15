package ru.ntwz.makemyfeed.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.makemyfeed.config.CommonConfig;
import ru.ntwz.makemyfeed.dto.mapper.InfoMapper;
import ru.ntwz.makemyfeed.dto.response.InfoStatusDTO;
import ru.ntwz.makemyfeed.service.InfoService;

@Service
public class InfoServiceImpl implements InfoService {

    private final CommonConfig commonConfig;

    @Autowired
    public InfoServiceImpl(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    @Override
    public InfoStatusDTO status() {
        return InfoMapper.toInfoStatusDTO("Service is up!", commonConfig.getVersion());
    }
}
