function MiningHelper() 
{
	this.apiKey = 'gcdr3dgdxv2zafja7a8tmbby';
	this.baseURL = 'http://api.rottentomatoes.com/api/public/v1.0';
}


MiningHelper.prototype.fetchBoxOffice = function() 
{
	this.fetchMovieList('/movies/box_office', 22);
};

MiningHelper.prototype.fetchInTheaters = function()
{
	this.fetchMovieListWithPages('/movies/in_theaters', 22, 3);
};

MiningHelper.prototype.fetchOpeningMovies = function()
{
	this.fetchMovieList('/movies/opening', 50);
};

MiningHelper.prototype.fetchUpcomingMovies = function()
{
	this.fetchMovieListWithPages('/movies/upcoming', 50, 1);
};

MiningHelper.prototype.fetchTopRentals = function()
{
	this.fetchMovieList('/dvds/top_rentals', 21);
};

MiningHelper.prototype.fetchCurrentRelease = function()
{
	this.fetchMovieListWithPages('/dvds/current_releases', 22, 1);
};

MiningHelper.prototype.fetchNewReleaseDVDs = function()
{
	this.fetchMovieListWithPages('/dvds/new_releases', 22, 1);
};

MiningHelper.prototype.fetchUpcomingDVDs = function()
{
	this.fetchMovieListWithPages('/dvds/upcoming', 50, 1);
};


MiningHelper.prototype.fetchSimilarMovies = function()
{
	var helper = this;
	
	$.ajax({
		type: 'GET',
		url: '../miner/fetchAllMovieIDs',
		dataType: 'json',
		success: function(data) {
			var time = 125;
			
			$.each(data, function(index, value) {
				// I need to slow down, only 10 calls/sec allowed
				setTimeout(function() {
					helper.fetch5SimilarMoviesFromRottenTomatoes(value.id);
				}, time);
				
				time += 125;
			});
		}
	});
};

MiningHelper.prototype.fetch5SimilarMoviesFromRottenTomatoes = function(id)
{
	var query = this.baseURL + '/movies/' + id + '/similar.json?apikey=' + this.apiKey + '&limit=5';
	
	$.ajax({
		type: 'GET',
		url: query,
		dataType: 'jsonp',
		success: this.insertMoviesIdsToDb
	});
};

MiningHelper.prototype.runInsertMoviesIdsToDbAjax = function(query)
{
	$.ajax({
		type: 'GET',
		url: query,
		dataType: 'jsonp',
		success: this.insertMoviesIdsToDb
	});
};

MiningHelper.prototype.insertMoviesIdsToDb = function(data)
{
	var dataObj = JSON.parse(JSON.stringify(data));
	var stringIds = '';
	
	$.each(dataObj.movies, function(index, value) {
		stringIds += value.id + ',';
	});
	
	$.ajax({
		type: 'GET',
		url: '../miner/insertMovieIdsToDb',
		data: {data: stringIds},
		success: function(data) {
			
		}
	});
};

MiningHelper.prototype.fetchMovieList = function(listType, amount)
{
	var query = this.baseURL + '/lists' + listType + '.json?apikey=' + this.apiKey + '&limit=' + amount + '&country=us';
	this.runInsertMoviesIdsToDbAjax(query);	
};

MiningHelper.prototype.fetchMovieListWithPages = function(listType, amountPerPage, pages)
{
	for (var i = 1; i < pages + 1; i++)
	{
		var query = this.baseURL + '/lists' + listType + '.json?apikey=' + this.apiKey + '&page_limit=' + amountPerPage + '&page=' + i + '&country=us';
		this.runInsertMoviesIdsToDbAjax(query); 		
	}
};