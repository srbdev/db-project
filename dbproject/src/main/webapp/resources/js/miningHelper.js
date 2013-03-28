function MiningHelper() 
{
	this.apiKey = 'gcdr3dgdxv2zafja7a8tmbby';
	this.baseURL = 'http://api.rottentomatoes.com/api/public/v1.0';
	
	this.tmdApiKey = '4455acfbdecb2317fe15d970ff676214';
	this.tmdBaseURL = 'http://api.themoviedb.org/3';
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

MiningHelper.prototype.fetchTmdLatestMovie = function()
{
	this.fetchTmdMovies('/movie/latest?api_key=' + this.tmdApiKey);
};

MiningHelper.prototype.fetchTmdUpcomingMovies = function()
{
	this.fetchTmdMovies('/movie/upcoming?api_key=' + this.tmdApiKey);
};

MiningHelper.prototype.fetchTmdNowPlayingMovies = function()
{
	this.fetchTmdMovies('/movie/now_playing?api_key=' + this.tmdApiKey);
};

MiningHelper.prototype.fetchTmdPopularMovies = function()
{
	this.fetchTmdMovies('/movie/popular?api_key=' + this.tmdApiKey);
};

MiningHelper.prototype.fetchTmdTopRatedMovies = function()
{
	this.fetchTmdMovies('/movie/top_rated?api_key=' + this.tmdApiKey);
};


MiningHelper.prototype.fetchRTMovieIdsFromDb = function()
{
	var helper = this;

	$.ajax({
		type: 'GET',
		url: '../miner/fetchMovieIDsForMining',
		data: {type: true},
		dataType: 'json', 
		success: function(data) {
			helper.fetchMovieInformationProcess(data, 'rt');
		}
	});
};

MiningHelper.prototype.fetchTMDMovieIdsFromDb = function()
{
	var helper = this;

	$.ajax({
		type: 'GET',
		url: '../miner/fetchMovieIDsForMining',
		data: {type: false},
		dataType: 'json', 
		success: function(data) {
			console.log(helper);
		}
	});
};

MiningHelper.prototype.fetchMovieInformationProcess = function(data, dbType) 
{
	var helper = this;
	var time = 125;

	$.each(data, function(index, value) {
		setTimeout(function() {
			if (dbType === 'rt')
			{
				helper.fetchSingleMovieInformationFromRT(value.id);
				helper.updateFetchedInfoFlag(value.id, 1, true);
			}
			else if (dbType === 'tmd')
			{

			}
		
		}, time);

		time += 125;
	});
};

MiningHelper.prototype.fetchSingleMovieInformationFromRT = function(id)
{
	var query = this.baseURL + '/movies/' + id + '.json?apikey=' + this.apiKey;

	var insertMovieInformationToDb = function(movie) {
		$.ajax({
			type: 'POST',
			url: '',
			data: {},
			success: function(data) {}
		});
	};

	$.ajax({
		type: 'GET',
		url: query,
		dataType: 'jsonp',
		success: function(data) {
			insertMovieInformationToDb(data);
		}
	});
};

MiningHelper.prototype.updateFetchedInfoFlag = function(id, status, dbType)
{
	$.ajax({
		type: 'POST',
		url: '../miner/updateFetchedInfoFlag',
		data: {id: id, status: status, type: dbType},
		success: function(data) {
			
		}
	});
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
					helper.updateCheckedSimilarMoviesStatus(value.id, 1);
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

MiningHelper.prototype.insertTmdMoviesIdsToDb = function(data)
{
	var stringIds = '';

	if (data.results === undefined)
		stringIds = data.id;
	else
	{
		$.each(data.results, function(index, value) {
			stringIds += value.id + ',';
		});
	}
	
	$.ajax({
		type: 'GET',
		url: '../miner/insertTmdMovieIdsToDb',
		data: {data: stringIds},
		success: function(data) {
			
		}
	});
};

MiningHelper.prototype.updateCheckedSimilarMoviesStatus = function(id, status)
{
	$.ajax({
		type: 'POST',
		url: '../miner/updateCheckedSimilarMoviesStatus',
		data: {id: id, status: status},
		success: function(data) {}
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

MiningHelper.prototype.fetchTmdMovies = function(url)
{
	$.ajax({
		type: 'GET',
		url: this.tmdBaseURL + url,
		success: this.insertTmdMoviesIdsToDb
	});
};