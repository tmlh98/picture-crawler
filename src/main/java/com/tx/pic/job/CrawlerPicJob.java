package com.tx.pic.job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.tx.pic.entity.Picture;
import com.tx.pic.service.ApiService;
import com.tx.pic.service.PictureService;
import com.tx.pic.util.TitleFilter;

@DisallowConcurrentExecution
public class CrawlerPicJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerPicJob.class);

	private ApiService apiService;
	private PictureService pictureService;
	private TitleFilter titleFilter;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取spring容器
		ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap()
				.get("context");
		// 初始化服务
		apiService = applicationContext.getBean(ApiService.class);
		pictureService = applicationContext.getBean(PictureService.class);
		titleFilter = applicationContext.getBean(TitleFilter.class);

		for (int i = 1; i < 300; i++) {
			// String html =
			// apiService.getHtml("https://www.2717.com/ent/meinvtupian/list_11_" + i +
			// ".html");
			String html = apiService.getHtml("https://www.2717.com/ent/meinvtupian/list_11_210.html");
			// 解析
			Document document = Jsoup.parse(html);

			Elements elements = document.select(".MeinvTuPianBox ul > li");
			// 下载图片

			for (Element element : elements) {
				String title = element.getElementsByTag("a").first().attr("title");
				// 去重判读
				if (this.titleFilter.contains(title)) {
					// 如果包含了，就不保存了，遍历下一个
					continue;
				}
				// 封装数据
				Picture picture = copyPicture(element);
				picture.setTitle(title);
				picture.setContent(title);
				//下载图片
				apiService.getImage(picture.getUrl());
				
				boolean result = pictureService.save(picture);
				if(result) {
					LOGGER.info("insert seuccess ! picture : {} " , picture);
				}else {
					LOGGER.info("insert fail ! picture : {} " , picture);
					
				}
				
			}

		}

	}

	private Picture copyPicture(Element element) {
		Picture picture = new Picture();
		String url = element.getElementsByTag("img").first().attr("src");
		picture.setUrl(url);

		String keywords = element.getElementsByTag("a").first().attr("class");
		picture.setKeywords(keywords);

		String tag = element.select("span > u > a").first().text();
		picture.setTag(tag);

		String likeNum = element.select("span > i").first().text();
		picture.setClickNum(Integer.valueOf(likeNum));
		return picture;
	}

}
