package com.srbdev.dbproject.daos;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.srbdev.dbproject.model.Movie;

public class MovieDao 
{
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource)
	{
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	/**
	 * Returns a list of movie IDs that have not been checked for similarity yet.
	 * 
	 * @return A list of movie IDs
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Movie> fetchAllMovieIDs()
	{
		String sql = "SELECT id FROM movieIDsToFetch WHERE checkedSimilarMovies = 0";
		
		Collection movies = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException 
			{
				Movie movie = new Movie();
				movie.setId(rs.getInt("id"));
				
				return movie;
			}
		});
		
		return (List<Movie>) movies;
	}
	
	/**
	 * Returns a list of movie IDs that have not been mined yet for the Rotten Tomatoes database.
	 * 
	 * @return A list of movie IDs
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Movie> fetchRTMovieIDsForMining()
	{
		String sql = "SELECT id FROM movieIDsToFetch WHERE fetchedInfo = 0";
		
		Collection movies = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Movie movie = new Movie();
				movie.setId(rs.getInt("id"));
				
				return movie;
			}
		});
		
		return (List<Movie>) movies;
	}
	
	/**
	 * Returns a list of movie IDs that have not been mined yet for The Movie Database.
	 * 
	 * @return A list of movie IDs
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Movie> fetchTMDMovieIDsForMining()
	{
		String sql = "SELECT id FROM tmdMovieIDsToFetch WHERE fetchedInfo = 0";
		
		Collection movies = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				Movie movie = new Movie();
				movie.setId(rs.getInt("id"));
				
				return movie;
			}
		});
		
		return (List<Movie>) movies;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int fetchIdForStudio(String name)
	{
		String sql = "SELECT id FROM Studios WHERE name = ?";
		
		return (Integer) jdbcTemplate.query(sql, new Object[] {name}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				return rs.getInt("id");
			}
		}).get(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int fetchIdForDirector(String name)
	{
		String sql = "SELECT id FROM Director WHERE name = ?";
		
		return (Integer) jdbcTemplate.query(sql, new Object[] {name}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				return rs.getInt("id");
			}
		}).get(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int fetchIdForReviewer(String critic)
	{
		String sql = "SELECT id FROM Reviewers WHERE critic = ?";
		
		return (Integer) jdbcTemplate.query(sql, new Object[] {critic}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				return rs.getInt("id");
			}
		}).get(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int fetchIdForReview(int reviewerId, Date reviewDate)
	{
		String sql = "SELECT id FROM Reviews WHERE reviewerId = ? AND reviewDate = ?";
		
		return (Integer) jdbcTemplate.query(sql, new Object[] {reviewerId, reviewDate}, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException
			{
				return rs.getInt("id");
			}
		}).get(0);
	}
	
	
	public void insertMovieIDToDb(int id, boolean flag)
	{
		String sql;
		if (flag)
			sql = "INSERT INTO movieIDsToFetch (id,checkedSimilarMovies,fetchedInfo) VALUES (?,?,?)";
		else
			sql = "INSERT INTO tmdMovieIDsToFetch (id,fetchedInfo) VALUES (?,?)";
		
		try {
			if (flag)
				jdbcTemplate.update(sql, new Object[] {id, 0, 0});
			else
				jdbcTemplate.update(sql, new Object[] {id, 0});
			
			System.out.println("INFO : Successfully added movie ID " +  id);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry movie ID " + id);
		}
	}
	
	
	public void insertMovieToDbFromRTData(int id, String title, int year, int runtime, String rating, String posterUrl)
	{
		String sql = "INSERT INTO Movies (id, title, year, runtime, rating, poster_url) VALUES (?,?,?,?,?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {id, title, year, runtime, rating, posterUrl});
			System.out.println("INFO : Successfully added movie (" +  id + ") title=" + title);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry movie ID " + id);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry movie ID " + id + " {" + title + "," + year  + "," + runtime + "," + rating + "}");
		}
	}
	
	public void insertSimilarMovieInformationToDb(int id1, int id2)
	{
		String sql = "INSERT INTO isSimilar (movie1Id, movie2Id) VALUES (?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {id1, id2});
			System.out.println("INFO : Successfully added similar movies: " +  id1 + ", " + id2);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry movies IDs " + id1 + ", " + id2);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry movie IDs " + id1 + ", " + id2);
		}
	}
	
	public void insertDirectorInformationToDb(String name)
	{
		String sql = "INSERT INTO Director (name) VALUES (?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {name});
			System.out.println("INFO : Successfully added director: " +  name);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry director name " + name);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry director name: " + name);
		}
	}
	
	public void insertStudioInformationToDb(String name)
	{
		String sql = "INSERT INTO Studios (name) VALUES (?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {name});
			System.out.println("INFO : Successfully added studio: " +  name);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry studio name " + name);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry studio name: " + name);
		}
	}
	
	public void insertActorInformationToDb(int id, String name)
	{
		String sql = "INSERT INTO Actors (id, name) VALUES (?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {id, name});
			System.out.println("INFO : Successfully added actor (" + id + "): " +  name);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry actor (" + id + "): " +  name);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry actor (" + id + "): " +  name);
		}
	}
	
	public void insertCastInformationToDb(int movieId, int actorId, String characters)
	{
		String sql = "INSERT INTO casts (movieId, actorId, characters) VALUES (?,?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {movieId, actorId, characters});
			System.out.println("INFO : Successfully added cast: movie=" + movieId + ", actor=" + actorId + ", characters=" + characters);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry cast: movie=" + movieId + ", actor=" + actorId + ", characters=" + characters);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry cast: movie=" + movieId + ", actor=" + actorId + ", characters=" + characters);
		}
	}
	
	public void insertGenreToDb(String type)
	{
		String sql = "INSERT INTO Genres (type) VALUES (?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {type});
			System.out.println("INFO : Successfully added genre: " + type);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry genre: " + type);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry genre: " + type);
		}
	}
	
	public void insertIsOfGenreInformationToDb(int movieId, String type)
	{
		String sql = "INSERT INTO isOfGenre (movieId, type) VALUES (?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {movieId, type});
			System.out.println("INFO : Successfully added genre information to movie " + movieId + ", genre=" + type);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry genre information to movie " + movieId + ", genre=" + type);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry genre information to movie " + movieId + ", genre=" + type);
		}
	}
	
	public void insertReviewerInformationToDb(String critic)
	{
		String sql = "INSERT INTO Reviewers (critic) VALUES (?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {critic});
			System.out.println("INFO : Successfully added genre information to reviewers: " + critic);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry genre information to reviewers: " + critic);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry reviewers: " + critic);
		}
	}
	
	public void insertReviewInformationToDb(int reviewerId, Date reviewDate, String publication, String score, String quote)
	{
		String sql = "INSERT INTO Reviews (reviewerId, reviewDate, publication, score, quote) VALUES (?,?,?,?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {reviewerId, reviewDate, publication, score, quote});
			System.out.println("INFO : Successfully added genre information to review information: {" + reviewerId + ", " + reviewDate + ", " + publication + ", " + score + ", " + quote +"}");
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry genre information to review information: {" + reviewerId + ", " + reviewDate + ", " + publication + ", " + score + ", " + quote +"}");
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry review information: {" + reviewerId + ", " + reviewDate + ", " + publication + ", " + score + ", " + quote +"}");
		}
	}
	
	public void insertReviewedByInformationToDb(int movieId, int reviewId)
	{
		String sql = "INSERT INTO reviewedBy (movieId, reviewId) VALUES (?,?)";
		
		try {
			jdbcTemplate.update(sql, new Object[] {movieId, reviewId});
			System.out.println("INFO : Successfully added genre information to reviewedBy: movie=" + movieId + ", reviewId=" + reviewId);
		} catch (DuplicateKeyException e) {
			System.err.println("ERROR : Duplicate entry genre information to reviewedBy: movie=" + movieId + ", reviewId=" + reviewId);
		} catch (DataIntegrityViolationException e) {
			System.err.println("ERROR : Data integrity violation entry reviewedBy: movie=" + movieId + ", reviewId=" + reviewId);
		}
	}
	
	
	public void updateCheckedSimilarMoviesStatus(int id, int status)
	{
		String sql = "UPDATE movieIDsToFetch SET checkedSimilarMovies = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	public void updateFetchInformationStatusForRT(int id, int status)
	{
		String sql = "UPDATE movieIDsToFetch SET fetchedInfo = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	public void updateFetchInformationStatusForTMD(int id, int status)
	{
		String sql = "UPDATE tmdMovieIDsToFetch SET fetchedInfo = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	public void updateMovieWithDirectorId(int movieId, int directorId)
	{
		String sql = "UPDATE Movies SET directorId = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {directorId, movieId});
	}
	
	public void updateMovieWithStudioId(int movieId, int studioId)
	{
		String sql = "UPDATE Movies SET studioId = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {studioId, movieId});
	}
}
