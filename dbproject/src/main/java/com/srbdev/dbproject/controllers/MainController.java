package com.srbdev.dbproject.controllers;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.srbdev.dbproject.daos.MovieDao;
import com.srbdev.dbproject.model.Actor;
import com.srbdev.dbproject.model.Infometric;
import com.srbdev.dbproject.model.Movie;

@Controller
public class MainController 
{
//	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	private MovieDao movieDao = (MovieDao) context.getBean("movieDao");
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) 
	{
		return "home";
	}
	
	
	/**
	 * URL to fetch top revenue movies for a given year and rating.
	 * @param year
	 * @param rating MPAA rating
	 * @return List of movies
	 */
	@RequestMapping(value = "/fetchTopMoviesForYearAndRating")
	public @ResponseBody List<Movie> fetchTopMoviesForYearAndRating(@RequestParam(required = true) int year, @RequestParam(required = true) String rating)
	{
		List<Movie> movies = movieDao.fetchTop5RevenueMoviesForYearAndRating(year, rating);
		return movies;
	}
	
	/**
	 * URL to fetch statistics of movies by years.
	 * @return List of results
	 */
	@RequestMapping(value = "/fetchMovieStatisticsForYear")
	public @ResponseBody List<Infometric> fetchMovieStatisticsForYear()
	{
		List<Infometric> results = movieDao.fetchStatisticsForMoviesPerYear();
		return results;
	}
	
	/**
	 * URL to fetch actors from a given birthplace.
	 * @param birthplace
	 * @return List of actors
	 */
	@RequestMapping(value = "/fetchActorsFrom")
	public @ResponseBody List<Actor> fetchActorsFrom(@RequestParam(required = true) String birthplace)
	{
		List<Actor> actors = movieDao.fetchActorsAliveFrom(birthplace);
		return actors;
	}
	
	/**
	 * URL to fetch the worst movies listed in the database.
	 * @return List of movies
	 */
	@RequestMapping(value = "/fetchWorstMovies")
	public @ResponseBody List<Movie> fetchWorstMovies()
	{
		List<Movie> movies = movieDao.fetchTop5WorstMovies();
		return movies;
	}
	
	/**
	 * URL to fetch the best movies listed in the database.
	 * @return List of movies
	 */
	@RequestMapping(value = "/fetchBestMovies")
	public @ResponseBody List<Movie> fetchBestMovies()
	{
		List<Movie> movies = movieDao.fetchTop5BestMovies();
		return movies;
	}
	
	/**
	 * URL to search for a movies matching the search term.
	 * @param search
	 * @return List of movies
	 */
	@RequestMapping(value = "/searchMovies")
	public @ResponseBody List<Movie> searchMovies(@RequestParam(required = true) String search)
	{
		List<Movie> movies = movieDao.searchMovie(search);
		return movies;
	}
	
	/**
	 * URL to search for an actor matching the search term.
	 * @param search
	 * @return List of actors
	 */
	@RequestMapping(value = "/searchActors")
	public @ResponseBody List<Actor> searchActors(@RequestParam(required = true) String search)
	{
		List<Actor> actors = movieDao.searchActor(search);
		return actors;
	}
	
	/**
	 * URL to fetch a list of actors from movies with the best revenues.
	 * @return List of results
	 */
	@RequestMapping(value = "/fetchActorsInTopRevenueMovies")
	public @ResponseBody List<Infometric> fetchActorsInTopRevenueMovies()
	{
		List<Infometric> actors = movieDao.fetchActorsInMoviesWithLargeRevenue();
		return actors;
	}
	
	/**
	 * URL to fetch the most reviewed movies listed in the database.
	 * @return List of movies
	 */
	@RequestMapping(value = "/fetchMostReviewedMovies")
	public @ResponseBody List<Movie> fetchMostReviewedMovies()
	{
		List<Movie> movies = movieDao.fetchTop5MostReviewedMovies();
		return movies;
	}
}
