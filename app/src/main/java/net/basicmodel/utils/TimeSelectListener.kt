package net.basicmodel.utils

/**
 * Copyright (C) 2021,2021/10/26, a Tencent company. All rights reserved.
 *
 * User : v_xhangxie
 *
 * Desc :
 */
interface TimeSelectListener {
    fun timeSelect(flag:String,time:String)
    fun dialogDismiss()
}