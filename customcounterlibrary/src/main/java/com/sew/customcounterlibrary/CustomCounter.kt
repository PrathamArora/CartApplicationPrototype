package com.sew.customcounterlibrary

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.min
import kotlin.math.max

/**
 * @author Pratham Arora
 *
 *
 *
 * A layout that arranges two buttons and two text views in such a way that you can use it as a
 * custom counter to increment/decrement quantities of something
 *     <com.sew.customcounterlibrary.CustomCounter
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     app:incrementButtonColor="#f00"
 *     app:decrementButtonColor="#0f0"
 *     app:valueColor="#ff0"
 *     app:numberColor="#00f"
 *     app:initialValue="5"
 *     app:maxValue="10"
 *     app:minValue="0" />
 *
 *     Set {@link R.styleable#CustomCounter_incrementButtonColor} to change color of incremental
 *     button
 *
 *     Set {@link R.styleable#CustomCounter_decrementButtonColor} to change color of decremental
 *     button
 *
 *     Set {@link R.styleable#CustomCounter_numberColor} to change color of the counter
 *     number
 *
 *     Set {@link R.styleable#CustomCounter_valueColor} to change color of text written below
 *     counter number
 *
 *     Set {@link R.styleable#CustomCounter_maxValue} to change maximum value of the counter
 *     number
 *
 *     Set {@link R.styleable#CustomCounter_minValue} to change minimum value of the counter
 *     number
 *
 *     Set {@link R.styleable#CustomCounter_initialValue} to change initial value of the counter
 *     number
 */
class CustomCounter : LinearLayout {

    private var imgMinus: ImageView? = null
    private var imgPlus: ImageView? = null
    private var tvNumber: TextView? = null
    private var tvValue: TextView? = null

    private var incrementColor: Int = resources.getColor(R.color.incrementDefaultColor, null)
    private var decrementColor: Int = resources.getColor(R.color.decrementDefaultColor, null)
    private var numberColor: Int = resources.getColor(R.color.black, null)
    private var valueColor: Int = resources.getColor(R.color.valueDefaultColor, null)
    private var minValue = 0
    private var maxValue = 100
    private var initialValue = 50
    private var numberTextSize = 26.0f
    private var captionTextSize = 12.0f
    private var onValueChangeListener: OnValueChangeListener? = null


    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyle, defStyleRes) {
        init(context, attrs)
    }

    interface OnValueChangeListener {
        fun onValueChange(oldValue: Int, newValue: Int)
    }

    /**
     * A callback where you can get the old value and new value of the counter
     *
     * @param onValueChangeListener The callback that will run
     */
    fun setOnValueChangeListener(onValueChangeListener: OnValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener
    }

    /**
     * Initializes the views of the CustomCounter
     *
     * @param context Context is required to inflate the views. Hence, it is nullable
     * @param attrs Attributes that are given through XML are read here. Can be nullable
     *
     */
    private fun init(context: Context, attrs: AttributeSet?) {

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomCounter, 0, 0)

            incrementColor = typedArray.getColor(
                R.styleable.CustomCounter_incrementButtonColor,
                incrementColor
            )
            decrementColor = typedArray.getColor(
                R.styleable.CustomCounter_decrementButtonColor,
                decrementColor
            )

            numberColor = typedArray.getColor(
                R.styleable.CustomCounter_numberColor,
                numberColor
            )
            valueColor = typedArray.getColor(
                R.styleable.CustomCounter_valueColor,
                valueColor
            )

            minValue = typedArray.getInt(
                R.styleable.CustomCounter_minValue,
                minValue
            )

            maxValue = typedArray.getInt(
                R.styleable.CustomCounter_maxValue,
                maxValue
            )
            initialValue = typedArray.getInt(
                R.styleable.CustomCounter_initialValue,
                initialValue
            )

            numberTextSize = typedArray.getDimension(
                R.styleable.CustomCounter_numberTextSize,
                numberTextSize
            )

            captionTextSize = typedArray.getDimension(
                R.styleable.CustomCounter_captionTextSize,
                captionTextSize
            )

            typedArray.recycle()
        }

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL

        //----------------------Decrease value-----------------------------
        imgMinus = ImageView(context)
        val minusImgLayoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
        minusImgLayoutParams.gravity = Gravity.START;
        imgMinus?.layoutParams = minusImgLayoutParams

        setMinusButtonColor(decrementColor)
        imgMinus?.setOnClickListener {
            val newValue = max(initialValue - 1, minValue)
            onValueChangeListener?.onValueChange(initialValue, newValue)
            initialValue = newValue
            setInitialValue(initialValue)
        }
//        imgMinus?.setPadding(5, 5, 5, 5)

        //----------------------Number Value-----------------------------
        val llNumberValue = LinearLayout(context)
        llNumberValue.layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
        llNumberValue.orientation = VERTICAL

        tvNumber = TextView(context)
        tvNumber?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        tvNumber?.gravity = Gravity.CENTER
        setNumberTextColor(numberColor)
        setNumberTextSize(numberTextSize)
        initialValue = min(initialValue, maxValue)
        initialValue = max(initialValue, minValue)
        setInitialValue(initialValue)


        tvValue = TextView(context)
        tvValue?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 0f)
        tvValue?.gravity = (Gravity.TOP or Gravity.CENTER_HORIZONTAL)
        tvValue?.text = context.resources.getText(R.string.value)
        tvValue?.visibility = View.GONE
        setValueTextSize(captionTextSize)
        setValueTextColor(valueColor)

        llNumberValue.addView(tvNumber)
        llNumberValue.addView(tvValue)

        //----------------------Increase value-----------------------------
        imgPlus = ImageView(context)
        val plusImgLayoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f)
        plusImgLayoutParams.gravity = Gravity.END;
        imgPlus?.layoutParams = plusImgLayoutParams
        setPlusButtonColor(incrementColor)
        imgPlus?.setOnClickListener {
            val newValue = min(initialValue + 1, maxValue)
            onValueChangeListener?.onValueChange(initialValue, newValue)
            initialValue = newValue
            setInitialValue(initialValue)
        }
//        imgPlus?.setPadding(5, 5, 5, 5)

        addView(imgMinus)
        addView(llNumberValue)
        addView(imgPlus)
    }

    /**
     * Set the color of the incremental button.
     * Default is green @color/incrementDefaultColor (#649d66)
     *
     * @param incrementColor A reference to the color, which you want to set on this button
     */
    fun setPlusButtonColor(incrementColor: Int) {
        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.twotone_add_circle_24)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, incrementColor)
        imgPlus?.setImageDrawable(wrappedDrawable)
//        imgPlus?.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    /**
     * Set the color of the decremental button.
     * Default is green @color/decrementDefaultColor (#e43f5a)
     *
     * @param decrementColor A reference to the color, which you want to set on this button
     */
    fun setMinusButtonColor(decrementColor: Int) {
        val unwrappedDrawable =
            AppCompatResources.getDrawable(context, R.drawable.twotone_remove_circle_24)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, decrementColor)
        imgMinus?.setImageDrawable(wrappedDrawable)
//        imgMinus?.scaleType = ImageView.ScaleType.FIT_CENTER
    }


    /**
     * Set the color of the Number in the counter.
     * Default is green @color/black (#000)
     *
     * @param numberColor A reference to the color, which you want to set on the counter number
     */
    fun setNumberTextColor(numberColor: Int) {
        tvNumber?.setTextColor(numberColor)
    }

    /**
     * Set the color of the text written below the number counter.
     * Default is green @color/valueDefaultColor (#828282)
     *
     * @param valueColor A reference to the color, which you want to set on the counter number
     */
    fun setValueTextColor(valueColor: Int) {
        tvValue?.setTextColor(valueColor)
    }

    /**
     * Set the text size of the text written below the number counter
     * Default is 12sp
     *
     * @param size unit of the text size is in sp
     */
    fun setValueTextSize(size: Float) {
        tvValue?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    /**
     * Set the initial value of the counter number
     * Default is 50
     *
     * @param initialValue should be between max and min value
     */
    fun setInitialValue(initialValue: Int) {
        this.initialValue = min(initialValue, maxValue)
        this.initialValue = max(this.initialValue, minValue)

        tvNumber?.text = this.initialValue.toString()
    }

    fun setMinValue(minValue: Int) {
        this.minValue = minValue
        tvNumber?.text = max(this.initialValue, this.minValue).toString()
    }


    fun setMaxValue(maxValue: Int) {
        this.maxValue = maxValue
        tvNumber?.text = min(this.initialValue, this.maxValue).toString()
    }

    /**
     * Returns the current integer value of counter number
     */
    fun getCurrentValue(): Int {
        return initialValue
    }


    /**
     * Set the text size of the number counter
     * Default is 26sp
     *
     * @param size unit of the text size is in sp
     */
    fun setNumberTextSize(size: Float) {
        tvNumber?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

}