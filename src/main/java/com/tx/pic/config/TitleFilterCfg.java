package com.tx.pic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tx.pic.entity.Picture;
import com.tx.pic.service.PictureService;
import com.tx.pic.util.TitleFilter;

@Configuration
public class TitleFilterCfg {

	@Autowired
	private PictureService pictureService;

	@Bean
	public TitleFilter titleFilter() {
		// 创建图片标题过滤器
		TitleFilter titleFilter = new TitleFilter();

		IPage<Picture> picturePage = pictureService.page(new Page<>(1, 5000));

		for (Picture pic : picturePage.getRecords()) {
			// 把查询到的数据放到过滤器中
			titleFilter.add(pic.getTitle());
		}

		// 返回创建好的过滤器
		return titleFilter;
	}

}
