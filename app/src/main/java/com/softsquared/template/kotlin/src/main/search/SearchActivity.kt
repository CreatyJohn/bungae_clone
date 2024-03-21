package com.softsquared.template.kotlin.src.main.search

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivitySearchBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.home.GoodsData
import com.softsquared.template.kotlin.src.main.home.SearchGoodsRVAdapter
import com.softsquared.template.kotlin.src.main.login.KeyboardVisibilityUtils


class SearchActivity : BaseActivity<ActivitySearchBinding>(ActivitySearchBinding::inflate) {

    private lateinit var KeyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.appbarlayout.bringToFront()

        binding.btnGoBack.setOnClickListener { finish() }
        binding.btnGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.btnCheckText.setOnClickListener {
            binding.etSearch.setText("")
        }

        pressRecommends.invoke()
        pressCategories.invoke()
        checkEditText.invoke()
        keyboardView.invoke()

        /** 키보드의 검색 버튼을 눌렀을 시, 실행되는 메서드 */
        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })

        /** 화면 출력시, editText 창이 바로 뜨게하기 */
        binding.etSearch.requestFocus()
    }

    private fun performSearch() {
        prefs.setString("SEARCH", binding.etSearch.text.toString())
        val intent = Intent(this, ResultSearchActivity::class.java)
        startActivity(intent)
    }

    /** 키보드 자동 출력 및 조건실행하는 람다 함수 */
    val keyboardView : () -> Unit = {
        KeyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = {
                //키보드가 보여질때
            },
            onHideKeyboard = {
                //키보드가 안보여질때
            })
    }

    /** edittext의 유무를 체크하는 람다 함수 */
    val checkEditText : () -> Unit = {
        /** edittext에 입력되는 값의 유무에 따라 버튼 활성화 */
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.etSearch.length() > 0) {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnCheckText.isEnabled = true// editText에 값이 있다면 true 없다면 false

                    if (binding.btnCheckText.isEnabled) {
                        binding.btnCheckText.show()

                    } else {
                        binding.btnCheckText.hide()
                    }
                } else {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnCheckText.isEnabled = false// editText에 값이 있다면 true 없다면 false

                    if (binding.btnCheckText.isEnabled) {
                        binding.btnCheckText.show()

                    } else {
                        binding.btnCheckText.hide()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
    
    /** 해당 카테고리를 누르면 누른 카테고리로 검색 됨 */
    val pressCategories : () -> Unit = {
        binding.btnCategoryBike.setOnClickListener {
            val textvalue = binding.tvMotobike.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategorySnikerz.setOnClickListener {
            val textvalue = binding.tvSnikerz.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryWatch.setOnClickListener {
            val textvalue = binding.tvWatch.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryStarGoods.setOnClickListener {
            val textvalue = binding.tvStargoods.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryCycler.setOnClickListener {
            val textvalue = binding.tvCycler.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryRider.setOnClickListener {
            val textvalue = binding.tvRider.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryFigure.setOnClickListener {
            val textvalue = binding.tvFiguar.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryCamping.setOnClickListener {
            val textvalue = binding.tvCamping.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryHealth.setOnClickListener {
            val textvalue = binding.tvHealth.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategorySoccer.setOnClickListener {
            val textvalue = binding.tvSoccer.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryNintendo.setOnClickListener {
            val textvalue = binding.tvNintendo.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.btnCategoryDslr.setOnClickListener {
            val textvalue = binding.tvDslr.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }
    }

    /** 해당 추천어를 누르면 누른 추천어로 검색 됨 */
    val pressRecommends : () -> Unit = {
        binding.llFirstRecommends.setOnClickListener {
            val textvalue = binding.tvFirstRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSecondRecommends.setOnClickListener {
            val textvalue = binding.tvSecondRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llThirdRecommends.setOnClickListener {
            val textvalue = binding.tvThirdRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llFourthRecommends.setOnClickListener {
            val textvalue = binding.tvFourthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llFifthRecommends.setOnClickListener {
            val textvalue = binding.tvFifthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSixthRecommends.setOnClickListener {
            val textvalue = binding.tvSixthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSeventhRecommends.setOnClickListener {
            val textvalue = binding.tvSeventhRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llEighthRecommends.setOnClickListener {
            val textvalue = binding.tvEighthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llNinethRecommends.setOnClickListener {
            val textvalue = binding.tvNinethRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llTenthRecommends.setOnClickListener {
            val textvalue = binding.tvTenthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }
    }
}

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