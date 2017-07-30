package com.skart.dao;

import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.skart.entity.Article;

@Transactional
@Repository
public class ArticleDAO implements IArticleDAO {
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Article getArticleById(int articleId) {
		return entityManager.find(Article.class, articleId);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Article> getAllArticles() {
		// String hql = "FROM Article as atcl ORDER BY atcl.articleId";
		String sel = this.jdbcTemplate.queryForObject("select title from article where id = ?",
				new Object[] { 101 }, String.class);
		// return (List<Article>)
		// entityManager.createQuery(hql).getResultList();
		//Article art = (new Article());
		art.setCategory(sel);
		return Arrays.asList(art);
	}*/

	@Override
	public void addArticle(Article article) {
		entityManager.persist(article);
	}

	@Override
	public void updateArticle(Article article) {
		Article artcl = getArticleById(article.getArticleId());
		artcl.setTitle(article.getTitle());
		artcl.setCategory(article.getCategory());
		entityManager.flush();
	}

	@Override
	public void deleteArticle(int articleId) {
		entityManager.remove(getArticleById(articleId));
	}

	@Override
	public boolean articleExists(String title, String category) {
		String hql = "FROM Article as atcl WHERE atcl.title = ? and atcl.category = ?";
		int count = entityManager.createQuery(hql).setParameter(1, title).setParameter(2, category).getResultList()
				.size();
		return count > 0 ? true : false;
	}
}
