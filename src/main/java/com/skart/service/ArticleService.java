package com.skart.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.skart.dao.IArticleDAO;
import com.skart.entity.Article;

@Service
public class ArticleService implements IArticleService {
	@Autowired
	private IArticleDAO articleDAO;

	@Override
	public Article getArticleById(int articleId) {
		Article obj = articleDAO.getArticleById(articleId);
		return obj;
	}

	/*@Override
	public List<Article> getAllArticles() {
		return articleDAO.getAllArticles();
	}*/

	@Override
	public synchronized boolean addArticle(Article article) {
		if (articleDAO.articleExists(article.getTitle(), article.getCategory())) {
			return false;
		} else {
			articleDAO.addArticle(article);
			return true;
		}
	}

	@Override
	public void updateArticle(Article article) {
		articleDAO.updateArticle(article);
	}

	@Override
	public void deleteArticle(int articleId) {
		articleDAO.deleteArticle(articleId);
	}

	@Override
	public List<Article> getAllArticles() {
		List<Article> articleList = new ArrayList<>();
		articleList.add(new Article(1,"JAVA","Language"));
		articleList.add(new Article(2,"C++","Language"));
		articleList.add(new Article(3,"C","Language"));
		articleList.add(new Article(4,"JAVASCRIPT","scripting"));
		return articleList;
	}
}
