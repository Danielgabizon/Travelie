package org.colman.travelie.features.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.Text
import org.colman.travelie.models.Post
import org.colman.travelie.ui.theme.Navy
@Composable
fun PostCard(post: Post,onCommentClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // Header: Profile image + Username + Location (no follow/3 dots)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (post.userProfileUrl != null) {
                AsyncImage(
                    model = post.userProfileUrl,
                    contentDescription = "User profile",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = post.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                post.locationName?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Image (centered, square, cropped)
        post.imageUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "Post image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // square like Instagram
                contentScale = ContentScale.Crop
            )
        }

        // Post text
        if (post.text.isNotBlank()) {
            Text(
                text = post.text,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 14.sp
            )
        }

        // Likes + Comments line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("❤️ ${post.likedBy.size} לייקים", fontSize = 13.sp)
            Text(
                text = "💬 ${post.commentCount} תגובות",
                fontSize = 13.sp,
                modifier = Modifier.clickable { onCommentClick(post.postId) }
            )
        }


        Spacer(modifier = Modifier.height(8.dp))
    }
}

//@Composable
//fun PostCard(post: post) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                if (post.userProfileUrl != null) {
//                    AsyncImage(
//                        model = post.userProfileUrl,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(CircleShape)
//                    )
//                }
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(post.userName, fontWeight = FontWeight.Bold, color = Navy)
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(post.text)
//
//            post.imageUrl?.let {
//                Spacer(modifier = Modifier.height(8.dp))
//                AsyncImage(
//                    model = it,
//                    contentDescription = "Post image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(180.dp)
//                        .clip(RoundedCornerShape(12.dp)),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}
