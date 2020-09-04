package com.hande.hzero.order.api.controller.v1;

import com.hande.hzero.order.config.SwaggerApiConfig;
import com.hande.hzero.order.domain.entity.Item;
import com.hande.hzero.order.infra.mapper.ItemMapper;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author huaxin.deng@hand-china.com 2020-08-18 19:27:38
 */
@Api(tags = SwaggerApiConfig.ITEM)
@RestController("ItemController.v1")
@RequestMapping("/v1/{organizationId}/item")
public class ItemController extends BaseController {

    private ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    @ApiOperation(value = "根据物料编码物料描述模糊查询")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<List<Item>> listItems(@ApiParam(value = "租户ID", required = true)
                                                            @PathVariable("organizationId") Long organizationId,
                                                @RequestParam String itemCode,
                                                @RequestParam String itemDescription){
        return Results.success(itemMapper.listItem(itemCode, itemDescription, organizationId));
    }
}
