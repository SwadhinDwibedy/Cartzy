📚 Cartzy - Bookstore Android App

Cartzy is a modern bookstore app built with Jetpack Compose, powered by OpenLibrary APIs. It provides beautifully organized book sections, including search functionality and smooth UI experiences.

✨ Key Features

🔎 Search bar to find books by title or author
🖼️ Book cover images from OpenLibrary
⭐ Ratings and author info (where available)
💬 Collapsible details on each book card
📥 Favorites list for quick access (upcoming)
💡 Recommended books (upcoming)
⚙️ Built with clean MVVM architecture
🚀 Fast UI performance using Material 3 & Compose

📚 Categories:
Best Selling Books
Editor's Choice
Motivational & Self-Help
Novels (All Books)
🖼️ Book cover images via OpenLibrary
⚙️ MVVM architecture with Repository pattern
🚀 Fast and smooth UI with Material 3 & Compose

🧰 Tech Stack
Kotlin, Jetpack Compose, Material 3
Retrofit + Gson, OkHttp, Coil
Coroutines for background tasks
OpenLibrary APIs for book data

🌐 APIs Used
Search: https://openlibrary.org/search.json?q=
Work Details: https://openlibrary.org/works/{work_id}.json
Ratings: https://openlibrary.org/works/{work_id}/ratings.json
User Reading List: https://openlibrary.org/people/{username}/books/already-read.json
Subject Listings: https://openlibrary.org/subjects/{subject}.json
Author Details: https://openlibrary.org/authors/{author_id}.json
Covers: https://covers.openlibrary.org/b/id/{cover_id}-M.jpg
Trending: https://openlibrary.org/recentchanges.json
Subjects used: bestsellers, motivational, fiction, novels, self_help

🗂 Project Structure (Simplified)

com.example.cartzy
├── data (models, repositories, API)
├── u_i.home (screens)
├── navigation (bottom nav)
└── MainActivity.kt

🙋‍♂️ Developer
Swadhin Dwibedy

📄 License

MIT License
