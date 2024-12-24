package com.example.pr26_novgorodtseva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pr26_novgorodtseva.ui.theme.Pr26_novgorodtsevaTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pr26_novgorodtsevaTheme {
                WalletApp()
            }
        }
    }
}
@Composable
fun WalletApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "wallet") {
        composable("wallet") {
            WalletScreen(
                onCardClick = { navController.navigate("payment_method") },
                onTrackClick = { navController.navigate("tracking_package") },
                onWalletClick = { },
                onProfileClick = {  }
            )
        }
        composable("payment_method") {
            PaymentMethodScreen(
                navController = navController,
                onProceed = { navController.popBackStack() }
            )
        }
        composable("tracking_package") {
            TrackingPackageScreen(navController = navController)
        }
        composable("loading_screen") {
            SuccessScreen(navController = navController)
        }
        composable("success_screen") {
            SuccessScreen(navController = navController)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletScreen(
    onCardClick: () -> Unit,
    onTrackClick: () -> Unit,
    onWalletClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Wallet", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF007BFF)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onTrackClick = onTrackClick,
                onWalletClick = onWalletClick,
                onProfileClick = onProfileClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF3F4F6))
        ) {
            UserProfileSection()
            Spacer(modifier = Modifier.height(20.dp))
            TopUpSection(onCardClick = onCardClick)
            Spacer(modifier = Modifier.height(20.dp))
            TransactionHistorySection()
        }
    }
}
@Composable
fun UserProfileSection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ico_face),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.Gray, CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Hello, Florian!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Current balance: ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "₽10,712.00",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF007AFF)
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "Toggle Balance Visibility",
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
        }
    }
}
@Composable
fun TransactionHistorySection() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Transaction History",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(
                    listOf(
                        Transaction("-₽3,000.00", "Delivery fee", "July 7, 2022"),
                        Transaction("₽2,000.00", "Lalalalala", "July 4, 2022"),
                        Transaction("-₽3,000.00", "Third Delivery", "July 1, 2022"),
                        Transaction("₽1,000.00", "Another One", "June 27, 2022"),
                        Transaction("₽2,500.00", "Experts Are The Best", "June 23, 2022"),
                        Transaction("-₽3,000.00", "Delivery fee", "June 17, 2022")
                    )
                ) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = MaterialTheme.shapes.small,
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}
@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = transaction.amount,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (transaction.amount.startsWith("-")) Color.Red else Color(0xFF00A86B)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.description,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = transaction.date,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}
@Composable
fun TopUpSection(onCardClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopUpOption(iconRes = R.drawable.ic_bank, label = "Bank", onClick = {})
                TopUpOption(iconRes = R.drawable.ic_transfer, label = "Transfer", onClick = {})
                TopUpOption(iconRes = R.drawable.ic_card, label = "Card", onClick = onCardClick)
            }
        }
    }
}

@Composable
fun TopUpOption(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(40.dp)
                .background(Color.Blue, CircleShape)
                .padding(8.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}
@Composable
fun BottomNavigationBar(onTrackClick: () -> Unit, onWalletClick: () -> Unit, onProfileClick: () -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ico_home),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Home", fontSize = 12.sp) }
        )

        NavigationBarItem(
            selected = false,
            onClick = onWalletClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ico_wallet),
                    contentDescription = "Wallet",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Blue
                )
            },
            label = { Text(text = "Wallet", fontSize = 12.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onTrackClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ico_track),
                    contentDescription = "Track",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Track", fontSize = 12.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ico_profile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp)
                )
            },
            label = { Text(text = "Profile", fontSize = 12.sp) }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    navController: NavController,
    onProceed: () -> Unit,
    viewModel: PaymentMethodViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val selectedPaymentMethod = remember { mutableStateOf("wallet") }
    val selectedCard = remember { mutableStateOf<String?>(null) }
    val cards = viewModel.cards.collectAsState(initial = emptyList())
    LaunchedEffect(cards.value) {
        if (cards.value.isNotEmpty() && selectedCard.value == null) {
            selectedCard.value = cards.value.first()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Payment Method", color = Color.Black, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button(
                onClick = onProceed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text("Proceed to pay", color = Color.White, fontSize = 16.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            PaymentOption(
                title = "Pay with wallet",
                description = "Complete the payment using your e-wallet",
                isSelected = selectedPaymentMethod.value == "wallet",
                onSelected = { selectedPaymentMethod.value = "wallet" }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            PaymentOption(
                title = "Credit / debit card",
                description = "Complete the payment using your debit card",
                isSelected = selectedPaymentMethod.value == "card",
                onSelected = { selectedPaymentMethod.value = "card" }
            )

            if (selectedPaymentMethod.value == "card") {
                Spacer(modifier = Modifier.height(16.dp))
                CardPanel(
                    cards = cards.value,
                    selectedCard = selectedCard.value,
                    onSelectCard = { selectedCard.value = it },
                    onRemoveCard = { card -> viewModel.removeCard(card) }
                )
            }
        }
    }
}
@Composable
fun PaymentOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelected
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 16.sp)
            Text(description, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
@Composable
fun CardPanel(cards: List<String>, selectedCard: String?, onSelectCard: (String) -> Unit, onRemoveCard: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        cards.forEach { card ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shadow(4.dp, shape = MaterialTheme.shapes.medium),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCard == card,
                        onClick = { onSelectCard(card) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = card,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onRemoveCard(card) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Card", tint = Color.Red)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingPackageScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Package", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("wallet") {
                            popUpTo("wallet") { inclusive = false }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF007BFF))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF3F4F6))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                GoogleMapView(modifier = Modifier.height(250.dp))
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Package is on the way!",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Tracking Number: R-7458-4567-4434-5854",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Package Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                StatusItemWithLines(
                    status = "Courier requested",
                    time = "July 7 2022  08:00am",
                    checked = true
                )
                StatusItemWithLines(
                    status = "Package ready for delivery",
                    time = "July 7 2022  08:30am",
                    checked = true
                )
                StatusItemWithLines(
                    status = "Package in transit",
                    time = "July 7 2022  10:30am",
                    checked = true
                )
                StatusItemWithLines(
                    status = "Package delivered",
                    time = "July 7 2022  03:30am",
                    checked = true
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        navController.navigate("loading_screen")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                ) {
                    Text(text = "Successful", color = Color.White)
                }
            }
        }
    }
}
@Composable
fun StatusItemWithLines(status: String, time: String, checked: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(uncheckedColor = Color.Gray)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = status, fontSize = 14.sp)
            Text(text = time, fontSize = 12.sp, color = Color.Gray)
        }
    }
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(
            color = Color.Gray,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 1f
        )
    }
}
@Composable
fun GoogleMapView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val googleMap = remember { mutableStateOf<GoogleMap?>(null) }

    DisposableEffect(context) {
        mapView.onCreate(null)
        onDispose {
            mapView.onDestroy()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { mapView ->
                mapView.getMapAsync { map ->
                    googleMap.value = map
                    val marker1 = LatLng(55.0084, 82.9357)
                    val marker2 = LatLng(55.0302, 82.9200)
                    val markerOptions1 = MarkerOptions().position(marker1).title("Marker 1")
                    val markerOptions2 = MarkerOptions().position(marker2).title("Marker 2")
                    map.addMarker(markerOptions1)
                    map.addMarker(markerOptions2)
                    val polylineOptions = PolylineOptions().add(marker1, marker2).color(Color.Red.toArgb())
                    map.addPolyline(polylineOptions)
                    googleMap.value?.moveCamera(CameraUpdateFactory.newLatLngZoom(marker1, 10f))
                }
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(navController: NavController) {
    val showContent = remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        delay(3000)
        showContent.value = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Success", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF007BFF))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF3F4F6)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!showContent.value) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    color = Color(0xFFFFA500),
                    strokeWidth = 6.dp
                )
            }

            if (showContent.value) {
                Image(
                    painter = painterResource(id = R.drawable.ico_success),
                    contentDescription = "Success",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .shadow(4.dp, CircleShape, clip = true)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Delivery Successfully!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007BFF)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your item has been delivered successfully",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Rate Rider",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF007BFF)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                repeat(5) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable(onClick = {  })
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ico_add),
                    contentDescription = "Add Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                        .graphicsLayer {
                            scaleX = if (showContent.value) 1.2f else 1f
                            scaleY = if (showContent.value) 1.2f else 1f
                        }
                )
                Text(
                    text = "Add feedback",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("tracking_package") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Done",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
class PaymentMethodViewModel : ViewModel() {
    private val _cards = MutableStateFlow(listOf("**** **** 3323", "**** **** 1547"))
    val cards: StateFlow<List<String>> = _cards
    fun removeCard(cardNumber: String) {
        _cards.value = _cards.value.filter { it != cardNumber }
    }
}
data class Transaction(val amount: String, val description: String, val date: String)