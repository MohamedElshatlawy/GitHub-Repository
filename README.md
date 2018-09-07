# GitHub-Repository
this app is created to get all repos belongs to team square through is app you can goto a specific repo or the owner of it
-----
Steps:

1. Initiate a new Android app.

2. Request the GitHub API to show Square's public repositories[1] and parse the JSON
   response.

3. Display a list of repositories, each list item should show
    - repo name
    - description
    - username of the repository owner

4. Request only 10 repos at a time. Use infinite scrolling to load more repos when user reaches end of the list. Check page ination documentation[2].

5. Cache the repos list locally on the device.

6. Show a light green background if the `fork` flag is false or missing, a white one
   otherwise.
7. On a long click on a list item show a dialog to ask if go to repository `html_url` or
   owner `html_url` which is opened then in the browser.

8. Implement swipe-to-refresh, that will clear the cache and request fresh data from the API.

Additional notes
----------------
- If your API request limit exceeds, you can generate and use a personal access token [here](https://github.com/settings/applications) and add `?access_token=<YOUR_ACCESS_TOKEN>` to the request URLs.

  [1]: https://api.github.com/users/square/repos
  [2]: https://developer.github.com/v3/#pagination
