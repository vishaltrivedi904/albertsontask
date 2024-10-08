package com.example.albertsontask.ui.profile.composablescreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.albertsontask.R
import com.example.albertsontask.application.AlbertsonApplication.Companion.context
import com.example.albertsontask.data.model.profile.Profile
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.ui.profile.composable.IconTextCard
import com.example.albertsontask.ui.profile.theme.Gray
import com.example.albertsontask.ui.profile.theme.Primary
import com.example.albertsontask.ui.profile.theme.White
import com.example.albertsontask.ui.profile.theme.fontFamily
import java.util.Locale

@Composable
fun ProfileScreen(user: Profile?) {

    val context= LocalContext.current

    user?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .background(Gray),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(data = it.picture)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.ic_placeholder),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp, 100.dp)
                    )

                    Text(
                        text = it.name!!,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = it.email?: stringResource(id = R.string.n_a),
                        fontSize = 16.sp,
                        color =Primary,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Normal,
                        textDecoration= TextDecoration.Underline,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable {
                                if (it.email != null) {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:${it.email}")
                                    }
                                    context.startActivity(
                                        Intent.createChooser(
                                            intent,
                                            "Send Email"
                                        )
                                    )
                                }
                            }
                    )
                }
            }



            IconTextCard(
                icon = painterResource(id = R.drawable.ic_gender),
                text = it.gender.toString().capitalize(Locale.ENGLISH)
            )

            IconTextCard(
                icon = painterResource(id = R.drawable.ic_phone),
                text = it.phone?.replace("-","")!!.replace("(","").replace(")","").replace(" ","")?: stringResource(id = R.string.n_a)
            )

            IconTextCard(
                icon = painterResource(id = R.drawable.ic_address),
                text = it.address!!
            )

        }


    }


}