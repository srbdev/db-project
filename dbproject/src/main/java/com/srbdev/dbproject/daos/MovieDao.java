package com.srbdev.dbproject.daos;

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
		} catch (DataIntegrityViolationException e)
		{
			System.err.println("ERROR : Data integrity violation enty movie ID " + id + " {" + title + "," + year  + "," + runtime + "," + rating + "}");
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
		} catch (DataIntegrityViolationException e)
		{
			System.err.println("ERROR : Data integrity violation enty movie IDs " + id1 + ", " + id2);
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
}
