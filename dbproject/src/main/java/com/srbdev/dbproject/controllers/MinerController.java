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
		logger.info("miner page.");
		
		return "miner";
	}
	
	@RequestMapping(value = "/fetchAllMovieIDs", method = RequestMethod.GET)
	public @ResponseBody List<Movie> fetchAllMovieIDs()
	{
		logger.info("Ajax call to fetchAllMovieIDs.");
		
		ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
		MovieDao mDao = (MovieDao) context.getBean("movieDao");
		List<Movie> movieIDs = mDao.fetchAllMovieIDs();
		
		return movieIDs;
	}
}
