package com.konkuk.boost.utils

import android.content.Context
import android.util.Log
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback

object KaKaoUtils {
    fun share(context: Context) {
        val template = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    MessageUtils.APP_KOREAN_NAME,
                    "https://user-images.githubusercontent.com/54172475/105176309-7144e900-5b68-11eb-9eae-2662f356bb66.jpg", // 공유링크 이미지
                    LinkObject.newBuilder().setWebUrl(MessageUtils.KAKAO_WEB_URL)
                        .setMobileWebUrl(MessageUtils.KAKAO_WEB_URL).build()
                )
                    .setDescrption(MessageUtils.APP_DESCRIPTION)
                    .setImageHeight(300)
                    .setImageWidth(600)
                    .build()
            )
            .addButton(
                ButtonObject(
                    MessageUtils.KAKAO_SHARE_BUTTON_TITLE, LinkObject.newBuilder()
                        .setWebUrl(MessageUtils.KAKAO_WEB_URL)
                        .setMobileWebUrl(MessageUtils.KAKAO_WEB_URL)
                        .setAndroidExecutionParams(MessageUtils.KAKAO_EXECUTION_PARAMS)
                        .setIosExecutionParams(MessageUtils.KAKAO_EXECUTION_PARAMS)
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
                    Log.e(MessageUtils.LOG_KEY, errorResult.toString())
                }

                override fun onSuccess(result: KakaoLinkResponse?) {
                }
            }
        )
    }
}