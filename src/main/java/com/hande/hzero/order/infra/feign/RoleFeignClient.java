package com.hande.hzero.order.infra.feign;

import com.hande.hzero.order.domain.dto.RoleDTO;
import com.hande.hzero.order.infra.feign.fallback.RoleFeignClientFallback;
import org.hzero.common.HZeroService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author huaxin.deng@hand-china.com 2020-08-20 19:27:28
 */
@FeignClient(value = HZeroService.Oauth.NAME, fallback = RoleFeignClientFallback.class, path = "")
public interface RoleFeignClient {

    @GetMapping("/hzero/v1/{organizationId}/roles/{roleId}")
    RoleDTO getRole(@PathVariable("organizationId") Long organizationId,
                    @PathVariable("roleId") Long roleId);
}
