package com.skart.dao;

import java.util.List;
import com.skart.entity.Article;

public interface IArticleDAO {
	//abstract List<Article> getAllArticles();

	Article getArticleById(int articleId);

	void addArticle(Article article);

	void updateArticle(Article article);

	void deleteArticle(int articleId);

	boolean articleExists(String title, String category);
}
