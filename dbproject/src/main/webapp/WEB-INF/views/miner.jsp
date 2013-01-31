<html>

<head>
	<script type="text/javascript" src="../resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="../resources/js/miningHelper.js"></script>

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

	<div class="appMainDiv">
		<span class="minerButton" onclick="miner.fetchBoxOffice()">Fetch box office</span>
		<span class="minerButton" onclick="miner.fetchInTheaters()">Fetch in theaters</span>
		<span class="minerButton" onclick="miner.fetchOpeningMovies()">Fetch opening movies</span>
		<span class="minerButton" onclick="miner.fetchUpcomingMovies()">Fetch upcoming movies</span>
		<span class="minerButton" onclick="miner.fetchTopRentals()">Fetch top rentals</span>
		<span class="minerButton" onclick="miner.fetchCurrentRelease()">Fetch current release DVDs</span>
		<span class="minerButton" onclick="miner.fetchNewReleaseDVDs()">Fetch new release DVDs</span>
		<span class="minerButton" onclick="miner.fetchUpcomingDVDs()">Fetch upcoming DVDs</span>
		<span class="minerButton" onclick="miner.fetchSimilarMovies()">Fetch similar movies</span>
	</div>
</body>

</html>