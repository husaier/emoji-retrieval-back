package org.bupt.hse.retrieval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.common.Result;
import org.bupt.hse.retrieval.service.SearchService;
import org.bupt.hse.retrieval.vo.ImageVO;
import org.bupt.hse.retrieval.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2023-10-27
 */
@RestController
@RequestMapping("api/search")
@Api(tags = "图片检索接口")
public class SearchController {

    private final static Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @GetMapping("query")
    @ApiOperation(value = "查询")
    public Result<PageVO<ImageVO>> query(@RequestParam("pageSize")
                                @ApiParam("page size")
                                    long pageSize,
                                         @RequestParam("cur")
                                @ApiParam("当前页数")
                                    long cur) {
        try {
            return Result.success(searchService.searchImages(cur, pageSize));
        } catch (BizException e) {
            return Result.failed(e.getMsg());
        }
    }
}
