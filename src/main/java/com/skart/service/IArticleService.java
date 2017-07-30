package com.skart.service;

import java.util.List;
import com.skart.entity.Article;

public interface IArticleService {
	//abstract List<Article> getAllArticles();

	Article getArticleById(int articleId);

	boolean addArticle(Article article);

	void updateArticle(Article article);

	void deleteArticle(int articleId);

	List<Article> getAllArticles();
}
