package com.ruoyi.oa.portal.mapper;

import java.util.List;
import com.ruoyi.oa.portal.domain.OaFavoriteApp;

/**
 * 用户收藏应用 Mapper
 */
public interface OaFavoriteAppMapper
{
    /**
     * 通过ID查询收藏
     */
    OaFavoriteApp selectById(Long id);

    /**
     * 查询收藏列表
     */
    List<OaFavoriteApp> selectList(OaFavoriteApp favoriteApp);

    /**
     * 根据用户查询收藏
     */
    List<OaFavoriteApp> selectByUserId(Long userId);

    /**
     * 根据用户和应用查询
     */
    OaFavoriteApp selectByUserAndApp(OaFavoriteApp favoriteApp);

    /**
     * 新增收藏
     */
    int insert(OaFavoriteApp favoriteApp);

    /**
     * 删除收藏
     */
    int deleteById(Long id);

    /**
     * 根据用户和应用删除
     */
    int deleteByUserAndApp(OaFavoriteApp favoriteApp);
}
