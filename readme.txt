####################################
# Real Estate app for NKU CSC 450  #
# Ethan Perry			   #
# Jack Adkins			   #
# Nolan Schumaker		   #
####################################

App is hosted on AWS and is available at http://tellview.net/real-estate

1) Unauthenticated requests are captured and redirected to the login/register page.

	a) A user can log in with an existing username, or register a new account.

	b) If a user attempts to register an existing account, they are logged in as that account.

	c) All newly registered users are created as regular users, not agents. 

2) Upon login, a user is presented with a home screen.
	
	a) User is greeted with their first name in the upper right corner.

	b) Users can be either agents or regular users. 

		i) If a user is not an agent, the user sees a button in the upper right corner that will allow them 
		   to promote themselves to an agent for testing purposes.

			1) Clicking this button will require the user to log back in.

	c) The user is presented with a table in the center of the screen that displays the listings in the database.

		i) The user can enter a maximum and minimum desired price, to filter the table.

		ii) Each listing in the table is selectable, selecting the listing displays a set of buttons.

			1) Selecting Update Info allows the user to edit the details of the selected listing.

				a) This button is only available to agents

			2) Selecting Upload Image allows the user to upload an image that will be associated with the listing.

				a) This button is only available to agents

				b) Images must be less than 50kb in size, and must be JPEGs.

			3) Selecting Show Image allows the user to view the image that is associated with the listing.

				a) This button is available to agents and users

			4) Selecting Mark Sold allows the user to mark a listing as sold.

				a) This button is only available to agents

				b) If a listing is marked as sold, it will no longer appear in the table.	

	d) User can navigate to the New Listing page via the Nav Bar.

		i) This page allows the user to add a new listing to the database.

		ii) If the user is on this page, the user can navigate back to the home page via the Nav Bar.

	e) User can logout by clicking the logout button in the upper right corner.

		i) This button will navigate the user back to the login page, and invalidate the session.