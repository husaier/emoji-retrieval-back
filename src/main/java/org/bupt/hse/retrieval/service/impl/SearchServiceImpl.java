package org.bupt.hse.retrieval.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.enums.BizExceptionEnum;
import org.bupt.hse.retrieval.infra.ImageInfraService;
import org.bupt.hse.retrieval.service.SearchService;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.utils.RedisUtil;
import org.bupt.hse.retrieval.utils.RestTemplateUtil;
import org.bupt.hse.retrieval.vo.ImageVO;
import org.bupt.hse.retrieval.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by Hu Saier <husserl@bupt.edu.cn>
 * 2024-10-01
 */
@Service
public class SearchServiceImpl implements SearchService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${file.download.address}")
    private String downloadAddress;

    @Autowired
    private ImageInfraService imageInfraService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageVO<ImageVO> getAllPage(long cur, long pageSize) throws BizException {
        UserDO curUser = userService.getCurUserInfo();
        Long userId = curUser.getId();
        List<UserDO> userList = userService.getAllUserInfo();
        Set<Long> starSet = userService.getLikeSet(userId);
        Map<Long, UserDO> userMap = userList.stream()
                .collect(Collectors
                        .toMap(UserDO::getId,
                                Function.identity(),
                                (x, y) -> x));
        Page<ImageDO> tmpPage = new Page<>(cur, pageSize);
        Page<ImageDO> page = imageInfraService.page(tmpPage);
        PageVO<ImageVO> pageVO = new PageVO<>();
        pageVO.setCur(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setPageNum(page.getPages());
        pageVO.setTotal(page.getTotal());
        List<ImageVO> imgList = page.getRecords().stream()
                .map((x) -> {
                    ImageVO vo = new ImageVO();
                    vo.setId(String.valueOf(x.getId()));
                    vo.setFileName(x.getFileName());
                    vo.setOriginName(x.getOriginName());
                    vo.setImgType(x.getImgType());
                    vo.setPublisher(String.valueOf(x.getPublisher()));
                    vo.setStar(starSet.contains(x.getId()));
                    UserDO userDO = userMap.get(x.getPublisher());
                    if (userDO != null) {
                        vo.setUserName(userDO.getName());
                    }
                    vo.setUploadTime(x.getUploadTime());
                    String address = String.format("%s/%s", downloadAddress, x.getId());
                    vo.setAddress(address);
                    vo.setDescription(x.getDescription());
                    vo.setStarCount(String.valueOf(x.getStarCount()));
                    return vo;
                }).collect(Collectors.toList());
        pageVO.setRecords(imgList);
        return pageVO;
    }

    @Override
    public PageVO<ImageVO> searchImages(String query, long cur, long pageSize)
            throws BizException {
        // 查缓存
        String redisKey = String.format("search_%s", query);
        String cacheResult = (String) redisUtil.get(redisKey);
        List<String> cacheResultIds = JSON.parseArray(cacheResult, String.class);
        List<String> searchResultIds;
        if (cacheResultIds == null || cacheResultIds.isEmpty()) {
            searchResultIds = searchFromModel(query);
            String cacheValue = JSON.toJSONString(searchResultIds);
            redisUtil.set(redisKey, cacheValue, 3600 * 24);
        } else {
            searchResultIds = cacheResultIds;
        }
        log.info(String.format("searchResultIds size: %d", searchResultIds.size()));
        // 分页计算
        long total = searchResultIds.size();
        long pages = total / pageSize;
        if (total % pageSize != 0) {
            pages++;
        }
        if (cur > pages) {
            throw new BizException(BizExceptionEnum.INVALID_CUR_PAGE);
        }
        long offset = (cur - 1) * pageSize;
        int i = (int) offset;
        List<Long> pageIds = new ArrayList<>();
        int count = 0;
        while (i < total && count < pageSize) {
            String s = searchResultIds.get(i);
            Long id = Long.parseLong(s);
            pageIds.add(id);
            i++;
            count++;
        }
        // 详细图片数据组装
        UserDO curUser = userService.getCurUserInfo();
        Long userId = curUser.getId();
        List<UserDO> userList = userService.getAllUserInfo();
        Set<Long> starSet = userService.getLikeSet(userId);
        Map<Long, UserDO> userMap = userList.stream()
                .collect(Collectors
                        .toMap(UserDO::getId,
                                Function.identity(),
                                (x, y) -> x));
        LambdaQueryWrapper<ImageDO> queryWrapper = new LambdaQueryWrapper<ImageDO>()
                .in(ImageDO::getId, pageIds);
        List<ImageDO> imgDOS = imageInfraService.list(queryWrapper);
        PageVO<ImageVO> pageVO = new PageVO<>();
        pageVO.setCur(cur);
        pageVO.setPageSize(pageSize);
        pageVO.setPageNum(pages);
        pageVO.setTotal(total);
        List<ImageVO> imageVOS = imgDOS.stream()
                .map((x) -> {
                   ImageVO vo = new ImageVO();
                   vo.setId(String.valueOf(x.getId()));
                   vo.setFileName(x.getFileName());
                   vo.setOriginName(x.getOriginName());
                   vo.setImgType(x.getImgType());
                   vo.setPublisher(String.valueOf(x.getPublisher()));
                   vo.setStar(starSet.contains(x.getId()));
                   UserDO userDO = userMap.get(x.getPublisher());
                   if (userDO != null) {
                       vo.setUserName(userDO.getName());
                   }
                   vo.setUploadTime(x.getUploadTime());
                   String address = String.format("%s/%s", downloadAddress, x.getId());
                   vo.setAddress(address);
                   vo.setDescription(x.getDescription());
                   vo.setStarCount(String.valueOf(x.getStarCount()));
                   return vo;
                }).collect(Collectors.toList());
        pageVO.setRecords(imageVOS);
        return pageVO;
    }

    private List<String> searchFromModel(String query) {
        Map<String, String> processParam = new HashMap<>();
        processParam.put("from", "0");
        processParam.put("pageSize", "200");
        processParam.put("query", query);
        String paramStr = JSON.toJSONString(processParam);
        Map<String, String> headers = new HashMap<>();
        JSONObject response = restTemplateUtil.post("http://127.0.0.1:8002/emoji/search", paramStr, headers);
//        log.info(String.format("model search response: %s", JSON.toJSONString(response)));
        if (response.getInteger("code") != 200) {
            log.error(BizExceptionEnum.FAIL_SEARCH_FROM_MODEL.getMsg());
            return new ArrayList<>();
        }
        JSONObject obj = response.getJSONObject("data");
        JSONArray array = obj.getJSONArray("records");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            res.add(array.getString(i));
        }
        return res;
    }

    @Override
    public PageVO<ImageVO> getLikePage(long cur, long pageSize) throws BizException {
        UserDO curUser = userService.getCurUserInfo();
        Long userId = curUser.getId();
        List<UserDO> userList = userService.getAllUserInfo();
        Set<Long> starSet = userService.getLikeSet(userId);
        Map<Long, UserDO> userMap = userList.stream()
                .collect(Collectors
                        .toMap(UserDO::getId,
                                Function.identity(),
                                (x, y) -> x));
        Page<ImageDO> tmpPage = new Page<>(cur, pageSize);
        LambdaQueryWrapper<ImageDO> pageQuery = new LambdaQueryWrapper<ImageDO>()
                .in(ImageDO::getId, starSet);
        Page<ImageDO> page = imageInfraService.page(tmpPage, pageQuery);
        PageVO<ImageVO> pageVO = new PageVO<>();
        pageVO.setCur(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setPageNum(page.getPages());
        pageVO.setTotal(page.getTotal());
        List<ImageVO> imgList = page.getRecords().stream()
                .map((x) -> {
                    ImageVO vo = new ImageVO();
                    vo.setId(String.valueOf(x.getId()));
                    vo.setFileName(x.getFileName());
                    vo.setOriginName(x.getOriginName());
                    vo.setImgType(x.getImgType());
                    vo.setPublisher(String.valueOf(x.getPublisher()));
                    vo.setStar(starSet.contains(x.getId()));
                    UserDO userDO = userMap.get(x.getPublisher());
                    if (userDO != null) {
                        vo.setUserName(userDO.getName());
                    }
                    vo.setUploadTime(x.getUploadTime());
                    String address = String.format("%s/%s", downloadAddress, x.getId());
                    vo.setAddress(address);
                    vo.setDescription(x.getDescription());
                    vo.setStarCount(String.valueOf(x.getStarCount()));
                    return vo;
                }).collect(Collectors.toList());
        pageVO.setRecords(imgList);
        return pageVO;
    }

    @Override
    public PageVO<ImageVO> getUploadPage(long cur, long pageSize) throws BizException {
        UserDO curUser = userService.getCurUserInfo();
        Long userId = curUser.getId();
        String userName = curUser.getName();
        Set<Long> starSet = userService.getLikeSet(userId);
        List<UserDO> userList = userService.getAllUserInfo();
        Map<Long, UserDO> userMap = userList.stream()
                .collect(Collectors
                        .toMap(UserDO::getId,
                                Function.identity(),
                                (x, y) -> x));
        Page<ImageDO> tmpPage = new Page<>(cur, pageSize);
        LambdaQueryWrapper<ImageDO> pageQuery = new LambdaQueryWrapper<ImageDO>()
                .eq(ImageDO::getPublisher, userId);
        Page<ImageDO> page = imageInfraService.page(tmpPage, pageQuery);
        PageVO<ImageVO> pageVO = new PageVO<>();
        pageVO.setCur(page.getCurrent());
        pageVO.setPageSize(page.getSize());
        pageVO.setPageNum(page.getPages());
        pageVO.setTotal(page.getTotal());
        List<ImageVO> imgList = page.getRecords().stream()
                .map((x) -> {
                    ImageVO vo = new ImageVO();
                    vo.setId(String.valueOf(x.getId()));
                    vo.setFileName(x.getFileName());
                    vo.setOriginName(x.getOriginName());
                    vo.setImgType(x.getImgType());
                    vo.setPublisher(String.valueOf(x.getPublisher()));
                    vo.setStar(starSet.contains(x.getId()));
                    UserDO userDO = userMap.get(x.getPublisher());
                    if (userDO != null) {
                        vo.setUserName(userDO.getName());
                    }
                    vo.setUploadTime(x.getUploadTime());
                    String address = String.format("%s/%s", downloadAddress, x.getId());
                    vo.setAddress(address);
                    vo.setDescription(x.getDescription());
                    vo.setStarCount(String.valueOf(x.getStarCount()));
                    return vo;
                }).collect(Collectors.toList());
        pageVO.setRecords(imgList);
        return pageVO;
    }
}
