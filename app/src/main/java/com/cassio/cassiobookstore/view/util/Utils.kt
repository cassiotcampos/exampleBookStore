package com.cassio.cassiobookstore.view.util

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

class Utils {
    companion object {
        // used to apply blur effect decreasing the size of the original image
        fun scaleBmp(bitmap: Bitmap): Bitmap {
            return Bitmap.createScaledBitmap(
                bitmap,
                2,
                4,
                true
            )
        }

        fun loadBlurriedImg(context: Context, url: String?, targetImgView: ImageView) {
            val myOptions = RequestOptions()
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(context)
                .asBitmap()
                .transition(BitmapTransitionOptions.withCrossFade())
                .load(url)
                .apply(myOptions)
                .into(object : SimpleTarget<Bitmap?>() {

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap?>?
                    ) {

                        transition?.let {
                            val transitionApplied = transition.transition(
                                Utils.scaleBmp(resource), BitmapImageViewTarget(targetImgView)
                            )

                            if (!transitionApplied) {
                                targetImgView.setImageBitmap(Utils.scaleBmp(resource))
                            }
                        }
                    }
                })
        }
    }
}