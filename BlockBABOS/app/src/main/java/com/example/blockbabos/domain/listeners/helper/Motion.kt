package com.example.blockbabos.domain.listeners.helper

class Motion {
    private var left = 0
    private var top = 0
    private var right = 0
    private var bottom = 0

    fun getLeft():Int{
        return left
    }
    fun getTop():Int{
        return top
    }
    fun getRight():Int{
        return right
    }
    fun getBottom():Int{
        return bottom
    }
    fun setLeft(left:Int){
        this.left = left
    }
    fun setTop(top:Int){
        this.top = top
    }
    fun setRight(right:Int){
        this.right = right
    }
    fun setBottom(bottom:Int){
        this.bottom = bottom
    }
    fun reset(){
        this.left = 0
        this.top= 0
        this.right = 0
        this.bottom = 0
    }

    override fun toString(): String {
        return "" + this.left + " " + this.top + " " + this.right + " " + this.bottom
    }
}