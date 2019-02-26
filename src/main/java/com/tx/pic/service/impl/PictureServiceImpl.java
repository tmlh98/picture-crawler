package com.tx.pic.service.impl;

import com.tx.pic.config.TitleFilterCfg;
import com.tx.pic.entity.Picture;
import com.tx.pic.mapper.PictureMapper;
import com.tx.pic.service.PictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author TianXin
 * @since 2019-02-21
 */
@Service
@AutoConfigureBefore(value = {TitleFilterCfg.class})
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

}
