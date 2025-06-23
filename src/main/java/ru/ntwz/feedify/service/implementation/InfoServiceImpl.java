package ru.ntwz.feedify.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ntwz.feedify.config.CommonConfig;
import ru.ntwz.feedify.dto.mapper.InfoMapper;
import ru.ntwz.feedify.dto.response.InfoHealthDTO;
import ru.ntwz.feedify.service.InfoService;

@Service
public class InfoServiceImpl implements InfoService {

    private final CommonConfig commonConfig;

    @Autowired
    public InfoServiceImpl(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    @Override
    public InfoHealthDTO health() {
        return InfoMapper.toInfoHealthDTO("Service is up!", commonConfig.getVersion());
    }
}
