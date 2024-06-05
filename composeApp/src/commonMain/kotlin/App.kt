import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kdbar.composeapp.generated.resources.Res
import kdbar.composeapp.generated.resources.baseline_local_bar_24
import kdbar.composeapp.generated.resources.baseline_sports_bar_24
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Brush.linearGradient(colors = listOf(themeBlue, peach)))
        ) {
            NavHost(navController = navController, startDestination = Navigation.Main.route) {
                composable(Navigation.Main.route) {
                    MainScreen(navController)
                }
                composable(Navigation.Highball.route) {
                    HighballScreen()
                }
                composable(Navigation.CockTail.route) {
                    CocktailScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val list = CocktailList()
    val initNum =
        if (Int.MAX_VALUE / 2 % 4 == 0) Int.MAX_VALUE / 2 else if ((Int.MAX_VALUE / 2 + 1) % 4 == 0) Int.MAX_VALUE / 2 + 1 else if ((Int.MAX_VALUE / 2 + 2) % 4 == 0) Int.MAX_VALUE / 2 + 2 else Int.MAX_VALUE / 2 + 3
    val pagerState = rememberPagerState(pageCount = {
        Int.MAX_VALUE
    }, initialPage = initNum)

    val currentCocktail = remember { mutableStateOf<Cocktail?>(null) }

    val showDialog = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Type(pagerState = pagerState, navController = navController)
        HorizontalPager(
            modifier = Modifier.fillMaxSize().clip(
                RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            ).background(color = Color.White), state = pagerState
        ) { page ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(25.dp))
                }
                val lis = when (page % 4) {
                    0 -> list.strongList
                    1 -> list.sparklingList
                    2 -> list.milkyList
                    else -> list.ssList
                }

                items(lis.size) {
                    Menu(lis[it]) { cocktail ->
                        currentCocktail.value = cocktail
                        showDialog.value = true
                    }
                }

            }
        }
    }
    if (showDialog.value) {
        infoDialog(cocktail = currentCocktail.value, onDismiss = { showDialog.value = false })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Menu(cocktail: Cocktail, onClick: (Cocktail) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 8.dp),
        shape = CircleShape,
        elevation = 5.dp,
        onClick = {
            onClick.invoke(cocktail)
        }
    ) {
        Box(
            modifier = Modifier.background(Brush.horizontalGradient(colorStops = cocktail.color))
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier.clip(CircleShape)
                    .background(color = Color.Black).padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(
                        modifier = Modifier.clip(CircleShape).size(32.dp)
                            .background(Brush.sweepGradient(colorStops = cocktail.color))
                    )

                    Spacer(
                        modifier = Modifier.width(24.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Row {
                            Text(
                                cocktail.name,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            if (cocktail.comment.isNotEmpty()) {
                                Text(
                                    " (${cocktail.comment.size})",
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        /* Row(modifier = Modifier.fillMaxWidth()) {
                             Column(modifier = Modifier.weight(1f)) {
                                 Text(
                                     "맛",
                                     fontSize = 8.sp,
                                     color = Color.White,
                                     fontWeight = FontWeight.Medium
                                 )
                                 Text(
                                     cocktail.flavor,
                                     fontSize = 10.sp,
                                     fontWeight = FontWeight.SemiBold,
                                     color = Color.White,
                                 )
                             }
                             Column(modifier = Modifier.weight(1f)) {
                                 Text(
                                     "난이도",
                                     fontSize = 8.sp,
                                     color = Color.White,
                                     fontWeight = FontWeight.Medium
                                 )
                                 Text(
                                     cocktail.level,
                                     fontSize = 10.sp,
                                     fontWeight = FontWeight.SemiBold,
                                     color = Color.White,
                                 )
                             }
                             Column(modifier = Modifier.weight(1f)) {
                                 Text(
                                     "도수",
                                     fontSize = 8.sp,
                                     color = Color.White,
                                     fontWeight = FontWeight.Medium
                                 )
                                 Text(
                                     "${cocktail.proof}%",
                                     fontSize = 10.sp,
                                     fontWeight = FontWeight.SemiBold,
                                     color = Color.White,
                                 )
                             }
                         }*/
                    }
                }
            }
            if (!cocktail.visible) {
                Box(
                    modifier = Modifier.width(96.dp).height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush = Brush.linearGradient(colors = listOf(themeBlue, peach)))
                        .align(Alignment.CenterEnd)
                ) {
                    Text(
                        "재고없음",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun Type(pagerState: PagerState, navController: NavHostController) {
    Column(
        Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "KDBAR", style = TextStyle(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 50.sp,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, peach)
                ),
                letterSpacing = 0.sp,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            ScreenButton(title = "하이볼 만들기", icon = Res.drawable.baseline_sports_bar_24) {
                navController.navigate(Navigation.Highball.route)
            }
            Spacer(modifier = Modifier.width(12.dp))
            ScreenButton(title = "칵테일 추천", icon = Res.drawable.baseline_local_bar_24) {
                navController.navigate(Navigation.CockTail.route)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth().height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) { iteration ->
                val color =
                    animateColorAsState(
                        targetValue = if ((pagerState.currentPage % 4) == iteration) peach else Color.White,
                        animationSpec = tween(
                            1000
                        )
                    )

                val size =
                    animateFloatAsState(targetValue = if ((pagerState.currentPage % 4) == iteration) 25f else 8f)

                val weight =
                    if ((pagerState.currentPage % 4) == iteration) FontWeight.ExtraBold else FontWeight.Medium
                val text = when (iteration) {
                    0 -> "STRONG"
                    1 -> "SPARKLING"
                    2 -> "MILKY"
                    else -> "SWEET&SOUR"
                }
                Text(
                    text,
                    color = color.value,
                    fontSize = (size.value).sp,
                    fontWeight = weight
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalResourceApi::class)
@Composable
fun ScreenButton(title: String, icon: DrawableResource, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(80.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 12.dp,
        onClick = {
            onClick.invoke()
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = peach
            )
            Text(
                title, style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    brush = Brush.linearGradient(
                        colors = listOf(themeBlue, peach)
                    ),
                    letterSpacing = 0.sp
                )
            )
        }
    }
}

@Composable
fun infoDialog(cocktail: Cocktail?, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss.invoke() }) {
        if (cocktail != null) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp).fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                elevation = 12.dp,
            ) {
                Column(
                    modifier = Modifier.background(Brush.horizontalGradient(colorStops = cocktail.color))
                        .padding(6.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                            .background(color = Color.Black)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(
                                modifier = Modifier.clip(CircleShape).size(32.dp)
                                    .background(Brush.sweepGradient(colorStops = cocktail.color))
                            )

                            Spacer(
                                modifier = Modifier.width(24.dp)
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    cocktail.name,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "맛",
                                            fontSize = 8.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            cocktail.flavor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "난이도",
                                            fontSize = 8.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            cocktail.level,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            "도수",
                                            fontSize = 8.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "${cocktail.proof}%",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (cocktail.comment.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = Color.Black).padding(8.dp)
                        ) {
                            items(cocktail.comment.size) {
                                Text(buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = Color.LightGray)) {
                                        append("익명${it + 1}: ")
                                    }
                                    withStyle(style = SpanStyle(color = Color.White)) {
                                        append(cocktail.comment[it])
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HighballScreen() {
    val selectedBase = remember { mutableStateOf<Bass?>(null) }
    val selectedDrink = remember { mutableStateOf<Drink?>(null) }
    val visible = remember { mutableStateOf(false) }
    val visible2 = remember { mutableStateOf(false) }
    val visible3 = remember { mutableStateOf(false) }
    val visible4 = remember { mutableStateOf(false) }
    val visible5 = remember { mutableStateOf(false) }
    val visible6 = remember { mutableStateOf(false) }

    val color =
        animateColorAsState(
            targetValue = if (!visible.value && !visible3.value) Color.Transparent else Color.White,
            animationSpec = tween(
                1000
            )
        )

    val color2 =
        animateColorAsState(
            targetValue = if (!visible2.value && !visible4.value) Color.Transparent else Color.White,
            animationSpec = tween(
                1000
            )
        )

    val color3 =
        animateColorAsState(
            targetValue = if (!visible2.value && !visible4.value) Color.Transparent else cran,
            animationSpec = tween(
                1000
            )
        )

    val color4 =
        animateColorAsState(
            targetValue = if (!visible5.value) Color.Transparent else Color.White,
            animationSpec = tween(
                1000
            )
        )

    val dotCount = remember { mutableStateOf(0) }

    LaunchedEffect(true) {
        visible.value = true
        delay(1000)
        visible2.value = true
    }

    LaunchedEffect(selectedBase.value) {
        if (selectedBase.value != null) {
            visible.value = false
            visible2.value = false
            delay(1000)
            visible3.value = true
            delay(1000)
            visible4.value = true
        }
    }

    LaunchedEffect(selectedDrink.value) {
        if (selectedDrink.value != null) {
            visible3.value = false
            visible4.value = false
            delay(1000)
            visible5.value = true
            delay(2000)
            visible5.value = false
            visible6.value = true

        }
    }
    LaunchedEffect(visible5.value) {
        for (i in 0..3) {
            delay(100)
            dotCount.value += 1
            delay(100)
            dotCount.value += 1
            delay(100)
            dotCount.value += 1
            delay(100)
            dotCount.value -= 1
            delay(100)
            dotCount.value -= 1
            delay(100)
            dotCount.value -= 1
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (!visible5.value || !visible6.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${if (visible3.value || (selectedBase.value != null && selectedDrink.value != null)) "${selectedBase.value!!.title}${if (selectedBase.value == Bass.Whisky || selectedBase.value == Bass.Vodka) "를" else "을"} 고르셨네요!\n음료" else "기주"}를 선택해주세요",
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color.value,
                    lineHeight = 45.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(30.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Row {
                        Text(
                            "1. ${if (visible4.value || (selectedBase.value != null && selectedDrink.value != null)) "콜라" else "위스키"}",
                            modifier = Modifier.clickable {
                                if (visible.value) {
                                    selectedBase.value = Bass.Whisky
                                } else {
                                    selectedDrink.value = Drink.Cola
                                }
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = color2.value,
                            fontSize = 40.sp
                        )
                        if ((visible4.value || (selectedBase.value != null && selectedDrink.value != null)) && selectedBase.value != null && selectedBase.value != Bass.Gin) {
                            Text(
                                "추천!",
                                fontWeight = FontWeight.SemiBold,
                                color = color3.value,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            "2. ${if (visible4.value || (selectedBase.value != null && selectedDrink.value != null)) "사이다" else "보드카"}",
                            modifier = Modifier.clickable {
                                if (visible.value) {
                                    selectedBase.value = Bass.Vodka
                                } else {
                                    selectedDrink.value = Drink.Cider
                                }
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = color2.value,
                            fontSize = 40.sp
                        )
                        if ((visible4.value || (selectedBase.value != null && selectedDrink.value != null)) && selectedBase.value != null && selectedBase.value == Bass.Gin) {
                            Text(
                                "추천!",
                                fontWeight = FontWeight.SemiBold,
                                color = color3.value,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            "3. ${if (visible4.value || (selectedBase.value != null && selectedDrink.value != null)) "토닉" else "럼"}",
                            modifier = Modifier.clickable {
                                if (visible.value) {
                                    selectedBase.value = Bass.Rum
                                } else {
                                    selectedDrink.value = Drink.Tonic
                                }
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = color2.value,
                            fontSize = 40.sp
                        )
                        if ((visible4.value || (selectedBase.value != null && selectedDrink.value != null)) && selectedBase.value != null && selectedBase.value != Bass.Rum) {
                            Text(
                                "추천!",
                                fontWeight = FontWeight.SemiBold,
                                color = color3.value,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            "4. ${if (visible4.value || (selectedBase.value != null && selectedDrink.value != null)) "진저에일" else "진"}",
                            modifier = Modifier.clickable {
                                if (visible.value) {
                                    selectedBase.value = Bass.Gin
                                } else {
                                    selectedDrink.value = Drink.Ginger
                                }
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = color2.value,
                            fontSize = 40.sp
                        )
                        if ((visible4.value || (selectedBase.value != null && selectedDrink.value != null)) && selectedBase.value != null && selectedBase.value == Bass.Whisky) {
                            Text(
                                "추천!",
                                fontWeight = FontWeight.SemiBold,
                                color = color3.value,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
        if (visible5.value) {
            Text(
                "제조중${".".repeat(dotCount.value)}",
                modifier = Modifier.align(Alignment.Center),
                color = color4.value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 50.sp
            )
        }
        AnimatedVisibility(
            visible = (visible6.value && selectedBase.value != null && selectedDrink.value != null),
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000)),
            modifier = Modifier
        ) {
            when (selectedBase.value!!) {
                Bass.Whisky -> {
                    when (selectedDrink.value!!) {
                        Drink.Cola -> {
                            HighballCup(
                                arrayListOf(Color.Black, whisky),
                                "위스키콕(잭콕)",
                                "위스키콕입니다! 잭다니엘로 만들면 잭콕입니다."
                            )
                        }

                        Drink.Cider -> {
                            HighballCup(
                                arrayListOf(Color.White, whisky),
                                "위스키 사이다",
                                "좀 더 달달한 하이볼"
                            )
                        }

                        Drink.Tonic -> {
                            HighballCup(
                                arrayListOf(Color.White, whisky),
                                "위스키 하이볼",
                                "가장 대중적인 하이볼입니다."
                            )
                        }

                        Drink.Ginger -> {
                            HighballCup(
                                arrayListOf(ginger, whisky),
                                "진저 하이볼",
                                "진저에일과 위스키의 조합은 아주 좋죠!"
                            )
                        }
                    }
                }

                Bass.Vodka -> {
                    when (selectedDrink.value!!) {
                        Drink.Cola -> {
                            HighballCup(
                                arrayListOf(Color.Black, Color.White),
                                "보드카 콕",
                                "온전한 콜라의 맛으로 취하고 싶을 때"
                            )
                        }

                        Drink.Cider -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "보드카 소다",
                                "보드카 토닉보다 알코올맛 덜납니다!"
                            )
                        }

                        Drink.Tonic -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "보드카 토닉",
                                "무난한 칵테일이죠"
                            )
                        }

                        Drink.Ginger -> {
                            HighballCup(
                                arrayListOf(ginger, Color.White),
                                "모스크뮬",
                                "구리잔에 시원하게 한 잔"
                            )
                        }
                    }
                }

                Bass.Gin -> {
                    when (selectedDrink.value!!) {
                        Drink.Cola -> {
                            HighballCup(
                                arrayListOf(Color.Black, Color.White),
                                "진콕",
                                "당신 뭘 만든거죠?"
                            )
                        }

                        Drink.Cider -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "진소다",
                                "진피즈와 비슷한 맛.. 맛있습니다."
                            )
                        }

                        Drink.Tonic -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "진토닉",
                                "최고의 조합"
                            )
                        }

                        Drink.Ginger -> {
                            HighballCup(
                                arrayListOf(ginger, Color.White),
                                "진벅",
                                "생각보다 괜찮습니다!"
                            )
                        }
                    }
                }

                Bass.Rum -> {
                    when (selectedDrink.value!!) {
                        Drink.Cola -> {
                            HighballCup(
                                arrayListOf(Color.Black, Color.White),
                                "럼콕",
                                "라임쥬스를 넣으면 쿠바리브레가 되죠"
                            )
                        }

                        Drink.Cider -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "럼소다",
                                "조금 달지도?"
                            )
                        }

                        Drink.Tonic -> {
                            HighballCup(
                                arrayListOf(Color.White, Color.White),
                                "럼토닉",
                                "나쁘진않은데.."
                            )
                        }

                        Drink.Ginger -> {
                            HighballCup(
                                arrayListOf(ginger, Color.White),
                                "보스턴 쿨러",
                                "진저에일은 어디든 어울리는 것 같아요"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HighballCup(colors: ArrayList<Color>, title: String, explanation: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Spacer(
                modifier = Modifier.height(100.dp).width(5.dp).clip(
                    RoundedCornerShape(bottomStart = 5.dp)
                ).background(Color.Black)
            )
            Column {
                Spacer(
                    modifier = Modifier.height(80.dp).width(40.dp)
                        .background(Brush.verticalGradient(colors = colors))
                )
                Spacer(modifier = Modifier.height(5.dp).width(40.dp).background(Color.Black))
            }
            Spacer(
                modifier = Modifier.height(100.dp).width(5.dp).clip(
                    RoundedCornerShape(bottomEnd = 5.dp)
                ).background(Color.Black)
            )
        }

        Spacer(modifier = Modifier.height(40.dp).width(5.dp))

        Text(
            title, fontWeight = FontWeight.SemiBold,
            fontSize = 35.sp,
            lineHeight = 35.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp).width(5.dp))

        Text(
            explanation, fontWeight = FontWeight.Medium,
            fontSize = 20.sp,
            lineHeight = 20.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun CocktailScreen() {
    val slot = remember { Slot() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            SlotItem(slot)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { startSlotMachine(slot) },
            colors = ButtonDefaults.buttonColors(backgroundColor = peach, contentColor = themeBlue)
        ) {
            Text("돌리기")
        }
    }
}

@Composable
fun SlotItem(slot: Slot) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp).fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 12.dp,
    ) {
        Column(
            modifier = Modifier.background(Brush.horizontalGradient(colorStops = slot.currentCocktail.color))
                .padding(6.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                    .background(color = Color.Black)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(
                        modifier = Modifier.clip(CircleShape).size(32.dp)
                            .background(Brush.sweepGradient(colorStops = slot.currentCocktail.color))
                    )

                    Spacer(
                        modifier = Modifier.width(24.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            slot.currentCocktail.name,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "맛",
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    slot.currentCocktail.flavor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "난이도",
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    slot.currentCocktail.level,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "도수",
                                    fontSize = 8.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "${slot.currentCocktail.proof}%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
            if (slot.currentCocktail.comment.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.Black).padding(8.dp)
                ) {
                    items(slot.currentCocktail.comment.size) {
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.LightGray)) {
                                append("익명${it + 1}: ")
                            }
                            withStyle(style = SpanStyle(color = Color.White)) {
                                append(slot.currentCocktail.comment[it])
                            }
                        })
                    }
                }
            }
        }
    }
}

class Slot {
    private val cocktailList =
        CocktailList().strongList + CocktailList().sparklingList + CocktailList().milkyList + CocktailList().ssList

    private val range = cocktailList.indices

    private var currentImageIndex by mutableStateOf(range.random())

    val currentCocktail: Cocktail
        get() = cocktailList[currentImageIndex]

    fun spin() {
        currentImageIndex = (currentImageIndex + 1) % cocktailList.size
    }
}

fun startSlotMachine(slot1: Slot) {
    val spinDuration = 100L

    val range = (10..15)
    // Coroutine scope
    CoroutineScope(Dispatchers.Default).launch {
        for (i in 0..range.random()) {
            // Spin each slot
            slot1.spin()

            // Delay for spinDuration milliseconds
            delay(spinDuration)
        }
    }
}