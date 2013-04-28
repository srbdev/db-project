package com.srbdev.dbproject.controllers;

import java.sql.Date;
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
import com.srbdev.dbproject.model.Movie;


@Controller
@RequestMapping(value = "/miner")
public class MinerController 
{
//	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
	private MovieDao movieDao = (MovieDao) context.getBean("movieDao");
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String miner(Model model) 
	{
		return "miner";
	}
	
	/**
	 * URL to fetch all the movie IDs.
	 * @return List of movie IDs
	 */
	@RequestMapping(value = "/fetchAllMovieIDs", method = RequestMethod.GET)
	public @ResponseBody List<Movie> fetchAllMovieIDs()
	{
		List<Movie> movieIDs = movieDao.fetchAllMovieIDs();
		return movieIDs;
	}
	
	/**
	 * URL to fetch the movie IDs for mining.
	 * @param type Database type
	 * @return List of movie IDs
	 */
	@RequestMapping(value = "/fetchMovieIDsForMining")
	public @ResponseBody List<Movie> fetchRTMovieIDsForMining(@RequestParam(required = true) boolean type)
	{
		List<Movie> movieIDs = type == true ? movieDao.fetchRTMovieIDsForMining() : movieDao.fetchTMDMovieIDsForMining();
		return movieIDs;
	}
	
	/**
	 * URL to insert movie IDs from the Rotten Tomatoes movie database to the database.
	 * @param data List of movie IDs
	 * @return true
	 */
	@RequestMapping(value = "/insertMovieIdsToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertMovieIdsToDb(@RequestParam(required = true) String data)
	{
		String[] ids = data.split(",");
		
		for (String id : ids)
		{
			if (id.length() > 0)
				movieDao.insertMovieIDToDb(Integer.parseInt(id), true);
		}
		
		return true;
	}
	
	/**
	 * URL to insert movie IDs from The Movie Database to the database.
	 * @param data List of movie IDs
	 * @return true
	 */
	@RequestMapping(value = "/insertTmdMovieIdsToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertTmdMovieIdsToDb(@RequestParam(required = true) String data)
	{
		String[] ids = data.split(",");
		
		for (String id : ids)
		{
			if (id.length() > 0)
				movieDao.insertMovieIDToDb(Integer.parseInt(id), false);
		}
		
		return true;
	}
	
	
	/**
	 * ACTUAL MINING URLs [START]
	 */
	/**
	 * URL to insert the similar movies information.
	 * @param id movie ID
	 * @param data list of similar movie IDs
	 * @return true
	 */
	@RequestMapping(value = "/insertSimilarMoviesInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertSimilarMoviesInformationToDb(@RequestParam(required = true) int id, @RequestParam(required = true) String data)
	{
		String[] ids = data.split(",");
		
		for (String s : ids)
		{
			if (s.length() > 0)
				movieDao.insertSimilarMovieInformationToDb(id, Integer.parseInt(s));
		}
		
		return true;
	}
	
	/**
	 * URL to insert movie information from the Rotten Tomatoes movie database to the database.
	 * @param id Movie ID
	 * @param title Title for the movie
	 * @param year Year of the movie
	 * @param runtime Runtime for the movie
	 * @param rating MPAA rating for the movie
	 * @param posterUrl Poster URL
	 * @return true
	 */
	@RequestMapping(value = "/insertMovieInformationToDbFromRT", method = RequestMethod.GET)
	public @ResponseBody boolean insertMovieInformationToDbFromRT(@RequestParam(required = true) int id, @RequestParam(required = true) String title, @RequestParam(required = true) int year, @RequestParam(required = true) int runtime, @RequestParam(required = true) String rating, @RequestParam(required = true) String posterUrl)
	{
		movieDao.insertMovieToDbFromRTData(id, title, year, runtime, rating, posterUrl);
		return true;
	}
	
	/**
	 * URL to insert director information to the database.
	 * @param name Name for the director
	 * @param id ID for the director
	 * @return true
	 */
	@RequestMapping(value = "/insertDirectorInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertDirectorInformationToDb(@RequestParam(required = true) String name, @RequestParam(required = true) int id)
	{
		movieDao.insertDirectorInformationToDb(name);
		
		int directorId = movieDao.fetchIdForDirector(name);
		movieDao.updateMovieWithDirectorId(id, directorId);
		
		return true;
	}
	
	/**
	 * URL to insert the studio information to the database.
	 * @param name Name for the studio
	 * @param id ID for the studio
	 * @return true
	 */
	@RequestMapping(value = "/insertStudioInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertStudioInformationToDb(@RequestParam(required = true) String name, @RequestParam(required = true) int id)
	{
		movieDao.insertStudioInformationToDb(name);
		
		int studioId = movieDao.fetchIdForStudio(name);
		movieDao.updateMovieWithStudioId(id, studioId);
		
		return true;
	}
	
	/**
	 * URL to insert cast information to the database.
	 * @param movieId ID for the movie
	 * @param actorId ID for the actor
	 * @param actorName Name of the actor
	 * @param characters Characters for the actor
	 * @return true
	 */
	@RequestMapping(value = "/insertCastInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertCastInformationToDb(@RequestParam(required = true) int movieId, @RequestParam(required = true) int actorId, @RequestParam(required = true) String actorName, @RequestParam(required = true) String characters)
	{
		movieDao.insertActorInformationToDb(actorId, actorName);
		movieDao.insertCastInformationToDb(movieId, actorId, characters);
		
		return true;
	}
	
	/**
	 * URL to insert the genre information to the database.
	 * @param movieId Movie ID
	 * @param type Genre of the movie
	 * @return true
	 */
	@RequestMapping(value = "/insertGenreInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertGenreInformationToDb(@RequestParam(required = true) int movieId, @RequestParam(required = true) String type)
	{
		movieDao.insertGenreToDb(type);
		movieDao.insertIsOfGenreInformationToDb(movieId, type);
		
		return true;
	}
	
	/**
	 * URL to insert the review information for the movie.
	 * @param movieId Movie ID
	 * @param critic Critic's name
	 * @param date Data of the review
	 * @param publication Publication for the review
	 * @param score Score
	 * @param quote Quote from the review
	 * @return true
	 */
	@RequestMapping(value = "/insertReviewInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertReviewInformationToDb(@RequestParam(required = true) int movieId, @RequestParam(required = true) String critic, @RequestParam(required = true) Date date, String publication, String score, String quote)
	{
		movieDao.insertReviewerInformationToDb(critic);
		
		int reviewerId = movieDao.fetchIdForReviewer(critic);
		if (publication == null) publication = "NULL";
		if (score == null) score = "NULL";
		if (quote == null) quote = "NULL";
		movieDao.insertReviewInformationToDb(reviewerId, date, publication, score, quote);
		
		int reviewId = movieDao.fetchIdForReview(reviewerId, date);
		movieDao.insertReviewedByInformationToDb(movieId, reviewId);
		
		return true;
	}
	
	/**
	 * URL to update an actor's information with data from The Movie Database.
	 * @param name Name of the actor
	 * @param aka AKA for the actor
	 * @param birthday Birthday of the actor
	 * @param deathday Deathday of the actor
	 * @param birthplace Birthplace of the actor
	 * @param pictureURL Link the his/her picture
	 * @return true
	 */
	@RequestMapping(value = "/updateActorInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateActorInformationFromTMD(@RequestParam(required = true) String name, String aka, Date birthday, Date deathday, String birthplace, String pictureURL)
	{
		movieDao.updateActorInformationFromTMD(name, aka, birthday, deathday, birthplace, pictureURL);		
		return true;
	}
	
	/**
	 * URL to update a studio's information with data from The Movie Database.
	 * @param name Name of the studio
	 * @param headquarters Headquarters foe the studio
	 * @param homepage URL for the studio's homepage
	 * @return true
	 */
	@RequestMapping(value = "/updateStudioInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateStudioInformationFromTMD(@RequestParam(required = true) String name, String headquarters, String homepage)
	{
		movieDao.updateStudioInformationFromTMD(name, headquarters, homepage);
		return true;
	}
	
	/**
	 * URL to update a movie's information with data from The Movie Databse.
	 * @param title Movie title
	 * @param revenue Revenue for the movie
	 * @param budget Budget for the movie
	 * @return true
	 */
	@RequestMapping(value = "/updateMovieInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateMovieInformationFromTMD(@RequestParam(required = true) String title, int revenue, int budget)
	{
		movieDao.updateMovieInformationFromTMD(title, revenue, budget);
		return true;
	}
	
	/**
	 * ACTUAL MINING URLs [END]
	 */
	
	
	/**
	 * URL to update the flag for checked similar movies.
	 * @param id Movie ID
	 * @param status 0 or 1
	 * @return true
	 */
	@RequestMapping(value = "/updateCheckedSimilarMoviesStatus", method = RequestMethod.POST)
	public @ResponseBody boolean updateCheckedSimilarMoviesStatus(@RequestParam(required = true) int id, @RequestParam(required = true) int status)
	{
		movieDao.updateCheckedSimilarMoviesStatus(id, status);
		return true;
	}
	
	/**
	 * URL to update the flag for fetched information
	 * @param id Movie ID
	 * @param status 0 or 1
	 * @param type Database type
	 * @return true
	 */
	@RequestMapping(value = "/updateFetchedInfoFlag", method = RequestMethod.POST)
	public @ResponseBody boolean updateFetchedInfoFlag(@RequestParam(required = true) int id, @RequestParam(required = true) int status, @RequestParam(required = true) boolean type)
	{
		if (type)
			movieDao.updateFetchInformationStatusForRT(id, status);
		else
			movieDao.updateFetchInformationStatusForTMD(id, status);
		
		return true;
	}
	
	/**
	 * URL to remove the actors that do not have pictures.
	 * @return true
	 */
	@RequestMapping(value = "/removeActorsWithoutPictures", method = RequestMethod.POST)
	public @ResponseBody boolean removeActorsWithoutPictures()
	{
		movieDao.removeActorsWithoutPictures();
		return true;
	}
	
	/**
	 * URL to replace the deathday of alive actors from "9999-01-01" to NULL.
	 * @return true
	 */
	@RequestMapping(value = "/cleanUpDeathDayForAliveActors", method = RequestMethod.POST)
	public @ResponseBody boolean cleanUpDeathdayForAliveActors()
	{
		movieDao.cleanUpDeathdayForAliveActors();
		return true;
	}
}
