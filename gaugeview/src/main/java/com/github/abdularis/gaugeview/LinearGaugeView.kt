package com.github.abdularis.gaugeview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class LinearGaugeView : View {

    private var _minNumber : Int = 0
    var minNumber : Int
        get() = _minNumber
        set(value) {
            _minNumber = value
            invalidate()
        }

    private var _maxNumber : Int = 100
    var maxNumber : Int
        get() = _maxNumber
        set(value) {
            _maxNumber = value
            invalidate()
        }

    private var _animating = false
    private var _currentNumberAnim : Int = 0
    private var _currentNumber : Int = 0
    var currentNumber : Int
        get() = _currentNumber
        set(value) {
            animateFilledBar(_currentNumber, value)
            _currentNumber = Math.min(value, _maxNumber)
        }

    private val emptyBarPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val filledBarPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path : Path = Path()
    private val rect = Rect()
    private var animator : ValueAnimator? = null

    var emptyColor : Int
        get() = emptyBarPaint.color
        set(value) {
            emptyBarPaint.color = value
            invalidate()
        }
    var filledColor : Int
        get() = filledBarPaint.color
        set(value) {
            filledBarPaint.color = value
            invalidate()
        }

    constructor(ctx : Context) : super(ctx)
    constructor(ctx : Context, attrs : AttributeSet) : super(ctx, attrs) {
        val a : TypedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LinearGaugeView, 0, 0)
        _minNumber = a.getInteger(R.styleable.LinearGaugeView_minNumber, _minNumber)
        _maxNumber = a.getInteger(R.styleable.LinearGaugeView_maxNumber, _maxNumber)
        _currentNumber = a.getInteger(R.styleable.LinearGaugeView_currentNumber, _currentNumber)
        emptyBarPaint.color = a.getColor(R.styleable.LinearGaugeView_emptyBarColor, Color.DKGRAY)
        filledBarPaint.color = a.getColor(R.styleable.LinearGaugeView_filledBarColor, Color.GRAY)

        a.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val top = paddingTop.toFloat()
        val left = paddingLeft.toFloat()
        val right = (width - paddingRight).toFloat()
        val bottom = (height - paddingBottom).toFloat()

        path.reset()
        path.moveTo(left, bottom)
        path.quadTo(right * .5f, bottom * 0.9f, right, top)
        path.lineTo(right, bottom)
        path.lineTo(left, bottom)
        path.close()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(path, emptyBarPaint)

        rect.set(0, 0, (width * getCurrentNumberOffset()).toInt(), height)
        canvas?.clipRect(rect)
        canvas?.drawPath(path, filledBarPaint)
    }

    private fun getCurrentNumberOffset() : Float {
        return if (_animating) _currentNumberAnim / _maxNumber.toFloat() else _currentNumber / _maxNumber.toFloat()
    }

    private fun animateFilledBar(current : Int, new : Int) {
        animator?.cancel()
        animator = ValueAnimator.ofInt(current, new)
        animator?.duration = 250
        animator?.interpolator = DecelerateInterpolator()
        animator?.addUpdateListener {
            _currentNumberAnim = it.animatedValue as Int
            invalidate()
        }
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                _animating = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                _animating = false
            }

            override fun onAnimationStart(animation: Animator?) {
                _animating = true
            }
        })
        animator?.start()
    }
}
