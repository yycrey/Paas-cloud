package paas.rey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import paas.rey.enums.BizCodeEnum;
import paas.rey.model.BannerDO;
import paas.rey.mapper.BannerMapper;
import paas.rey.model.ProductDO;
import paas.rey.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import paas.rey.utils.JsonData;
import paas.rey.vo.BannerVO;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yeycrey
 * @since 2025-01-04
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, BannerDO> implements BannerService {
    @Autowired
    private BannerMapper bannerMapper;
    /*
        轮播图列表分页查询
     */
    @ApiOperation(value = "轮播图列表分页接口")
    @Override
    public JsonData pageList(int page, int size) {
        Page<BannerDO> pages = new Page<BannerDO>();
        pages.setCurrent(page);
        pages.setSize(size);
        Page<BannerDO> resultPage =  bannerMapper.selectPage(pages,new QueryWrapper<BannerDO>()
                .orderByDesc("weight"));
        if (resultPage.getRecords().isEmpty()){
            return JsonData.buildError(BizCodeEnum.CODE_DATABASE_FIND_ERROR);
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("total_record",resultPage.getTotal());
        objectObjectHashMap.put("data",resultPage.getRecords().stream().map(this::beanProcess).collect(Collectors.toList()));
        objectObjectHashMap.put("total_page",resultPage.getPages());
        return JsonData.buildSuccess(BizCodeEnum.CODE_DATABASE_FIND_SUCCESS,objectObjectHashMap);
    }

    private BannerVO beanProcess(BannerDO bannerDO){
        BannerVO bannerVO = new BannerVO();
        BeanUtils.copyProperties(bannerDO,bannerVO);
        return bannerVO;
    }
}
