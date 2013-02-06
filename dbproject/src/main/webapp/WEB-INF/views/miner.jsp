<html>

<head>
	<script type="text/javascript" src="../resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="../resources/js/miningHelper.js"></script>

	<link href='http://fonts.googleapis.com/css?family=Droid+Sans' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" type="text/css" href="../resources/css/dbproject.css">

	<title>Miner</title>
</head>

<body>
	<script type="text/javascript">
		var miner;

		$(document).ready(function() {
			miner = new MiningHelper();
		});
	</script>

	<header>
		Ripe Tomatoes
		
		<p><a href="/dbproject">Home</a> <a href="/dbproject/miner/home">Miner</a></p>
	</header>

	<div class="rtMainContent">
		<div class="rtContentSection">
			<p><h2>Rotten Tomatoes Database</h2></p>
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchBoxOffice()">Get</span> 
			Fetches movies currently at the box office.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchInTheaters()">Get</span>
			Fetches movies currently at the theaters.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchOpeningMovies()">Get</span>
			Fetches movies opening.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchUpcomingMovies()">Get</span>
			Fetches upcoming movies.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTopRentals()">Get</span>
			Fetches top rental movies.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchCurrentRelease()">Get</span>
			Fetches the current release DVDs.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchNewReleaseDVDs()">Get</span>
			Fetches the new release DVDs.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchUpcomingDVDs()">Get</span>
			Fetches the upcoming DVDs.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchSimilarMovies()">Get</span>
			Fetches similar movies from the database.
		</div>
		<div class="rtContentSection">
			<p><h2>The Movie Database</h2></p>
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTmdLatestMovies()">Get</span>
			Fetches latest movies.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTmdUpcomingMovies()">Get</span>
			Fetches upcoming movies.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTmdNowPlayingMovies()">Get</span>
			Fetches movies now playing at the theaters.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTmdPopularMovies()">Get</span>
			Fetches popular movies.
		</div>
		<div class="rtContentSection">
			<span class="minerButton" onclick="miner.fetchTmdTopRatedMovies()">Get</span>
			Fetches top rated movies.
		</div>
	</div>
</body>

</html>