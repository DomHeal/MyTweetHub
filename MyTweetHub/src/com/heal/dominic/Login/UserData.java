package com.heal.dominic.Login;

import java.net.URL;
import twitter4j.Twitter;

public class UserData{
	private static int FavouriteCnt;
	private static int FollowersCnt;
	private static int FollowingCnt;
	private static URL MiniProfilePic;
	private static String Name;
	private static String Username;
	private static URL ProfilePic;
	private static int TweetCnt;

	public void setFavouriteCnt(int favouriteCnt) {
		FavouriteCnt = favouriteCnt;
	}

	public void setFollowersCnt(int followersCnt) {
		FollowersCnt = followersCnt;
	}

	public void setFollowingCnt(int followingCnt) {
		FollowingCnt = followingCnt;
	}

	public void setMiniProfilePic(URL string) {
		MiniProfilePic = string;
	}

	public void setProfilePic(URL string) {
		ProfilePic = string;
	}

	public void setTweetCount(int statusesCount) {
		TweetCnt = statusesCount;
	}

	public void setTwitter(final Twitter twitter) {
		LoginGUI.twitter = twitter;
	}
	
	public static int getFollowerCnt() {
		return FollowersCnt;
	}

	public static int getFavouriteCn() {
		return FavouriteCnt;
	}

	public static int getFollowingCnt() {
		return FollowingCnt;
	}

	public static URL getProfilePic() {
		return ProfilePic;
	}

	public static int getTweetCnt() {
		return TweetCnt;
	}
	
	public static String getUsername() {
		return Username;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public static String getName() {
		return Name;
	}
	
	public void setUsername(String username) {
		Username = username;
	}

	public static URL getMiniProfilePic() {
		return MiniProfilePic;
	}
}
