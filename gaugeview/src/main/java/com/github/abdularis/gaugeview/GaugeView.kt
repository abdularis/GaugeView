package com.github.abdularis.gaugeview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View

open class GaugeView : View {
    protected var _minNumber : Int = 0
    var minNumber : Int
        get() = _minNumber
        set(value) {
            _minNumber = value
            invalidate()
        }

    protected var _maxNumber : Int = 100
    var maxNumber : Int
        get() = _maxNumber
        set(value) {
            _maxNumber = value
            invalidate()
        }

    protected var _currentNumber : Int = 0
    open var currentNumber : Int
        get() = _currentNumber
        set(value) {
            _currentNumber = Math.min(value, _maxNumber)
            invalidate()
        }

    protected open val currentNumberOffset: Float
        get() = _currentNumber.toFloat() / Math.abs(_minNumber.toFloat() - _maxNumber.toFloat())

    constructor(ctx : Context) : super(ctx)
    constructor(ctx : Context, attrs : AttributeSet) : super(ctx, attrs) {
        val a : TypedArray = ctx.obtainStyledAttributes(attrs, R.styleable.GaugeView, 0, 0)
        _minNumber = a.getInteger(R.styleable.GaugeView_minNumber, _minNumber)
        _maxNumber = a.getInteger(R.styleable.GaugeView_maxNumber, _maxNumber)
        _currentNumber = a.getInteger(R.styleable.GaugeView_currentNumber, _currentNumber)
        a.recycle()
    }
}