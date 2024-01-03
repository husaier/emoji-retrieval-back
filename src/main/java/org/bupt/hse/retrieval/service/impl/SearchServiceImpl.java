package org.bupt.hse.retrieval.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.bupt.hse.retrieval.common.BizException;
import org.bupt.hse.retrieval.entity.ImageDO;
import org.bupt.hse.retrieval.entity.UserDO;
import org.bupt.hse.retrieval.infra.ImageInfraService;
import org.bupt.hse.retrieval.service.SearchService;
import org.bupt.hse.retrieval.service.UserService;
import org.bupt.hse.retrieval.vo.ImageVO;
import org.bupt.hse.retrieval.vo.PageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Override
    public PageVO<ImageVO> searchImages(long cur, long pageSize)
            throws BizException {
        UserDO curUser = userService.getCurUserInfo();
        Long userId = curUser.getId();
        List<UserDO> userList = userService.getUserInfo();
        Set<Long> likeSet = userService.getLikeSet(userId);
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
                   vo.setId(x.getId());
                   vo.setFileName(x.getFileName());
                   vo.setOriginName(x.getOriginName());
                   vo.setImgType(x.getImgType());
                   vo.setUserId(x.getUserId());
                   vo.setLike(likeSet.contains(x.getId()));
                   UserDO userDO = userMap.get(x.getUserId());
                   if (userDO != null) {
                       vo.setUserName(userDO.getName());
                   }
                   vo.setUploadTime(x.getUploadTime());
                   String address = String.format("%s/%s", downloadAddress, x.getId());
                   vo.setAddress(address);
                   vo.setDescription("图片描述");
                   return vo;
                }).collect(Collectors.toList());
        pageVO.setRecords(imgList);
        return pageVO;
    }
}
