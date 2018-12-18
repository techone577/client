package com.eureka.client.service;


import com.eureka.client.model.constant.NettyHeader;
import com.eureka.client.model.entity.NettyRespEntity;
import com.eureka.client.model.syncMap.SyncMap;
import org.springframework.stereotype.Service;

/**
 * @author techoneduan
 * @date 2018/12/17
 */
@Service
public class RequestNettyService extends AbstractNettyService {

    @Override
    public void dealRequest (NettyRespEntity respEntity) {
        if (!SyncMap.hasKey(respEntity.getRequestId())) {
            SyncMap.put(respEntity.getRequestId(), respEntity.getResponse());
        }
    }

    @Override
    public boolean matching (String factor) {
        return NettyHeader.REQUEST.equals(factor);
    }
}
