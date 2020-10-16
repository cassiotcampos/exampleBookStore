package com.cassio.cassiobookstore.view

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

abstract class ImageUtils {

    companion object {

        fun loadImgAndBgFromUrlWithOneRequest(
            context: Context,
            url: String,
            targetImgViewThumb: ImageView,
            targetImgViewBg: ImageView,
            priority: Priority = Priority.LOW,
            diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL,
            withTransition: Boolean = false
        ) {

            val myOptions = RequestOptions()
                .priority(priority)
                .diskCacheStrategy(diskCacheStrategy)

            var builder = Glide.with(context)
                .asBitmap()

            if (withTransition) {
                builder = builder.transition(BitmapTransitionOptions.withCrossFade())
            }

            builder = builder
                .apply(myOptions)
                .load(url)


            builder.into(object : SimpleTarget<Bitmap?>() {

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {

                    transition?.let {
                        val transitionAppliedThumb = transition.transition(
                            resource, BitmapImageViewTarget(targetImgViewThumb)
                        )

                        if (!transitionAppliedThumb) {
                            targetImgViewThumb.setImageBitmap(resource)
                        }

                        val transitionAppliedBg = transition.transition(
                            scaleBmp(resource), BitmapImageViewTarget(targetImgViewBg)
                        )

                        if (!transitionAppliedBg) {
                            targetImgViewBg.setImageBitmap(scaleBmp(resource))
                        }
                    }
                }
            })
        }


        fun loadImgFromUrl(
            context: Context,
            url: String,
            targetImgView: ImageView,
            priority: Priority = Priority.LOW,
            diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.ALL,
            withTransition: Boolean = false,
            withBlurryEffect: Boolean = false
        ) {
            val myOptions = RequestOptions()
                .priority(priority)
                .diskCacheStrategy(diskCacheStrategy)

            var builder = Glide.with(context)
                .asBitmap()

            if (withTransition) {
                builder = builder.transition(BitmapTransitionOptions.withCrossFade())
            }

            builder = builder
                .load(url)
                .apply(myOptions)

            if (withBlurryEffect) {
                builder.into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {

                        transition?.let {
                            val transitionApplied = transition.transition(
                                scaleBmp(resource), BitmapImageViewTarget(targetImgView)
                            )

                            if (!transitionApplied) {
                                targetImgView.setImageBitmap(scaleBmp(resource))
                            }
                        }
                    }
                })
            } else {
                builder.into(targetImgView)
            }
        }

        // used to apply blur effect decreasing the size of the original image
        private fun scaleBmp(bitmap: Bitmap): Bitmap {
            return Bitmap.createScaledBitmap(
                bitmap,
                2,
                4,
                true
            )
        }

    }
}