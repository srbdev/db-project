package com.srbdev.dbproject.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.srbdev.dbproject.model.Movie;

public class MovieDao 
{
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	
	
	public List<Movie> fetchAllMovieIDs()
	{
		String sql = "SELECT id FROM movieIDsToFetch";
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);
			List<Movie> movieIDs = new LinkedList<Movie>();
			ResultSet rs = ps.executeQuery();
			
			while (rs.next())
			{
				Movie m = new Movie();
				m.setId(rs.getInt("id"));
				movieIDs.add(m);
			}
			
			rs.close();
			ps.close();
			
			return movieIDs;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (connection != null)
			{
				try {
					connection.close();
				} catch (SQLException e) {}
			}
		}
	}
}
