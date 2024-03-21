package com.softsquared.template.kotlin.src.main.login

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.*
import android.view.ViewTreeObserver
import android.view.Window
import android.widget.EditText
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMyPhoneBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.util.PreferenceUtil


class MyPhoneActivity : BaseActivity<ActivityMyPhoneBinding>(ActivityMyPhoneBinding::inflate) {

    val SUBACTIITY_REQUEST_CODE = 100

    private var count: Int = 0
    private lateinit var editText: EditText
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.llBirth.hide()
        binding.llPhoneComapany.hide()
        binding.llPhoneNumber.hide()
        binding.btnNextRegisterBirth.hide()
        binding.btnNextRegisterPhone.hide()

        /** 버튼 비활성화 */
        binding.btnNextRegister.isEnabled = false
        binding.btnNextRegister2.isEnabled = false
        binding.btnNextRegisterBirth.isEnabled = false
        binding.btnNextRegisterPhone.isEnabled = false

        binding.btnNextRegister.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
        binding.btnNextRegister2.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
        /** 기초 설정 완*/

        /** editText를 살펴보기 위한 변수선언 */
        var message: String = ""

        editText = findViewById<EditText>(R.id.et_name_plz)

        /** 화면 출력시, editText 창이 바로 뜨게하기 */
        editText.requestFocus()

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /** 메세지 입력 값 담기 */
                message = editText.text.toString()

                /** 값 유무에 따른 활성화 여부 */
                binding.btnNextRegister.isEnabled =
                    message.isNotEmpty() // editText에 값이 있다면 true 없다면 false
                binding.btnNextRegister2.isEnabled =
                    message.isNotEmpty() // editText에 값이 있다면 true 없다면 false

                if (binding.btnNextRegister.isEnabled && binding.btnNextRegister2.isEnabled) {
                    binding.btnNextRegister.setBackgroundResource(R.drawable.rectangle_ripple_effect)
                    binding.btnNextRegister2.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                } else {
                    binding.btnNextRegister.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    binding.btnNextRegister2.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window, onShowKeyboard = {
            binding.clBackground.run {
                //smoothScrollTo(scrollX, scrollY + keyboardHeight)
                //키보드 올라왔을때 원하는 동작
                when (count) {
                    0 -> binding.btnNextRegister2.show()
                    else -> binding.btnNextRegister2.makeInvisible()
                }
            }
        },
            onHideKeyboard = {
                binding.clBackground.run {
                    //키보드 내려갔을때 원하는 동작
                    //smoothScrollTo(scrollX, scrollY + keyboardHeight)
                    binding.btnNextRegister2.makeInvisible()
                }
            })

        binding.btnNextRegister.setOnClickListener {
            when (count) {
                0 -> {
                    tvBirthStart()
                    binding.nameView.setBackgroundResource(R.color.lightgray)
                    binding.btnNextRegister.hide()
                    binding.btnNextRegisterBirth.show()
                }
                else -> tvPhoneCompanyStart()
            }
        }

        binding.btnNextRegister2.setOnClickListener {
            when (count) {
                0 -> {
                    tvBirthStart()
                    binding.nameView.setBackgroundResource(R.color.lightgray)
                }
                else -> tvPhoneCompanyStart()
            }
        }

        binding.btnPhoneCompany.setOnClickListener {
            count = 2
            when (count) {
                2 -> {
                    val intent = Intent(this, PhoneCompanyActivity::class.java)
                    startActivityForResult(intent, SUBACTIITY_REQUEST_CODE)
                }
            }
        }

//        /** 뭔지 잘은 모르겠지만 아마 메소드 매니저인듯,, 필수요소임 */
//        val imm: InputMethodManager =
//            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }

    fun tvBirthStart () {
        count = 1

        binding.tvNamePlz.text = "생년월일을\n입력해주세요"
        binding.btnNextRegister2.hide()
        binding.llBirth.show()
        binding.etBirthPlz.requestFocus()
        binding.btnNextRegister.hide()
        binding.btnNextRegisterBirth.show()

        binding.etBirthPlz.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //텍스트를 입력 후

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
    }

    fun tvPhoneCompanyStart () {
        count = 2

        binding.tvNamePlz.text = "통신사를\n선택해주세요"
        binding.llPhoneComapany.show()

        val intent = Intent(this, PhoneCompanyActivity::class.java)
        startActivityForResult(intent, SUBACTIITY_REQUEST_CODE)
    }

    fun tvPhoneNumberStart () {

        binding.btnNextRegisterBirth.hide()
        binding.btnNextRegisterPhone.show()
        binding.tvNamePlz.text = "휴대폰번호를\n입력해주세요"
        binding.llPhoneNumber.show()
        binding.etNumberPlz.requestFocus()

        binding.etNumberPlz.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.etNumberPlz.length() == 11) {
                    binding.btnNextRegisterPhone.text = "확인"
                    binding.tvNamePlz.text = "입력한 정보를\n확인해주세요"

                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnNextRegisterPhone.isEnabled = true// editText에 값이 있다면 true 없다면 false

                    if (binding.btnNextRegisterPhone.isEnabled) {
                        binding.btnNextRegisterPhone.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnNextRegisterPhone.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }

                } else {
                    binding.btnNextRegisterPhone.isEnabled = false// editText에 값이 있다면 true 없다면 false

                    if (binding.btnNextRegisterPhone.isEnabled) {
                        binding.btnNextRegisterPhone.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnNextRegisterPhone.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnNextRegisterPhone.setOnClickListener {
            if (binding.etNamePlz.length() >= 2 && binding.etBirthPlz.length() == 6 && binding.etBirthBack.length() == 1 && binding.etNumberPlz.length() == 11) {
                val i = Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                showCustomToast("본인인증으로 로그인")
                startActivity(i)
                saveMyInfo.invoke()
                prefs.setString("LOGINRESULT", "MYPHONE")
            }
        }
    }

    /** Activity Result 가 있는 경우 실행되는 콜백함수 */
    @Deprecated("Deprecated in Java")
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

    val saveMyInfo : () -> Unit = {
        val myname = binding.etNamePlz.text
        prefs.setString("USERNAME", "$myname")
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