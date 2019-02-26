package com.tx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tx.pic.entity.Picture;
import com.tx.pic.service.ApiService;
import com.tx.pic.util.TitleFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureCrawlerApplicationTests {

	@Autowired
	private ApiService apiService;
	@Autowired
	private TitleFilter titleFilter;

	@Test
	public void contextLoads() throws Exception {

		for (int i = 1; i < 2; i++) {
//			String html = apiService.getHtml("https://www.2717.com/ent/meinvtupian/list_11_" + i + ".html");
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
				//封装数据
				Picture picture = copyPicture(element);
				picture.setTitle(title);
				picture.setContent(title);
				
				String image = apiService.getImage(picture.getUrl());
				System.out.println(image);
				
				System.out.println(picture);
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
