/**
 * The InfoHelper object handles the fetching and displaying functionality for
 * the front-end of the application, which is the section that is the most
 * interactive and informative for the user.
 */
function InfoHelper()
{
	// Base URL to display picture IDs from the actors
	this.movieDbBasePictureURL = 'http://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185';
}


/**
 * Result/information display functions [START]
 */

/**
 * Clears the result tiles from the info page.
 */
InfoHelper.prototype.removeResultTiles = function()
{
	$('.rtResultTiles').remove();
};

/**
 * Displays the yearly statistics about the movies in the database.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayYearlyStatistics = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 200*data.length + 75);
	
	tile.append(tileTitle);
	
	$.each(data, function(index, value) {
		var stat = $('<div />').addClass('rtResultSubSection');
		
		var info = $('<div />')
			.css('width', 800 - 185)
			.css('height', 200)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var name = $('<h2 />').text(value.year);
		var sum = $('<p />').text('Sum: ' + objectHandle.formatInDollars(value.sum));
		var average =$('<p />').text('Average: ' +  objectHandle.formatInDollars(value.average));
		var min = $('<p />').text('Minimum: ' + objectHandle.formatInDollars(value.minimum));
		var max = $('<p />').text('Maximum: ' + objectHandle.formatInDollars(value.maximum));
		
		info.append(name);
		info.append(sum);
		info.append(average);
		info.append(min);
		info.append(max);
		
		stat.append(info);
		
		content.append(stat);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Displays the results from the actor search.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayActorSearchResult = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 300*data.length + 30);
	
	tile.append(tileTitle);
	
	if (data.length === 0)
	{
		content.append($('<p />').text('No results found.'));
		tile.append(content);
		$('body').append(tile);
		
		return;
	}
	
	$.each(data, function(index, value) {
		var actor = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', objectHandle.movieDbBasePictureURL + value.pictureURL)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 185)
			.css('height', 300)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var name = $('<h2 />').text(value.name);
		var birthplace = $('<p />').text('Birthplace: ' + value.birthplace);
		var birthday = $('<p />').text('Birthday: ' + value.birthday);
		var deathday = $('<p />').text('Deathday: ' + (value.deathday ? value.deathday : 'N/A'));
		
		info.append(name);
		info.append(birthplace);
		info.append(birthday);
		info.append(deathday);
		
		actor.append(picture);
		actor.append(info);
		
		content.append(actor);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Displays the results from the movie search.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayMovieSearchResult = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 225*data.length + 30);
	
	tile.append(tileTitle);
	
	if (data.length === 0)
	{
		content.append($('<p />').text('No results found.'));
		tile.append(content);
		$('body').append(tile);
		
		return;
	}
	
	$.each(data, function(index, value) {
		var movie = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', value.poster_url)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 120)
			.css('height', 225)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var title = $('<h2 />').text(value.title);
		var year = $('<p />').text('Year: ' + value.year);
		var rating = $('<p />').text('Rating: ' + value.rating);
		var runtime = $('<p />').text('Runtime: ' + value.runtime);
		var budget = $('<p />').text('Budget: ' + objectHandle.formatInDollars(value.budget));
		var revenue = $('<p />').text('Revenue: ' + objectHandle.formatInDollars(value.revenue));
		
		info.append(title);
		info.append(year);
		info.append(rating);
		info.append(runtime);
		info.append(budget);
		info.append(revenue);
		
		movie.append(picture);
		movie.append(info);
		
		content.append(movie);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Displays the results from the rated movies.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayRatedMovieResult = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 225*data.length + 30);
	
	tile.append(tileTitle);
	
	$.each(data, function(index, value) {
		var movie = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', value.poster_url)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 120)
			.css('height', 225)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var title = $('<h2 />').text(value.title);
		var year = $('<p />').text('Year: ' + value.year);
		var score = $('<p />').text('Score: ' + value.rating);
		
		info.append(title);
		info.append(year);
		info.append(score);
		
		movie.append(picture);
		movie.append(info);
		
		content.append(movie);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Dsiplays the list of actors from the movies with the largest revenues.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayActorsFromLargeRevenueMovies = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 300*data.length + 30);
	
	tile.append(tileTitle);
	
	$.each(data, function(index, value) {
		var actor = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', objectHandle.movieDbBasePictureURL + value.pictureURL)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 185)
			.css('height', 300)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var name = $('<h2 />').text(value.name);
		var title = $('<p />').text('Title: ' + value.title);
		var year = $('<p />').text('Year: ' + value.year);
		var revenue = $('<p />').text('Revenue: ' + objectHandle.formatInDollars(value.revenue));
		
		info.append(name);
		info.append(title);
		info.append(year);
		info.append(revenue);
		
		actor.append(picture);
		actor.append(info);
		
		content.append(actor);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Displays the list of movies with the most reviews.
 * @param data Data from the database
 * @param object InfoHelper object
 */
InfoHelper.prototype.displayMostReviewedMovies = function(data, object)
{
	var objectHandle = object;
	
	objectHandle.removeResultTiles();
	
	var tile = $('<div />').addClass('rtMainContent rtMainContentWithTitle rtResultTiles');
	var tileTitle = $('<div />')
			.addClass('sectionTitle')
			.css('width', 90)
			.text('RESULTS');
	var content = $('<div />').addClass('rtContentSection').css('height', 225*data.length + 30);
	
	tile.append(tileTitle);
	
	$.each(data, function(index, value) {
		var movie = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', value.poster_url)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 120)
			.css('height', 225)
			.css('float', 'left')
			.css('margin-bottom', 10);
		
		var title = $('<h2 />').text(value.title);
		var year = $('<p />').text('Year: ' + value.year);
		var nReviews = $('<p />').text('Number of reviews: ' + value.numberOfReviews);
		
		info.append(title);
		info.append(year);
		info.append(nReviews);
		
		movie.append(picture);
		movie.append(info);
		
		content.append(movie);
	});
	
	tile.append(content);
	$('body').append(tile);
};

/**
 * Formats the input amount into the dollar format with the dollar sign and
 * commas, and with two decimal points.
 * @param amount Amount (in dollars)
 * @returns {String} Formatted amount
 */
InfoHelper.prototype.formatInDollars = function(amount)
{
	var n = amount.toFixed(2).split(".");
    
	return "$" + n[0].split("").reverse().reduce(function(acc, num, i, orig) {
        return  num + (i && !(i % 3) ? "," : "") + acc;
    }, "") + "." + n[1];
};

/**
 * Result/information display functions [END]
 */


/**
 * Query functions [START]
 */

/**
 * AJAX call to fetch the top movies by revenue, year and MPAA rating.
 */
InfoHelper.prototype.fetchTopMoviesForYearAndRating = function()
{
	objectHandle = this; 
	
	var year = $('#rtSearchTopRevenueMoviesYear').val();
	var rating = $('#rtSearchTopRevenueMoviesRating').val();
	
	$.ajax({
		type: 'GET',
		url: 'fetchTopMoviesForYearAndRating',
		data: {year: year, rating: rating},
		success: function(data) {
			objectHandle.displayMovieSearchResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch movie statistics for the last 10 years.
 */
InfoHelper.prototype.fetchMovieStatisticsForYear = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchMovieStatisticsForYear',
		success: function(data) {
			objectHandle.displayYearlyStatistics(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch the actors given a location.
 */
InfoHelper.prototype.fetchActorsFrom = function()
{
	objectHandle = this; 
	
	var birthplace = $('#rtSearchActorsByLocationInput').val();
	
	$.ajax({
		type: 'GET',
		url: 'fetchActorsFrom',
		data: {birthplace: birthplace},
		success: function(data) {
			objectHandle.displayActorSearchResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch the worst movies.
 */
InfoHelper.prototype.fetchWorstMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchWorstMovies',
		success: function(data) {
			objectHandle.displayRatedMovieResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch the best movies.
 */
InfoHelper.prototype.fetchBestMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchBestMovies',
		success: function(data) {
			objectHandle.displayRatedMovieResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to search for movies.
 */
InfoHelper.prototype.searchMovies = function()
{
	objectHandle = this; 
	
	var search = $('#rtSearchMoviesInput').val();
	
	$.ajax({
		type: 'GET',
		url: 'searchMovies',
		data: {search: search},
		success: function(data) {
			objectHandle.displayMovieSearchResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to search for actors.
 */
InfoHelper.prototype.searchActors = function()
{
	objectHandle = this; 
	
	var search = $('#rtSearchActorsInput').val();
	
	$.ajax({
		type: 'GET',
		url: 'searchActors',
		data: {search: search},
		success: function(data) {
			objectHandle.displayActorSearchResult(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch the list of actors from movies with the largest revenues.
 */
InfoHelper.prototype.fetchActorsInTopRevenueMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchActorsInTopRevenueMovies',
		success: function(data) {
			objectHandle.displayActorsFromLargeRevenueMovies(data, objectHandle);
		}
	});
};

/**
 * AJAX call to fetch the list of most reviewed movies.
 */
InfoHelper.prototype.fetchMostReviewedMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchMostReviewedMovies',
		success: function(data) {
			objectHandle.displayMostReviewedMovies(data, objectHandle);
		}
	});
};

/**
 * Query functions [END]
 */




