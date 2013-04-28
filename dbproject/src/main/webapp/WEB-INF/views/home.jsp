<html>

<head>
	<script type="text/javascript" src="resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="resources/js/infoHelper.js"></script>

	<link href='http://fonts.googleapis.com/css?family=Droid+Sans' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" type="text/css" href="resources/css/dbproject.css">

	<title>Home</title>
</head>

<body>
	<script type="text/javascript">
		var helper;

		$(document).ready(function() {
			helper = new InfoHelper();
		});
	</script>

	<header></header>
	<div class="subheader">
		<div class="subheaderLinks">
			<a id="selectedSubheaderLink" href="/dbproject">Home</a> 
			<a href="/dbproject/miner/home">Miner</a>
		</div>
	</div>

	<div class="rtMainContent rtMainContentWithTitle">
	    <div class="sectionTitle" id="stWelcome">
	      WELCOME
	    </div>
    
	    <div class="rtContentSection">
	      This is the 564 Introduction to Database Management project homepage. If you are starting fresh with an empty database, head over to the <a href="miner/home">miner's</a> page to populate the database. 
	    </div>
  	</div>
  	
  	<div class="rtMainContent rtMainContentWithTitle">
	    <div class="sectionTitle" id="stWelcome">
	   		INFO 	
	    </div>
    
	    <div class="rtContentSection">
	    	You can use this section to learn more information about the movies and actors stored in the database.
	    	
	    	<p>1. Search for movies: 
	    	<input id="rtSearchMoviesInput" class="rtTextInput" type="text" placeholder="i.e. Star Wars"></input> 
	    	<span class="minerButton" onclick="helper.searchMovies()">Search</span></p>
	    	
	    	<p>2. Search for actors: 
	    	<input id="rtSearchActorsInput" class="rtTextInput" type="text" placeholder="i.e. Natalie Portman"></input> 
	    	<span class="minerButton" onclick="helper.searchActors()">Search</span></p>
	    	
	    	<p>3. Search for actors by birthplace: 
	    	<input id="rtSearchActorsByLocationInput" class="rtTextInput" type="text" placeholder="i.e. Los Angeles"></input> 
	    	<span class="minerButton" onclick="helper.fetchActorsFrom()">Search</span></p>
	    	
	    	<p>4. Display top 5 best movies:  
	    	<span class="minerButton" onclick="helper.fetchBestMovies()">Get</span></p>
	    	
	    	<p>5. Display the 5 worst movies:  
	    	<span class="minerButton" onclick="helper.fetchWorstMovies()">Get</span></p>
	    	
	    	<p>6. Search top 5 movies by revenue, year and MPAA rating:
	    	<input id="rtSearchTopRevenueMoviesYear" class="rtTextInput" type="text" placeholder="i.e. 2011"></input>
	    	<input id="rtSearchTopRevenueMoviesRating" class="rtTextInput" type="text" placeholder="i.e. R"></input>  
	    	<span class="minerButton" onclick="helper.fetchTopMoviesForYearAndRating()">Search</span></p>
	    	
	    	<p>7. Display actors in movies with largest revenues:  
	    	<span class="minerButton" onclick="helper.fetchActorsInTopRevenueMovies()">Get</span></p>
	    	
	    	<p>8. Display the 5 most reviewed movies:  
	    	<span class="minerButton" onclick="helper.fetchMostReviewedMovies()">Get</span></p>
	    	
	    	<p>9. Display movie statistics for the last 10 years:  
	    	<span class="minerButton" onclick="helper.fetchMovieStatisticsForYear()">Get</span></p>
	    </div>
  	</div>
</body>
</html>
