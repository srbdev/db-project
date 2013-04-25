<html>

<head>
	<script type="text/javascript" src="resources/js/jquery-1.9.0.js"></script>
	<script type="text/javascript" src="../resources/js/infoHelper.js"></script>

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
	    </div>
  	</div>
</body>
</html>
