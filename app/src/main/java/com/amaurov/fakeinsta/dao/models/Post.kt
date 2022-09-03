package com.amaurov.fakeinsta.dao.models

import com.google.firebase.database.Exclude
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class Post(
    @get:Exclude
    var id: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val picture: String? = null,
    val caption: String? = null,
    val hashtags: HashMap<String, Int> = hashMapOf(),
    val likes: HashMap<String, Int> = hashMapOf(),
    val timeOfPosting: LocalDateTime? = null
) {
    data class Builder(
        private var id: String? = null,
        private var userId: String? = null,
        private var userName: String? = null,
        private var picture: String? = null,
        private var caption: String? = null,
        private var hashtags: HashMap<String, Int> = hashMapOf(),
        private var likes: HashMap<String, Int> = hashMapOf(),
        private var timeOfPosting: LocalDateTime? = LocalDateTime.now()
    ) {
        fun userId(userId: String) = apply { this.userId = userId }
        fun userName(userName: String) = apply { this.userName = userName }
        fun picture(picture: String) = apply { this.picture = picture }
        fun caption(caption: String) = apply { this.caption = caption }
        fun hashtags(hashtags: HashMap<String, Int>) = apply { this.hashtags = hashtags }
        fun timeOfPosting(timeOfPosting: LocalDateTime?) = apply { this.timeOfPosting = timeOfPosting }
        fun build() = Post(id, userId, userName, picture, caption, hashtags, likes, timeOfPosting)
    }
}
