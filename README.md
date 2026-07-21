
# Anime to Anime Backend
This is the backend repository for the [Anime to Anime](https://sata.li/animetoanime/) web daily game.  
It is a Spring Boot project hosted on Heroku that pulls data from Tenrai (originally Jaikan before the API announced it's EOL in October) and stores data in a PostgreSQL database hosted on Supabase. It sends responses to a [frontend](https://github.com/satasatalight/animetoanime-frontend).

## TODO:
- Stream data from database (requires refactoring the psql models)
- Stream responses to frontend
- Optimize getAnimeStaff to pull voice actors first
- Improve shortest path search
- implement a backup service to jaikan
- implement average score statistic