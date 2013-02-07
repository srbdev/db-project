package com.srbdev.dbproject.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String miner(Model model) 
	{
		return "miner";
	}
	
	@RequestMapping(value = "/fetchAllMovieIDs", method = RequestMethod.GET)
	public @ResponseBody List<Movie> fetchAllMovieIDs()
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
		MovieDao mDao = (MovieDao) context.getBean("movieDao");
		List<Movie> movieIDs = mDao.fetchAllMovieIDs();
		
		return movieIDs;
	}
	
	@RequestMapping(value = "/insertMovieIdsToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertMovieIdsToDb(@RequestParam(required = true) String data)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
		MovieDao mDao = (MovieDao) context.getBean("movieDao");
		String[] ids = data.split(",");
		
		for (String id : ids)
		{
			if (id.length() > 0)
				mDao.insertMovieIDToDb(Integer.parseInt(id), true);
		}
		
		return true;
	}
	
	@RequestMapping(value = "/insertTmdMovieIdsToDb", method = RequestMethod.GET)
	public @ResponseBody boolean insertTmdMovieIdsToDb(@RequestParam(required = true) String data)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
		MovieDao mDao = (MovieDao) context.getBean("movieDao");
		String[] ids = data.split(",");
		
		for (String id : ids)
		{
			if (id.length() > 0)
				mDao.insertMovieIDToDb(Integer.parseInt(id), false);
		}
		
		return true;
	}
	
	@RequestMapping(value = "/updateCheckedSimilarMoviesStatus", method = RequestMethod.POST)
	public @ResponseBody boolean updateCheckedSimilarMoviesStatus(@RequestParam(required = true) int id, @RequestParam(required = true) int status)
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
		MovieDao mDao = (MovieDao) context.getBean("movieDao");
		
		mDao.updateCheckedSimilarMoviesStatus(id, status);
		
		return true;
	}
}
