📚 Cartzy - Bookstore Android App

Cartzy is a sleek and modern bookstore Android app built with Jetpack Compose and powered by the OpenLibrary APIs. It offers a smooth browsing experience, clean UI design, and rich book discovery features.

✨ Key Features

🔎 Search bar to find books by title or author

📚 Book Categories:

Best Selling Books

Editor's Choice

Motivational & Self-Help

Novels (All Books)

🖼️ Book cover images from OpenLibrary

⭐ Ratings and author info (where available)

💬 Collapsible details on each book card

📥 Favorites list for quick access (upcoming)

💡 Recommended books (upcoming)

⚙️ Built with clean MVVM architecture

🚀 Fast UI performance using Material 3 & Compose

🧰 Tech Stack

Kotlin, Jetpack Compose, Material 3

Retrofit, OkHttp, Gson, Coil

Coroutines for async operations

OpenLibrary APIs for book data

🌐 APIs Used

Search: https://openlibrary.org/search.json?q=

Work Details: https://openlibrary.org/works/{work_id}.json

Ratings: https://openlibrary.org/works/{work_id}/ratings.json

User Reading List: https://openlibrary.org/people/{username}/books/already-read.json

Subjects: https://openlibrary.org/subjects/{subject}.json

Author Details: https://openlibrary.org/authors/{author_id}.json

Cover Images: https://covers.openlibrary.org/b/id/{cover_id}-M.jpg

Recent Changes (Trending): https://openlibrary.org/recentchanges.json

Subjects used: bestsellers, motivational, fiction, novels, self_help

🗂 Project Structure (Simplified)

com.example.cartzy
├── data (models, repositories, API services)
├── u_i.home (screens per category)
├── navigation (bottom nav setup)
└── MainActivity.kt

🙋‍♂️ Developer

Swadhin DwibedyLinkedIn

📄 License

MIT License
