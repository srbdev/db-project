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
	
	/**
	 * Given a studio name, returns the ID for the studio.
	 * @param name Name for the movie studio
	 * @return ID for the studio
	 */
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
	
	/**
	 * Given the name of a director, returns the ID for the director.
	 * @param name Name for the director
	 * @return ID for the director
	 */
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
	
	/**
	 * Given the critic name, returns the ID for the critic.
	 * @param critic Name for the critic
	 * @return ID for the critic
	 */
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
	
	/**
	 * Given a reviewer ID and the review date, returns the ID for the review.
	 * @param reviewerId ID for the reviewer
	 * @param reviewDate Date of the review
	 * @return ID for the review
	 */
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
	
	
	/**
	 * Given a flag for either the Rotten Tomatoes movie database or The Movie Database,
	 * inserts the movie ID to the database.
	 * @param id ID for the movie
	 * @param flag Database flag
	 */
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
	
	
	/**
	 * Inserts a movie information from the Rotten Tomatoes movie database.
	 * @param id ID for the movie
	 * @param title Title of the movie
	 * @param year Year of the movie
	 * @param runtime Runtim of the movie
	 * @param rating MPAA rating for the movie
	 * @param posterUrl URL to the poster.
	 */
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
	
	/**
	 * Inserts movie IDs to isSimilar table.
	 * @param id1 movie ID
	 * @param id2 movie ID
	 */
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
	
	/**
	 * Inserts the director information to the database.
	 * @param name Name of the director
	 */
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
	
	/**
	 * Inserts the Studio information to the database.
	 * @param name Name of the studio
	 */
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
	
	/**
	 * Inserts the actor information to the database.
	 * @param id ID of the actor
	 * @param name Name of the actor
	 */
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
	
	/**
	 * Inserts the cast information to the database for a given movie and a given actor.
	 * @param movieId ID for the movie
	 * @param actorId ID for the actor
	 * @param characters List of character(s) for actor
	 */
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
	
	/**
	 * Inserts the genre information to the Genre table.
	 * @param type Genre
	 */
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
	
	/**
	 * Inserts the genre information for a movie.
	 * @param movieId ID for the movie
	 * @param type Genre for the movie
	 */
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
	
	/**
	 * Inserts the reviewer information to the database.
	 * @param critic Name of the critic
	 */
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
	
	/**
	 * Inserts the review information to the database.
	 * @param reviewerId ID for the reviewer
	 * @param reviewDate Date of the review
	 * @param publication Publication
	 * @param score Score for the movie
	 * @param quote Quote from the review
	 */
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
	
	/**
	 * Inserts review and reviewer information to reviewedBy table.
	 * @param movieId Movie ID
	 * @param reviewId Review ID
	 */
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
	
	
	/**
	 * Updates the status of the flag for similar movies checked for a given movie.
	 * @param id Movie ID
	 * @param status 0 or 1
	 */
	public void updateCheckedSimilarMoviesStatus(int id, int status)
	{
		String sql = "UPDATE movieIDsToFetch SET checkedSimilarMovies = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	/**
	 * Updates the fetched information flag for a given movie for the Rotten Tomatoes movie database.
	 * @param id Movie ID
	 * @param status 0 or 1
	 */
	public void updateFetchInformationStatusForRT(int id, int status)
	{
		String sql = "UPDATE movieIDsToFetch SET fetchedInfo = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	/**
	 * Updates the fetched information flag for a given movie for The Movie Database.
	 * @param id Movie ID
	 * @param status 0 or 1
	 */
	public void updateFetchInformationStatusForTMD(int id, int status)
	{
		String sql = "UPDATE tmdMovieIDsToFetch SET fetchedInfo = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {status, id});
	}
	
	/**
	 * Given a movie, updates the director ID for the Movies table.
	 * @param movieId Movie ID
	 * @param directorId ID for the director
	 */
	public void updateMovieWithDirectorId(int movieId, int directorId)
	{
		String sql = "UPDATE Movies SET directorId = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {directorId, movieId});
	}
	
	/**
	 * Given a movie, updates the studio ID for the Movies table.
	 * @param movieId Movie ID
	 * @param studioId ID for the studio
	 */
	public void updateMovieWithStudioId(int movieId, int studioId)
	{
		String sql = "UPDATE Movies SET studioId = ? WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {studioId, movieId});
	}
	
	/**
	 * Updates the actor information from The Movie Database to complement the already collected
	 * information from the Rotten Tomatoes movie database.
	 * @param name Name of the actor
	 * @param aka AKA for the actor
	 * @param birthday Birthday of the actor
	 * @param deathday Deathday for the actor
	 * @param birthplace Birth place for the actor
	 * @param pictureURL URL for the actor's picture
	 */
	public void updateActorInformationFromTMD(String name, String aka, Date birthday, Date deathday, String birthplace, String pictureURL)
	{
		String sql = "UPDATE Actors SET aka = ?, birthday = ?, deathday = ?, birthplace = ?, pictureURL = ? WHERE name = ?";
		jdbcTemplate.update(sql, new Object[] {aka, birthday, deathday, birthplace, pictureURL, name});
		System.out.println("INFO : Updated actor " + name + " with aka=" + aka + ", birthday=" + birthday + ", deathday=" + deathday + ", birthplace=" + birthplace + ", pictureURL=" + pictureURL);
	}
	
	/**
	 * Updates the movie information from The Movie Database to complement the already collected
	 * information from the Rotten Tomatoes movie database.
	 * @param title Title of the movie
	 * @param revenue Revenue for the movie
	 * @param budget Budget for the movie
	 */
	public void updateMovieInformationFromTMD(String title, int revenue, int budget)
	{
		String sql = "UPDATE Movies SET revenue = ?, budget = ? WHERE title = ?";
		jdbcTemplate.update(sql, new Object[] {revenue, budget, title});
		System.out.println("INFO : Updated movie " + title + " with revenue=" + revenue + " and budget=" + budget);
	}
	
	/**
	 * Updates the studio information from The Movie Database to complement the already collected
	 * information from the Rotten Tomatoes movie database.
	 * @param name Name of the studio
	 * @param headquarters Location for the headquarters
	 * @param homepage homepage URL
	 */
	public void updateStudioInformationFromTMD(String name, String headquarters, String homepage)
	{
		String sql = "UPDATE Studios SET headquarters = ?, homepage = ? WHERE name = ?";
		jdbcTemplate.update(sql, new Object[] {headquarters, homepage, name});
		System.out.println("INFO : Updated studio " + name + " with headquarters=" + headquarters + ", homepage=" + homepage);
	}
}
