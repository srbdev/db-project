/**
 * The InfoHelper object handles the fetching and displaying functionality for
 * the front-end of the application, which is the section that is the most
 * interactive and informative for the user.
 */
function InfoHelper()
{
	this.movieDbBasePictureURL = 'http://d3gtl9l2a4fn1j.cloudfront.net/t/p/w185';
}


/**
 * Result/information display functions [START]
 */

InfoHelper.prototype.removeResultTiles = function()
{
	$('.rtResultTiles').remove();
};

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

InfoHelper.prototype.displayMovieSearchResult = function(data, object)
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
		var movie = $('<div />').addClass('rtResultSubSection');
		var picture = $('<img />')
			.attr('src', value.poster_url)
			.css('box-shadow', '0px 1px 3px rgba(0,0,0,0.3)')
			.css('float', 'left');
		
		var info = $('<div />')
			.css('width', 800 - 120)
			.css('height', 300)
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

InfoHelper.prototype.fetchTopMoviesForYearAndRating = function(year, rating)
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchTopMoviesForYearAndRating',
		data: {year: year, rating: rating},
		success: function(data) {
			console.log(data);
		}
	});
};

InfoHelper.prototype.fetchMovieStatisticsForYear = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchMovieStatisticsForYear',
		success: function(data) {
			console.log(data);
		}
	});
};

InfoHelper.prototype.fetchActorsFrom = function(birthplace)
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchActorsFrom',
		data: {birthplace: birthplace},
		success: function(data) {
			console.log(data);
		}
	});
};

InfoHelper.prototype.fetchWorstMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchWorstMovies',
		success: function(data) {
			console.log(data);
		}
	});
};

InfoHelper.prototype.fetchBestMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchBestMovies',
		success: function(data) {
			console.log(data);
		}
	});
};

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

InfoHelper.prototype.fetchActorsInTopRevenueMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchActorsInTopRevenueMovies',
		success: function(data) {
			console.log(data);
		}
	});
};

InfoHelper.prototype.fetchMostReviewedMovies = function()
{
	objectHandle = this; 
	
	$.ajax({
		type: 'GET',
		url: 'fetchMostReviewedMovies',
		success: function(data) {
			console.log(data);
		}
	});
};

/**
 * Query functions [END]
 */




