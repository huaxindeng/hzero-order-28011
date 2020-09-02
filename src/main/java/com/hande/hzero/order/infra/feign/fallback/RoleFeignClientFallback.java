package com.hande.hzero.order.infra.feign.fallback;

import com.hande.hzero.order.domain.dto.RoleDTO;
import com.hande.hzero.order.infra.feign.RoleFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author huaxin.deng@hand-china.com 2020-08-20 19:29:57
 */
@Component
public class RoleFeignClientFallback implements RoleFeignClient {
    @Override
    public RoleDTO getRole(Long organizationId, Long roleId) {
//        throw new CommonException("oauth service is not available, please check");
        return new RoleDTO("", "SALE_MANAGER_28011");
    }
}
