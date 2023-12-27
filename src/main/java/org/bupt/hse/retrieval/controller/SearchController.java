package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bupt.hse.retrieval.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-27
 */
@RestController
@RequestMapping("api/search")
@Api(tags = "图片检索接口")
@Slf4j
public class SearchController {

    @GetMapping("query")
    @ApiOperation(value = "查询")
    public Result<String> query() {
        return Result.failed();
    }
}
