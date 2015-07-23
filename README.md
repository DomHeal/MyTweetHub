# MyTweetHub
Java, Twitter REST API, Twitter4j, JMapViewer, BeautyEye Library -This application is a part of a third year project which consisted of integration of the Twitter REST API using Twitter4j Library. Features consisted of a graphical user interface of OAuth 2.0 with basic Twitter functionality. Using the JMapViewer library, a map visualisation can be used to visualise Tweets in a specific location with an algorithm for displaying the relationships between each response from a Tweet source ID.
<p align="center">
<img src="http://i.imgur.com/6aRY6N9.png" /img>
<img src="http://i.imgur.com/mtMLEgb.png" /img>
<img src="http://i.imgur.com/vRGEwzM.png" /img>
</p>

Instructions
------------------------------------
- Run TwitterAppGUI.java
- Authorise Application Through WebBrowser
- Enter and Check OAuth 2.0 Pin
- Interact with Application.

Features
------------------------------------
- Auth 2.0
- Statistics Panel
  - Followers
  - Favourites
  - Following
  - Tweet Count
- Timelines
  - Global
  - Personal
- Top Trend (UK-based)
- Tweet Interaction
  - Post
  - Delete
  - Favourite
  - Retweet
  - Additional Information
- Map Visualisation
  - Specific Tweets in Related Co-ordinates
  - Tweet Relationship from Related Tweet Source

Implementation
---------------------------------
Source Tweet:
``` java
// Get Date from Tweet Source ID
		tweetDate = twitter.showStatus(tweetID).getCreatedAt();
// Save the Coordinates from Source Tweet - Longitude and Langitude
		sourceCoordinate = new Coordinate(twitter.showStatus(tweetID)
		.getGeoLocation().getLatitude(), twitter
		.showStatus(tweetID).getGeoLocation().getLongitude());
// Create Style for Source Tweet - RED
			Style style = new Style(Color.BLACK, Color.RED, null, null);
// Add it to the Map
			map().addMapMarker(
					new MapMarkerDot(null, "Source", sourceCoordinate, style));
```
Response Tweets:
``` java
mentions = twitter.getMentionsTimeline(new Paging().count(100));
for (int i = 0; mentions.size() > i; i++) {
	if (mentions.get(i).getCreatedAt().after(tweetDate) == true 
		&& mentions.get(i).getGeoLocation() != null) {
			tweetCoordinate = new Coordinate(mentions.get(i).getGeoLocation().getLatitude(),
				mentions.get(i).getGeoLocation().getLongitude());
			Style style = new Style(Color.BLACK, Color.YELLOW, null, null);
			map().addMapMarker(new MapMarkerDot(layer, 
				mentions.get(i).getUser().getScreenName()+ ": " 
				+ mentions.get(i).getText(),tweetCoordinate, style));
	}
}
```

Future Implementations
------------------------------------
- Graphical User Interface Updated (Map Frame)
- Grouping Tweet by region
- SQL / Database Implementation

