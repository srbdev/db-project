package com.srbdev.dbproject.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

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
