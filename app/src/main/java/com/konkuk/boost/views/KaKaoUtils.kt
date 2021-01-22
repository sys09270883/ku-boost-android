package com.konkuk.boost.views

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback

object KaKaoUtils{
    fun share(context : Context){
        val template = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    "건국대학교 부스트", // 공유링크 타이틀
                    "https://user-images.githubusercontent.com/54172475/105176309-7144e900-5b68-11eb-9eae-2662f356bb66.jpg", // 공유링크 이미지
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()
                )
                    .setDescrption("건국대학교의 모든 정보를 편리하게 확인해보세요.") // 공유링크 설명
                    .setImageHeight(300)
                    .setImageWidth(600)
                    .build()
            )
            .addButton(
                ButtonObject(
                    "앱에서 보기", LinkObject.newBuilder() // 버튼 타이틀
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs: MutableMap<String, String> =
            HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

        KakaoLinkService.getInstance().sendDefault(
            context,
            template,
            serverCallbackArgs,
            object : ResponseCallback<KakaoLinkResponse?>() {
                override fun onFailure(errorResult: ErrorResult) {
                    Log.d("kakao_utils",errorResult.toString())
                }
                override fun onSuccess(result: KakaoLinkResponse?) {
                }
            }
        )
    }
}