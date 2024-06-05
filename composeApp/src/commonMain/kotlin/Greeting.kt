import androidx.compose.ui.graphics.Color

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

val whisky = Color(0xFFCC7814)
val whisky2 = Color(0xFFF3D1A7)
val cran = Color(0xFFE70957)
val orange = Color(0xFFFF8400)
val mellon = Color(0xFF6AFF00)
val blue = Color(0xFF005EFF)
val lemon = Color(0xFFFFE600)
val peach = Color(0xFFFC91FF)
val cassis = Color(0xFF290074)
val ginger = Color(0xFFC79738)
val lime = Color(0xFFA5FF14)
val grapefruit = Color(0xFFFF3D00)
val coffee = Color(0xFF240D00)

val themeBlue = Color(0xFF3560FA)

data class Cocktail(
    val name: String,
    val flavor: String,
    val level: String,
    val proof: Int,
    val comment: ArrayList<String> = arrayListOf(),
    val color: Array<Pair<Float, Color>> = arrayOf(
        0.0f to Color.White,
        1f to Color.White
    ),
    val visible: Boolean = true,
)

class CocktailList() {
    val strongList = arrayListOf(
        Cocktail(
            "갓파더",
            "위스키/체리",
            "★★★★",
            36,
            comment = arrayListOf(
                "위스키 본연의 맛을 느끼고 싶을때 홀짝이면서 먹기 좋고 양도 적당해서 굿",
                "맛있게 다음날 죽으실분 추천드립니다"
            ),
            arrayOf(
                0.0f to whisky,
                1f to whisky2,
            )
        ),
        Cocktail(
            "올드패션드",
            "위스키/허브",
            "★★☆",
            28,
            color = arrayOf(
                0.0f to whisky,
                1f to whisky2,
            ),
            comment = arrayListOf(
                "위스키 본래 맛을 즐기는 사람들에게 추천",
                "이게어떻게28도 적당한당도 적당한목넘김 위스키잘못마시는데 도수높게 먹고싶은분 추천"
            )
        ),
        Cocktail(
            "갓마더", "체리", "★★★★☆", 36, color = arrayOf(
                0.0f to whisky,
                0.7f to Color.White,
            )
        ),
        Cocktail(
            "코스모 폴리탄", "오렌지/크렌베리", "★★★", 24, color = arrayOf(
                0.0f to cran,
                1f to orange
            )
        ),
        Cocktail(
            "오렌지 블라썸", "오렌지/솔", "★★", 20, color = arrayOf(
                0.0f to orange,
                1f to Color.White,
            )
        )
    )
    val sparklingList = arrayListOf(
        Cocktail(
            "미도리 사워",
            "메론",
            "☆",
            4,
            comment = arrayListOf(
                "★★★★★",
                "음료수",
                "비주얼 합격 맛 합격. 기분 좋은 멜론 맛. 술이 맞나 싶을 정도로 술 맛이 아예 안 남. 상큼함과 달콤함의 비율이 적절해서 좋았음."
            ),
            color = arrayOf(
                0.0f to mellon,
                0.8f to Color.White,
            )
        ),
        Cocktail(
            "블루 사파이어", "레몬/복숭아", "☆", 5, color = arrayOf(
                0.0f to blue,
                0.6f to peach,
                1f to lemon,
            ), visible = false
        ),
        Cocktail(
            "카시스 소다", "베리", "☆", 5, color = arrayOf(
                0.0f to cassis,
                1.0f to Color.White,
            )
        ),
        Cocktail("진토닉", "솔", "★", 10, comment = arrayListOf("단 것보다 깔끔한 맛의 술을 마시고 싶은 사람에게 추천")),
        Cocktail(
            "모스크 뮬", "진저에일", "★", 8, color = arrayOf(
                0.0f to ginger,
                1.0f to Color.White,
            ), comment = arrayListOf("부담없이 무난하게 마실수 있음", "술이지만 건강한 쌍화차를 마시는 느낌")
        ),
        Cocktail(
            "쿠바리브레", "콜라", "★", 10, color = arrayOf(
                0.0f to Color.Black,
                1.0f to lime,
            ), comment = arrayListOf("음료술")
        )
    )
    val milkyList = arrayListOf(
        Cocktail(
            "깔루아 밀크", "커피우유", "☆", 5, color = arrayOf(
                0.0f to coffee,
                1.0f to Color.White,
            )
        ),
        Cocktail(
            "토스티드 아몬드", "커피/아몬드", "☆", 6, color = arrayOf(
                0.0f to coffee,
                0.5f to whisky,
                1.0f to Color.White,
            ), comment = arrayListOf("당도 딱 적당하게 줘서 좋았어여 근데 깔루아밀크가 더 맛있을듯")
        ),
        Cocktail(
            "화이트러시안", "커피우유", "★★", 22, color = arrayOf(
                0.0f to coffee,
                0.7f to Color.White,
            )
        )
    )
    val ssList = arrayListOf(
        Cocktail(
            "아마레또 사워", "시큼/체리", "★☆", 12, color = arrayOf(
                0.0f to whisky,
                1.0f to lemon
            ), visible = false
        ),
        Cocktail(
            "카시스프라페", "베리/복숭아", "☆", 12, color = arrayOf(
                0.0f to cassis,
                0.4f to peach,
                0.7f to orange,
                1.0f to lemon
            )
        ),
        Cocktail(
            "준벅",
            "메론/파인애플",
            "☆",
            8,
            color = arrayOf(
                0.0f to mellon,
                1.0f to lemon
            ),
            comment = arrayListOf("상큼과 달달의 조화가 좋았어염ㅎㅎ 더 상큼해두 맛있을듯! 끝맛도 알콜맛이 살짝만 올라오는 느낌이라서 목넘김이 조음")
        ),
        Cocktail(
            "블루 하와이안",
            "코코넛/파인애플",
            "★",
            15,
            comment = arrayListOf("색이 넘 이쁘고 얼음에 희석되면서 더 맛있어지는 걱 같아요~~ 첫 맛은 술향이 생각보다 강했는데 마실수록 맛있네여ㅕ\uD83D\uDC4D"),
            color = arrayOf(
                0.0f to blue,
                1.0f to lemon
            ), visible = false
        ),
        Cocktail(
            "보드카 선라이즈", "오렌지/석류", "☆", 8, color = arrayOf(
                0.0f to orange,
                1.0f to cran
            )
        ),
        Cocktail(
            "솔티독", "소금/자몽", "★☆", 10, color = arrayOf(
                0.0f to grapefruit,
                1.0f to Color.White
            ), comment = arrayListOf("소금발린자몽칵테일")
        ),
        Cocktail(
            "시브리즈", "크렌베리/자몽", "★☆", 10, color = arrayOf(
                0.0f to grapefruit,
                0.7f to cran
            )
        ),
        Cocktail(
            "우우", "복숭아/크렌베리", "★☆", 9, comment = arrayListOf("술찌들한테는 우우가 아주 딱이다."), color = arrayOf(
                0.0f to peach,
                1.0f to cran
            )
        ),
        Cocktail(
            "헤어리 네이블", "복숭아/오렌지", "★", 8, color = arrayOf(
                0.0f to peach,
                1.0f to orange
            )
        ),
        Cocktail(
            "퍼지 네이블", "복숭아/오렌지", "☆", 5, color = arrayOf(
                0.0f to peach,
                1.0f to orange
            )
        ),
        Cocktail(
            "피치크러쉬", "복숭아/크렌베리", "☆", 5, color = arrayOf(
                0.0f to peach,
                1.0f to cran
            )
        )
    )
}

enum class Navigation(val route: String) {
    Main("main_screen"),
    Highball("highball_screen"),
    CockTail("cocktail_screen"),
}

enum class Bass(val title: String) {
    Whisky("위스키"), Vodka("보드카"), Gin("진"), Rum("럼")
}

enum class Drink(val title: String) {
    Cola("콜라"), Cider("사이다"), Tonic("토닉"), Ginger("진저에일")
}