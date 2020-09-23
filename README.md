# BlockBABOS - BlockBuster attribute based ordering system

## MindMap

- Tinder for Movies
- Filtering Categories
- Suggests trailers of movies
- Feature: swipe trailers (right: yep; left: nope, up: add to "to be watched")
- API: https://developers.themoviedb.org/3/movies/get-movie-details



## MVP Minimal Viable Product

- Swipe through movies
  - If like => refine suggestions
  - If dislike => refine suggestions
  - If up => add to "to be watched"
- Select category to get suggestions:
  - Category
    - New
    - Trending
    - Blockbuster
    - Genre:
      - Horror
      - Action
      - ...
- List of movies "to be watched"
  - CRUD 
- Local storage of lists either in JSON file or through local DB
  - e.g. https://docs.couchbase.com/userprofile-couchbase-mobile/standalone/userprofile/android/userprofile_basic.html



# Bewertung

| Aufgabe                                      | Punktzahl | Totale Punktzahl |
| -------------------------------------------- | --------- | ---------------- |
| Einsatz von Kotlin                           | 3         | 9                |
| Verwendung von Webservices                   | 1-3       | 1-3              |
| Funktionalität (pro Funktion / Screen)       | 1-5       | 3-15             |
| Ressourcen: Lokalisierung                    | 1-2       | 1-2              |
| Verwendung von Notifications                 | 1-2       | 1-2              |
| Ressourcen: Unterschiedliche Styles          | 1-2       | 1-2              |
| Integration von Drittkomponenten (Libraries) | 1-3       | 1-3              |
| Verwendung von Jetpack-Komponenten           | 1-3       | 1-3              |
| **TOTAL**                                    |           | **18-39**        |

