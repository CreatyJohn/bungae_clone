# android-kotlin

Kotlin ver

# 11월 26일 (첫째 날)
#### 깃허브 레포 연동

### 추가 커밋 내용
#### app/build.gradle - 프로젝트 및 모듈에 사용할 라이브러리 추가
##### 핵심 코드
- 여러 뷰들을 커스텀 시켜줄 ripple 라이브러리 추가
- 뷰페이저 속에서 인디케이터를 사용해야 하므로 indicator 라이브러리 추가
```
implementation 'com.balysv:material-ripple:1.0.2'
implementation 'com.tbuonomo.andrui:viewpagerdotsindicator:4.1.2'
```

#### app/src/main/AndroidManifest.xml - 매니페스트 액티비티 및 초기 세팅
##### 핵심 코드
- 아래와 같은 형식으로 앞으로 함께 할 모든 액티비티 (외부 API 액티비티 포함)를 앱에 포함 시켜줌
```
<activity
    android:name=".src.main.login.LoginActivity"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@style/AppTheme.NoActionBar">

</activity>
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/MainActivity.kt - 바텀 네비게이션 바 구현
##### 핵심 코드
- 기본적으로 템플릿에 저장 되어있는 네비게이션 바를 커스텀 하여 BottomNavBar를 구현함
```        
binding.mainBtmNav.run {
    setOnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_main_btm_nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
            }
            R.id.menu_main_btm_nav_search -> {
            }
            R.id.menu_main_btm_nav_regist -> {
                startActivity(intent)
            }
            R.id.menu_main_btm_nav_chat -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, ChatFragment())
                    .commitAllowingStateLoss()
            }
            R.id.menu_main_btm_nav_my_page -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, ProfileFragment())
                    .commitAllowingStateLoss()
            }
        }
        true
    }
    selectedItemId = R.id.menu_main_btm_nav_home
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/LoginActivity.kt - 로그인 화면 구현
##### 핵심 코드
- 로그인 페이지에서 기본적으로 보여지는 얕은 광로를 뷰 페이저로 구현함
```
val imageList = arrayListOf<Int>().apply {
    for (i in 0..3) {
        add(R.drawable.slide_one)
        add(R.drawable.slide_two)
        add(R.drawable.slide_three)
        add(R.drawable.slide_four)
    }
}

binding.vpBoungeInfo.apply {
    adapter = ViewPagerAdapter(imageList, binding.vpBoungeInfo)
    offscreenPageLimit = 1
    getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            sliderImageHandler.removeCallbacks(sliderImageRunnable)
            sliderImageHandler.postDelayed(sliderImageRunnable, 4500)
        }
    })
    setPageTransformer { page, position ->
        page.translationX = position * 0
    }
}
```
- 이미지 슬라이더를 사용할때 필요한 메서드 (ms 기준으로 움직임을 주거나 안주거나)
```
sliderImageHandler.removeCallbacks(sliderImageRunnable)
sliderImageHandler.postDelayed(sliderImageRunnable, 5000)
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/ViewPagerAdapter.kt - 홈화면 속 배너 광고화면 구현
##### 핵심 코드
- 아래는 Viewpager2를 사용한 뷰페이저 어댑터이다.
- viewpager2는 viewpager1과 다르게 리사이클러뷰 어댑터를 기반으로 한다. (매우 유사하게 빌드됨.. 그냥 똑같을 지도..?)
```
class ViewPagerAdapter(private val sliderItems: MutableList<Int>, private val viewPager2: ViewPager2): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.iv_for_viewpager)

        fun onBind(image: Int) {
            imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewpager_activity, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.onBind(sliderItems[position])
        if (position == sliderItems.size - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    private val runnable = Runnable { sliderItems.addAll(sliderItems) }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/splash/SplashActivity.kt - 앱 시작시 실행되는 스플래시 화면 구현
##### 핵심 코드
- 기본적으로 스플래시 화면을 구현할 때, 가장 많이 사용되는 핸들러
- 구현되는 느낌은 쉽게 스레드를 몇초(ms 기준으로)동안 마비시켜 지연시키는 느낌을 주는 형식
```
Handler(Looper.getMainLooper()).postDelayed({
    startActivity(Intent(this, MainActivity::class.java))
    finish()
}, 1500)
// Actionbar 제거
supportActionBar?.hide()

val handler = Handler(Looper.getMainLooper())
handler.postDelayed(Runnable {
    Intent(this, LoginActivity::class.java).apply {
        startActivity(this)
        finish()
    }
}, 1000)
```

#### app/src/main/res/drawable/custom_ripple_effect.xml - 앞으로 View에 가장 자주 사용되는 커스텀 리플 이펙터
##### 핵심 코드
- drawable 폴더에 xml로 생성하여 사용
- 가끔가다 버튼 클릭의 효과를 주길 원하는 뷰에 사용하면 제일 적절함
```
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:color="@android:color/darker_gray"> <!-- Ripple Effect 클릭시 색상 -->

    <!-- 배경색 -->
    <item android:id="@android:id/background">
        <shape android:shape="rectangle">
            <solid android:color="@android:color/white"/>
        </shape>
    </item>

</ripple>
```

# 11월 27일 (둘째 날)
## 컨디션 : 빵빵함

### 추가 커밋 내용
#### app/build.gradle - 프로젝트 및 모듈에 사용할 라이브러리 추가
##### 핵심 코드
- 이번엔 필요에 따라 뷰페이저2와 글라이드 라이브러리를 업데이트 했다
```
implementation 'androidx.viewpager2:viewpager2:1.0.0'

implementation 'com.github.bumptech.glide:glide:4.9.0'
```

#### app/src/debug/res/drawable/ic_lightning_background.xml
##### 핵심 발언
- 대체로 여러 아이콘이나 화면에 필요한 요소들을 누끼따고 업데이트 하는 디자인의 날이었다
- 이것 말고도 drawble 폴더와 layout 폴더를 많이 만지는 날이었다

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/LoginActivity.kt - 인디케이터 적용
##### 핵심 코드
- 드디어 인디케이터를 적용했다
- 점으로 이루어진 인디케이터는 뷰페이저(광고)의 개수와 움직이는 화면에 따라 반응한다
```
/** Indicator */
binding.indicator0IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
binding.indicator1IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
binding.indicator2IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
binding.indicator3IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))

/** 자동 및 유사 무한 루프 되는 포지션의 값이 4로 나눴을때 떨어지는 몫이 0~3인경우 각각의 인디케이터로 해당하게 됨 */
when(position % 4){
    0 -> binding.indicator0IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
    1 -> binding.indicator1IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
    2 -> binding.indicator2IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
    3 -> binding.indicator3IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/MiniPageActivity.kt - 액티비티로 만든, 수제!!!! 바텀시트 다이얼로그
##### 핵심 코드
- 바보같이 바텀시트 다이얼로그를 몰라서 직접 액티비티로 유사하게 구현했다
- 아래의 코드는 해당 액티비티가 실행되면, 아랫방향에서 위로 올라오는 애니메이션을 적용시킨 코드다
```
binding.flBackground.setOnClickListener {
    finish()
    overridePendingTransition(R.anim.none, R.anim.vertical_exit)
}
```
- ....., 역시 머리가 나쁘면 몸이 고생한다는 것을 수시간의 구글링 끝에 알게되는 시간이었다

#### app/src/main/java/com/softsquared/template/kotlin/src/splash/SplashActivity.kt - 스플래시 스크린에 GIF 아이콘 (번개장터 아이콘) 적용
##### 핵심 코드
- 아까 다운받은 글라이드 라이브러리를 사용해서 GIF를 띄웠다
- 애니메이션 만들기에 시간과 귀찮음이 따랐다, 그냥 GIF 적용시켜버리기~
```
Glide.with(this).load(R.raw.splash_lightning).into(binding.ivGifBounge)
```
- 아, 그리고 스플래시 화면이 나올때마다 자꾸 어디서 방향을 잃고 튀어나오길래
수제 바텀시트 다이얼로그에서 사용한 화면 전환 애니메이션으로 화면 전환을 정지시켜버렸다
```
overridePendingTransition(R.anim.none, R.anim.none)
```

# 11월 28일 (셋째 날)
## 컨디션 : 마냥 재밌음

### 추가 커밋 내용
#### app/src/main/java/com/softsquared/template/kotlin/src/main/MainActivity.kt - 바텀네비게이션바 업데이트
##### 핵심 코드
- 구현된 화면에 따라 바텀네비게이션바에 프래그먼트를 추가연결
```
R.id.menu_main_btm_nav_search -> {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_frm, MyPageFragment())
        .replace(R.id.main_frm, ProfileFragment())
        .commitAllowingStateLoss()
}
R.id.menu_main_btm_nav_regist -> {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_frm, MyPageFragment())
        .replace(R.id.main_frm, ProfileFragment())
        .commitAllowingStateLoss()
}
R.id.menu_main_btm_nav_chat -> {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_frm, MyPageFragment())
        .replace(R.id.main_frm, ChatFragment())
        .commitAllowingStateLoss()
}
R.id.menu_main_btm_nav_my_page -> {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_frm, MyPageFragment())
        .replace(R.id.main_frm, ProfileFragment())
        .commitAllowingStateLoss()
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeFragment.kt - 홈 화면 구현
#### app/src/main/java/com/softsquared/template/kotlin/src/main/chat/ChatFragment.kt - 번개톡 화면 구현
#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/ProfileFragment.kt - 프로필 화면 구현
##### 핵심 코드
- 없다, 실패한 코드가 많아 불필요한 코드들뿐..

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/MyPhoneActivity.kt - 내 핸드폰으로 본인인증 구현
##### 핵심 코드
- 본인인증 API는 구현하지 못했지만 그래도 자동기입은 완성했음
- 앱 로그인 화면처럼 빈 칸이 있으면 버튼이 비활성화됨
```
binding.etBirthPlz.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        //텍스트를 입력 후

binding.etBirthPlz.doOnTextChanged { text, start, counts, after ->
    if(counts == 6) {
        binding.etBirthBack.requestFocus()
    }
}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //텍스트 입력 전
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //텍스트 입력 중
        if(binding.etBirthPlz.length() == 6) binding.etBirthBack.requestFocus()
    }
})

binding.etBirthBack.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        //텍스트를 입력 후

/** 작동 안됨 */
binding.etBirthBack.doOnTextChanged { text, start, counts, after ->
    if(counts == 0) {
        tvPhoneCompanyStart()
    }
}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //텍스트 입력 전
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        //텍스트 입력 중
        if(binding.etBirthBack.length() == 1) tvPhoneCompanyStart()
    }
})

/** editText를 살펴보기 위한 변수선언 */
var messagebirth: String = ""

binding.etBirthPlz.addTextChangedListener(object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /** 값 변경 시 실행되는 함수 */
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        /** 메세지 입력 값 담기 */
        messagebirth = binding.etBirthBack.text.toString()

        /** 값 유무에 따른 활성화 여부 */
        binding.btnNextRegisterBirth.isEnabled =
            messagebirth.isNotEmpty() // editText에 값이 있다면 true 없다면 false

        if (binding.btnNextRegisterBirth.isEnabled) {
            binding.btnNextRegisterBirth.setBackgroundResource(R.drawable.rectangle_ripple_effect)

        } else {
            binding.btnNextRegisterBirth.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
})
```
- 화면 출력시 키보드가 반응하는 클래스
- 마찬가지로 칸이 다 차면 버튼이 활성화됨과 동시에 다음 칸으로 넘어감
```
/** 다른 클래스 : 화면 출력시 키보드 반응하기 */
class KeyboardVisibilityUtils(
    private val window: Window,
    //private val onShowKeyboard: ((keyboardHeight: Int) -> Unit)? = null,
    private val onShowKeyboard: (() -> Unit)? = null,
    private val onHideKeyboard: (() -> Unit)? = null
) {

    private val MIN_KEYBOARD_HEIGHT_PX = 150

    private val windowVisibleDisplayFrame = Rect()
    private var lastVisibleDecorViewHeight: Int = 0


    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        window.decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
        val visibleDecorViewHeight = windowVisibleDisplayFrame.height()

        // Decide whether keyboard is visible from changing decor view height.
        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                // Calculate current keyboard height (this includes also navigation bar height when in fullscreen mode).
                //val currentKeyboardHeight = window.decorView.height - windowVisibleDisplayFrame.bottom
                // Notify listener about keyboard being shown.
                //onShowKeyboard?.invoke(currentKeyboardHeight)
                onShowKeyboard?.invoke()
            } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                // Notify listener about keyboard being hidden.
                onHideKeyboard?.invoke()
            }
        }
        // Save current decor view height for the next call.
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }

    init {
        window.decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun detachKeyboardListeners() {
        window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }
}
```
- Activity Result 가 있는 경우 실행되는 콜백함수
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if(resultCode == Activity.RESULT_OK){
        Log.d("PHONEDATA", "In onActivityResult")
        when(requestCode){
            SUBACTIITY_REQUEST_CODE -> {
                /** getExtra */
                val name = data?.getStringExtra("PHONE_DATA")
                if(name == null) {
                    Log.e("PHONEDATA", "메인에서 받아온 data : NULL")
                } else {
                    binding.btnPhoneCompany.text = name
                    Log.e("PHONEDATA", "메인에서 받아온 data : ${name}")
                    binding.companyView.setBackgroundResource(R.color.lightgray)
                    tvPhoneNumberStart()
                }
            }
        }
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/PhoneCompanyActivity.kt - 통신사 선택하는 수제!! 바텀시트 다이얼로그22
##### 핵심 코드
- 이때도 몰라서 그냥 또 노가다 했음
- 라디오 버튼을 그룹으로 묶고 커스텀 해가지고 멋진 라디오 버튼을 구현해냈음
```  
 binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
        when(checkedId) {
            R.id.btn_SKT -> finishActivity
            R.id.btn_KT -> finishActivity
            R.id.btn_LG -> finishActivity
            R.id.btn_SKT_other -> finishActivity
            R.id.btn_KT_other -> finishActivity
            R.id.btn_LG_other -> finishActivity
            R.id.btn_SKT -> {
                val name = binding.btnSKT.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            R.id.btn_KT -> {
                val name = binding.btnKT.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            R.id.btn_LG -> {
                val name = binding.btnLG.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            R.id.btn_SKT_other -> {
                val name = binding.btnSKTOther.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            R.id.btn_KT_other -> {
                val name = binding.btnKTOther.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            R.id.btn_LG_other -> {
                val name = binding.btnLGOther.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("PHONE_DATA", name)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
 }
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeFragment.kt - 홈 화면에 광고 배너 구현 
##### 핵심 코드
- 뷰 페이저와 이미지 글라이더를 사용하여 광고 배너 구현
- 물론 뷰 페이저 어댑터도 사용했음
``` 
// Actionbar 제거
mainActivity.supportActionBar?.hide()

val imageList = arrayListOf<Int>().apply {
    for (i in 0..9) {
        add(R.drawable.banner_1)
        add(R.drawable.banner_2)
        add(R.drawable.banner_3)
        add(R.drawable.banner_4)
        add(R.drawable.banner_5)
        add(R.drawable.banner_6)
        add(R.drawable.banner_7)
        add(R.drawable.banner_8)
        add(R.drawable.banner_9)
        add(R.drawable.banner_ten)
    }
}

binding.vpBanner.apply {
    adapter = ViewPagerAdapter(imageList, binding.vpBanner)
    offscreenPageLimit = 1
    getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            sliderImageHandler.removeCallbacks(sliderImageRunnable)
            sliderImageHandler.postDelayed(sliderImageRunnable, 4000)
        }
    })
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/ViewPagerAdapter.kt - 홈 화면 광고의 뷰 어댑터
##### 핵심 코드
- 리사이클러뷰에 능숙하다면 한번에 따라할 수 있는 뷰페이저2 어댑터
```
class ViewPagerAdapter( private val sliderItems: MutableList<Int>, private val viewPager2: ViewPager2): RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.slide_bv)

        fun onBind(image: Int) {
            imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vpbanners_list_item, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.onBind(sliderItems[position])
        if (position == sliderItems.size - 1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    private val runnable = Runnable { sliderItems.addAll(sliderItems) }
}
```
- src/main/home/ViewPagerAdapter.kt -> src/main/home/BannerPagerAdapter.kt : 이후에 이름을 바꿨음

# 11월 29일 (넷째 날)
## 컨디션 : 그래도 재밌음

### 추가 커밋 내용
#### app/build.gradle - 라이브러리 또 추가
##### 핵심 코드
- 말 그대로 API 29 모듈을 사용하기 위해 라이브러리 추가
```
// API 29 ~
implementation 'com.google.android.material:material:1.1.0'

implementation 'com.google.android.material:material:1.3.0-alpha01'
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/GoodsData.kt - 상품 등록/조회 데이터 클라스
#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/GoodsRVAdapter.kt - 상품 등록/조회 데이터 어댑터
##### 핵심 코드
- 상품 조회 및 상품 등록을 하기 위한 클래스 데이터와 어댑터 추가
- 요번 어댑터는 리사이클러뷰 어댑터임
```
// GoodsData.kt
data class GoodsData(
    val price : String,
    val info : String,
    val address : String,
    val uptime : String,
    val img : Int
)

// GoodsRVAdapter.kt
class GoodsRVAdapter(private val context: Context) : RecyclerView.Adapter<GoodsRVAdapter.ViewHolder>() {

    var datas = mutableListOf<GoodsData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_ex,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtPrice: TextView = itemView.findViewById(R.id.tv_rv_price)
        private val txtGoodsInfo: TextView = itemView.findViewById(R.id.tv_rv_info)
        private val txtAddress: TextView = itemView.findViewById(R.id.tv_rv_add)
        private val txtPatchTime: TextView = itemView.findViewById(R.id.tv_rv_patchtime)
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)

        fun bind(item: GoodsData) {
            txtPrice.text = item.price.toString()
            txtGoodsInfo.text = item.info.toString()
            txtAddress.text = item.address.toString()
            txtPatchTime.text = item.uptime.toString()
            Glide.with(itemView).load(item.img).into(imgProfile)

        }
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeFragment.kt - 탭 레이아웃 + 뷰페이저 추가
##### 핵심 코드
- 추후에는 프로필 프래그먼트로 옮겼지만, 만들어지기에 앞서 홈 프래그먼트에 탭 레이아웃 + 뷰페이저를 사용해봤음
```
/** 탭 레이아웃 + 뷰페이저 https://onlyfor-me-blog.tistory.com/295 */
val adapter = RVPageAdapter(mainActivity.supportFragmentManager)
adapter.addFragment(RecommendGoodsFragment(), "추천상품")
adapter.addFragment(BrandMenuFragment(), "브랜드")
binding.afterLoginViewpager.adapter = adapter
binding.afterLoginTablayout.setupWithViewPager(binding.afterLoginViewpager)
```
- 그에 따른 뷰 페이저 어댑터
```
// app/src/main/java/com/softsquared/template/kotlin/src/main/home/RVPageAdapter.kt
class RVPageAdapter(manager: FragmentManager): FragmentPagerAdapter(manager)
{
    private val fragmentList = ArrayList<Fragment>()
    private val titleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = titleList.size

    override fun getPageTitle(position: Int): CharSequence = titleList[position]

    fun addFragment(fragment: Fragment, title: String)
    {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/RecommendGoodsFragment.kt - 탭 레이아웃의 첫 뷰 페이저 (첫번째 탭 화면)
##### 핵심 코드
- 말 그대로 상품을 조회하는 페이지인 첫번째 탭 레이아웃의 첫 탭
- 그 속에 상품을 조회하는 리사이클러뷰의 값을 어댑터로 기입 해주는 과정
```
private fun initRecycler() {
    goodsRVAdapter = GoodsRVAdapter(requireContext())
    binding.rvRecommendGoods.adapter = goodsRVAdapter

    datas.apply {
        add(GoodsData(img = R.drawable.banner_3, price = "90,000원", info = "첫번째 물품", address = "강동구 상일동", uptime = "59분 전"))
        add(GoodsData(img = R.drawable.banner_4, price = "90,000원", info = "두번째 물품", address = "하남시 미사동", uptime = "3달 전"))
        add(GoodsData(img = R.drawable.banner_6, price = "90,000원", info = "세번째 물품", address = "은평구 신사2동", uptime = "1시간 전"))
        add(GoodsData(img = R.drawable.banner_7, price = "90,000원", info = "네번째 물품", address = "강동구 고덕동", uptime = "10분 전"))
        add(GoodsData(img = R.drawable.banner_ten, price = "90,000원", info = "다섯번째 물품", address = "중랑구 목동", uptime = "30초 전"))

        goodsRVAdapter.datas = datas
        goodsRVAdapter.notifyDataSetChanged()
    }
}
```

# 11월 30일 (다섯째 날)
## 컨디션 : 하하

### 추가 커밋 내용
#### app/build.gradle - 라이브러리 또또 추가
##### 핵심 코드
- 라이브러리 추가 코드지만 나름 중요한 이유는, 해당 라이브러리가 네이버 소셜 로그인에 꼭 필요한 라이브러리이기 때문이다.
```
implementation 'com.navercorp.nid:oauth:5.1.0'
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0"
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/GoodsRVAdapter.kt - 굿즈(상품)의 리사이클러뷰
- 가장 기본이 되고 많이 사용 될 리사이클러뷰 코드의 대표이다.
- 앞으로는 같은 방식으로 많이 쓰일테니 따로 적진 않겠다.
##### 핵심 코드
```
class GoodsRVAdapter(val dataList: ArrayList<GoodsData>, val context: Context) : RecyclerView.Adapter<GoodsRVAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtPrice: TextView = itemView.findViewById(R.id.tv_rv_price)
        private val txtGoodsInfo: TextView = itemView.findViewById(R.id.tv_rv_info)
        private val txtAddress: TextView = itemView.findViewById(R.id.tv_rv_add)
        private val txtPatchTime: TextView = itemView.findViewById(R.id.tv_rv_patchtime)
        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)

        fun bind(item: GoodsData) {
            txtPrice.text = item.price.toString()
            txtGoodsInfo.text = item.info.toString()
            txtAddress.text = item.address.toString()
            txtPatchTime.text = item.uptime.toString()
            Glide.with(itemView).load(item.img).into(imgProfile)

            txtPrice.text = item.price
            txtGoodsInfo.text = item.info
            imgProfile.setImageResource(item.img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_grid3,parent,false)
        
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.count()

    override fun onBindViewHolder(holder: GoodsRVAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeFragment.kt - 액션바 제거
- 액션바가 거추장 스러워 제거하는 코드를 썼으나 프래그먼트에 사용해서 그런지 잘 적용되지 않은 모습이다.
##### 핵심 코드
```
mainActivity.supportActionBar?.hide()
binding.clToolbar.bringToFront()
requireActivity().setStatusBarTransparent()
binding.abActivityMaterialDetail.bringToFront()
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/MiniPageActivity.kt - 네이버 소셜 로그인
##### 핵심 코드
- 오늘 그래들에서 설정한 네이버 소셜 로그인 라이브러리를 적용해보았다.
```
binding.run {
            binding.btnNaverLogin.setOnClickListener {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                    override fun onSuccess() {
                        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                            override fun onSuccess(result: NidProfileResponse) {
                                name = result.profile?.name.toString()
                                email = result.profile?.email.toString()
                                gender = result.profile?.gender.toString()
                                val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                                Log.e(TAG, "로그인 성공")
                                Log.e(TAG, "=================================")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 액세스토큰 : $naverAccessToken")
                                startActivity(startMain)
                                showCustomToast("네이버 로그인 성공")
                                Log.e(TAG, "로그인 성공")
                            }

                            override fun onError(errorCode: Int, message: String) {
                                showCustomToast("로그인 에러")
                                Log.e(TAG, "로그인 에러")
                                finish()
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                showCustomToast("로그인 실패")
                                Log.e(TAG, "로그인 실패")
                                finish()
                            }
                        })
                    }

                    override fun onError(errorCode: Int, message: String) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        Log.e(TAG, "naverAccessToken : $naverAccessToken")
                        showCustomToast("로그인 실패")
                        Log.e(TAG, "로그인 에러")
                        finish()
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        showCustomToast("로그인 실패")
                        Log.e(TAG, "로그인 실패")
                        finish()
                    }
                }

                NaverIdLoginSDK.initialize(this@MiniPageActivity, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), "번개장터_clone")
                NaverIdLoginSDK.authenticate(this@MiniPageActivity, oAuthLoginCallback)
            }
        }
```
- 네이버로 사용자 정보 호출
```

        /** 네이버 로그인 사용자 정보 호출 함수 */
        fun callProfileApi(callback: NidProfileCallback<NidProfileResponse>) = CoroutineScope(Dispatchers.Main).launch {
            val response: Response<NidProfileResponse>
            try {
                response = withContext(Dispatchers.IO) {
                    NidProfileApi().requestApi()
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                callback.onError(-1, t.toString())
                return@launch
            }

            val res = response.body()

            when (response.code()) {
                in 200 until 300 -> {
                    if (res?.profile != null && !res.profile!!.id.isNullOrEmpty()) {
                        callback.onSuccess(res)
                        Log.e(TAG, "네이버 로그인 정보 호출 성공")
                    } else {
                        callback.onFailure(response.code(), "${res?.resultCode ?: ""} ${res?.message ?: ""}")
                        Log.e(TAG, "네이버 로그인 정보 호출 실패")
                    }
                }
                in 400 until 500 -> callback.onFailure(response.code(), response.message())
                else -> {
                    Log.e(TAG, "네이버 로그인 정보 호출 실패")
                    callback.onError(response.code(), response.message())
                }
            }
        }
```


#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/PreferenceUtil.kt - sharedPreference
##### 핵심 코드
- 로컬로 여러 정보를 저장하고 교환하기 위한 전역 sharedPreference 설정
```
package com.softsquared.template.kotlin.src.main.login

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/BottomSheetDialogSetting.kt
##### 핵심 코드
- 몇일전부터 알아두면 좋았을법한 바텀시트 다이얼로그
- 뒷화면을 띄워주고 설정하는데 자주 사용한다.
```
package com.softsquared.template.kotlin.src.main.myPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.MainActivity

@Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
class BottomSheetDialogSetting : BottomSheetDialogFragment() {

    private val TAG = "SOCIALLOGIN"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottomsheetlayout, container, false)

        view?.findViewById<Button>(R.id.btn_setting)?.text = "로그아웃"

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_parcel)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_client)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_receller)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_setting)?.setOnClickListener {
            dismiss()
        }
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/ProfileFragment.kt
##### 핵심 코드
- 회원탈퇴와 로그아웃
```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            Log.d(TAG, "In onActivityResult")
            when(requestCode){
                SUBACTIITY_REQUEST_CODE -> {
                    /** getExtra */
                    val name = data?.getStringExtra("SENDRESULT")
                    if(name == null) {
                        Log.e(TAG, "메인에서 받아온 data : NULL")
                    } else {
                        binding.btnLogout.text = "$name 로그아웃"
                        binding.btnLogoutForever.text = "$name 회원탈퇴"
                        Log.e(TAG, "$name")
                    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = PreferenceUtil(mainActivity.applicationContext)

        val loginresult = prefs.getString("LOGINRESULT", "정보없음")
        Log.e(TAG, loginresult)

        val loginresultName = prefs.getString("MYNAME", "정보없음")
        Log.e(TAG, loginresultName)

        val loginresultNaver = prefs.getString("NAVERNAME", "정보없음")
        Log.e(TAG, loginresultName)
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/BottomSheetDialogSetting.kt
##### 핵심 코드
- 몇일전부터 알아두면 좋았을법한 바텀시트 다이얼로그
- 뒷화면을 띄워주고 설정하는데 자주 사용한다.
```
package com.softsquared.template.kotlin.src.main.myPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.MainActivity

@Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
class BottomSheetDialogSetting : BottomSheetDialogFragment() {

    private val TAG = "SOCIALLOGIN"

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottomsheetlayout, container, false)

        view?.findViewById<Button>(R.id.btn_setting)?.text = "로그아웃"

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_parcel)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_client)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_receller)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_setting)?.setOnClickListener {
            dismiss()
        }
    }
}
```

# 12월 1일 (여섯째 날)
## 컨디션 : 너무 졸리지만, 어느정도 집중도를 컨트롤 할 수 있게 되었음

### 추가 커밋 내용
#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeFragment.kt
##### 핵심 코드
- 태그 레이아웃에 앱바에 이어 스크롤 시에 딸려가는 함수
```
  binding.mainScrollView.run {
        try {
        header = binding.llAppbar
            stickListener = { _ ->
                Log.e(TAG, "stickListener")
                binding.llAppbar.setBackgroundResource(R.color.white)
                binding.btnList.setImageResource(R.drawable.ic_list_black)
                binding.btnNoticeBell.setImageResource(R.drawable.ic_notification_black)
                binding.btnSearch.setImageResource(R.drawable.ic_search_black)
            }
            freeListener = { _ ->
                Log.e(TAG, "freeListener")
                binding.llAppbar.setBackgroundResource(R.color.transparent)
                binding.btnList.setImageResource(R.drawable.ic_list)
                binding.btnNoticeBell.setImageResource(R.drawable.ic_notification)
                binding.btnSearch.setImageResource(R.drawable.ic_search)
            }
        } catch (e: Exception){
        Log.e(TAG, "에러남 ${e.printStackTrace()}")
        }
  }
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/home/HomeScrollView.kt
##### 핵심 코드
- 스크롤 커스텀 뷰이다, 여러모로 쓸모 있었다
- 탭 레이아웃을 앱바에 붙일때 쓰였음
```
package com.softsquared.template.kotlin.src.main.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView

class HomeScrollView : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    /** 커스텀 뷰의 생성자이다. OVER_SCROLL_NEVER 설정을 안하면 역 스크롤시, 이상한 애니메이션이 출력된다. */
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attr,
        defStyleAttr
    ) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var header: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f // 천장에 붙은 뷰가 다른 스크롤 뷰의 레이아웃 뒤에 가려지게 되는걸 방지하는 코드
                it.setOnClickListener { _ ->
                    //클릭 시, 헤더뷰가 최상단으로 오게 스크롤 이동
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    /** 천장에 붙어있는지 아닌지를 체크하는 옵저버 변수 */
    private var mIsHeaderSticky = false
    /** 천장에 붙어있는지 아닌지를 체크하는 옵저버 변수 -> 초기화 */
    private var mHeaderInitPosition = 0f

    /** 해당 함수는 레이아웃에 변경이 생길 때 일어날 것을 추가해주면 된다. onAttachToWindow 에 넣지 않을 것 */
    override fun onGlobalLayout() {
        mHeaderInitPosition = header?.top?.toFloat() ?: 0f
    }

    /** 중요, 해당 함수는 어느 시점에 헤더를 천장에 붙일지, 다시 땔지 등을 구현한다. */
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)

        val scrolly = t

        if (scrolly > mHeaderInitPosition) {
            stickHeader(scrolly - mHeaderInitPosition)
        } else {
            freeHeader()
        }
    }

    /** 헤더가 천장을 넘어섰을 때 stickHeader함수를 통해 천장에 붙이고, 그렇지 않을 경우 freeHeader를 통해 천장에서 다시 땐다. */
    private fun stickHeader(position: Float) {
        header?.translationY = position
        callStickListener()
    }

    /** 붙어있지 않으면 -> 리스너 콜 -> flag를 true로 변환 */
    private fun callStickListener() {
        if (!mIsHeaderSticky) {
            stickListener(header ?: return)
            mIsHeaderSticky = true
        }
    }

    /** 헤더의 translationY를 0으로 해서 복원해줌 */
    private fun freeHeader() {
        header?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (mIsHeaderSticky) {
            freeListener(header ?: return)
            mIsHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}
```

# 12월 1일 (일곱째 날)
## 컨디션 : 일주일이 되었다. 벌써??

### 추가 커밋 내용
#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/ProfileFragment.kt
##### 핵심 코드
- 탭 레이아웃의 좌우 변환시 탭별로 특이사항을 실행하게 해주는 기능을 구현했다
```
 /** 탭별로 특이사항 실행 */
binding.afterLoginTablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
    override fun onTabSelected(tab: Tab) { // 선택 X -> 선택 O
        when (tab.position) {
            0 -> { //탭레이아웃 포지션 얻기 0 이 Tab 1
                Log.e(TAG, "판매상품 탭")
            }
            1 -> {
                Log.e(TAG, "상점후기 탭")
            }
            2 -> {
                Log.e(TAG, "찜하기 탭, 액티비티 실행")
                val intent = Intent(requireContext(), WishListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onTabUnselected(tab: Tab) { // 선택 O -> 선택
    }

    override fun onTabReselected(tab: Tab) { // 선택 O -> 선택 O
    }
})
```

#### app/build.gradle, app/src/main/AndroidManifest.xml
##### 핵심 코드
- 이날부터 API를 구현하기 위해 그래들에 새로운 라이브러리들을 추가했다
- 레트로핏 GSON과 URL을 이미지로 바꿔주는 등의 기능을 하는 글라이드를 업데이트했다.
```
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

implementation 'de.hdodenhof:circleimageview:3.1.0' //Circle ImageView
```
- 매니페스트에도 기능을 추가했다.
- 인터넷을 사용하게 해주는 기능과, http에 s를 무조건적으로 붙여주는 기능이다.
```
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <application
        android:hardwareAccelerated="true"
        android:usesCleartextTraffic="true"
        android:name=".src.main.login.GlobalApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_lightning"
```
- API를 사용하기 위한 레트로핏 import 패키지 들이다
```
import com.softsquared.template.kotlin.src.main.reqres.APIS
import com.softsquared.template.kotlin.src.main.reqres.PostModel
import com.softsquared.template.kotlin.src.main.reqres.PostResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
```

# 12월 2일 (여덟째 날)
## 컨디션 : 으아 죽을것같아 살려줘어

### 추가 커밋 내용
#### app/build.gradle - 프로젝트
##### 핵심 코드
- API공부가 끝나서 이제 모든 기능들을 추가하기 위해 프로젝트 그래들에 라이브러리들을 마구 추가했다
```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'com.google.gms.google-services'
}

android {
@@ -74,27 +75,36 @@ dependencies {

    implementation 'androidx.viewpager2:viewpager2:1.0.0'

    implementation 'com.github.bumptech.glide:glide:4.9.0'
    //Glide
    implementation 'com.github.bumptech.glide:glide:4.12.0'

// API 29 ~
    implementation 'com.google.android.material:material:1.1.0'

    implementation 'com.google.android.material:material:1.3.0-alpha01'

    // NaverLogin
    implementation 'com.navercorp.nid:oauth:5.1.0'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0"

    implementation 'com.facebook.android:facebook-android-sdk:[8,9)'

    implementation 'androidx.work:work-runtime-ktx:2.7.0'

    // KakaoLogin Module
    implementation "com.kakao.sdk:v2-user:2.0.1"

    // Circle Image
    implementation 'de.hdodenhof:circleimageview:3.1.0'

    // Retrofit(Rest API)
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    implementation 'de.hdodenhof:circleimageview:3.1.0' //Circle ImageView

    // Firebase
    implementation 'com.google.firebase:firebase-core:20.1.2'
    implementation 'com.google.firebase:firebase-storage:20.0.1'

}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/goods/BindingAdapter.kt - 바인딩어댑터
##### 핵심 코드
- 별거 없는 어댑터중 하나인데 왜 굳이 또 썼냐 하냐믄..
- 글라이더의 사용법을 익혔기 때문이다.
```
object BindingAdapter {
    @JvmStatic
//    @BindingAdapter("app:imageUrl")
    fun loadImage(imageView: ImageView, url: String){

        val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://~")
        val storageReference = storage.reference
        val pathReference = storageReference.child("images/$url")

        pathReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(imageView.context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(imageView)
        }
    }
}
```

# 12월 3일 (아홉째 날)
## 컨디션 : 뭔가.. 가면갈수록 API가 많아져서 더뎌지는 느낌?? 공부할거 산더미.. ㅜㅡㅜ

### 추가 커밋 내용
#### app/src/main/java/com/softsquared/template/kotlin/src/main/goods/PushGoodsActivity.kt
##### 핵심 코드
- 처음으로 적용해본 API
- 상품 조회 API 이다
- 근데 작동이안됨 (그래서 주석처리가 되어있다)
```
/** 상품조회 API */
//    val goodsGet : () -> Unit = {
//        val initializeRequest = InitializeRequest(
//            "${binding.etContents.text}",
//            true,
//            true,
//            true,
//            "",
//            200,
//            200,
//            Integer.parseInt(binding.etPrice.text.toString()),
//            "",
//            0,
//            200,
//            "${binding.etGoodsTitle.text}",
//            6)
//
//        RetrofitBuilder.api.initRequest(initializeRequest).enqueue(object : Callback<InitializeResponse> {
//            override fun onResponse(
//                call: Call<InitializeResponse>,
//                response: Response<InitializeResponse>
//            ) {
//                if(response.isSuccessful) {
//                    Log.d("test", response.body().toString())
//                    var data = response.body() // GsonConverter를 사용해 데이터매핑
//                }
//            }
//
//            override fun onFailure(call: Call<InitializeResponse>, t: Throwable) {
//                Log.d("test", "실패$t")
//            }
//
//        })
```
- 텍스트를 감시하는 역할의 클래스이다.
- 옵저버 패턴으로 특정 일을 할 수 있다. (천단위가 될때마다 콤마 설정같은경우..?)
```
class CustomTextWatcher internal constructor(private val editText: EditText) : TextWatcher {
    var strAmount = ""
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!TextUtils.isEmpty(s.toString()) && s.toString() != strAmount) {
            strAmount = makeStringComma(s.toString().replace(",", ""))
            editText.setText(strAmount)
            val editable = editText.text
            Selection.setSelection(editable, strAmount.length)
        }
    }

    override fun afterTextChanged(s: Editable) {}
    protected fun makeStringComma(str: String): String {    // 천단위 콤마설정.
        if (str.length == 0) {
            return ""
        }
        val value = str.toLong()
        val format = DecimalFormat("###,###")
        return format.format(value)
    }
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/reqres/RetrofitBuilder.kt, app/src/main/java/com/softsquared/template/kotlin/src/main/reqres/PostGoodsData.kt
##### 핵심 코드
- 처음으로 API를 위해 적용한 레트로핏 빌더와 Data class 들이다. (object와 interface도 있음)
```
package com.softsquared.template.kotlin.src.main.reqres

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//싱글턴
object RetrofitClient {

    //레트로핏 클라이언트 선언
    private  var retrofitClient: Retrofit? = null

    //레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit? {
        Log.e("BUNGGE", "getClient() called")

        if(retrofitClient == null) {

            //레트로핏 빌더를 통해 인스턴스 생성
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofitClient
    }
}
```
- 이게 Data Class들
```
package com.softsquared.template.kotlin.src.main.reqres

data class PostGoodsData(
    val contents: String,
    val isChangable: Boolean,
    val isFreeShop: Boolean,
    val isSafepay: Boolean,
    val location: String,
    val mainCategoryIdx: Int,
    val middleCategoryIdx: Int,
    val price: Int,
    val productStatus: String,
    val quantity: Int,
    val subCategoryIdx: Int,
    val title: String,
    val userIdx: Int
)

data class KeywordGoodsData(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<KeywordResult>
)

data class KeywordResult(
    val createdAt: String,
    val favCnt: Int,
    val imageUrl: String,
    val location: String,
    val price: Int,
    val productIdx: Int,
    val safepay: Boolean,
    val title: String
)
```

# 12월 4일 (열번째 날)
## 컨디션 : 응? 나 살아있네?

#### app/src/main/java/com/softsquared/template/kotlin/config/BaseActivity.kt, app/src/main/java/com/softsquared/template/kotlin/config/BaseFragment.kt
##### 핵심 코드
- 템플릿 기본 적용이 되는 BaseActivity, BaseFragment 속 다이얼로그 함수를 재기했다.
- 다이얼로그와 몇초뒤에 꺼지는 다이얼로그 종료, 그리고 기본 다이얼로그를 설정했다.
```
fun handlerLoadingDialog() {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(Runnable {
        dismissLoadingDialog()
    }, 800)
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/splash/SplashActivity.kt
##### 핵심 코드
- JWT가 있기 전, 스플래시 스크린으로 앱이 실행될때 토큰이 있는지 없는지 판단하여 자동로그인을 해준다.
```
Log.d(TAG, "로그인 방법 : ${prefs.getString("LOGINRESULT", "null")}")
Log.d(TAG, "액세스 토큰: ${prefs.getString("ACCESSTOKEN", "null")}")
Log.d(TAG, "사용자 이름 : ${prefs.getString("USERNAME", "null")}")

val loginResult = prefs.getString("LOGINRESULT", "null")

// 로그인 정보 확인 : 자동로그인
when(loginResult) {
    "NAVER" -> {
        intentT = Intent(this, MainActivity::class.java)
    }
    "KAKAO" -> {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패 $error")
                intentT = Intent(this, LoginActivity::class.java)
            }
            else if (tokenInfo != null) {
                Log.e(TAG, "토큰 정보 보기 성공 $tokenInfo")
                intentT = Intent(this, MainActivity::class.java)
            }
        }
    }
    "MYPHONE" -> {
        intentT = Intent(this, MainActivity::class.java)
    }
    else -> {
        Log.e(TAG, "토큰 정보 보기 실패  ${prefs.getString("LOGINRESULT", "null")}")
        intentT = Intent(this, LoginActivity::class.java)
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/util/LoadingDialog.kt
##### 핵심 코드
- 요곤 그냥 다이얼로그 액티비티
- 신기한건 터치불가로 만드는 기능..?
```
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.softsquared.template.kotlin.databinding.DialogLoadingBinding
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.MainActivity

class LoadingDialog(context: Context) : Dialog(context) {
    private lateinit var binding: DialogLoadingBinding


//    private lateinit var binding: DialogLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_loading)

//        Glide.with(context).load(R.raw.loading_gif).into(binding.ivGifBounge)
        Glide.with(context).load(R.raw.loading_gif)
            .apply(RequestOptions().override(200,200))
            .into(findViewById<ImageView>(R.id.ivGifBounge) as ImageView)

        // 취소 불가능
        setCancelable(false)
        window!!.setBackgroundDrawable(ColorDrawable())
        window!!.setDimAmount(0.2f)
    }

    override fun show() {
        if(!this.isShowing) super.show()
        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}
```

# 12월 5일 (열 한번째 날)
## 컨디션 : 이제는 해탈하고 내 할꺼 그냥 묵묵히 할라구여 ^^7

### 추가 커밋 내용
#### 그래들 추가
```
    implementation 'com.google.firebase:firebase-database-ktx:20.0.4'
    implementation 'com.google.firebase:firebase-storage-ktx:20.0.1'
    implementation 'com.google.firebase:firebase-messaging-ktx:23.1.0'
    
        // Firebase
    implementation platform('com.google.firebase:firebase-bom:31.1.0')
    implementation 'com.google.firebase:firebase-analytics-ktx'
    implementation "com.google.firebase:firebase-storage-ktx"
```

#### app/src/main/java/com/softsquared/template/kotlin/config/BaseActivity.kt
##### 핵심 코드
- 이제는 따로 계속 선언해주기 귀찮고 해서 그냥 Base에 오브젝트 선언해버렸음
```
companion object {
    lateinit var prefs: PreferenceUtil
}
```

#### app/src/main/java/com/softsquared/template/kotlin/config/BaseFragment.kt
##### 핵심 코드
- 별거 아닌데 요것도 설정해줬음 이런 유도리로 할일과 코드의 양을 줄이는 것도 피로에 개선이 됨
```
companion object {
    lateinit var prefs: PreferenceUtil
    lateinit var mainActivity: MainActivity
}

override fun onAttach(context: Context) {
    super.onAttach(context)

    mainActivity = MainActivity()
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/MiniPageActivity.kt - 로그인 첫 화면 창
##### 핵심 코드
- 드디어 내 API 기능들의 기본이 되는 불러오기 코드가 완성되었다!!!
- 레트로핏의 Gson파싱 방법을 통해 서버와 정보를 req, res를 한다.
```
/** Api 불러오기 */
private val PostGoodsAPI = RetrofitClient.apiService

PostGoodsAPI.postGoodsData(goodsdata)
    .enqueue(object : Callback<RequestPostGoods> {
        override fun onResponse(call: Call<RequestPostGoods>, response: Response<RequestPostGoods>) {
            requestPostGoods.value = response.body()
            Log.d(TAG, "응답 메세지 : ${requestPostGoods.value!!.message}")
            Log.d(TAG, "응답 코드 : ${requestPostGoods.value!!.code}")
            Log.d(TAG, "상품 IDX : ${requestPostGoods.value!!.result.productIdx}")
        }
        RESPONSE_STATE.FAIL -> {
            Log.e(TAG,"API 요청에 실패하였습니다 : $responseState")
        override fun onFailure(call: Call<RequestPostGoods>, t: Throwable) {
            Log.d(TAG, "응답 메세지 : ${requestPostGoods.value!!.message}")
            Log.d(TAG, "응답 코드 : ${requestPostGoods.value!!.code}")
        }
    }
})
```
- 이걸로 구현한 네이버 JWT 탐색기
```
PostTokenAPI.postNaverLoginToken("$naverAccessToken")
                                    .enqueue(object : Callback<ResultJWT> {
                                        override fun onResponse(
                                            call: Call<ResultJWT>,
                                            response: Response<ResultJWT>
                                        ) {
                                            jwtResponse.value = response.body()
                                            Log.d(TAG, jwtResponse.value!!.message)
                                            Log.d(TAG, jwtResponse.value!!.code.toString())

                                            when(jwtResponse.value!!.code) {
                                                // 실패코드
                                                3014 -> {
                                                    startActivity(startNext)
                                                }
                                                // 성공코드
                                                1000 -> {
                                                    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                                                    prefs.setString("JWT", jwtResponse.value!!.result.jwt)
                                                    prefs.setInt("USERIDX", jwtResponse.value!!.result.userIdx)
                                                    Log.d(TAG, "JWT : ${jwtResponse.value!!.result.jwt}")
                                                    Log.d(TAG, "USERIDX : ${jwtResponse.value!!.result.userIdx}")
                                                    showCustomToast("네이버 로그인 성공")
                                                    Log.e(TAG, "네이버 로그인 성공")
                                                }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResultJWT>,
                                            t: Throwable
                                        ) {
                                            Log.d(TAG, jwtResponse.value!!.message)
                                            onDestroy()
                                        }
                                    })
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/login/StoreNameActivity.kt
##### 핵심 코드
- 첫 로그인, 즉 회원가입 시 나오는 화면이다.
- 추가 기능은 나만의 상점이름을 정하는 기능이다.
```
class StoreNameActivity : BaseActivity<ActivityStoreNameBinding>(ActivityStoreNameBinding::inflate) {

    /** Api 불러오기 */
    private val PostTokenAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startMain = Intent(this, MainActivity::class.java)
        val startLogin = Intent(this, LoginActivity::class.java)

        binding.btnGoBack.setOnClickListener { finish() }

        /** 첫 로그인시 회원가입 화면 : 나만의 상점 이름 짓기 */
        val tokenResponse = MutableLiveData<ResultToken>()
        binding.btnFinalStoreName.setOnClickListener {
            val howtologin = prefs.getString("LOGINRESULT", "null")
            val mytoken = prefs.getString("ACCESSTOKEN", "null")

            when (howtologin) {
                "NAVER" -> {
                    var myname = MyName(binding.etStoreName.text.toString())
                    prefs.setString("USERNAME", "$myname")

                    PostTokenAPI.postNaverToken(mytoken, myname).enqueue(object : Callback<ResultToken> {
                        override fun onResponse(call: Call<ResultToken>, response: Response<ResultToken>) {
                            tokenResponse.value = response.body()
                            Log.d(TAG, "${tokenResponse.value}")

                            // 화면 전환
                            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            showCustomToast("네이버 로그인 성공")
                            Log.e(TAG, "네이버 로그인 성공")
                        }
                        override fun onFailure(call: Call<ResultToken>, t: Throwable) {
                            Log.d(TAG, "${tokenResponse.value}")
                            startLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(startLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            showCustomToast("네이버 로그인 실패")
                            Log.e(TAG, "네이버 로그인 실패")
                        }
                    })
                }
                else -> {
                    startLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(startLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/BottomSheetDialogSetting.kt
##### 핵심 코드
- 회원 탈퇴 기능의 완성이다.
```
            /** 회원탈퇴 */
            when (loginresult) {
                "KAKAO" -> {
                    kakaoUserDelete.invoke()
//                    deleteUser.invoke() //추후에 카카오 로그인이 구현된다면 사용
                    prefs.destroyData()
                }
                "NAVER" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("회원탈퇴 완료")
                    deleteUser.invoke()
                    prefs.destroyData()
                }
                "MYPHONE" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("회원탈퇴 완료")
                    prefs.destroyData()
                }
                else -> { mainActivity.showCustomToast("회원정보 조회 오류") }
            }
            
            
                /** 회원탈퇴 API */
    val deleteUser : () -> Unit = {

        mainActivity.showLoadingDialog(requireContext())

        val userDelete = MutableLiveData<JustResponse>()
        val userIdx = prefs.getInt("USERIDX", 0)

        UserDataAPI.deleteUserData(userIdx)
            .enqueue(object : retrofit2.Callback<JustResponse> {
                override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                    userDelete.value = response.body()

                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.code}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.message}")
                }
                override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                    t.printStackTrace()

                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.code}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.message}")
                }
            })

        mainActivity.dismissLoadingDialog()
    }
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/myPage/EditUserDataActivity.kt
##### 핵심 코드
- 회원 상점의 이름과 소개글을 수정하는 기능이다.
```
class EditUserDataActivity : BaseActivity<ActivityEditUserDataBinding>(ActivityEditUserDataBinding::inflate){

    /** Api 불러오기 */
    private val UserDataAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = prefs.getString("USERNAME", "null")
        binding.etStoreId.setText(username)

        val maxLength = 1000
        binding.etStoreContents.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

        /** edittext의 사이즈를 재주는 메서드 사용 (addTextChangedListener) */
        binding.etStoreContents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input: String = binding.etStoreContents.text.toString()
                binding.tvTextCount.text = input.length.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /** edittext에 입력되는 값의 유무에 따라 버튼 활성화 */
        binding.etStoreId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.etStoreId.length() > 0) {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnApply.isEnabled = true// editText에 값이 있다면 true 없다면 false

                    if (binding.btnApply.isEnabled) {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }
                } else {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnApply.isEnabled = false// editText에 값이 있다면 true 없다면 false

                    if (binding.btnApply.isEnabled) {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }
                }

                if (binding.etStoreId.length() in 0..2)
                {
                    binding.appCompatTextView.text = "상점명은 최소 2자, 최대 10자까지 입력 가능합니다."
                    binding.appCompatTextView.setTextColor(Color.parseColor("#D21404"))
                    binding.view2.setBackgroundColor(Color.parseColor("#D21404"))
                } else {
                    binding.appCompatTextView.text = "한글, 영어, 숫자만 사용할 수 있어요. (최대 10자)"
                    binding.appCompatTextView.setTextColor(Color.parseColor("#000000"))
                    binding.view2.setBackgroundColor(Color.parseColor("#000000"))
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnApply.setOnClickListener {
            val resultPatchUser = MutableLiveData<JustResponse>()
            val myname = MyName(binding.etStoreId.text.toString())
            val userIdx = prefs.getInt("USERIDX", 0)

            /** 상품조회 API */
            UserDataAPI.patchUserData(myname, userIdx)
                .enqueue(object : retrofit2.Callback<JustResponse> {
                    override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                        resultPatchUser.value = response.body()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        prefs.setString("USERNAME", binding.etStoreId.text.toString())
                        Log.e(TAG, "상점이름 : ${binding.etStoreId.text.toString()}")

                        finish()
                    }
                    override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                        t.printStackTrace()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        finish()
                    }
                })
        }

        binding.btnGoBack.setOnClickListener { finish() }
    }
}
```

# 12월 6일 (열 두번째 날)
## 컨디션 : 이야 3일 남았다~ (어떡하냐..;;)

### 추가 커밋 내용
#### app/src/main/java/com/softsquared/template/kotlin/src/main/goods/PushGoodsActivity.kt
##### 핵심 코드
- 상품이 등록 될때 사용되는 데이터를 로그로 찍어보기 위한 수단!
```
/** 상품등록 API */
binding.btnRegistPay.setOnClickListener {
    showLoadingDialog(this)
    val userIDX = prefs.getInt("USERIDX",0)
    Log.d(TAG, "onCreate: $userIDX")
    val title : EditText = binding.etGoodsTitle
    val contents : EditText = binding.etContents
    val price : EditText = binding.etPrice
    val safePay : Boolean = binding.cbPay.isChecked
    val titleT : EditText = binding.etGoodsTitle
    Log.d(TAG, "title: $titleT")
    val priceT : EditText = binding.etPrice
    Log.d(TAG, "price: $priceT")
    val contentsT : EditText = binding.etContents
    Log.d(TAG, "contents: $contentsT")
    val issafePayT : Boolean = binding.cbPay.isChecked
    Log.d(TAG, "issafePay: $issafePayT")
    imagelistT.add("https://firebasestorage.googleapis.com/v0/b/bunggeclone.appspot.com/o/banner_5.png?alt=media&token=cbc5ab37-cec0-4557-a187-9d38ee075467")
    Log.d(TAG, "imagelist: $imagelistT")
    val userIDXT = prefs.getInt("USERIDX",0)
    Log.d(TAG, "userIDX: $userIDXT")

    val goodsdata = PostGoodsData(
        "$title",
        "$contents",
        userIDX,
        "$price",
        safePay
        "$titleT",
        "$priceT",
        "$contentsT",
        issafePayT,
        "$imagelistT",
        userIDXT
    )
    Log.d(TAG, "${goodsdata}")
    
    val goodsdata = PostGoodsData(
        "$title",
        "$contents",
        userIDX,
        "$price",
        safePay
        "$titleT",
        "$priceT",
        "$contentsT",
        issafePayT,
        "$imagelistT",
        userIDXT
    )
    Log.d(TAG, "${goodsdata}")
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/goods/GallaryActivity.kt
##### 핵심 코드
- 상품 등록시 , 내 핸드폰의 갤러리에서 이미지를 bitmap으로 가져와서
외부 로컬스토리지(파이어베이스)로 저장해주는 기능을 추가로 만들었다.
```
class GallaryActivity : BaseActivity<ActivityGallaryBinding>(ActivityGallaryBinding::inflate) {

    /** 파이어베이스 : 갤러리 저장 */
    private val storage by lazy {
        Firebase.storage
    }

    data class ImgGridData (
        var imgGrid : String
    )

//    private lateinit var mSuccessHandler: Handler
//    private lateinit var mErrorHandler: ErrorHandler
    private lateinit var imageURI : Uri
    private val datas = mutableListOf<ImgGridData>()
    lateinit var imgGridRVAdapter: ImgGridRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSubmitItemButton()
        initRecycler()
        initAddImageButton()
    }

    private fun initAddImageButton() {
        binding.btnAddPhoto.setOnClickListener {
            Log.d(TAG,"사진 추가 -> 권한 확인")
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                -> {
                    // 권한이 존재하는 경우
                    getImageFromAlbum()
                    Log.d(TAG,"권한 확인 -> 갤러리 이동")
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    // 권한이 거부 되어 있는 경우
                    showPermissionContextPopup()
                    Log.d(TAG,"권한 확인 -> 권한 실패")
                }
                else -> {
                    // 처음 권한을 시도했을 때 띄움
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        ETC.REQUEST_FIRST
                    )
                }
            }
        }
        Log.e(TAG, "initAddImageButton: 끝", )
    }

    private fun showPermissionContextPopup() {
    }

    private fun getImageFromAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, ETC.REQUEST_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK) {
            showCustomToast("잘못된 접근입니다")
            return
        }
        when(requestCode){
            ETC.REQUEST_GET_IMAGE -> {
                val selectedImageURI : Uri? = data?.data
                if( selectedImageURI != null ) {
                    imageURI = selectedImageURI
                    addTask("$imageURI")
                    binding.tvTextCount.text = imgGridRVAdapter.datas.size.toString()
                }else {
                    showCustomToast("이미지를 가져오지 못했습니다")
                }
            }
            else -> {
                showCustomToast("이미지를 가져오지 못했습니다")
            }
        }
        Log.e(TAG, "onActivityResult: 끝", )
    }

    private fun initSubmitItemButton() {
        binding.btnCheck.setOnClickListener {
            // 선택된 이미지 uri가 존재할 경우
            // 이미지를 스토리지에 저장시킴
            // 스토리지에 저장 시킨 후 다시 값을 받아와 모델에 uri값을 넣어 만들어 줘야함
            showLoadingDialog(this)
            var imageuri = arrayListOf<String>()
            if (imageURI != null) {
                uploadPhoto(
                    imageURI,
                    mSuccessHandler = { uri ->
                        Log.i(TAG, "사진 업로드 성공")
                        showCustomToast("사진 업로드 성공")

                        val count = prefs.getInt("IMAGECOUNT", 100)+1

                        if (count == datas.size) {
                            imageuri.add(uri)
                            prefs.setStringArray("IMAGEARRAY", imageuri)
                            Log.i(TAG, "완성된 배열 : $imageuri")
                            dismissLoadingDialog()
                            finish()
                        } else {
                            imageuri.add(uri)
                        }
                    },
                    mErrorHandler = {
                        showCustomToast("사진 업로드에 실패")
                        dismissLoadingDialog()
                    })
            } else {
                // 이미지 uri가 존재하지 않는 경우
                Log.e(TAG, "이미지 정보(URI)가 없어요!")
                dismissLoadingDialog()
            }
        }
        Log.e(TAG, "initSubmitItemButton: 끝", )
    }

    private fun uploadPhoto(
        imageURI: Uri,
        mSuccessHandler: (String) -> Unit,
        mErrorHandler: () -> Unit,
    ) {
        for (i in 0 until datas.size) {
            val fileName = "${System.currentTimeMillis()}.png"
            storage.reference.child("article/photo").child(fileName)
                .putFile(datas[i].imgGrid.toUri())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // 파일 업로드에 성공했기 때문에 파일을 다시 받아 오도록 해야함
                        storage.reference.child("article/photo").child(fileName).downloadUrl
                            .addOnSuccessListener { uri ->
                                mSuccessHandler(uri.toString())
                            }.addOnFailureListener {
                                mErrorHandler()
                            }
                        prefs.setInt("IMAGECOUNT", i)
                    } else {
                        mErrorHandler()
                    }
                }
            /** 사진 업로드 되면 */
            prefs.setInt("URIMAXSIZE", datas.size)
            prefs.setString("URIFAKEIMAGE", "${datas[i].imgGrid.toUri()}")
            Log.e(TAG, "$i 번째 갤러리 이미지 URI : ${datas[i].imgGrid.toUri()}")
        }
    }

    /** 배열 추가 */
    private fun initRecycler() {
        imgGridRVAdapter = ImgGridRVAdapter(this)
        binding.rvAddPhoto.adapter = imgGridRVAdapter

        datas.apply {
        }

        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        binding.rvAddPhoto.layoutManager = gridLayoutManager
        imgGridRVAdapter.datas = datas
        imgGridRVAdapter.notifyDataSetChanged() // 검색결과 배열 갱신
    }

    /** 외부에서 배열에 추가할 수 있게 사용하는 곳 */
    fun addTask(AddImage: String) {
        //editText의 결과를 가져온다.
        val result = ImgGridData(AddImage)
        datas.add(result)

        Log.e(TAG, "$datas")

        binding.rvAddPhoto.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
    }
}
```

#### app/src/main/java/com/softsquared/template/kotlin/src/main/goods/GallaryActivity.kt
##### 핵심 코드
- 상품 등록시 , 내 핸드폰의 갤러리에서 이미지를 bitmap으로 가져와서
  외부 로컬스토리지(파이어베이스)로 저장해주는 기능을 추가로 만들었다.



# 12월 7일 (열 세번째 날)
## 컨디션 : 이야 이틀 남았다~ (어떡하냐..;;)
### 이 날은 아마 별도의 스케줄로 XML 수정 이외에 미룬게 많았다..

# 12월 7일 (열 세번째 날)
## 컨디션 : 이야 이틀 남았다~ (어떡하냐..;;)
### 이 날은 아마 별도의 스케줄로 XML 수정 이외에 미룬게 많았다..

# 12월 8일 (열 네섯째 날)
## 컨디션 : 마지막 날이다. (어떡하냐..;;)

### 필수 커밋 내용
#### 최종으로 내가 사용한 서버 연동 API 들이다
##### 핵심코드
```
interface IRetrofit {

    /** Keyword 로 Regist 한 Goods 들을 Inquire 하는 Get request API */
    @GET(API.GET_PRODUCTS)
    fun getGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Query("keyword") keyword: String
    ) : Call<GetGoodsData>

    /** Regist 한 모든 Goods 들을 Inquire 하는 Get request API */
    @GET(API.GET_PRODUCTS)
    fun getAllGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
    ) : Call<GetGoodsData>

    /** 특정 상품을 조회하는 Get request API */
    @GET(API.GET_SPECIAL_PRODUCTS)
    fun getSpecialGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<GetSpecialGoodsData>

    /** 특정 상품의 이미지 조회하기 */
    @GET(API.GET_PRODUCTS_IMAGE)
    fun getGoodsImage(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<GetGoodsImageAPI>

    /** UserIdx 로 특정 User 를 Inquire 하는 GET request API */
    @GET(API.GET_USER_DATA)
    fun getUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<GetUserData>

    /** 전체 Categories 조회하기 */
    @GET(API.GET_CATEGORIES_DATA)
    fun getCategoriesData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
    ) : Call<GetCategoriesAPI>

    /** 전체 Categories 조회하기 */
    @GET(API.GET_CATEGORIES_NUM_DATA)
    fun getCategoriesDataNum(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("categoryNum") categoryNum: Int
    ) : Call<GetCategoriesNumAPI>

    /** 특정 회원에 대한 후기 조회하기 */
    @GET(API.GET_REVIEW_DATA)
    fun getReviewData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<GetReviewData>



    /** NAVER_ACCESSTOKEN 을 POST request 하여 회원가입 하는 API */
    @POST(API.POST_NEW_TOKEN)
    fun postNaverToken(
        @Header("NAVER-ACCESS-TOKEN") naverAccessToken: String,
        @Body name: MyName
    ) : Call<ResultToken>

    /** NAVER_ACCESSTOKEN 을 POST request 하여 JWT 및 UserIdx 를 Response 하는 API : 로그인 API */
    @POST(API.POST_LOGIN_TOKEN)
    fun postNaverLoginToken(
        @Header("NAVER-ACCESS-TOKEN") naverAccessToken: String
    ) : Call<ResultJWT>

    /** New Goods 들을 Regist 하기 위해 Post request 하는 API */
    @POST(API.POST_GOODS_DATA)
    fun postGoodsData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body postgoodsdata: PostGoodsData
    ) : Call<RequestPostGoods>

    /** 찜하기 or 찜 해제 기능의 API */
    @POST(API.POST_FAVORITE_PRODUCT)
    fun postProductFav(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<PostFavorite>



    /** regist 된 user 의 UserIdx 를 가지고 정보를 완전히 Delete : 회원탈퇴 */
    @PATCH(API.DELETE_USER_DATA)
    fun deleteUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** regist 된 user 의 UserIdx 를 가지고 정보를 수정 */
    @PATCH(API.PATCH_USER_DATA)
    fun patchUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body name: MyName,
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** regist 된 user 의 UserIdx 를 가지고 정보를 수정 */
    @PATCH(API.PATCH_STORE_INFO)
    fun patchStoreInfoData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body introduce: Introduce,
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** 상품 내용 수정 API */
    @PATCH(API.PATCH_EDIT_PRODUCTS)
    fun patchEditProducts(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body editProducts: EditProducts,
        @Path("productIdx") productIdx: Int
    ) : Call<JustResponse>

    

    /** 특정 상품 삭제 */
    @DELETE(API.PATCH_EDIT_PRODUCTS)
    fun deleteProducts(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<JustResponse>
}
```

#### 또한 아래는 사용한 API에 따른 데이터 클라스들이다.
##### 핵심 코드
```
** 특정 회원조회 API */
// res
data class GetUserData (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetUserDataResult
)

data class GetUserDataResult(
    val auth: Int,
    val safeCnt: Int,
    val createdAt: String,
    val followerCnt: Int,
    val followingCnt: Int,
    val name: String,
    val profileImg: Any,
    val rate: Double,
    val sellCnt: Int,
    val userIdx: Int,
    val introduce: String
)

/** 모든 카테고리 조회 API */
// res
data class GetCategoriesAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCategoriesDataResult>
)

data class GetCategoriesDataResult(
    val mainCategory: Int,
    val mainCategoryName: String,
    val middleCategory: Int,
    val middleCategoryName: String,
    val subCategory: Int,
    val subCategoryName: String
)

/** 특정 회원에 대한 후기 조회 API */
// res
data class GetReviewData (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetReviewDataResult>
)

data class GetReviewDataResult(
    val reviewIdx: Int,
    val rate: Double,
    val contents: String,
    val title: String,
    val name: String,
    val reviewerIdx: Int,
    val createdAt: Timestamp,
    val revieweeIdx: Int,
    val productIdx: Int
)

/** 특정 카테고리로 조회 API */
// res
data class GetCategoriesNumAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCategoriesResult>
)

data class GetCategoriesResult(
    val productIdx: Int,
    val createdAt: Timestamp,
    val imageUrl: String,
    val title: String,
    val location: String,
    val price: String,
    val tradeStatus: String,
    val isFreeShip: Boolean,
    val isFav: Boolean,
    val isSafepay: Boolean
)

/** 특정 상품의 이미지 조회 API */
// res
data class GetGoodsImageAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetGoodsImageResult
)

data class GetGoodsImageResult (
    val productIdx: Int,
    val imageList: List<String>
)

/** 상품등록 API */
// req
data class PostGoodsData(
    val title: String,
    val price: String,
    val contents: String,
    val isSafepay: Boolean,
    val imageList: List<String>,
    val userIdx: Int,
    val mainCategoryIdx: Int,
    val middleCategoryIdx: Int,
    val subCategoryIdx: Int
)

// res
data class RequestPostGoods(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ResultReqPostGoods
)

data class ResultReqPostGoods(
    val productIdx: Int
)

/** 상품 찜하기 & 찜해제 API */
data class PostFavorite(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<ResultFavorite>
)

data class ResultFavorite(
    val favoriteIdx: Int
)

/** 상품조회 API */
data class GetGoodsData(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<ResultGoods>
)

data class ResultGoods(
    val productIdx: Int,
    val imageUrl: String,
    val isSafepay: Boolean,
    val title: String,
    val price: String,
    val location: String,
    val contents: String,
    val createdAt: Timestamp,
    val favCnt: Int,
    val isFav: Boolean
)

/** 상품조회 API */
data class GetSpecialGoodsData(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ResultGetSpecialGoods
)

data class ResultGetSpecialGoods(
    val productIdx: Int,
    val price: String,
    val title: String,
    val location: String,
    val createdAt: Timestamp,
    val productStatus: String,
    val quantity: Int,
    val favCnt: Int,
    val chatCnt: Int,
    val contents: String,
    val isFreeShip: Boolean,
    val isChangable: Boolean
)

/** 토큰삽입 API */
data class ResultToken(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)

data class Result(
    val userIdx: Int,
    val jwt: String
)

/***/
data class MyName(
    val name: String
)

/** 소개글 API */
data class Introduce(
    val introduce: String
)

/** 상품 내용 수정 API */
data class EditProducts(
    val quantity: Int,
    val mainCategory: Int,
    val middleCategory: Int,
    val subCategory: Int,
    val title: String,
    val price: String,
    val location: String,
    val contents: String,
    val productStatus: String,
    val isChangable: Boolean,
    val isFreeShip: Boolean,
    val isSafePay: Boolean
)

/** 토큰삽입 API */
data class ResultJWT(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: JWTResult
)

data class JWTResult(
    val jwt: String,
    val userIdx: Int
)

/** 그냥 response만 받을때 */
data class JustResponse (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: String
)

/** response 에러 */
//data class ResponseError (
//    val timestamp: String,
//    val status: Int,
//    val error: String,
//    val message: String,
//    val path: String
//)
```