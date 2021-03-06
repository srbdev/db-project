/**
 * The MiningHelper object handles the mining functionality for the front-end of
 * the application.
 */
function MiningHelper() 
{
	this.apiKey = 'gcdr3dgdxv2zafja7a8tmbby';
	this.baseURL = 'http://api.rottentomatoes.com/api/public/v1.0';
	
	this.tmdApiKey = '4455acfbdecb2317fe15d970ff676214';
	this.tmdBaseURL = 'http://api.themoviedb.org/3';
}

/**
 * This section contains functions that fetches the movie IDs from the different
 * API calls available to the Rotten Tomatoes movie database and The Movie 
 * Database.
 */
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
/**
 * Section [END]
 */


/**
 * Fetches the list of IDs collected from the Rotten Tomatoes movie database.
 */
MiningHelper.prototype.fetchRTMovieIdsFromDb = function()
{
	var objectHandle = this;

	$.ajax({
		type: 'GET',
		url: '../miner/fetchMovieIDsForMining',
		data: {type: true},
		dataType: 'json', 
		success: function(data) {
			// This is function is called if the list of movie IDs returns 
			// successfully from the db
			objectHandle.fetchMovieInformationProcess(data, 'rt');
		}
	});
};

/**
 * Fetches the list of IDs collected from The Movie Database.
 */
MiningHelper.prototype.fetchTMDMovieIdsFromDb = function()
{
	var objectHandle = this;

	$.ajax({
		type: 'GET',
		url: '../miner/fetchMovieIDsForMining',
		data: {type: false},
		dataType: 'json', 
		success: function(data) {
			// This is function is called if the list of movie IDs returns 
			// successfully from the db
			objectHandle.fetchMovieInformationProcess(data, 'tmd');
		}
	});
};

/**
 * This function handles the list of movie IDs collected and populates the db for
 * the project using the adequate function depending if the IDs come from the
 * Rotten Tomatoes movie database or The Movie Database.
 * @param  {[type]} data   list of movie IDs
 * @param  {[type]} dbType 'rt' for Rotten Tomatoes and 'tmd' for The Movie Database
 */
MiningHelper.prototype.fetchMovieInformationProcess = function(data, dbType) 
{
	var objectHandle = this;
	var time = 500;

	// Loops over each single ID from the inputted list
	$.each(data, function(index, value) {

		// A timeout is included because limited to only 10 queries a second from the
		// open APIs
		setTimeout(function() {
			if (dbType === 'rt')
				objectHandle.fetchSingleMovieInformationFromRT(value.id);
		
		}, time);

		time += 500;
	});
};

/**
 * Loops over potential ID's from The Movie Database and if valid and matches a 
 * movie from the project database, updates its revenue and budget information.
 */
MiningHelper.prototype.fetchSingleMovieInformationFromTMD = function()
{
	var objectHandle = this;
	var count = new Array(200000+1).join('0').split('');
	var time = 200;

	var fetchMovieInformation = function(id) {
		var query = objectHandle.tmdBaseURL + '/movie/' + id + '?api_key=' + objectHandle.tmdApiKey;

		$.ajax({
			type: 'GET',
			url: query,
			dataType: 'jsonp',
			success: function(data) {
				if (data.title)
				{
					var title = data.title;
					var budget = data.budget;
					var revenue = data.revenue;

					$.ajax({
						type: 'POST',
						url: '../miner/updateMovieInformationFromTMD',
						data: {title: title, revenue: revenue, budget: budget},
						success: function(data) {}					
					});
				}
			}
		});
	};


	$.each(count, function(index, value) {
		setTimeout(function() {
			console.log(index);
			fetchMovieInformation(index);
		}, time);

		time += 200;
	});
};

/**
 * Loops over potential ID's from The Movie Database and if valid and matches an
 * actor from the project database, updates its AKA, birthday, deathday, birthplace
 * and picture URL.
 */
MiningHelper.prototype.fetchSingleActorInformationFromTMD = function() 
{
	var objectHandle = this;
	var count = new Array(200000+1).join('0').split('');
	var time = 200;

	var fetchActorInformation = function(id) {
		var query = objectHandle.tmdBaseURL + '/person/' + id + '?api_key=' + objectHandle.tmdApiKey;

		$.ajax({
			type: 'GET',
			url: query,
			dataType: 'jsonp',
			success: function(data) {
				
				if (data.name)
				{
					var name = data.name;
					var aka = data.also_known_as.join() ? data.also_known_as.join() : 'NULL';
					var birthday = data.birthday;
					var deathday = (data.deathday === null || data.deathday.length === 0) ? '9999-01-01' : data.deathday;
					var birthplace = data.place_of_birth ? data.place_of_birth : 'NULL';
					var pictureURL = data.profile_path ? data.profile_path : 'NULL';

					$.ajax({
						type: 'POST',
						url: '../miner/updateActorInformationFromTMD',
						data: {name: name, aka: aka, birthday: birthday, deathday: deathday, birthplace: birthplace, pictureURL: pictureURL},
						success: function(data) {}					
					});
				}
			}
		});
	};


	$.each(count, function(index, value) {
		setTimeout(function() {
			console.log(index);
			fetchActorInformation(index);
		}, time);

		time += 200;
	});
};

/**
 * Loops over potential ID's from The Movie Database and if valid and matches a
 * studio from the project database, updates its headquarter location and
 * homepage URL.
 */
MiningHelper.prototype.fetchSingleStudioInformationFromTMD = function()
{
	var objectHandle = this;
	var count = new Array(200000+1).join('0').split('');
	var time = 200;

	var fetchStudioInformation = function(id) {
		var query = objectHandle.tmdBaseURL + '/company/' + id + '?api_key=' + objectHandle.tmdApiKey;

		$.ajax({
			type: 'GET',
			url: query,
			dataType: 'jsonp',
			success: function(data) {
				if (data.name)
				{
					var name = data.name;
					var headquarters = data.headquarters;
					var homepage = data.homepage;

					$.ajax({
						type: 'POST',
						url: '../miner/updateStudioInformationFromTMD',
						data: {name: name, headquarters: headquarters, homepage: homepage},
						success: function(data) {}					
					});
				}
			}
		});
	};


	$.each(count, function(index, value) {
		setTimeout(function() {
			console.log(index);
			fetchStudioInformation(index);
		}, time);

		time += 200;
	});
};

/**
 * This function handles populating the db for the project from the information
 * from the Rotten Tomatoes movie database.
 * @param  {[type]} id a single movie ID
 */
MiningHelper.prototype.fetchSingleMovieInformationFromRT = function(id)
{
	var objectHandle = this;
	var query = this.baseURL + '/movies/' + id + '.json?apikey=' + this.apiKey;

	/**
	 * This section includes the definition for the functions updating the information for the movies
	 */
	var insertMovieInformationToDb = function(movie) {
		$.ajax({
			type: 'GET',
			url: 'insertMovieInformationToDbFromRT',
			data: {id: movie.id, title: movie.title, year: movie.year, runtime: movie.runtime, rating: movie.mpaa_rating, posterUrl: movie.posters.profile},
			success: function(data) {
				if (data === true)
					// Puts a flag in the list of movie IDs so no need to collect twice and
					// waste the allowed queries.
					objectHandle.updateFetchedInfoFlag(movie.id, 1, true);
			}
		});
	};

	var insertSimilarMoviesInformationToDb = function(movie)
	{
		var query = objectHandle.baseURL + '/movies/' + movie.id + '/similar.json?apikey=' + objectHandle.apiKey + '&limit=5';
		var mId = movie.id;
	
		$.ajax({
			type: 'GET',
			url: query,
			dataType: 'jsonp',
			success: function(data) {
				var dataObj = JSON.parse(JSON.stringify(data));
				var stringIds = '';
				
				$.each(dataObj.movies, function(index, value) {
					stringIds += value.id + ',';
				});
				
				$.ajax({
					type: 'GET',
					url: '../miner/insertSimilarMoviesInformationToDb',
					data: {id: mId, data: stringIds},
					success: function(data) {}
				});
			}
		});
	};

	var insertDirectorInformationToDb = function(movie)
	{
		var director = movie.abridged_directors[0].name;
		var id = movie.id;

		$.ajax({
			type: 'GET',
			url: '../miner/insertDirectorInformationToDb',
			data: {name: director, id: id},
			success: function(data) {}
		});
	};

	var insertStudioInformationToDb = function(movie)
	{
		var studio = movie.studio;
		var id = movie.id;

		$.ajax({
			type: 'GET',
			url: '../miner/insertStudioInformationToDb',
			data: {name: studio, id: id},
			success: function(data) {}
		});
	};

	var insertCastInformationToDb = function(movie)
	{
		var cast = movie.abridged_cast;
		var movieId = movie.id;

		$.each(cast, function(index, value) {

			$.ajax({
				type: 'GET',
				url: '../miner/insertCastInformationToDb',
				data: {movieId: movieId, actorId: value.id, actorName: value.name, characters: value.characters.join()},
				success: function(data) {}
			});

		});
	};

	var insertGenreInformationToDb = function(movie)
	{
		var genres = movie.genres;
		var movieId = movie.id;

		$.each(genres, function(index, value) {

			$.ajax({
				type: 'GET',
				url: '../miner/insertGenreInformationToDb',
				data: {movieId: movieId, type: value},
				success: function(data) {}
			});

		});
	};

	var insertReviewInformationToDb = function(movie)
	{
		var movieId = movie.id;
		var query = objectHandle.baseURL + '/movies/' + movie.id + '/reviews.json?review_type=all&page_limit=20&page=1&country=us&apikey=' + objectHandle.apiKey;

		$.ajax({
			type: 'GET',
			url: query,
			dataType: 'jsonp',
			success: function(data) {
				
				var reviews = data.reviews;

				$.each(reviews, function(index, value) {

					var params = {};
					params.movieId = movieId;
					params.critic = value.critic;
					params.date = value.date;

					if (value.publication) params.publication = value.publication;
					if (value.original_score) params.score = value.original_score;
					if (value.quote) params.quote = value.quote;

					$.ajax({
						type: 'GET',
						url: '../miner/insertReviewInformationToDb',
						data: params,
						success: function(data) {}
					});

				});

			}
		});
	};
	/**
	 * [END]
	 */
	
	// This call to the open API gets the information about a movie by its ID and
	// if returning successfully, it populates the database for the project with
	// all the necessary information.
	$.ajax({
		type: 'GET',
		url: query,
		dataType: 'jsonp',
		success: function(data) {

			// List of functions to update information in the db
			insertMovieInformationToDb(data);
			insertSimilarMoviesInformationToDb(data);
			insertDirectorInformationToDb(data);
			insertStudioInformationToDb(data);
			insertCastInformationToDb(data);
			insertGenreInformationToDb(data);
			insertReviewInformationToDb(data);
		
		}
	});
};

/**
 * Updates the flag when a movie information has been fetched once.
 * @param  {[type]} id     movie ID
 * @param  {[type]} status fetched or unfetched
 * @param  {[type]} dbType Rotten Tomatoes movie database or The Movie Database
 */
MiningHelper.prototype.updateFetchedInfoFlag = function(id, status, dbType)
{
	$.ajax({
		type: 'POST',
		url: '../miner/updateFetchedInfoFlag',
		data: {id: id, status: status, type: dbType},
		success: function(data) {}
	});
};

/**
 * Fetches the similar movies from the Rotten Tomatoes movie database given a
 * list of movie IDs.
 */
MiningHelper.prototype.fetchSimilarMovies = function()
{
	var objectHandle = this;
	
	$.ajax({
		type: 'GET',
		url: '../miner/fetchAllMovieIDs',
		dataType: 'json',
		success: function(data) {
			var time = 125;
			
			$.each(data, function(index, value) {
				// I need to slow down, only 10 calls/sec allowed
				setTimeout(function() {
					objectHandle.fetch5SimilarMoviesFromRottenTomatoes(value.id);
					objectHandle.updateCheckedSimilarMoviesStatus(value.id, 1);
				}, time);
				
				time += 125;
			});
		}
	});
};

/**
 * Fetches 5 similar movies the inputted movie ID.
 * @param  {[type]} id a movie ID
 */
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

/**
 * Given a query to the Rotten Tomatoes movie database, inserts the resulting
 * movie IDs to the project database.
 * @param  {[type]} query a query to the Rotten Tomatoes movie database
 */
MiningHelper.prototype.runInsertMoviesIdsToDbAjax = function(query)
{
	$.ajax({
		type: 'GET',
		url: query,
		dataType: 'jsonp',
		success: this.insertMoviesIdsToDb
	});
};

/**
 * Given a list of movie IDs, inserts them into the project database.
 * @param  {[type]} data list of movie IDs
 */
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
			alert('ID(s) added successfuly to the database.');
		}
	});
};

/**
 * Given a list of movie IDs from The Movie Database, inserts them into the
 * project database.
 * @param  {[type]} data list of movie IDs
 */
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
			alert('ID(s) added successfuly to the database.');
		}
	});
};

/**
 * Updates the flag whether a movie ID was checked or not already.
 * @param  {[type]} id     movie ID
 * @param  {[type]} status 0 or 1
 */
MiningHelper.prototype.updateCheckedSimilarMoviesStatus = function(id, status)
{
	$.ajax({
		type: 'POST',
		url: '../miner/updateCheckedSimilarMoviesStatus',
		data: {id: id, status: status},
		success: function(data) {}
	});
};

/**
 * Given the specific API call and amount from the Rotten Tomatoes movie 
 * database, fetches the list of movie IDs.
 * @param  {[type]} listType API call
 * @param  {[type]} amount   amount of movies to fetch
 */
MiningHelper.prototype.fetchMovieList = function(listType, amount)
{
	var query = this.baseURL + '/lists' + listType + '.json?apikey=' + this.apiKey + '&limit=' + amount + '&country=us';
	this.runInsertMoviesIdsToDbAjax(query);	
};

/**
 * Given the specifiv API call, the amount of movies to fetch, and the number of
 * pages to fetch from the Rotten Tomatoes movie database, fetches the list of
 * movie IDs.
 * @param  {[type]} listType      API call
 * @param  {[type]} amountPerPage number of results per page
 * @param  {[type]} pages         page number
 */
MiningHelper.prototype.fetchMovieListWithPages = function(listType, amountPerPage, pages)
{
	for (var i = 1; i < pages + 1; i++)
	{
		var query = this.baseURL + '/lists' + listType + '.json?apikey=' + this.apiKey + '&page_limit=' + amountPerPage + '&page=' + i + '&country=us';
		this.runInsertMoviesIdsToDbAjax(query); 		
	}
};

/**
 * Performs a call to The Movie Database to fetch movie IDs.
 * @param  {[type]} url URL to the API call
 */
MiningHelper.prototype.fetchTmdMovies = function(url)
{
	$.ajax({
		type: 'GET',
		url: this.tmdBaseURL + url,
		success: this.insertTmdMoviesIdsToDb
	});
};

/**
 * Removes the actors that do not have pictures.
 */
MiningHelper.prototype.removeActorsWithoutPictures = function()
{
	$.ajax({
		type: 'POST',
		url: '../miner/removeActorsWithoutPictures',
		success: function(data) {
			alert('Actors removed successfully.');
		}
	});
};

/**
 * Refactors the deathday of the alive actors.
 */
MiningHelper.prototype.cleanUpDeathdayForAliveActors = function()
{
	$.ajax({
		type: 'POST',
		url: '../miner/cleanUpDeathDayForAliveActors',
		success: function(data) {
			alert('Actors\' deathday updated successfully.');
		}
	});
};
