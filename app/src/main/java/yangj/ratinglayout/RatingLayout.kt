package yangj.ratinglayout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout

/**
 * Created by YangJ on 2018/12/28.
 */
class RatingLayout : LinearLayout {

    companion object {
        /**
         * 默认星星数量为5个
         */
        private const val DEFAULT_STAR_COUNT = 5
    }

    /**
     * star间距
     */
    private var mStarPadding = 0

    /**
     * star评分等级
     */
    private var mStarLevel = 0

    /**
     * star评分等级是否仅仅用于展示并且不可更改
     */
    private var mIsIndicator = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        val metrics =  resources.displayMetrics
        mStarPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, metrics).toInt()
        for (i in 1..DEFAULT_STAR_COUNT) {
            addView(createRatingView(i))
        }
    }

    /**
     * 创建star
     */
    private fun createRatingView(index: Int): ImageView {
        val imageView = ImageView(context)
        imageView.id = index
        imageView.tag = false
        imageView.setImageResource(R.drawable.a8i)
        imageView.setPadding(mStarPadding, mStarPadding, mStarPadding, mStarPadding)
        imageView.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f)
        imageView.setOnClickListener { it ->
            if (mIsIndicator) return@setOnClickListener
            mStarLevel = it.id
            notifyChildViewChanged()
        }
        return imageView
    }

    /**
     * 根据点击的star作出相关改变，例如：
     * 1.所有star均未点亮，主动点击最后一个star时，需要将左边的star全部点亮
     * 2.所有star均点亮，主动点击第一个star时，需要将右边的star全部熄灭
     */
    private fun notifyChildViewChanged() {
        val index = mStarLevel - 1
        // 对当前点击的star点亮状态进行判断
        val isEnabled = getChildAt(index).tag as Boolean
        if (isEnabled) {
            // 如果当前点击的star点亮状态为点亮
            val count = childCount
            for (i in index + 1 until count) {
                val imageView = getChildAt(i) as ImageView
                val tag = imageView.tag as Boolean
                if (tag) {
                    imageView.tag = !tag
                    imageView.setImageResource(R.drawable.a8i)
                }
            }
        } else {
            // 如果当前点击的star点亮状态为熄灭
            for (i in 0..index) {
                val imageView = getChildAt(i) as ImageView
                val tag = imageView.tag as Boolean
                if (tag) {
                    continue
                }
                imageView.tag = !tag
                imageView.setImageResource(R.drawable.a8h)
            }
        }
    }

    /**
     * star评分等级是否仅仅用于展示并且不可更改
     * @param isIndicator 参数为true表示不可以更改评分等级，false表示可以更改评分等级
     */
    fun setIsIndicator(isIndicator: Boolean) {
        mIsIndicator = isIndicator
    }

    /**
     * 获取star评分等级
     */
    fun getLevel(): Int {
        return mStarLevel
    }

    /**
     * 设置star评分等级
     */
    fun setLevel(level: Int) {
        val index = level - 1
        if (index in 0 until childCount) {
            mStarLevel = level
            notifyChildViewChanged()
        }
    }
}