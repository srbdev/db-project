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
	
	@RequestMapping(value = "/fetchAllMovieIDs", method = RequestMethod.GET)
	public @ResponseBody List<Movie> fetchAllMovieIDs()
	{
		List<Movie> movieIDs = movieDao.fetchAllMovieIDs();
		return movieIDs;
	}
	
	@RequestMapping(value = "/fetchMovieIDsForMining")
	public @ResponseBody List<Movie> fetchRTMovieIDsForMining(@RequestParam(required = true) boolean type)
	{
		List<Movie> movieIDs = type == true ? movieDao.fetchRTMovieIDsForMining() : movieDao.fetchTMDMovieIDsForMining();
		return movieIDs;
	}
	
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
	
	@RequestMapping(value = "/insertMovieInformationToDbFromRT", method = RequestMethod.GET)
	public @ResponseBody boolean insertMovieInformationToDbFromRT(@RequestParam(required = true) int id, @RequestParam(required = true) String title, @RequestParam(required = true) int year, @RequestParam(required = true) int runtime, @RequestParam(required = true) String rating, @RequestParam(required = true) String posterUrl)
	{
		movieDao.insertMovieToDbFromRTData(id, title, year, runtime, rating, posterUrl);
		return true;
	}
	
	@RequestMapping(value = "/insertDirectorInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertDirectorInformationToDb(@RequestParam(required = true) String name, @RequestParam(required = true) int id)
	{
		movieDao.insertDirectorInformationToDb(name);
		
		int directorId = movieDao.fetchIdForDirector(name);
		movieDao.updateMovieWithDirectorId(id, directorId);
		
		return true;
	}
	
	@RequestMapping(value = "/insertStudioInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertStudioInformationToDb(@RequestParam(required = true) String name, @RequestParam(required = true) int id)
	{
		movieDao.insertStudioInformationToDb(name);
		
		int studioId = movieDao.fetchIdForStudio(name);
		movieDao.updateMovieWithStudioId(id, studioId);
		
		return true;
	}
	
	@RequestMapping(value = "/insertCastInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertCastInformationToDb(@RequestParam(required = true) int movieId, @RequestParam(required = true) int actorId, @RequestParam(required = true) String actorName, @RequestParam(required = true) String characters)
	{
		movieDao.insertActorInformationToDb(actorId, actorName);
		movieDao.insertCastInformationToDb(movieId, actorId, characters);
		
		return true;
	}
	
	@RequestMapping(value = "/insertGenreInformationToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertGenreInformationToDb(@RequestParam(required = true) int movieId, @RequestParam(required = true) String type)
	{
		movieDao.insertGenreToDb(type);
		movieDao.insertIsOfGenreInformationToDb(movieId, type);
		
		return true;
	}
	
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
	
	@RequestMapping(value = "/updateActorInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateActorInformationFromTMD(@RequestParam(required = true) String name, String aka, Date birthday, Date deathday, String birthplace, String pictureURL)
	{
		movieDao.updateActorInformationFromTMD(name, aka, birthday, deathday, birthplace, pictureURL);		
		return true;
	}
	
	@RequestMapping(value = "/updateStudioInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateStudioInformationFromTMD(@RequestParam(required = true) String name, String headquarters, String homepage)
	{
		movieDao.updateStudioInformationFromTMD(name, headquarters, homepage);
		return true;
	}
	
	@RequestMapping(value = "/updateMovieInformationFromTMD", method = RequestMethod.POST)
	public @ResponseBody boolean updateMovieInformationFromTMD(@RequestParam(required = true) String title, int revenue, int budget)
	{
		movieDao.updateMovieInformationFromTMD(title, revenue, budget);
		return true;
	}
	
	/**
	 * ACTUAL MINING URLs [END]
	 */
	
	
	@RequestMapping(value = "/updateCheckedSimilarMoviesStatus", method = RequestMethod.POST)
	public @ResponseBody boolean updateCheckedSimilarMoviesStatus(@RequestParam(required = true) int id, @RequestParam(required = true) int status)
	{
		movieDao.updateCheckedSimilarMoviesStatus(id, status);
		return true;
	}
	
	@RequestMapping(value = "/updateFetchedInfoFlag", method = RequestMethod.POST)
	public @ResponseBody boolean updateFetchedInfoFlag(@RequestParam(required = true) int id, @RequestParam(required = true) int status, @RequestParam(required = true) boolean type)
	{
		if (type)
			movieDao.updateFetchInformationStatusForRT(id, status);
		else
			movieDao.updateFetchInformationStatusForTMD(id, status);
		
		return true;
	}
}
